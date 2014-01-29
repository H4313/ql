//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.modele;

import java.util.*;

import com.h4313.optitournee.modele.Livraison.EtatLivraison;
import com.h4313.optitournee.outils.Constantes;
import com.h4313.optitournee.outils.GestionCalendar;
import com.h4313.optitournee.outils.Graph;
import com.h4313.optitournee.outils.GrapheEntreLivraisons;
import com.h4313.optitournee.outils.Pair;
import com.h4313.optitournee.outils.SolutionState;
import com.h4313.optitournee.outils.TSP;

/**
 * La classe <code>Itineraire</code> constitue le point d'entr�e du mod�le et permet la liaison
 * entre le plan de la ville d'un c�t�, et les livraisons de l'autre.
 *
 * @author H4403
 */
public class Itineraire {

	/**
	 * Le type <code>EtatItineraire</code> repr�sente l'�tat du mod�le.
	 *
	 */
	public static enum EtatItineraire{ PLAN_NON_CHARGE, ITINERAIRE_NON_CHARGE, NON_CALCULE, HORS_PLAGE_HORAIRE, PRET};

	/**
	 * L'attribut <code>tempxMaxTsp</code> repr�sente le temps de calcul maximum de la tourn�e.
	 */
	private static int tempsMaxTsp = Constantes.TEMPS_MAX_TSP_DEFAUT;

	/**
	 * La m�thode <code>augmenterTempsTSP</code> permet d'augmenter <code>tempsMaxTsp</code>
	 *
	 */
	public void augmenterTempsTSP(){
		tempsMaxTsp = Constantes.COEF_AUGM_TEMPS_TSP * tempsMaxTsp;
	}

	/**
	 * L'attribut <code>idDernerCLient</code> repr�sente l'id du dernier client enregistr�
	 */
	private int idDernierCLient;

	/**
	 * L'attribut <code>plagesHoraire</code> est la liste des plages horaires charg�es dans le mod�le.
	 *
	 * @see PlageHoraire
	 */
	private List<PlageHoraire> plagesHoraire;

	/**
	 * L'attribut <code>entrepot</code> est l'entrepot charg� dans le mod�le.
	 *
	 * @see NoeudItineraire
	 */
	private NoeudItineraire entrepot;

	/**
	 * L'attribut <code>plan</code> est le plan charg� dans le mod�le.
	 *
	 * @see Plan
	 */
	private Plan plan;

	private static int dernierItineraireId = 0;
	private int itineraireId;
	
	/**
	 * L'attribut <code>etat</code> est l'�tat du mod�le.
	 *
	 * @see EtatItineraire
	 */
	private EtatItineraire etat;

	/**
	 * Constructeur de la classe <code>Itineraire</code>
	 */
	public Itineraire()
	{
		this.itineraireId = Itineraire.dernierItineraireId++;
		this.plagesHoraire = null;
		this.entrepot = null;
		this.plan = null;
		this.idDernierCLient = -1;
		this.etat = EtatItineraire.PLAN_NON_CHARGE;
	}
	
	public Itineraire(Plan plan, NoeudItineraire entrepot, List<PlageHoraire> plages)
	{
		this.itineraireId = Itineraire.dernierItineraireId++;
		this.plagesHoraire = plages;
		this.entrepot = new NoeudItineraire(entrepot.getAdresse());
		this.plan = plan;
		this.idDernierCLient = -1;
		this.etat = EtatItineraire.PLAN_NON_CHARGE;
	}

	/**
	 * Constructeur de la classe <code>Itineraire</code>
	 *
	 * @param reseau	La liste des noeuds repr�sentant le plan de la ville.
	 */
	public Itineraire(List<Noeud> reseau)
	{
		this.itineraireId = Itineraire.dernierItineraireId++;
		this.plagesHoraire = null;
		this.entrepot = null;
		this.plan= new Plan(reseau);
		this.idDernierCLient = -1;
		this.etat = EtatItineraire.ITINERAIRE_NON_CHARGE;
	}
	
	public int getItineraireId()
	{
		return this.itineraireId;
	}

	/**
	 * Getter de <code>plagesHoraire</code>
	 *
	 * @return	<code>this.plagesHoraire</code>
	 */
	public List<PlageHoraire> getPlagesHoraire() {
		return plagesHoraire;
	}

	/**
	 * Setter de <code>plagesHoraire</code>
	 *
	 * @param plagesHoraire La liste des plages horaire
	 */
	public void setPlagesHoraire(List<PlageHoraire> plagesHoraire) {
		this.plagesHoraire = plagesHoraire;
		this.idDernierCLient = this.trouverIdDernierClient();
	}

	/**
	 * la m�thode <code>getProchainIdClient</code> retourne l'id du prochain client cr�able
	 * @return l'id du prochain client cr�able
	 */
	public int getProchainIdClient() {
		return this.idDernierCLient+1;
	}

	/**
	 * La m�thode <code>trouverIdDernierClient</code> cherche parmi les plages horaires de l'itin�raire, l'id du dernier client
	 * @return l'id du dernier client, -1 si aucun client
	 */
	private int trouverIdDernierClient(){
		if(this.plagesHoraire != null){
			int maxId = -1;
			for(PlageHoraire ph : this.plagesHoraire){
				int id = ph.trouverIdDernierClient();
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

	/**
	 * Getter de <code>entrepot</code>
	 *
	 * @return	<code>this.entrepot</code>
	 */
	public NoeudItineraire getEntrepot() {
		return entrepot;
	}

	/**
	 * Setter de <code>entrepot</code>
	 *
	 * @param entrepot L'entrepot
	 */
	public void setEntrepot(NoeudItineraire entrepot) {
		this.entrepot = entrepot;
	}

	/**
	 * Setter de <code>etat</code>
	 *
	 * @param etat Le nouvel �tat du mod�le
	 */
	public void setEtat(EtatItineraire etat) {
		this.etat = etat;
	}

	/**
	 * Getter de <code>plan</code>
	 *
	 * @return	<code>this.plan</code>
	 */
	public Plan getPlan()
	{
		return this.plan;
	}


	/**
	 * La m�thode <code>annulerCalcul</code> annule le calcul en remettant l'�tat �� non calcul� et en supprimant les trajets
	 */
	public void annulerCalcul(){
		for(PlageHoraire p : plagesHoraire) {
			for(Livraison l : p.getAllLivraison()) {
				l.setEtat(EtatLivraison.NON_CALCULE);
				l.setTrajet(null);
			}
		}
		this.entrepot.setTrajet(null);
		this.etat = EtatItineraire.NON_CALCULE;
	}


	/**
	 * La m�thode <code>calculerTournee</code> calcule la tourn�e �� effectuer
	 * L'ordre des livraisons dans les plages horaires sont modifi�s.
	 * Les trajets sont ajout�s aux livraisons, de m�me que les heures de passage
	 *
	 *  @return 0 si une solution optimale a �t� trouv�e,
	 *   1 si une solution non optimale a �t� trouv�e,
	 *   -1 si le calcul n'a pas r�ussi
	 *   -2 si le calcul n'a pas donn� de r�sultat dans le temps imparti
	 */
	public int calculerTournee() {
		//Calcul autoris�
		if(this.testItineraireCharger()) {
			int nbNoeuds=0;
			//Evaluation du nombre de livraisons et indexage des livraisons
			List<NoeudItineraire> indexNoeudsItineraire = new ArrayList<NoeudItineraire>();
			indexNoeudsItineraire.add(entrepot);
			for(PlageHoraire p : plagesHoraire) {
				for(Livraison l : p.getAllLivraison()) {
					indexNoeudsItineraire.add(l);
					nbNoeuds++;
				}
			}
			int [][] arcsGraphe = new int[nbNoeuds+1][nbNoeuds+1];
			//Arc inexistant => -1
			for(int i = 0 ; i<nbNoeuds+1 ; i++) {
				for(int j=0 ; j<nbNoeuds+1 ; j++) {
					arcsGraphe[i][j] = -1;
				}
			}

			//Recherche de la premiere plage horaire non vide
			int idxProchainePlageHoraireNonVide = this.getIdxProchainePlageHoraireNonVide(-1);
			if(idxProchainePlageHoraireNonVide == -1){
				//toutes les plages horaires sont vides : calcul impossible
				return -1;
			}

			//Liens entre entrepot et premiere plage horaire
			for(Livraison l : plagesHoraire.get(idxProchainePlageHoraireNonVide).getAllLivraison()) {
				List<Noeud> plusCourtChemin = plan.dijkstra(entrepot.adresse, l.adresse);
				int distanceMin = 1;
				for(int i=0 ; i<plusCourtChemin.size()-1 ; i++) {
					 distanceMin += Math.round(plusCourtChemin.get(i).getTroncon(plusCourtChemin.get(i+1)).getCoutTroncon());
				}
				arcsGraphe[0][indexNoeudsItineraire.indexOf(l)] = distanceMin;
			}

			//Liens entre livraisons d'une plage horaire entre eux et avec
			//livraisons de la plage horaire suivante
			//for(int i=0 ; i<idxDernierPlageHoraireNonVide ; i++) {

			int idxPlageCourante = idxProchainePlageHoraireNonVide;
			idxProchainePlageHoraireNonVide = this.getIdxProchainePlageHoraireNonVide(idxProchainePlageHoraireNonVide);
			while(idxProchainePlageHoraireNonVide != -1){
				PlageHoraire p = plagesHoraire.get(idxPlageCourante);
				for(Livraison l : p.getAllLivraison()) {
					for(Livraison l2 : p.getAllLivraison()) {
						if(!l2.equals(l)) {
							List<Noeud> plusCourtChemin = plan.dijkstra(l.adresse, l2.adresse);
							int distanceMin = 0;
							for(int j=0 ; j<plusCourtChemin.size()-1 ; j++) {
								 distanceMin += Math.round(plusCourtChemin.get(j).getTroncon(plusCourtChemin.get(j+1)).getCoutTroncon());
							}
							arcsGraphe[indexNoeudsItineraire.indexOf(l)][indexNoeudsItineraire.indexOf(l2)] = distanceMin;
						}
					}
					PlageHoraire p2 = plagesHoraire.get(idxProchainePlageHoraireNonVide);
					for(Livraison l2 : p2.getAllLivraison()) {
						if(!l2.equals(l)) {
							List<Noeud> plusCourtChemin = plan.dijkstra(l.adresse, l2.adresse);
							int distanceMin = 0;
							for(int j=0 ; j<plusCourtChemin.size()-1 ; j++) {
								 distanceMin += Math.round(plusCourtChemin.get(j).getTroncon(plusCourtChemin.get(j+1)).getCoutTroncon());
							}
							arcsGraphe[indexNoeudsItineraire.indexOf(l)][indexNoeudsItineraire.indexOf(l2)] = distanceMin;
						}
					}
				}

				//On passe �� la plage suivante
				idxPlageCourante = idxProchainePlageHoraireNonVide;
				idxProchainePlageHoraireNonVide = this.getIdxProchainePlageHoraireNonVide(idxProchainePlageHoraireNonVide);
			}

			//Liens entre livraisons de la derniere plage horaire entre eux et avec
			//l'entrepot
			PlageHoraire p = plagesHoraire.get(idxPlageCourante);
			for(Livraison l : p.getAllLivraison()) {
				for(Livraison l2 : p.getAllLivraison()) {
					if(!l2.equals(l)) {
						List<Noeud> plusCourtChemin = plan.dijkstra(l.adresse, l2.adresse);
						int distanceMin = 1;
						for(int j=0 ; j<plusCourtChemin.size()-1 ; j++) {
							 distanceMin += Math.round(plusCourtChemin.get(j).getTroncon(plusCourtChemin.get(j+1)).getCoutTroncon());
						}
						arcsGraphe[indexNoeudsItineraire.indexOf(l)][indexNoeudsItineraire.indexOf(l2)] = distanceMin;
					}
				}
				List<Noeud> plusCourtChemin = plan.dijkstra(l.adresse, entrepot.adresse);
				int distanceMin = 0;
				for(int j=0 ; j<plusCourtChemin.size()-1 ; j++) {
					 distanceMin += Math.round(plusCourtChemin.get(j).getTroncon(plusCourtChemin.get(j+1)).getCoutTroncon());
				}
				arcsGraphe[indexNoeudsItineraire.indexOf(l)][0] = distanceMin;
			}

			Graph grapheLivraisons = new GrapheEntreLivraisons(nbNoeuds+1,arcsGraphe);

			TSP tsp = new TSP(grapheLivraisons);
			//Resolution du TSP en tempsMaxTsp maximum
			tsp.solve(tempsMaxTsp, grapheLivraisons.getNbVertices() * grapheLivraisons.getMaxArcCost()+1);
			if(tsp.getSolutionState() == SolutionState.NO_SOLUTION_FOUND){
				//Pas de solution trouv�e dans le temps impartie
				return -2;
			}
			else if(tsp.getSolutionState() == SolutionState.INCONSISTENT) {
				//Pas de solution
				return -1;
			}


			List<NoeudItineraire> noeudsOrdonnes = new ArrayList<NoeudItineraire>();
			noeudsOrdonnes.add(entrepot);
			int i = 0;
			do {
				noeudsOrdonnes.add(indexNoeudsItineraire.get(tsp.getNext()[i]));
				i = tsp.getNext()[i];
			} while(i!=0);
			remplirTournee(noeudsOrdonnes);

			if(tsp.getSolutionState() == SolutionState.SOLUTION_FOUND){
				//Solution trouv�e mais pas optimale
				return 1;
			}
			else if(tsp.getSolutionState() == SolutionState.OPTIMAL_SOLUTION_FOUND) {
				//Solution optimale trouv�e
				return 0;
			}
			return 0;
		}
		return -1;
	}


	/**
	 * Recup�re l'index de la prochaine plage horaire non vide
	 * @param idxCourant index de la plage courante
	 * @return l'index de la prochaine plage horaire non vide
	 * ou -1 si elles sont toutes vides
	 */
	private int getIdxProchainePlageHoraireNonVide(int idxCourant){
		if(idxCourant < -1) return -1;

		for(int idx = idxCourant+1; idx < this.plagesHoraire.size(); idx++){
			if(!this.plagesHoraire.get(idx).estVide()){
				return idx;
			}
		}
		return -1;
	}

	/**
	 * La methode remplirTournee modifie l'ordre des <code>livraisons</code>,
	 * de chaque <code>PlageHoraire</code> et attribue a chacune de ces livraisons,
	 * le trajet suivant.
	 *
	 * @param noeuds	Le tableau ordonn� des livraisons
	 */
	private void remplirTournee(List<NoeudItineraire> noeuds) {
		int indexNoeuds = 0;
		//Entrepot
		List<Troncon> tronconsEntrepot = plan.getCheminlePlusCourt(noeuds.get(indexNoeuds).adresse,noeuds.get(indexNoeuds+1).adresse);
		indexNoeuds++;
		Trajet trajetEntrepot = new Trajet(tronconsEntrepot);
		entrepot.setTrajet(trajetEntrepot);
		etat = EtatItineraire.PRET;
		for(PlageHoraire p : this.plagesHoraire) {
			List<Livraison> livraisonsOrdonnees = new ArrayList<Livraison>();
			for(int i=indexNoeuds ; i<indexNoeuds+p.getAllLivraison().size() ; i++) {
				livraisonsOrdonnees.add((Livraison)noeuds.get(i));
			}
			p.setLivraisons(livraisonsOrdonnees);
			for(int i=0 ; i<p.getAllLivraison().size() ; i++) {
				List<Troncon> tronconsTrajet = plan.getCheminlePlusCourt(noeuds.get(indexNoeuds).adresse,noeuds.get(indexNoeuds+1).adresse);
				indexNoeuds++;
				Trajet trajet = new Trajet(tronconsTrajet);
				p.getLivraison(i).setTrajet(trajet);
			}
		}
		calculerHoraires();
	}


	/**
	 * La m�thode <code>calculerHoraires</code> modifie les horaires de passage
	 * de chaque livraison selon l'ordre de passage d�termin� auparavant.
	 *
	 */
	void calculerHoraires(){

		etat = EtatItineraire.PRET;
		int idxPlageCourante = this.getIdxProchainePlageHoraireNonVide(-1);
		if(idxPlageCourante != -1){


			Calendar heureLivraison = (Calendar) this.plagesHoraire.get(idxPlageCourante).getHeureDebut().clone();

			//L'entrepot prend l'heure tel que la 1�re livraison se fasse au d�but de sa plage horaire
			Calendar heureEntrepot = (Calendar) heureLivraison.clone();
			GestionCalendar.ajouteSecondes(heureEntrepot, -1*this.entrepot.getTrajet().getDureeTrajet());
			this.entrepot.setHeure(heureEntrepot);

			while(idxPlageCourante != -1){
				for(Livraison liv : this.plagesHoraire.get(idxPlageCourante).getAllLivraison()){
					liv.setHeure((Calendar)heureLivraison.clone());

					if(!this.plagesHoraire.get(idxPlageCourante).testDansPlageHoraire(heureLivraison)) {
						liv.setEtat(EtatLivraison.HORS_PLAGE_HORAIRE);
						etat = EtatItineraire.HORS_PLAGE_HORAIRE;
					}
					else {
						liv.setEtat(EtatLivraison.POSSIBLE);
					}

					GestionCalendar.ajouteSecondes(heureLivraison, liv.getTrajet().getDureeTrajet() + Constantes.DUREE_ATTENTE_LIVRAISON);
				}

				idxPlageCourante = this.getIdxProchainePlageHoraireNonVide(idxPlageCourante);
				//Si on est en avance, on decale les horaires pour que la 1�re livraison se fasse au d�but de sa plage horaire
				if(idxPlageCourante != -1){
					if(heureLivraison.before(this.plagesHoraire.get(idxPlageCourante).getHeureDebut())){
						heureLivraison = (Calendar) this.plagesHoraire.get(idxPlageCourante).getHeureDebut().clone();
					}
				}
			}
		}
	}

	/**
	 * La m�thode <code>ajouterLivraison</code> permet d'ajouter une livraison au mod�le
	 * en l'incluant dans le trajet et en r��valuant les heures de passage en cons�quence
	 *
	 * @param livraison La livraison �� ajouter
	 * @param noeudPrec La livraison pr�c�dent �� la livraison �� ajouter
	 * @param plage		La plage horaire dans laquelle la livraison doit �tre ajout�e
	 */
	public void ajouterLivraison(Livraison livraison, NoeudItineraire noeudPrec, PlageHoraire plage) {
		if(this.testItineraireCharger()){

			if(plage == null){
				if(noeudPrec == this.entrepot){
					plage = this.plagesHoraire.get(0);
				}
				else{
					Pair<Livraison, PlageHoraire> pairTmp = this.rechercheLivraisonParNoeud(noeudPrec.getAdresse());
					if(pairTmp != null){
						plage = pairTmp.getSecond();
					}
					else{
						plage = this.plagesHoraire.get(0);
					}

				}
			}
			//Ajout autoris�
			plage.ajoutLivraison(livraison, noeudPrec);

			if(this.testTourneeCalculee()){
				Trajet nouvTrajetAvant = new Trajet(this.plan.getCheminlePlusCourt(noeudPrec.getAdresse(), livraison.getAdresse()));
				Trajet nouvTrajetApres = new Trajet(this.plan.getCheminlePlusCourt(livraison.getAdresse(), noeudPrec.getTrajet().getDestination()));
				livraison.setTrajet(nouvTrajetApres);
				noeudPrec.setTrajet(nouvTrajetAvant);
				calculerHoraires();
			}



		}
	}

	/**
	 * La m�thode <code>supprimerLivraison</code> supprime une livraison du mod�le
	 * en modifiant les trajets et les heures de passage en cons�quence.
	 *
	 * @param noeudSel Le noeud sur lequel la livraison doit �tre supprim�e
	 *
	 * @return couple(couple(livraison supprim�e, plage horaire), livraison pr�c�dente)
	 *
	 */
	public Pair<Pair<Livraison, PlageHoraire>, NoeudItineraire> supprimerLivraison(Noeud noeudSel) {
		if(this.testItineraireCharger()){
			//Suppression autoris�e
			Livraison liv = null;
			for(PlageHoraire ph : this.plagesHoraire){
				liv = ph.rechercheLivraison(noeudSel);
				if(liv != null){
					//Livraison supprimer
					Pair<Livraison, PlageHoraire> pairLiv = new Pair<Livraison, PlageHoraire>(liv, ph);
					if(this.testTourneeCalculee()){
						//La tourn�e est d�j�� calcul�e
						NoeudItineraire noeudPrecedent = this.rechercheNoeudLivraisonPrecedent(liv);
						Trajet nouvTrajet = new Trajet(this.plan.getCheminlePlusCourt(noeudPrecedent.getAdresse(), liv.getTrajet().getDestination()));

						noeudPrecedent.setTrajet(nouvTrajet);
						ph.supprimerLivraison(liv);

						calculerHoraires();
						return new Pair<Pair<Livraison, PlageHoraire>, NoeudItineraire>(pairLiv, noeudPrecedent);
					}
					else{
						//La tourn�e n'est pas encore calcul�e
						ph.supprimerLivraison(liv);
						return new Pair<Pair<Livraison, PlageHoraire>, NoeudItineraire>(pairLiv, null);
					}
				}
			}
		}
		return null;
	}

	/**
	 * La m�thode <code>rechercherLivraisonparNoeud</code> recup�re une livraison �� l'adresse d'un noeud.
	 * @param noeudAdr Noeud sur lequel la livraison est recherch�e
	 * @return La livraison sur le noeud, <code>null</code> si aucune livraison n'est trouve�
	 */
	public Pair<Livraison, PlageHoraire> rechercheLivraisonParNoeud(Noeud noeudAdr){
		if(this.testItineraireCharger()){
			for(PlageHoraire ph : this.plagesHoraire){
				Livraison liv = ph.rechercheLivraison(noeudAdr);
				if(liv != null){
					return new Pair<Livraison, PlageHoraire>(liv, ph);
				}
			}
		}
		return null;
	}


	/**
	 * La m�thode <code>rechercheNoeudLivraisonPrecedent</code> r�cup�re
	 * le <code>NoeudLivraison</code> pr�c�dent une livraison
	 * @param livraison dont on cherche le <code>NoeudLivraison</code> pr�c�dent
	 * @return le <code>NoeudLivraison</code> pr�c�dent, <code>null</code> sinon
	 */
	public NoeudItineraire rechercheNoeudLivraisonPrecedent(Livraison livraison){
		int idxPremierePlageNonVide = 0;
		for(int idxPlage=0; idxPlage< this.plagesHoraire.size(); idxPlage++){
			List<Livraison> listeLivraison = this.plagesHoraire.get(idxPlage).getAllLivraison();
			if(listeLivraison.size() == 0){
				idxPremierePlageNonVide++;
			}
			else{
				int idxLiv = listeLivraison.indexOf(livraison);
				if(idxLiv != -1){
					//Livraison trouv�e
					if(idxLiv == 0){
						//premiere livraison de la plage horaire: on regarde la plage horaire precedente
						if(idxPlage == idxPremierePlageNonVide){
							//premiere plage horaire: on retourne l'entrepot
							return this.entrepot;
						}
						else{
							return this.plagesHoraire.get(idxPlage-1).getDerniereLivraison();
						}

					}
					else{
						return this.plagesHoraire.get(idxPlage).getLivraison(idxLiv-1);
					}
				}
			}
		}
		return null;
	}


	/**
	 * La m�thode <code>testPlanCharger</code> v�rifie si un plan est charg� dans l'itin�raire
	 * @return <code>true</code> si oui, <code>false</code> sinon.
	 */
	public boolean testPlanCharger(){
		return this.etat != EtatItineraire.PLAN_NON_CHARGE;
	}

	/**
	 * La m�thode <code>testItineraireCharger</code> v�rifie si un itin�raire est charg�
	 * @return <code>true</code> si oui, <code>false</code> sinon.
	 */
	public boolean testItineraireCharger(){
		return this.testPlanCharger() && (this.etat != EtatItineraire.ITINERAIRE_NON_CHARGE);
	}

	/**
	 * La m�thode <code>testTourneeCalculee</code> v�rifie si la tourn�e a �t� calcul�e
	 * @return <code>true</code> si oui, <code>false</code> sinon.
	 */
	public boolean testTourneeCalculee(){
		return this.testItineraireCharger() && (this.etat != EtatItineraire.NON_CALCULE);
	}

	/**
	 * La m�thode <code>texteDescription</code> g�n�re un texte qui d�crit l'itin�raire du livreur.
	 *
	 * @return Le texte de description, <code>null</code> en cas d'�chec.
	 */
	public String texteDescription(){
		String texte="\t\t\tFeuille de route\r\n";
		if(this.testTourneeCalculee()){
			String texteTmp = "";
			texte+="D�part de l'entrepot: ";
			texte+=GestionCalendar.calendarToHeureString(this.entrepot.getHeure());
			texte+="\r\n";
			texte+=entrepot.getTrajet().texteDescription();
			for(int i=0; i<this.plagesHoraire.size(); i++){
				if(!this.plagesHoraire.get(i).estVide()){
					texteTmp=this.plagesHoraire.get(i).texteDescription();
					if(texteTmp== null){
						return null;
					}
					texteTmp += "\r\n";
					texte += texteTmp;
				}
			}
			texte+="Retour �� l'entrepot";
			return texte;
		}
		else{
			return null;
		}
	}

	/**
	 * La m�thode <code>afficheLivraisonEtEntrepot</code> affiche
	 * diverses informations sur <code>this</code>.
	 * Utile uniquement pour la phase de test.
	 */
	public void afficheLivraisonEtEntrepot()
	{
		System.out.println("Entrepot: adresse="+this.entrepot.getAdresse().getId());
		for (PlageHoraire ph:this.plagesHoraire)
		{
			ph.afficherPlageHoraireText();
		}
	}

}

