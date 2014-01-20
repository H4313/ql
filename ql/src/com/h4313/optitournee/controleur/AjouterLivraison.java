//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.controleur;

import com.h4313.optitournee.modele.Itineraire;
import com.h4313.optitournee.modele.Livraison;
import com.h4313.optitournee.modele.Noeud;
import com.h4313.optitournee.modele.NoeudItineraire;
import com.h4313.optitournee.modele.PlageHoraire;
import com.h4313.optitournee.outils.Pair;

/**
 * La classe AjouterLivraison est une commande pour ajouter une livraison
 * � celles d�j� charg�es.
 *
 * @author H4403
 */
public class AjouterLivraison implements Commande {

	/**
	 * Itin�raire sur lequel la commande est �x�cut�e et o� sera ajout� la livraison
	 */
	private Itineraire itineraire;

	/**
	 * Noeud o� sera ajout� la nouvelle livraison
	 */
	private Noeud noeudSel;

	/**
	 * Noeud o� se trouve la livraison(ou l'entrep�t) pr�c�dent
	 */
	private Noeud noeudPrec;

	/**
	 * Objet Livraison une fois cr�� par la commande
	 */
	private Livraison livraisonAjoutee;

	/**
	 * <code>NoeudItineraire</code> correspondant au <code>noeudPrec</code>
	 */
	private NoeudItineraire noeudItinerairePrecedent;

	/**
	 * La m�thode <code>AjouterLivraison</code> est le constructeur de la commande
	 *
	 * @param iti Itineraire sur lequel la commande sera appliqu�
	 * @param adr Noeud sur lequel la nouvelle livraison sera cr��e
	 * @param noeudPrec Noeud o� se trouve la livraison (ou l'entrep�t) pr�c�dent
	 */
	public AjouterLivraison(Itineraire iti, Noeud adr, Noeud noeudPrec){
		this.itineraire = iti;
		this.noeudSel = adr;
		this.noeudPrec = noeudPrec;
		this.livraisonAjoutee = null;
		this.noeudItinerairePrecedent = null;
	}

	/**
	 * La methode <code>annuler</code> annule l'ajout en supprimant la livraison ajout�e
	 *
	 * @return "true" si l'op�ration a r�ussie
	 */
	public boolean annuler() {
		this.itineraire.supprimerLivraison(this.livraisonAjoutee.getAdresse());
		return true;
	}

	/**
	 * La methode <code>refaire</code> refait l'ajout si la commande a �t� annul�e
	 *
	 * @return <code>true</code> si l'op�ration a r�ussie
	 */
	public boolean refaire() {
		this.itineraire.ajouterLivraison(this.livraisonAjoutee, this.noeudItinerairePrecedent, null);
		return true;
	}

	/**
	 * La methode <code>faire</code> r�cup�re les infos necessaires pour la nouvelle livraison et ajoute cette derni�re
	 *
	 * @return <code>true</code> si l'op�ration a r�ussie
	 */
	public boolean faire() {
		int idClient = this.itineraire.getProchainIdClient();
		this.livraisonAjoutee = new Livraison(idClient, this.noeudSel, idClient);
		Pair<Livraison, PlageHoraire> pairTmp = this.itineraire.rechercheLivraisonParNoeud(this.noeudPrec);

		if(pairTmp != null){
			this.noeudItinerairePrecedent = pairTmp.getFirst();
		}
		else{
			this.noeudItinerairePrecedent = this.itineraire.getEntrepot();
		}
		this.itineraire.ajouterLivraison(this.livraisonAjoutee, this.noeudItinerairePrecedent, null);


		return true;
	}

}

