//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.modele;
import com.h4313.optitournee.outils.GestionCalendar;
import java.util.Calendar;



/**
 * La classe <code>Livraison</code> sp�cialise la classe <code>NoeudItin�raire</code>.
 * Elle repr�sente une livraison � effectuer.
 *
 * @author H4403
 * @see NoeudItineraire
 */
public class Livraison extends NoeudItineraire {

	/**
	 * Le type <code>EtatLivraison</code> repr�sente la liste des �tats
	 * possibles d'une livraison du mod�le
	 */
	public static enum EtatLivraison{NON_CALCULE, HORS_PLAGE_HORAIRE, POSSIBLE};

	/**
	 * L'attribut <code>id</code> repr�sente l'identifiant de la livraison.
	 *
	 */
	private Integer id;

	/**
	 * Identifiant du client
	 */
	private Integer idClient;

	/**
	 * Repr�sente l'�tat de la livraison.
	 *
	 * @see EtatLivraison
	 */
	private EtatLivraison etat;


	/**
	 * Constructeur de la classe <code>Livraison</code>
	 *
	 * @param id Identifiant de la livraison.
	 * @param adresse Noeud du plan o� doit �tre livr� la livraison
	 * @param idClient Identifiant du client.
	 *
	 * @see Noeud
	 */
	public Livraison (int id, Noeud adresse, int idClient)
	{
		super(adresse);
		this.id= new Integer(id);
		this.idClient= new Integer(idClient);
		this.etat= EtatLivraison.NON_CALCULE;
		this.heure = null;

	}

	/**
	 * Getter de <code>etat</code>
	 * @return <code>this.etat</code>
	 */
	public EtatLivraison getEtat() {
		return this.etat;
	}

	/**
	 * Setter de <code>etat</code>
	 * @param etat Le nouvel �tat de la livraison.
	 * @see EtatLivraison
	 */
	public void setEtat(EtatLivraison etat) {
		this.etat = etat;
	}

	/**
	 * La m�thode <code>texteDescription</code> g�n�re un texte de description
	 * de la livraison
	 *
	 * @param heureDebut Heure de d�but de la plage horaire de la livraison
	 * @return Le texte de description, <code>null</code> si �chec.
	 */
	public String texteDescription(Calendar heureDebut) {

		if(this.etat == EtatLivraison.NON_CALCULE){
			return null;
		}
		else{


			String texte="\tLivraison " + this.id + " chez le client " + this.idClient+" � "+GestionCalendar.calendarToHeureString(this.heure);
			String texteTmp = "";
			if(this.etat == EtatLivraison.HORS_PLAGE_HORAIRE){
				texte += ", Hors plage horaire";
			}
			texte += "\r\n";
			texteTmp=this.trajetSuivant.texteDescription();
			if(this.trajetSuivant == null || texteTmp == null){
				return null;
			}
			texte+=texteTmp;
			return texte;
		}

	}

	/**
	 * La m�thode <code>testAdresse</code> v�rifie si la livraison a pour adresse un noeud existant.
	 * @param noeudAdr Noeud Adresse � tester.
	 * @return <code>true</code> si l'adresse de la livraison est valide, <code>false</code> sinon.
	 */
	public boolean testAdresse(Noeud noeudAdr){
		return this.adresse.equals(noeudAdr);
	}

	/**
	 * La m�thode <code>afficherLivraisonText</code> affiche
	 * diverses informations sur <code>this</code>.
	 * Utile uniquement pour la phase de test.
	 */
	public void afficherLivraisonText()
	{
		System.out.println("Livraison: "+"id="+ this.id+" adr="+this.adresse.getId());
	}

	/**
	 * Getter de <code>idClient</code>
	 * @return <code>this.idClient</code>
	 */
	public int getIdClient(){
		return this.idClient;
	}


}

