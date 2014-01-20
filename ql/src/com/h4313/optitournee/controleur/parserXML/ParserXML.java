/**
 *
 */
package com.h4313.optitournee.controleur.parserXML;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.h4313.optitournee.modele.*;
import com.h4313.optitournee.outils.Constantes;
import com.h4313.optitournee.outils.Constantes.TypeFichier;
import com.h4313.optitournee.outils.Pair;

import org.w3c.dom.*;
import org.xml.sax.SAXException;


/**
 * @author H4403
 *
 *	Cette classe fournit des services pour le remplissage
 *	du mod???le dans notre application. Il s'agit
 *	d'un subordonn??? du contr???leur qui lui confie
 *  cette t???che de recup???ration de donn???es et cr???ation d'objets du mod???le.
 *
 *	Premi???rement, elle permet d'effectuer le choix d'un fichier
 * 	ouvrant un gestionnaire de fichier.
 *	Deuxi???mement, elle fournit le service de v???rification et
 *  parsage du fichier charg???. De ce parsage,
 *	on retire des objets que l'on r???ifie dans le mod???le.
 *
 */
public class ParserXML {

	/**
	 * Repr???sente le type de fichier ??? ouvrir.
	 *
	 *  @see TypeFichier
	 */
	private TypeFichier type;


	/**
	 * Repr???sente le fichier ??? traiter.
	 *  L'instance est cr??????e une fois pour le fichier du plan et est ensuite
	 *  mise ??? jour pour le fichier des livraisons
	 * @see File
	 */
	private File fichierATraiter;


	/**
	 * Constructeur qui permet de sp???cifier le chemin du fichier ??? parser et le type
	 * de fichier dont il s'agit.
	 * @param cheminFichier Chemin d'acc???s du fichier ??? parser
	 *
	 * @param type Type de fichier (PLAN ou LIVRAISONS)
	 *
	 * @see TypeFichier
	 */
	public ParserXML(String cheminFichier,TypeFichier type)
	{
		this.fichierATraiter = new File(cheminFichier);
		this.type = type;
	}




	/**
	 * 	La m???thode <code>verifierSiValide</code> effectue la v???rification de la conformit??? du fichier XML ??? traiter.
	 *  Cette v???rification se fait ??? partir du sch???ma XML correspondant.
	 *  Ce sch???ma XML d???finit une structure assez stricte qui doit ???tre respect???e.
	 * 	Si le fichier XML n'est pas conforme, une exception de type
	 *  XmlParserException est lev???e.
	 *
	 * 	@throws XmlParserException Exception lev???e lorsque
	 *  n'importe quelle exception interne ??? la m???thode est lev???e.
	 * 	Elle regroupe les autres exceptions et permet de l'ext???rieur de savoir
	 *  quelle exception a ???t??? lev???e dans le parser.
	 */
	private void verifierSiValide() throws XmlParserException
	{
		Source xmlFile=null;
		try
		{
			xmlFile = new StreamSource(this.fichierATraiter);
			SchemaFactory schemaFactory = SchemaFactory
			    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			File file;
			if(this.type==TypeFichier.LIVRAISONS)
			{
				file =new File(Constantes.LIVRAISONSCHEMAPATH);
			}
			else
			{
				file= new File(Constantes.PLANSCHEMAPATH);
			}

			//Chargement du sch???ma ??? utiliser pour la validation
			Schema schema = schemaFactory.newSchema(file);
			Validator validator = schema.newValidator();

			//Lancement de la validation grace ??? l'objet validator. Si le fichier XML n'est pas valide
			// au sch???ma une execption est lev???e.
			validator.validate(xmlFile);

			String[] tableauString= xmlFile.getSystemId().split("/");
			System.out.println(tableauString[tableauString.length-1] + " est VALIDE");
		}
		catch (SAXException saxEx)
		{
			String[] tableauString= xmlFile.getSystemId().split("/");
			throw new XmlParserException((tableauString[tableauString.length-1]  + " N'EST PAS VALIDE \n Cause: "+saxEx.getLocalizedMessage()+"\n"),saxEx);
		}
		catch (IOException iOEx)
		{
			 String[] tableauString= xmlFile.getSystemId().split("/");
			 throw new XmlParserException((tableauString[tableauString.length-1] + " N'EST PAS VALIDE \n Cause: "+iOEx.getLocalizedMessage()+"\n"),iOEx);
		}

	}


	/**
	 * la m???thode <code>genererPlanDepuisXML</code> permet de g???n???rer le plan sous forme d'une liste de noeuds.
	 * Cette liste de noeuds est celle qui sert pour la cr???ation du plan.
	 *
	 * @return Liste de noeuds repr???sentant le plan.
	 * @throws XmlParserException Execption lev???e en cas d'erreur lors
	 * 	du parsage du fichier.
	 */
	public List<Noeud> genererPlanDepuisXML() throws XmlParserException
	{

        File xml = this.fichierATraiter;

        ArrayList<Noeud> listeDesNoeuds=null;
        if (xml != null)
        {
             try
             {
                 // creation d'un constructeur de documents a l'aide d'une fabrique
                DocumentBuilder constructeur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                // lecture du contenu d'un fichier XML avec DOM
                Document document = constructeur.parse(xml);

                verifierSiValide();
                Element racine = document.getDocumentElement();

                if (racine.getNodeName().equals("Reseau")){

                	//Cr???ation et remplissage de la liste de noeuds
                    listeDesNoeuds= new ArrayList<Noeud>();
                    construirePlanAPartirDeDOMXML(racine,listeDesNoeuds);
                }

            }
            catch (ParserConfigurationException pce)
            {
            	String message= new String("Erreur de configuration du parseur DOM  lors de l'appel a fabrique.newDocumentBuilder(); ");
                throw new XmlParserException(message,pce);

            }
            catch (SAXException se)
            {
                String message = new String("Erreur lors du parsing du document lors de l'appel a construteur.parse(xml)");
                throw new XmlParserException(message,se);
            }
            catch (IOException ioe)
            {
                String message= new String("Erreur d'entree/sortie lors de l'appel a construteur.parse(xml)");
                throw new XmlParserException(message,ioe);
            }
            catch (XmlParserException myEx)
            {
				throw myEx;
			}
        }

        return listeDesNoeuds;
	}

	/**
	 * 	Verifie qu'il n'existe pas d???j??? un Noeud ou une livraison ayant le m???me idenfiant.
	 * S'il n'existe pas de Noeud ou livraison ayant le m???me identifiant dans la liste alors une exception
	 * {@link IndexOutOfBoundsException} est lev???e. Sinon l'element existe d???j??? et on l???ve alors l'exception
	 *  du type {@link XmlParserException}.
	 * 
	 * @param reseau La liste de des noeuds d???j??? enregistr???s.
	 * @param livraisons La Liste des livraisons d???j??? enregist???es.
	 * @param id L'identifaint pour lequel on effectue la v???rification
	 * @throws XmlParserException L'exception qui est lev???e si il ya des doublons.
	 */
	private void verifDoublonId(List<Noeud> reseau, List<Livraison> livraisons, int id) throws XmlParserException
	{
		try
		{
			if (!(reseau==null))
			{
				if(!reseau.get(id).equals(null))
				{
					throw new XmlParserException("Erreur s???mantique:Pr???sence de doublons (M???mes identifiants)");
				}
			}

			if (!(livraisons==null))
			{
				if (!livraisons.get(id).equals(null))
				{
					throw new XmlParserException("Erreur s???mantique:Pr???sence de doublons (M???mes identifiants)");
				}
			}

		}
		catch (IndexOutOfBoundsException iobe)
		{
			// cette exception confirme qu'il n'y pas d???ja d'objet avec cet identifiant
			// On peut donc enregister une noeud ??? cette adresse avec cet identifiant.
		}
		catch (XmlParserException xmle)
		{
			throw xmle;
		}
	}

	/**
<<<<<<< .mine
	 * Verifie qu'un nouveau noeud ??? enregistrer dans la liste des noeuds du plan n'a pas 
	 * les m???mes coordonn???es qu'un autre noeud qui existe d???j???.
	 * 
	 * @param reseau L'ensemble des noeuds d???j??? enregistr???s.
	 * @param nouveauNoeud Le noeud pour lequel on veut v???rifier que les coordonn???es sont diff???rents.
	 * @throws XmlParserException L'exception qui est lev???e si il ya des doublons.
	 */
	private void verifDoublonCooordonnees(List<Noeud> reseau, Noeud nouveauNoeud) throws XmlParserException
	{
		try {
			
			for (Noeud n:reseau)
			{
				if (n.memeCordonnes(nouveauNoeud))
				{
					throw new XmlParserException("Erreur s???mantique:Pr???sence de doublons (Noeuds ayant les m???mes coordonn???es)");
				}
			}
			
		} catch (XmlParserException e) {
			
			throw e;
		}		
	}
	
	/**
	 * La m???thode <code>construirePlanAPartirDeDOMXML</code> parcours l'???l???ment DOM repr???sentant
	 * la racine du document et rempli la liste des noeuds avec les noeuds cr??????s.
	 * Les noeuds sont d'abord tous cr??????s et ensuite on cr???e les tron???ons contenus dans chaque noeud.
	 *
	 * @param elemReseau Repr???sente l'???l???ment DOM qui est la racine du document.
	 * @param listeARetourner La liste ??? remplir ??? partir des informations du fichier XML du plan
	 * @throws XmlParserException Exception lev???e en cas d'erreur lors du parcours des ???l???ments DOM
	 */
	private void construirePlanAPartirDeDOMXML(Element elemReseau,ArrayList<Noeud> listeARetourner )throws XmlParserException
	{
		try
		{

			NodeList listeDeNoeudsXML= elemReseau.getChildNodes();
			Node noeudXML= null;
			ArrayList<Element> listeDesNoeud= listeDesNoeudsFils(elemReseau, "Noeud");

			boolean premierTourBoucle = true;
			for (Element e: listeDesNoeud)
			{
				int id = Integer.parseInt(e.getAttribute("id"));
				int abs = Integer.parseInt(e.getAttribute("x"));
				int ord = Integer.parseInt(e.getAttribute("y"));

				Noeud noeudAajouter=new Noeud(id,abs,ord);

				if (!premierTourBoucle)
				{
					verifDoublonId(listeARetourner, null, id);
					verifDoublonCooordonnees(listeARetourner,noeudAajouter);
				}
				{
					premierTourBoucle=false;
				}
				listeARetourner.add(id,noeudAajouter);
			}

			//Deuxi???me boucle qui ajoute les troncons sortants des noeuds
			for (int j=0;j<listeDeNoeudsXML.getLength();j++)
			{
				noeudXML= listeDeNoeudsXML.item(j);
				if (noeudXML.getNodeName().equals("Noeud") && (noeudXML.getNodeType()== Node.ELEMENT_NODE))
				{

					Element elemNoeudXML = (Element)noeudXML;
					ArrayList<Element> listeTroncon= listeDesNoeudsFils(elemNoeudXML, "TronconSortant");

					int idNoeudSource=Integer.parseInt(elemNoeudXML.getAttribute("id"));

					Noeud noeudSource= listeARetourner.get(idNoeudSource);

					for (Element troncon:listeTroncon )
					{

						int destination =Integer.parseInt(troncon.getAttribute("destination"));
						Float vitesse= Float.parseFloat(troncon.getAttribute("vitesse"));
						Float longueur= Float.parseFloat(troncon.getAttribute("longueur"));
						String nomRue= troncon.getAttribute("nomRue");

						//On recup???re le noeud correspondant ??? la destination
						Noeud noeudDestination=listeARetourner.get(destination);

						Troncon tr= new Troncon(nomRue,vitesse,longueur,noeudDestination);

						noeudSource.ajouterTroncons_sortants(tr);

					}

				}
			}

		}
		catch (Exception e)
		{
			throw new XmlParserException(e.getMessage(),e);
		}
	}


	/**
	 * 	La m???thode <code>genererLivraisonsDepuisXML</code> permet de g???n???rer les plages horaires et
	 * 	les livraisons ??? effectuer contenues dans celles-ci.
	 * 	En plus de la liste des plage horaires, cette m???thode fournit
	 * 	aussi le noeud repr???sentant l'entrep???t.
	 *
	 * 	@return Couple (Entrepot, liste des plage horaires).
	 *
	 * 	@throws XmlParserException Execption lev???e en cas d'erreur
	 * lors du parsage du fichier.
	 *
	 * @see NoeudItineraire
	 *
	 * @see  PlageHoraire
	 */
	public Pair<NoeudItineraire,ArrayList<PlageHoraire> > genererLivraisonsDepuisXML(List<Noeud> reseau) throws XmlParserException
	{
		//ouvrirFichier(this.CHARGERLIVRAISONS);
		File xml = this.fichierATraiter;
		ArrayList<PlageHoraire> listeDePlageHoraire = null;
		NoeudItineraire entrepot = null;

		Pair<NoeudItineraire, ArrayList<PlageHoraire> > coupleEntrpotPlageH= null;

		if (xml != null)
		{
            try
            {
            	// creation d'un constructeur de documents a l'aide d'une fabrique
                DocumentBuilder constructeur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                // lecture du contenu d'un fichier XML avec DOM
                Document document = constructeur.parse(xml);

                	verifierSiValide();
                	Element racine = document.getDocumentElement();
                    if (racine.getNodeName().equals("JourneeType"))
                    {
                    	listeDePlageHoraire= new ArrayList<PlageHoraire>();
                    	entrepot= new NoeudItineraire();
                    	construireLivraisonsAPArtirDeDOMXML(racine, listeDePlageHoraire, entrepot,reseau);

                    	//Cr???ation du couple ??? retourner
                    	coupleEntrpotPlageH= new Pair<NoeudItineraire,ArrayList<PlageHoraire>>(entrepot,listeDePlageHoraire);

                    }

            }
            catch (ParserConfigurationException pce)
            {
            	String message= new String("Erreur de configuration du parseur DOM \n lors de l'appel a fabrique.newDocumentBuilder(); ");
                throw new XmlParserException(message,pce);

            }
            catch (SAXException se)
            {
                String message = new String("Erreur lors du parsing du document \n lors de l'appel a construteur.parse(xml)");
                throw new XmlParserException(message,se);


            }
            catch (IOException ioe)
            {
                String message= new String("Erreur d'entree/sortie \n lors de l'appel a construteur.parse(xml)");
                throw new XmlParserException(message,ioe);
            }
            catch (XmlParserException myEx)
            {
				throw myEx;
			}
		}

		return coupleEntrpotPlageH;
	}


	/**
	 * La m???thode <code>construireLivraisonsAPArtirDeDOMXML</code> parcours l'???l???ment
	 * DOM repr???sentant la racine du document et remplit la liste des plages horaires.
	 * On cr???e d'abord la liste des livraisons et ensuite la plage horaire avec
	 * la liste des livraisons correspondante.
	 *
	 * @param elemJourneType Element DOM repr???sentant la racine du document.
	 *
	 * @param listeARetourner Liste des plages horaires ??? remplir.
	 *
	 * @param entrepot <code>NoeudItin???raire</code> repr???sentant l'entrep???t.
	 *
	 * @param reseau Liste des noeuds qui constituent le plan.
	 *
	 * @throws XmlParserException Exception lev???e en cas d'erreur
	 * lors du parcour des ???l???ments DOM
	 *
	 * @see NoeudItineraire
	 *
	 * @see Element
	 *
	 */
	private void construireLivraisonsAPArtirDeDOMXML(Element elemJourneType,
			ArrayList<PlageHoraire> listeARetourner, NoeudItineraire entrepot, List<Noeud> reseau)
					throws XmlParserException
	{

		NodeList livraisonsXml= elemJourneType.getChildNodes();
		Node noeudXML= null;

			for(int i=0;i<livraisonsXml.getLength();i++)
			{
				noeudXML= livraisonsXml.item(i);
				if ((noeudXML.getNodeType()== Node.ELEMENT_NODE))
				{
					if (noeudXML.getNodeName().equals("Entrepot"))
					{
						Element e= (Element)noeudXML;
						int idAdresse = Integer.parseInt(e.getAttribute("adresse"));

						try
						{
							entrepot.setAdresse(reseau.get(idAdresse));
						}
						catch (IndexOutOfBoundsException iobe)
						{
							throw new XmlParserException(iobe.getLocalizedMessage()+". L'adresse n'existe pas dans le r???seau. ",iobe);
						}
					}
					else
					{
						if(noeudXML.getNodeName().equals("PlagesHoraires"))
						{
							Element elemPlageHoraire= (Element)noeudXML;
							ArrayList<Element> plagesXml = listeDesNoeudsFils(elemPlageHoraire, "Plage");
							ArrayList<Pair<Calendar, Calendar>> listePlages= new ArrayList<Pair<Calendar,Calendar>>();

							for (Element p :plagesXml)
							{
								String [] heureDeb= p.getAttribute("heureDebut").split(":");
								String [] heureFin= p.getAttribute("heureFin").split(":");

								GregorianCalendar c= new GregorianCalendar();
								GregorianCalendar tHeureDeb= new GregorianCalendar(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH), Integer.parseInt(heureDeb[0]),Integer.parseInt(heureDeb[1]),Integer.parseInt(heureDeb[2]));
								GregorianCalendar tHeureFin= new GregorianCalendar(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH), Integer.parseInt(heureFin[0]),Integer.parseInt(heureFin[1]),Integer.parseInt(heureFin[2]));
								if (tHeureFin.before(tHeureDeb))
								{
									throw new XmlParserException("Erreur s???mantique: Une plage horaire n'est pas conforme: l'heure de d???but est apr???s l'heure de fin");
								}
								else
								{
									Pair<Calendar, Calendar> plage=new Pair<Calendar, Calendar>(tHeureDeb, tHeureFin);
									if(verifChevauchement(listePlages,plage ))
									{
										throw new XmlParserException("Erreur s???mantique: Il y'a chevauchement de deux plages horaires");
									}
									else
									{
										listePlages.add(plage);
									}

								}

								ArrayList<Element> listeLivraisonsXml= listeDesNoeudsFils(p, "Livraisons");

								ArrayList<Livraison> listeDsLivraison= new ArrayList<Livraison>();


								for(Element ll :listeLivraisonsXml)
								{
									ArrayList<Element> livraisons= listeDesNoeudsFils(ll, "Livraison");

									boolean premierTourBoucle=true;
									for(Element l :livraisons )
									{
										//recuperer l'ID de la livraison
										int idLivraison= Integer.parseInt(l.getAttribute("id"));

										//recuperer l'id client
										int idClient = Integer.parseInt(l.getAttribute("client"));

										try
										{
											//recupere l'adresse
											Noeud adr = reseau.get(Integer.parseInt(l.getAttribute("adresse")));

											Livraison livr = new Livraison(idLivraison,adr,idClient);

											if (!premierTourBoucle){
												verifDoublonId(null, listeDsLivraison, idLivraison-1);
											}
											{
												premierTourBoucle=false;
											}
											//L'indexe commence ??? 1
											listeDsLivraison.add(idLivraison-1,livr);

											}
										catch (IndexOutOfBoundsException iobe)
										{
											throw new XmlParserException(iobe.getLocalizedMessage()+" L'adresse n'existe pas dans le r???seau. ",iobe);

										}
										catch (XmlParserException xmle) {
											throw xmle;
										}

									}

								}

								PlageHoraire plageHoraire= new PlageHoraire(tHeureDeb, tHeureFin, listeDsLivraison);
								listeARetourner.add(plageHoraire);

							}


						}
					}
				}


			}


	}

	/**
	 *
	 * La m???thode chevauchement teste si deux plages horaires se chevauchent.
	 *
	 * @param firstPlage La premi???re plage horaire.
	 *
	 * @param secondPlage La deuxi???me plage horaire.
	 *
	 * @return <code>true</code> s'il ya chevauchement, <code>false</code> sinon.
	 *
	 * @see Pair
	 * @see Calendar
	 *
	 */
	private boolean chevauchement(Pair<Calendar, Calendar> firstPlage, Pair<Calendar, Calendar> secondPlage)
	{
		boolean heurDebDansIntervalle= firstPlage.getFirst().before(secondPlage.getFirst()) && secondPlage.getFirst().before(firstPlage.getSecond());

		boolean heureFinDansIntervalle= firstPlage.getFirst().before(secondPlage.getSecond()) && secondPlage.getSecond().before(firstPlage.getSecond());

		boolean egalite=firstPlage.getFirst().compareTo(secondPlage.getFirst())==0 &&  firstPlage.getSecond().compareTo(secondPlage.getSecond())==0;


		boolean result = heurDebDansIntervalle || heureFinDansIntervalle || egalite;

		return result;
	}


	/**
	 * La m???thode <code>verifChevauchement</code> verifie qu'il n'y a pas de chevauchement
	 * entre une plage horaire ??? charger et les autres plages horaires d???j??? charg???es.
	 *
	 * @param listePlages La liste des plages horaires d???j??? charg???es.
	 * @param plageToTest La plage horaire ??? charger et pour laquelle ou v???rifie l'abscence de chevauchement.
	 * @return <code>true</code> s'il y a chevauchement entre deux plages, <code>false</code> sinon.
	 */
	private boolean verifChevauchement(List<Pair<Calendar, Calendar>> listePlages, Pair<Calendar, Calendar> plageToTest)
	{
		boolean result=false;
		if (!(listePlages.size()==0))
		{
			int i= 0;
			while(!result && i<listePlages.size()){
				result=chevauchement(listePlages.get(i), plageToTest);
				i++;
			}
		}

		return result;
	}


	/**
	 * La m???thode <code>listeDesNoeudsFils</code> permet d'obtenir sous forme de liste d'???l???ment l'ensemble
	 * des ???l???ments fils d'un certain ???l???ment.
	 * Il s'agit d'une "Conversion" de Node ??? element de tous les ???l???ments fils.
	 *
	 * @param e L'???l???ment p???re dont on veut r???cup???rer les fils.
	 * @param name Le nom des ???l???ments fils que l'on d???sire r???cup???rer
	 * @return Liste d'???lement fils
	 */
	private ArrayList<Element> listeDesNoeudsFils(Element e, String name)
	{
		ArrayList< Element> listeDesNoeudFils= new ArrayList<Element>();
		NodeList listeDeNoeudsXML= e.getChildNodes();
		Node noeudXML= null;

		for (int i=0;i<listeDeNoeudsXML.getLength();i++)
		{
			noeudXML= listeDeNoeudsXML.item(i);
			if (noeudXML.getNodeName().equals(name) && (noeudXML.getNodeType()== Node.ELEMENT_NODE))
			{
				Element child= (Element)noeudXML;
				listeDesNoeudFils.add(child);
			}
		}

		return listeDesNoeudFils;
	}


}
