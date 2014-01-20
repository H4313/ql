//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.modele;

import java.util.Calendar;

/**
 * La classe <code>NoeudItineraire</code> repr�sente un point du plan qui dispose
 * d'un statut particulier. Elle est la classe g�n�rique pour les livraisons. 
 * Elle repr�sente �galement l'objet m�tier Entrep�t.
 *
 * @author H4403
 */
public class NoeudItineraire {

	/**
	 * Le trajet pour se rendre au prochain <code>NoeudItin�raire</code>.
	 *
	 * @see Trajet
	 */
	protected Trajet trajetSuivant;

	/**
	 * Repr�sente le point du plan ou se situe ce <code>NoeudItin�raire</code>.
	 *
	 * @see Noeud
	 */
	protected Noeud adresse;

	/**
	 * Heure de passage dans le noeud
	 */
	protected Calendar heure;

	/**
	 * Constructeur vide de la classe <code>NoeudItineraire</code>
	 */
	public NoeudItineraire ()
	{
		this.adresse = null;
	}

	/**
	 * Constructeur de la classe <code>NoeudItineraire</code> 
	 * avec sp�cification de l'adresse.
	 * @param adresse Le noeud du plan repr�sentant l'adresse du NoeudItin�raire.
	 * @see Noeud
	 */
	public NoeudItineraire (Noeud adresse){
		this.adresse = adresse;
	}

	/**
	 * Getter de <code>adresse</code>
	 * @return <code>this.adresse</code>
	 */
	public Noeud getAdresse(){
		return adresse;
	}

	/**
	 * Setter de <code>adresse</code>
	 * @param newAdresse La nouvelle adresse du Noeud itin�raire
	 */
	public void setAdresse(Noeud newAdresse){
		this.adresse= newAdresse;
	}

	/**
	 * Setter de <code>trajet</code>
	 * @param traj Le nouveau trajet � suivre depuis ce noeud itin�raire
	 */
	public void setTrajet(Trajet traj){
		this.trajetSuivant=traj;
	}

	/**
	 * Getter de <code>trajetSuivant</code>
	 *
	 * @return <code>this.trajetSuivant</code>
	 */
	public Trajet getTrajet(){
		return trajetSuivant;
	}


	/**
	 * Getter de <code>heure</code>
	 * @return <code>this.heure</code>
	 */
	public Calendar getHeure() {
		return heure;
	}
	
	/**
	 * Setter de <code>heure</code>
	 * @param heure La nouvelle heure de passage
	 */
	public void setHeure(Calendar heure) {
		this.heure = heure;
	}

}

