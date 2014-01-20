//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.modele;

import java.util.*;

/**
 * La classe <code>Plan</code> repr�sente le plan de la ville
 * contenant les diff�rents noeuds d'intersections
 * 
 * @author H4403
 */
public class Plan {

	/**
	 * L'attribut noeuds repr�sente les intersections du plan
	 * 
	 * @see Noeud
	 */
	private List<Noeud> noeuds;

	/**
	 * Constructeur de la classe <code>Plan</code>
	 */
	public Plan()
	{
		noeuds = new ArrayList<Noeud>();
	}
	
	/**
	 * Constructeur de la classe <code>Plan</code>
	 * � partir de la liste des noeuds
	 * @param plan	Liste des noeuds du plan
	 */
	public Plan(List<Noeud> plan)
	{
		this.noeuds=plan;
	}
	
	
	/**
	 * La m�thode <code>ajouterNoeud</code> ajoute un noeud � la liste des noeuds du plan
	 * 
	 * @param nouveauNoeud Le noeud � ajouter
	 */
	public void ajouterNoeud(Noeud nouveauNoeud) {
		noeuds.add(nouveauNoeud);
	}

	/**
	 * Getter de <code>noeuds</code>
	 * @return <code>this.noeuds</code>
	 */
	public List<Noeud> getNoeuds()
	{
		return noeuds;
	}
	
	/**
	 * Setter de <code>noeuds</code>
	 * @param noeuds La nouvelle liste des noeuds
	 */
	public void setNoeuds(List<Noeud> noeuds)
	{
		this.noeuds = noeuds;
	}
	
	/**
	 * La m�thode <code>getCheminLePlusCourt</code> calcule le chemin le plus court
	 * entre un noeud de d�part et un noeud d'arriv�e.
	 * @param noeudDepart	Le noeud de depart
	 * @param noeudArrive	Le noeud d'arriv�e
	 * @return	La liste des tron�ons ordonn�s du chemin le plus court
	 */
	public List<Troncon> getCheminlePlusCourt(Noeud noeudDepart, Noeud noeudArrive) {
		List<Noeud> cheminLePlusCourtNoeuds = dijkstra(noeudDepart,noeudArrive);
		List<Troncon> cheminLePlusCourtTroncons = new ArrayList<Troncon>();
		for(int i=0 ; i<cheminLePlusCourtNoeuds.size()-1 ; i++) {
			cheminLePlusCourtTroncons.add(cheminLePlusCourtNoeuds.get(i).getTroncon(cheminLePlusCourtNoeuds.get(i+1)));
		}
		return cheminLePlusCourtTroncons;
	}
	
	/**
	 * La methode <code>dijkstra</code> calcule le plus court chemin 
	 * entre un noeud de d�part et un noeud d'arriv�e.
	 * 
	 * @param noeudDepart	Le noeud de d�part
	 * @param noeudArrive	Le noeud d'arriv�e
	 * @return La liste des noeuds ordonn�s du chemin le plus court 
	 * 			(contient les noeuds de depart et d'arriv�e)
	 */
	public List<Noeud> dijkstra(Noeud noeudDepart, Noeud noeudArrive) {
		boolean cheminTrouve = false;
		if(noeudDepart == null || noeudArrive == null) {
			return null;
		}
		List<Float> dist = new ArrayList<Float>();
		List<Boolean> visite = new ArrayList<Boolean>();
		List<Noeud> precedent = new ArrayList<Noeud>();
		for(int i = 0 ; i < this.noeuds.size() ; i++) {
			if(this.noeuds.get(i).equals(noeudDepart)) {
				dist.add((float)0);
			}
			else {
				dist.add(Float.POSITIVE_INFINITY);				
			}
			visite.add(false);
			precedent.add(null);
		}
		List<Noeud> noeudsNonVus = new ArrayList<Noeud>();
		noeudsNonVus.add(noeudDepart);
		while(!noeudsNonVus.isEmpty()) {
			Noeud noeudProche = noeudNonVisitePlusProche(noeudsNonVus,dist);
			if(noeudProche == null) {
				return null;
			}
			if(noeudProche.equals(noeudArrive)) {
				cheminTrouve = true;
				break;
			}
			noeudsNonVus.remove(noeudProche);
			visite.set(this.noeuds.indexOf(noeudProche),true);
			for(int i = 0 ; i < noeudProche.getTroncons_sortants().size() ; i++) {
				Troncon tronconSortant = noeudProche.getTroncons_sortants().get(i);
				float alt = dist.get(this.noeuds.indexOf(noeudProche)) + tronconSortant.getCoutTroncon();
				if(alt < dist.get(this.noeuds.indexOf(tronconSortant.getDestination()))) {
					dist.set(this.noeuds.indexOf(tronconSortant.getDestination()),alt);
					precedent.set(this.noeuds.indexOf(tronconSortant.getDestination()),noeudProche);
					if(visite.get(this.noeuds.indexOf(tronconSortant.getDestination())) == false) {
						noeudsNonVus.add(tronconSortant.getDestination());
					}
				}
			}
		}
		if(!cheminTrouve) {
			return null;
		}
		else {
			List<Noeud> cheminLePlusCourt = new ArrayList<Noeud>();
			Noeud u = noeudArrive;
			while(precedent.get(this.noeuds.indexOf(u)) != null) {
				cheminLePlusCourt.add(0,u);
				u = precedent.get(this.noeuds.indexOf(u));
			}
			cheminLePlusCourt.add(0,noeudDepart);
			return cheminLePlusCourt;	
		}
	}
	
	/**
	 * La methode <code>noeudNonVisitePlusProche</code> calcule le noeud non visit� le plus proche
	 * d'une source, les distances � la source etant stock�es dans la liste <code>dist</code>
	 * 
	 * @param noeudsNonVus	La liste des noeuds non visites
	 * @param dist			La liste des distances des noeuds a la source 
	 * @return Le noeud le plus proche
	 */
	private Noeud noeudNonVisitePlusProche(List<Noeud> noeudsNonVus, List<Float> dist) {
		int indexNoeud = -1;
		float distMin = Float.POSITIVE_INFINITY;
		for(int i = 0 ; i < noeudsNonVus.size() ; i++) {
			if(dist.get(this.noeuds.indexOf(noeudsNonVus.get(i))) < distMin) {
				distMin = dist.get(this.noeuds.indexOf(noeudsNonVus.get(i)));
				indexNoeud = i;
			}
		}
		if(indexNoeud == -1) {
			return null;
		}
		return noeudsNonVus.get(indexNoeud);
	}

	/**
	 * La m�thode <code>afficherPlanText</code> affiche
	 * diverses informations sur <code>this</code>.
	 * Utile uniquement pour la phase de test.
	 */
	public void affichePlanText()
	{
		for(Noeud n:this.noeuds)
		{
			System.out.println("Noeud:"+n.toString());
			for (Troncon t:n.getTroncons_sortants())
			{
				t.afficherTroncon();
			}
		}
	}
	
}

