//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.modele;

import java.util.*;

/**
 * La classe <code>Noeud</code> repr�sente un noeud du plan.
 *
 * @author H4403
 */
public class Noeud {

	/**
	 * L'attribut <code>id</code> repr�sente l'identifiant du noeud sur le plan
	 */
	protected Integer id;

	/**
	 * L'attribut <code>x</code> repr�sente l'abscisse du noeud sur le plan
	 */
	protected Integer x;

	/**
	 * L'attribut <code>y</code> repr�sente l'ordonn�e du noeud sur le plan
	 */
	protected Integer y;

	/**
	 * L'attribut <code>troncons_sortants</code> repr�sente l'ensemble des tron�ons sortant du noeud
	 * @see Troncon
	 */
	private List<Troncon> troncons_sortants;

	/**
	 * Constructeur de la classe <code>Noeud</code>.
	 * On ne sp�cifie pas les noeuds sortants. La liste des troncons est cr�ee
	 * mais elle ne contient aucun troncon.
	 * 
	 * @param id l'identifiant du noeud
	 * @param abs l'abscisse du point sur le plan
	 * @param ord l'ordonn�e du point sur le plan
	 */
	public Noeud(Integer id, Integer abs, Integer ord) {
		this.id = id;
		this.x = abs;
		this.y = ord;
		this.troncons_sortants = new ArrayList<Troncon>();
	}

	/**
	 * Constructeur de copie de la classe <code>Noeud</code>.
	 * Il permet de cr�er un objet <code>Noeud</code> � partir d'un autre objet d�ja existant.
	 * @param n Noeud dont on veut faire la copie.
	 */
	public Noeud(Noeud n)
	{
		this.id = n.id;
		this.x = n.x;
		this.y = n.y;
		this.troncons_sortants = new ArrayList<Troncon>(n.troncons_sortants);
	}

	/**
	 * Constructeur de copie.
	 * Il permet de cr�er un objet <code>Noeud</code> � partir d'un autre objet d�ja existant.
	 * @param n Noeud dont on veut faire la copie.
	 * @param keep_troncon si <code>true</code>, copie aussi les tron�on sortants
	 */
	public Noeud(Noeud n, boolean keep_troncon)
	{
		this.id = n.id;
		this.x = n.x;
		this.y = n.y;
		if(keep_troncon)
		{
			this.troncons_sortants = new ArrayList<Troncon>(n.troncons_sortants);
		}
		else
		{
			this.troncons_sortants = new ArrayList<Troncon>();
		}
	}
	
	/**
	 * Constructeur de la classe <code>Noeud</code>
	 *
	 * @param id	L'id du noeud
	 * @param abs	L'abscisse du noeud
	 * @param ord	L'ordonn�e du noeud
	 * @param troncons	La liste des tron�on sortants du noeud
	 */
	public Noeud(Integer id, Integer abs, Integer ord, List<Troncon> troncons)
	{
		this.id = id;
		this.x = abs;
		this.y = ord;
		this.troncons_sortants=troncons;
	}
	
	/**
	 * La m�thode <code>ajouterTroncons_sortants</code> ajoute un tron�on 
	 * sortant � la liste des tron�ons sortants du noeud
	 *
	 * @param troncon Le tron�on sortant � ajouter � la liste des tron�ons sortants.
	 */
	public void ajouterTroncons_sortants(Troncon troncon) {
		this.troncons_sortants.add(troncon);
	}

	/**
	 * Getter de <code>troncons_sortants</code>
	 * @return this.troncons_sortants
	 */
	public List<Troncon> getTroncons_sortants() {
		return troncons_sortants;
	}

	/**
	 * La m�thode <code>getTroncon</code> renvoie un tron�on pr�cis 
	 * pr�sent dans <code>troncons_sortants</code> 
	 * selon la destination voulue
	 *
	 * @param destination	La destination recherch�e
	 * @return				Le troncon si trouv�, <code>null</code> sinon
	 */
	public Troncon getTroncon(Noeud destination) {
		for(Troncon t : troncons_sortants) {
			if(t.getDestination().equals(destination)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Getter de <code>id</code>
	 * @return <code>this.id</code>
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Setter de <code>id</code>
	 * @param id Le nouvel identifiant du noeud
	 * @deprecated Il est pr�f�rable de cr�er le noeud avec son identifiant et de ne plus
	 * modifier celui-ci.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Getter de <code>x</code>
	 * @return <code>this.x</code>
	 */
	public Integer getX() {
		return x;
	}

	/**
	 * Setter de <code>x</code>
	 * @param x La nouvelle abscisse du noeud
	 */
	public void setX(Integer x) {
		this.x = x;
	}

	/**
	 * Getter de <code>y</code>
	 * @return <code>this.y</code>
	 */
	public Integer getY() {
		return y;
	}

	/**
	 * Setter de <code>y</code>
	 * @param y La nouvelle ordonn�e du noeud
	 */
	public void setY(Integer y) {
		this.y = y;
	}

	/**
	 * Setter de <code>troncons_sortants</code>
	 * @param troncons_sortants La liste des troncons sortants du noeud
	 */
	public void setTroncons_sortants(List<Troncon> troncons_sortants) {
		this.troncons_sortants = troncons_sortants;
	}

	/**
	 *La m�thode <code>toString</code> retourne une liste
	 *textuelle d'informations sur le noeud.
	 *@return Texte d'information sur le noeud 
	 */
	@Override
	public String toString() {
		return "(" + id + ", " + x + ", " + y + ")";
	}

	/**
	 * La m�thode <code>equals</code> permet de comparer
	 * deux noeuds
	 * @param obj Le noeud � comparer � <code>this</code>
	 * 
	 * @return <code>true</code> si <code>this</code> et <code>obj</code> sont identiques,
	 * 			<code>false</code> sinon.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Noeud other = (Noeud) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}
	
	/**
	 * Verifie si deux noeux ont les m�mes coordonn�es. Si oui
	 * on renvoi Vrai.
	 * @param noeud Le noeud avec lequel effectuer la comparaison.
	 * @return True s'ils ont les m�mes cooredonn�es et false sinon.
	 */
	public boolean memeCordonnes(Noeud noeud)
	{
		return (this.x==noeud.getX() && this.y==noeud.getY());
	}

}
