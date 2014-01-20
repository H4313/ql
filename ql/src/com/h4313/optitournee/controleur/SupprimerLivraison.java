//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.controleur;

import com.h4313.optitournee.modele.Itineraire;
import com.h4313.optitournee.modele.Livraison;
import com.h4313.optitournee.modele.Noeud;
import com.h4313.optitournee.modele.NoeudItineraire;
import com.h4313.optitournee.modele.PlageHoraire;
import com.h4313.optitournee.outils.Pair;

/**
 * La classe <code>SupprimerLivraison</code> est une commande qui supprime une livraison
 *
 * @author H4403
 */
public class SupprimerLivraison implements Commande {

	/**
	 * <code>Itineraire</code> sur lequel la commande est �x�cut�e et o� sera supprim�e la livraison
	 */
	private Itineraire itineraire;

	/**
	 * <code>Noeud</code> o� se trouve la livraison � supprimer
	 */
	private Noeud noeudSel;

	/**
	 * Objet <code>Livraison</code> qui est supprim�
	 */
	private Livraison livraisonSuppr;

	/**
	 * Noeud itin�raire pr�c�dent � la livraison supprim�e pour pouvoir la remettre
	 * en cas d'annulation
	 */
	private NoeudItineraire noeudItinerairePrecedent;

	/**
	 * Plage horaire de la livraison qui est supprim�e
	 */
	private PlageHoraire plageHoraireDeLaLivraison;

	/**
	 * la m�thode <code>SupprimerLivraison</code> est le constructeur de la commande
	 *
	 * @param iti Itin�raire sur lequel la suppression a lieu
	 * @param adr Noeud o� se trouve la livraison � supprimer
	 */
	public SupprimerLivraison(Itineraire iti, Noeud adr){
		this.itineraire = iti;
		this.noeudSel = adr;
		this.livraisonSuppr = null;
		this.plageHoraireDeLaLivraison = null;
		this.noeudItinerairePrecedent = null;
	}

	/**
	 * La m�thode <code>annuler</code> annule la suppression en r�ajoutant la livraison
	 * @return <code>true</code> si l'op�ration a r�ussie
	 */
	public boolean annuler() {
		if(this.livraisonSuppr !=null){
			this.itineraire.ajouterLivraison(this.livraisonSuppr, this.noeudItinerairePrecedent, this.plageHoraireDeLaLivraison);
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * La m�thode <code>refaire</code> supprime � nouveau la livraison si la commande a �t� annul�e
	 * @return <code>true</code> si l'op�ration a r�ussie
	 */
	public boolean refaire() {
		this.itineraire.supprimerLivraison(noeudSel);
		return this.livraisonSuppr!=null;
	}

	/**
	 * La m�thode <code>faire</code> r�cup�re les infos n�cessaires et supprime pour la premi�re fois
	 * la livraison
	 * @return <code>true</code> si l'op�ration a r�ussie
	 */
	public boolean faire() {
		Pair<Pair<Livraison, PlageHoraire>, NoeudItineraire> pairTmp = this.itineraire.supprimerLivraison(noeudSel);
		if(pairTmp == null) return false;
		this.livraisonSuppr = pairTmp.getFirst().getFirst();
		this.plageHoraireDeLaLivraison = pairTmp.getFirst().getSecond();
		this.noeudItinerairePrecedent = pairTmp.getSecond();
		return this.livraisonSuppr!=null;

	}

}

