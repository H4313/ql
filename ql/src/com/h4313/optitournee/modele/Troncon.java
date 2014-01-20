//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.modele;

import com.h4313.optitournee.outils.Constantes;

//TODO Remplir la documentation pour la classe Troncon
/**
 * La classe <code>Troncon</code> repr�sente la route entre deux noeuds du plan
 *
 * @author H4403
 */
public class Troncon {

	/**
	 * L'attribut <code>nomRue</code> repr�sente le nom de la rue.
	 */
	private String nomRue;

	/**
	 * L'attribut <code>vitesse</code> repr�sente la vitesse 
	 * de d�placement sur le tron�on en m�tres/seconde
	 *
	 */
	private Float vitesse;

	/**
	 * L'attribut <code>longueur</code> repr�sente la longueur 
	 * du tron�on en m�tres.
	 *
	 */
	private Float longueur;

	/**
	 * L'attribut <code>nbPassage</code> repr�sente le nombre
	 * de fois que ce tron�on est parcouru pendant la tourn�e
	 *
	 */
	private Integer nbPassage;

	/**
	 * L'attribut <code>destination</code> repr�sente le noeud 
	 * sur lequel d�bouche ce tron�on.
	 *
	 * @see Noeud
	 */
	private Noeud destination;

	/**
	 * Constructeur de la classe <code>Troncon</code> avec les 
	 * informations tir�es du fichier XML
	 *
	 * @param nomRue	Nom de la rue
	 * @param vitesse	Vitesse du tron�on
	 * @param longueur	Longueur du tron�on
	 * @param noeudDestination	Noeud de destination
	 */
	public Troncon(String nomRue,Float vitesse, Float longueur, Noeud noeudDestination)
	{
		this.nomRue=nomRue;
		this.vitesse= vitesse;
		this.longueur= longueur;
		this.destination= noeudDestination;
		this.nbPassage=0;
	}

	/**
	 * Setter de <code>destination</code>
	 * @param dest	Destination du tron�on
	 */
	public void setDestination(Noeud dest){
		this.destination= dest;
	}

	/**
	 * Getter de <code>destination</code>
	 * @return <code>this.destination</code>
	 */
	public Noeud getDestination(){
		return  this.destination;
	}

	/**
	 * Getter de <code>nbPassage</code>
	 * @return <code>this.nbPassage</code>
	 */
	public Integer getNbPassage() {
		return nbPassage;
	}

	/**
	 * Setter de <code>nbPassage</code>
	 * @param nbPassage Nombre de passages sur le tron�on
	 */
	public void setNbPassage(Integer nbPassage) {
		this.nbPassage = nbPassage;
	}

	/**
	 * La m�thode <code>getTempsTroncon</code> permet d'obtenir
	 * le temps de parcours du tron�on en secondes.
	 * @return Float Le temps de parcours du tron�on en secondes
	 */
	public Float getCoutTroncon() {
		return (this.longueur*Constantes.COUT_AU_METRE_EN_EURO);
	}

	/**
	 * La m�thode <code>texteDescription</code> g�n�re un texte de description
	 * du tron�on
	 *
	 * @return Le texte de description.
	 */
	public String texteDescription(){
		return this.nomRue + ", " + this.longueur + " m.";
	}


	/**
	 * la m�thode <code>afficherTroncon</code> affiche des
	 * informations diverses sur le tron�on.
	 * Utile uniquement dans la phase de tests.
	 */
	public void afficherTroncon(){
		System.out.print("	Tron�on:"+this.nomRue + ", " + this.longueur + " m. \r\n");
	}
}

