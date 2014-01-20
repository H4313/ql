//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.modele;

import java.util.*;

//import optiFretCourly.modele.Livraison.EtatLivraison;

/**
 * La classe <code>Trajet</code> repr�sente une suite de tron�ons
 * � parcourir pour aller d'une livraison (ou de l'entrep�t) � une autre
 *
 * @author H4403
 */

public class Trajet {

	/**
	 * L'attribut <code>troncons</code> repr�sente la liste des tron�ons du trajet.
	 *
	 * @see Troncon
	 */
	private List<Troncon> troncons;

	/**
	 * La m�thode <code>setTronconsAPartirDeNoeuds</code> met � jour l'attribut <code>troncons</code>,
	 * � partir de la liste de noeuds parcourus dans le trajet.
	 * .
	 *
	 * @param noeuds	La liste des noeuds du trajet
	 */
	public void setTronconsAPartirDeNoeuds(List<Noeud> noeuds) {
		List<Troncon> tronconsAAjouter = new ArrayList<Troncon>();
		for(int i=0 ; i<noeuds.size()-1 ; i++) {
			tronconsAAjouter.add(noeuds.get(i).getTroncon(noeuds.get(i+1)));
		}
		this.troncons = tronconsAAjouter;
	}

	/**
	 * Constructeur de la classe <code>trajet</code> avec la liste
	 * des tron�ons qui composent le trajet.
	 * @param troncons La liste des des tron�ons qui composent le trajet.
	 */
	public Trajet(List<Troncon> troncons)
	{
		this.troncons = troncons;
	}

	/**
	 * Getter de <code>troncons</code>
	 * @return	<code>this.troncons</code>
	 */
	public List<Troncon> getTroncons()
	{
		return troncons;
	}

	/**
	 * La m�thode <code>getDestination</code> r�cup�re 
	 * la destination finale d'un trajet.
	 * @return Le noeud destination
	 */
	public Noeud getDestination(){
		int taille = this.troncons.size();
		if(taille > 0){
			return this.troncons.get(taille-1).getDestination();
		}
		return null;
	}

	/**
	 * La m�thode <code>texteDescription</code> g�n�re un texte de description
	 * du trajet
	 *
	 * @return Le texte de description, <code>null</code> si �chec.
	 */
	public String texteDescription() {
		String texte="";
		String texteTmp = "";
		for(int i=0; i<this.troncons.size(); i++){
			texte += "\t\t";
			texteTmp=this.troncons.get(i).texteDescription();
			if(texteTmp== null){
				return null;
			}
			texteTmp += "\r\n";
			texte+=texteTmp;
		}
		return texte;
	}

	/**
	 * La m�thode <code>getDureeTrajet</code> renvoie la dur�e en secondes de parcours du trajet
	 *
	 * @return	La dur�e du trajet en secondes
	 */
	public int getDureeTrajet() {
		int dureeTrajet = 0;
		for(int i=0 ; i<this.troncons.size() ; i++) {
			dureeTrajet += this.troncons.get(i).getCoutTroncon();
		}
		return dureeTrajet;
	}

}

