//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.controleur;

import java.util.ArrayList;

import com.h4313.optitournee.outils.Pair;
import com.h4313.optitournee.controleur.parserXML.ParserXML;
import com.h4313.optitournee.controleur.parserXML.XmlParserException;
import com.h4313.optitournee.modele.*;
import com.h4313.optitournee.modele.Itineraire.EtatItineraire;
import com.h4313.optitournee.outils.Constantes;
import com.h4313.optitournee.outils.Constantes.TypeFichier;
import com.h4313.optitournee.outils.EcrireFichierTexte;
import com.h4313.optitournee.vue.VueApplication;

/**
 * La classe <code>Controleur</code> contr�le et organise l'ensemble de l'application.
 *
 * @author H4403
 */
public class Controleur {


	/**
	 * L'attribut <code>historique</code> permet de g�rer les commandes r�alis�es par l'utilisateur pour qu'il
	 * puisse les annuler et les refaire
	 *
	 * @see Historique
	 */
	private Historique historique;

	/**
	 * L'attribut <code>itineraire</code> repr�sente l'objet itin�raire du mod�le, c'est le point d'entr�e du mod�le
	 *
	 * @see Itineraire
	 */
	protected Itineraire itineraire;

	/**
	 * L'attribut <code>vueAppli</code> est la vue principale de l'application et c'est avec elle que le controleur
	 * communique
	 */
	protected VueApplication vueAppli;


	/**
	 *	Objet <code>Controleur</code> unique qui est un singleton
	 */
	private static Controleur controleurSingleton = null;

	/**
	 * La m�thode <code>getControleur</code> permet de r�cup�rer l'instance unique du contr�leur
	 *
	 * @return le contr�leur
	 */
	public static Controleur getControleur(){
		if(controleurSingleton == null){
			controleurSingleton = new Controleur();
		}
		return controleurSingleton;
	}

	/**
	 * La m�thode <code>Controleur</code> est le constructeur priv� de la classe <code>Controleur</code>.
	 * Pour obtenir l'objet <code>controleur</code>, il faut r�cup�rer le singleton avec la m�thode <code>getControleur</code>
	 */
	private Controleur(){
		this.historique = new Historique();

		this.itineraire = null;

		this.vueAppli = new VueApplication(this);

	}

	/**
	 * La m�thode <code>chargerPlan</code> charge un fichier pass� en param�tre et cr�e les objets du mod�le
	 * pour repr�senter le plan en m�moire.
	 * Si le chargement �choue, un message est envoy� � l'utilisateur
	 *
	 * @param adresse Chemin d'acc�s du fichier XML d'entr�e
	 */
	public void chargerPlan(String adresse)
	{
		this.vueAppli.afficherCurseurAttente();
		try
		{
			ParserXML planParser= new ParserXML(adresse, TypeFichier.PLAN);
			this.itineraire= new Itineraire(planParser.genererPlanDepuisXML());
			this.itineraire.setEtat(EtatItineraire.ITINERAIRE_NON_CHARGE);

			this.historique.viderHistorique();

			this.vueAppli.rafraichir();
			this.vueAppli.afficherMessageInfo(Constantes.CHARGEMENT_PLAN_REUSSI);
		}
		catch (XmlParserException e)
		{
			//this.vueAppli.afficherMessageErreur(Constantes.CHARGEMENT_PLAN_IMPOSSIBLE);
			this.vueAppli.afficherMessageErreur(e.getMsgUtilisateur());
			//this.vueAppli.afficherMessageErreur(e.getMsgUtilisateur());
		}
		this.vueAppli.afficherCurseurNormal();
		
	}



	/**
	 * La m�thode <code>calculerTournee</code> cr�e et lance une commande <code>CalculerTournee</code>
	 * qui va calculer la toun�e entre les livraisons d�j� charg�es.
	 * Si le calcul �choue, un message est envoy� � l'utilisateur.
	 */
	public void calculerTournee()
	{
		this.vueAppli.afficherCurseurAttente();
		Commande cmd = new CalculerTournee(this);
		this.historique.faire(cmd);

		this.vueAppli.afficherCurseurNormal();
		this.vueAppli.rafraichir();
	}

	/**
	 * La m�thode <code>augmenterTempsTSP</code> permet d'augmenter le temps de calcul
	 * du <code>TSP</code> si ce dernier est insuffisant.
	 */
	public void augmenterTempsTSP(){
		this.itineraire.augmenterTempsTSP();
	}


	/**
	 * La m�thode <code>chargerLivraisons</code> charge un fichier XML et cr�e les objets du mod�le
	 * pour repr�senter les livraisons en m�moire.
	 * Si le chargement �choue, un message est envoy� � l'utilisateur
	 *
	 * @param adresse Chemin d'acc�s du fichier XML d'entr�e
	 */
	public void chargerLivraisons(String adresse)
	{
		this.vueAppli.afficherCurseurAttente();
		try
		{
			if (this.itineraire != null)
			{
				ParserXML livraisonParser= new ParserXML(adresse,TypeFichier.LIVRAISONS);

				Pair<NoeudItineraire,ArrayList<PlageHoraire> > dataLIvr=livraisonParser.genererLivraisonsDepuisXML(this.itineraire.getPlan().getNoeuds());
				this.itineraire.setPlagesHoraire(dataLIvr.getSecond());
				this.itineraire.setEntrepot(dataLIvr.getFirst());
				this.itineraire.setEtat(EtatItineraire.NON_CALCULE);

				this.historique.viderHistorique();

				this.vueAppli.rafraichir();
				this.vueAppli.afficherMessageInfo(Constantes.CHARGEMENT_LIVRAISONS_REUSSI);
			}
		}
		catch (XmlParserException e)
		{
			//this.vueAppli.afficherMessageErreur(Constantes.CHARGEMENT_ITINERAIRE_IMPOSSIBLE);
			this.vueAppli.afficherMessageErreur(e.getMsgUtilisateur());
			//this.vueAppli.afficherMessageErreur(e.getMsgUtilisateur());
		}
		this.vueAppli.afficherCurseurNormal();
	}

	/**
	 * La m�thode <code>genererFeuilleDeRoute</code> g�n�re un fichier texte d�crivant la feuille de route pour le livreur
	 *
	 * @param adresseFichier Chemin d'acc�s du fichier de sortie
	 */
	public void genererFeuilleDeRoute(String adresseFichier){
		this.vueAppli.afficherCurseurAttente();
		if(adresseFichier != null && !adresseFichier.isEmpty()){
			String texte = this.itineraire.texteDescription();
			if( texte == null){
				this.vueAppli.afficherMessageErreur(Constantes.GENERATION_FDR_IMPOSSIBLE);
			}
			else{
				int res = adresseFichier.indexOf('.');
				if(res == -1){
					adresseFichier += '.' + Constantes.EXTENSION_DEFAUT_SORTIE_FEUILLE;
				}

				if(EcrireFichierTexte.ecrireFichierTexte(adresseFichier, texte) != 0){
					this.vueAppli.afficherMessageErreur(Constantes.GENERATION_FDR_IMPOSSIBLE);
					this.vueAppli.afficherMessageInfo(texte);
				}
				else{
					this.vueAppli.afficherMessageInfo(texte);
				}
			}
		}
		this.vueAppli.afficherCurseurNormal();
	}


	/**
	 * La m�thode <code>annulerOperation</code> annule si possible la derni�re op�ration r�alis�e par l'utilisateur
	 *
	 */
	public void annulerOperation(){
		this.vueAppli.afficherCurseurAttente();
		int retour = this.historique.annulerOperation();
		if(retour == -1){
			this.vueAppli.afficherMessageErreur(Constantes.ANNULER_IMPOSSIBLE);
		}
		else{
			this.vueAppli.rafraichir();
		}
		this.vueAppli.afficherCurseurNormal();
	}

	/**
	 * La m�thode <code>refaireOperation</code> refait si possible la derni�re op�ration annul�e par l'utilisateur
	 *
	 */
	public void refaireOperation(){
		this.vueAppli.afficherCurseurAttente();
		int retour = this.historique.refaireOperation();
		if(retour == -1){
			this.vueAppli.afficherMessageErreur(Constantes.REFAIRE_IMPOSSIBLE);
		}
		else{
			this.vueAppli.rafraichir();
		}
		this.vueAppli.afficherCurseurNormal();
	}

	/**
	 * La m�thode <code>annulerPossible</code> v�rifie si une annulation est possible.
	 * @return <code>true</code> si on peut annuler
	 */
	public boolean annulerPossible(){
		return this.historique.annulerPossible();
	}

	/**
	 * La m�thode <code>refairePossible</code> v�rifie si une op�ration peut �tre refaite.
	 * @return <code>true</code> si on peut refaire
	 */
	public boolean refairePossible(){
		return this.historique.refairePossible();
	}

	/**
	 * la m�thode <code>ajouterLivraison</code> cr�e une commande <code>AjouterLivraison</code> qui ajoute une livraison
	 * � celles d�j� charg�es.
	 * @param noeudSel noeud du plan o� est ajout� la livraison
	 * @param noeudPrec noeud du plan o� se trouve la livraison (ou l'entrep�t) qui sera r�alis�e avant.
	 * La nouvelle livraison aura la m�me plage horaire que cette derni�re.
	 */
	public void ajouterLivraison(Noeud noeudSel, Noeud noeudPrec){
		if(this.itineraire != null && this.itineraire.testItineraireCharger()){
			this.vueAppli.afficherCurseurAttente();

			Commande cmdAjout = new AjouterLivraison(this.itineraire, noeudSel, noeudPrec);
			if(this.historique.faire(cmdAjout)){
				//ajout r�ussi
				this.vueAppli.afficherMessageInfo(Constantes.AJOUT_REUSSI);
			}
			else{
				//echec
				this.vueAppli.afficherMessageInfo(Constantes.AJOUT_ECHEC);
			}
			this.vueAppli.rafraichir();

			this.vueAppli.afficherCurseurNormal();
		}
	}

	/**
	 * la m�thode <code>supprimerLivraison</code> cr�e une commande <code>SupprimerLivraison</code>
	 *  qui supprime la livraison sur le noeud pass� en param�tre.
	 * @param noeudSel Noeud sur lequel la livraison sera supprim�e
	 */
	public void supprimerLivraison(Noeud noeudSel){
		if(this.itineraire != null && this.itineraire.testItineraireCharger()){

			this.vueAppli.afficherCurseurAttente();

			if(noeudSel == null){
				this.vueAppli.afficherMessageInfo(Constantes.AUCUN_NOEUD_SELECT);
			}
			else{
				Commande cmdSuppr = new SupprimerLivraison(this.itineraire, noeudSel);
				if(this.historique.faire(cmdSuppr)){
					//supression r�ussi
					this.vueAppli.afficherMessageInfo(Constantes.SUPPRESSION_REUSSIE);
				}
				else{
					//echec
					this.vueAppli.afficherMessageInfo(Constantes.SUPPRESSION_ECHEC);
				}
				this.vueAppli.rafraichir();
			}

			this.vueAppli.afficherCurseurNormal();
		}
	}

	/**
	 * La m�thode <code>getItineraire</code> est un getter pour obtenir le point d'entr�e du mod�le
	 * @return l'itin�raire, point d'entr�e du mod�le
	 */
	public Itineraire getItineraire() {
		return itineraire;
	}


}

