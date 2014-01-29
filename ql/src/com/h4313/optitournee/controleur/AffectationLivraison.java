package com.h4313.optitournee.controleur;

import java.util.ArrayList;
import java.util.List;

import com.h4313.optitournee.modele.GestItineraire;
import com.h4313.optitournee.modele.Itineraire;
import com.h4313.optitournee.modele.Livraison;
import com.h4313.optitournee.modele.NoeudItineraire;
import com.h4313.optitournee.modele.PlageHoraire;
import com.h4313.optitournee.modele.Itineraire.EtatItineraire;
import com.h4313.optitournee.modele.Plan;

public abstract class AffectationLivraison
{
	public static void affecter(GestItineraire gestItineraire, Plan plan, 
			NoeudItineraire entrepot, PlageHoraire plageHoraireInitiale)
	{
		
		List<Livraison> livraisonsInitiales = plageHoraireInitiale.getLivraisons();
		
		// Itineraire 1
		List<Livraison> livraisons1 = new ArrayList<Livraison>();
		livraisons1.add(livraisonsInitiales.get(0));
		livraisons1.add(livraisonsInitiales.get(1));
		PlageHoraire plage1 = new PlageHoraire(plageHoraireInitiale.getHeureDebut(), 
											plageHoraireInitiale.getHeureFin(), 
											livraisons1);
		List<PlageHoraire> plages1 = new ArrayList<PlageHoraire>();
		plages1.add(plage1);
		Itineraire itineraire1 = new Itineraire(plan, entrepot, plages1);
		itineraire1.setEtat(EtatItineraire.NON_CALCULE);
		gestItineraire.ajouterItineraire(itineraire1);
		
		// Itineraire 2
		List<Livraison> livraisons2 = new ArrayList<Livraison>();
		livraisons2.add(livraisonsInitiales.get(2));
		livraisons2.add(livraisonsInitiales.get(3));
		PlageHoraire plage2 = new PlageHoraire(plageHoraireInitiale.getHeureDebut(), 
											plageHoraireInitiale.getHeureFin(), 
											livraisons2);
		List<PlageHoraire> plages2 = new ArrayList<PlageHoraire>();
		plages2.add(plage2);
		Itineraire itineraire2 = new Itineraire(plan, entrepot, plages2);
		itineraire2.setEtat(EtatItineraire.NON_CALCULE);
		gestItineraire.ajouterItineraire(itineraire2);
		

		// Itineraire 3
		List<Livraison> livraisons3 = new ArrayList<Livraison>();
		livraisons3.add(livraisonsInitiales.get(4));
		livraisons3.add(livraisonsInitiales.get(5));
		PlageHoraire plage3 = new PlageHoraire(plageHoraireInitiale.getHeureDebut(), 
											plageHoraireInitiale.getHeureFin(), 
											livraisons3);
		List<PlageHoraire> plages3 = new ArrayList<PlageHoraire>();
		plages3.add(plage3);
		Itineraire itineraire3 = new Itineraire(plan, entrepot, plages3);
		itineraire3.setEtat(EtatItineraire.NON_CALCULE);
		gestItineraire.ajouterItineraire(itineraire3);
	}
}
