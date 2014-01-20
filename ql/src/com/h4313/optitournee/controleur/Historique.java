//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.controleur;

import java.util.*;
import com.h4313.optitournee.outils.Constantes;

/**
 * La classe <code>Historique</code> g�re un historique des commandes r�alis�es par l'utilisateur pour qu'il puisse
 * les annuler et les refaire
 *
 * @author H4403
 */
public class Historique {

	/**
	 * L'attribut <code>commandes</code> est une liste des commandes r�alis�es.
	 *
	 * @see Commande
	 */
	private List<Commande> commandes;

	/**
	 * L'attribut <code>indiceDerniereCommande</code> correspond � l'indice de <code>commandes</code>
	 * de la derni�re commande qui a �t� r�alis�e.
	 *
	 */
	private Integer indiceDerniereCommande;

	/**
	 * La m�thode <code>Historique</code> est le constructeur de la classe <code>Historique</code>
	 * Initialise une liste vide de commandes r�alis�es.
	 */
	public Historique(){
		this.commandes = new ArrayList<Commande>();
		this.indiceDerniereCommande = -1;
	}

	/**
	 * La m�thode <code>annulerOperation</code> annule la derniere op�ration s'il y en a une
	 *
	 * @return -1 si l'op�ration d'annulation n'a pas pu �tre r�alis�e. 0 sinon.
	 */
	public int annulerOperation() {
		if(!this.annulerPossible()){
			return -1;
		}
		else{
			this.commandes.get(indiceDerniereCommande).annuler();
			this.indiceDerniereCommande--;
			return 0;
		}
	}

	/**
	 * La m�thode <code>refaireOperation</code> refait l'op�ration qui vient d'�tre annul�e.
	 *
	 * @return -1 si l'op�ration d'annulation n'a pas pu �tre r�alis�e. 0 sinon.
	 */
	public int refaireOperation() {
		if(!this.refairePossible()){
			return -1;
		}
		else{
			this.commandes.get(indiceDerniereCommande+1).refaire();
			this.indiceDerniereCommande++;
			return 0;
		}
	}

	/**
	 * La m�thode <code>annulerPossible</code> v�rifie si une op�ration peut �tre annul�e
	 * @return <code>true</code> si on peut annuler
	 */
	public boolean annulerPossible(){
		return this.indiceDerniereCommande >= 0;
	}

	/**
	 * La m�thode <code>refairePossible</code> v�rifie si une op�ration peut �tre refaite
	 * @return <code>true</code> si on peut refaire
	 */
	public boolean refairePossible(){
		return this.indiceDerniereCommande < this.commandes.size()-1;
	}

	/**
	 * La m�thode <code>viderHistorique</code> vide compl�tement l'historique
	 *
	 */
	public void viderHistorique() {
		this.commandes.clear();
		this.indiceDerniereCommande = -1;
	}

	/**
	 * La m�thode <code>faire</code> ajoute une nouvelle commande � l'historique et l'ex�cute.
	 * Cette op�ration supprimera la <code>Commande</code> la plus ancienne de <code>commandes</code>
	 * si l'historique est plein.
	 * Cette op�ration supprimera la liste des commandes annul�es pr�c�demment.
	 *
	 * @param nouvCommande Commande qui sera ajout�e et execut�e.
	 * @return <code>true</code> si l'op�ration s'est bien d�roul�e
	 */
	public boolean faire(Commande nouvCommande) {
		if(nouvCommande != null){
			if(nouvCommande.faire()){

				//Suppression des commandes annul�es
				while(this.commandes.size() > this.indiceDerniereCommande+1){
					this.commandes.remove(commandes.size()-1);
				}

				//Suppression des commandes les plus vieilles si la taille max de l'historique est atteint
				while(this.commandes.size() >= Constantes.TAILLE_MAX_HISTORIQUE){
					this.commandes.remove(0);
					this.indiceDerniereCommande--;
				}

				this.commandes.add(nouvCommande);
				this.indiceDerniereCommande++;
				return true;
			}
		}
		return false;
	}

}

