//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.modele;

import java.awt.Color;
import java.util.*;

//import optiFretCourly.modele.Livraison.EtatLivraison;
import com.h4313.optitournee.outils.GestionCalendar;

/**
 * La classe <code>PlageHoraire</code> repr�sente une plage horaire de livraison
 * et contient les livraisons � effectuer dans cette plage.
 *
 * @author H4403
 */
public class PlageHoraire {

	/**
	 * Repr�sente la couleur � associer � la plage horaire
	 * lors de l'affichage.
	 *
	 * @see Color
	 */
	@SuppressWarnings("unused")
	private Color couleur;

	/**
	 * L'ensemble des livraisons qu'il y a � effectuer dans l'intervalle
	 * de temps de la plage horaire.
	 *
	 * @see Livraison
	 */
	private List<Livraison> livraisons;

	/**
	 * L'heure de d�but de la plage horaire.
	 *
	 * @see Calendar
	 */
	private Calendar heureDebut;


	/**
	 * L'heure de fin de la plage horaire.
	 *
	 * @see Calendar
	 */

	private Calendar heureFin;


	/**
	 * Constructeur de la classe <code>PlageHoraire</code>
	 * � partir de donn�es du fichier XML.
	 *
	 * @param debut Heure de d�but de la plage
	 * @param fin Heure de fin de la plage
	 * @param livraisons L'ensemble des livraisons � effectuer dans cette plage horaire.
	 */

	public PlageHoraire(Calendar debut, Calendar fin, List<Livraison> livraisons){
		this.heureDebut= debut;
		this.heureFin= fin;
		this.livraisons= livraisons;
		this.couleur= null;
	}

	/**
	 * Getter de <code>heureDebut</code>
	 * @return <code>this.heureDebut</code>
	 */
	public Calendar getHeureDebut() {
		return heureDebut;
	}
	
	public Calendar getHeureFin() {
		return heureFin;
	}

	/**
	 * Getter de <code>livraisons</code>
	 * @return <code>this.livraisons</code>
	 */
	public List<Livraison> getAllLivraison(){
		return this.livraisons;
	}


	/**
	 * Setter de <code>livraisons</code>
	 * @param livraisons La liste des nouvelles livraisons de la plage horaire
	 */
	public void setLivraisons(List<Livraison> livraisons) {
		this.livraisons = livraisons;
	}

	/**
	 * la m�thode <code>ajouterLivraison</code> permet d'ajouter une livraison
	 * � la liste de livraisons de la plage horaire.
	 * @param livraison La nouvelle livraison � ajouter.
	 * @param noeudPrec Le noeudItin�raire (Entrep�t ou livraison) pr�cedant cette livraison.
	 */
	public void ajoutLivraison(Livraison livraison, NoeudItineraire noeudPrec){
		int idx = this.livraisons.indexOf(noeudPrec);
		this.livraisons.add(idx+1, livraison);
	}

	/**
	 * La m�thode <code>getLivraison</code> permet d'obtenir 
	 * une livraison de la plage horaire � partir de son identifiant
	 *
	 * @param idLivraison L'identifiant de la livraison recherch�e
	 * @return	La livraison si elle est trouv�e, <code>null</code> sinon
	 */
	public Livraison getLivraison(int idLivraison)
	{
		Livraison livr=null;
		try
		{
			livr=this.livraisons.get(idLivraison);
		}
		catch(IndexOutOfBoundsException ioob)
		{
			ioob.printStackTrace();
			System.out.println("L'indexe est plus grand que la taille du tableau: IndexOutOfBoundsException");
		}
		return livr;
		
	}

	public List<Livraison> getLivraisons()
	{
		return livraisons;
	}
	
	/**
	 * La m�thode <code>estVide</code> v�rifie si la plage horaire est vide
	 * @return <code>true</code> si la plage horaire est vide, <code>false</code> sinon
	 */
	public boolean estVide(){
		return this.livraisons.isEmpty();
	}

	/**
	 * La m�thode <code>texteDescription</code> g�n�re un texte de description
	 * de la plage horaire comprenant la liste des livraisons de la plage.
	 *
	 * @return Le texte de description.
	 */
	public String texteDescription() {

		String texteTmp = textePlage();

		for(int i=0; i<this.livraisons.size(); i++){
				texteTmp+=this.livraisons.get(i).texteDescription(this.heureDebut);
		}
		return texteTmp;
	}

	/**
	 *La m�thode <code>textePlage</code> retourne une liste
	 *textuelle d'informations sur la plage horaire.
	 *@return Texte d'information sur la plage
	 */
	public String textePlage()
	{
		return "Plage horaire de " + GestionCalendar.calendarToHeureString(this.heureDebut) + " � " + GestionCalendar.calendarToHeureString(this.heureFin) + "\r\n";
	}


	/**
	 *La m�thode <code>textePlage</code> affiche des
	 *informations diverses sur la plage horaire
	 *Utile uniquement dans la phase de tests.
	 */
	public void afficherPlageHoraireText() {
		System.out.println("Plage horaire de " + GestionCalendar.calendarToHeureString(this.heureDebut) + " � " + GestionCalendar.calendarToHeureString(this.heureFin) + "\r\n");
		for(Livraison l:this.livraisons){
			l.afficherLivraisonText();
		}

	}

	/**
	 * 
	 */
	public void mettreAJourHoraire(){
		//int dureePlageHoraireEffective = 0;
		for(Livraison liv : this.livraisons){
			liv.getTrajet().getDureeTrajet();
		}
	}

	/**
	 * La m�thode <code>supprimerLivraison</code> supprime 
	 * une livraison de la plage horaire
	 * @param liv livraison � supprimer
	 * @return <code>true</code> si la livraison a �t� supprim�e, <code>false</code> sinon.
	 */
	public boolean supprimerLivraison(Livraison liv){
		return this.livraisons.remove(liv);
	}


	/**
	 * La m�thode <code>rechercheLivraison</code> recup�re
	 * la livraison de la plage horaire � un noeud pr�cis
	 * @param noeud adresse de la livraison � rechercher
	 * @return la livraison trouv�e, <code>null</code> sinon
	 */
	public Livraison rechercheLivraison(Noeud noeud){
		for(Livraison liv : this.livraisons){
			if(liv.testAdresse(noeud)){
				//Livraison trouv�e
				return liv;
			}
		}
		return null;
	}

	/**
	 * La m�thode <code>getDerniereLivraison</code> recup�re 
	 * la derni�re livraison de la plage horaire
	 * @return la derni�re livraison de la plage horaire,
	 * 			<code>null</code> si la plage horaire n'a pas de livraisons
	 */
	public Livraison getDerniereLivraison(){
		int taille = this.livraisons.size();
		if(taille > 0){
			return this.livraisons.get(taille-1);
		}
		return null;

	}
	/**
	 * La methode getDureeTotaleSecondes permet d'obtenir la dur�e de la plage horaire
	 * entre l'heure de debut et de fin, en secondes
	 *
	 * @return	La dur�e totale de la plage horaire en secondes
	 */
	public int getDureeTotaleSecondes() {
		return Math.round(heureFin.getTimeInMillis()-heureDebut.getTimeInMillis())/1000;
	}

	/**
	 * La m�thode <code>testDansPlageHoraire</code> v�rifie
	 * si une heure est dans la plage horaire
	 * @param heure	L'heure � tester
	 * @return <code>true</code> si une heure est dans la plage horaire, 
	 * 			<code>false</code> sinon.
	 */
	public boolean testDansPlageHoraire(Calendar heure){
		return GestionCalendar.entreDate(heure, heureDebut, heureFin);
	}

	/**
	 * La m�thode <code>trouverIdDernierClient</code> cherche 
	 * parmi les livraisons de la plage horaire, l'id du dernier client
	 * @return l'id du dernier client, -1 si aucun client
	 */
	public int trouverIdDernierClient(){
		if(this.livraisons != null){
			int maxId = -1;
			for(Livraison liv : this.livraisons){
				int id = liv.getIdClient();
				if(id > maxId){
					maxId = id;
				}
			}
			return maxId;
		}
		else{
			return -1;
		}
	}

}



