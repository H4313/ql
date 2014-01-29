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
import com.h4313.optitournee.outils.Constantes;

public abstract class AffectationLivraison
{
	public static void affecter(GestItineraire gestItineraire, Plan plan, 
			NoeudItineraire entrepot, PlageHoraire plageHoraireInitiale)
	{
		
		List<Livraison> livraisonsInitiales = plageHoraireInitiale.getLivraisons();
		
		List<List<Livraison>> clusters = kmeans(livraisonsInitiales,2);
		boolean clustersValides = false;
		
		List<List<Livraison>> prochainsClusters = new ArrayList<List<Livraison>>();
		List<List<Livraison>> clustersFinaux = new ArrayList<List<Livraison>>();
		
		while(!clustersValides)
		{
			System.out.println("TEST "+clusters.size());
			prochainsClusters.clear();
			clustersValides = true;
			for(List<Livraison> cluster : clusters)
			{
				Double poidsTotal = 0.0;
				Double volumeTotal = 0.0;
				for(int i = 0 ; i < cluster.size() ; i++)
				{
					poidsTotal += cluster.get(i).getPoids();
					volumeTotal += cluster.get(i).getVolume();
				}
				if(poidsTotal > Constantes.POIDS_STOCK_DRONE_EN_KG
				   || volumeTotal > Constantes.VOLUME_STOCK_DRONE_EN_L)
				{
					System.out.println("COUCOU" + poidsTotal);
					if(cluster.size() == 1)
					{
						System.out.println("ERREUR");
					}
					List<List<Livraison>> newClusters = kmeans(cluster,2);
					prochainsClusters.add(newClusters.get(0));
					prochainsClusters.add(newClusters.get(1));
					clustersValides = false;
				}
				else
				{
					clustersValides = clustersValides && true;
					clustersFinaux.add(cluster);
				}
			}
			clusters = new ArrayList<List<Livraison>>(prochainsClusters);
		}
		System.out.println("NB CLUSTERS = "+clustersFinaux.size());
		
		for(int i = 0 ; i < clustersFinaux.size() ; i++)
		{
			PlageHoraire plage = new PlageHoraire(plageHoraireInitiale.getHeureDebut(), 
					plageHoraireInitiale.getHeureFin(), 
					clustersFinaux.get(i));
			List<PlageHoraire> plages = new ArrayList<PlageHoraire>();
			plages.add(plage);
			Itineraire itineraire = new Itineraire(plan, entrepot, plages);
			itineraire.setEtat(EtatItineraire.NON_CALCULE);
			gestItineraire.ajouterItineraire(itineraire);
		}

		
		
	/*	// Itineraire 1
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
		gestItineraire.ajouterItineraire(itineraire3);*/
	}
	
	private static List<List<Livraison>> kmeans(List<Livraison> livraisons, int k)
	{
		List<List<Livraison>> clusters = new ArrayList<List<Livraison>>();
		for(int i = 0 ; i < k ; i++)
		{
			clusters.add(new ArrayList<Livraison>());
		}
		List<Double> centroidX = new ArrayList<Double>();
		List<Double> centroidY = new ArrayList<Double>();
		Double eps = Double.POSITIVE_INFINITY;
		
		List<Livraison> tempLiv = new ArrayList<Livraison>(livraisons);
		
		for(int i = 0 ; i < k ; i++) 
		{
			int index = (int)(Math.random()*tempLiv.size());
			Livraison l = tempLiv.get(index);
			centroidX.add((double)l.getAdresse().getX());
			centroidY.add((double)l.getAdresse().getY());
			tempLiv.remove(index);
		}
		
		while(eps > 1.0)
		{
			for(int i = 0 ; i < k ; i++)
			{
				clusters.get(i).clear();
			}
			Double delta = 0.0;
			//Clusters
			for(Livraison l : livraisons)
			{
				Double lowestDistance = Double.POSITIVE_INFINITY;
				int closestCentroid = -1;
				for(int i = 0 ; i < k ; i++)
				{
					Double x = l.getAdresse().getX() - centroidX.get(i);
					Double y = l.getAdresse().getY() - centroidY.get(i);
					Double distance = Math.sqrt(x*x + y*y);
					if(distance < lowestDistance)
					{
						lowestDistance = distance;
						closestCentroid = i;
					}
				}
				clusters.get(closestCentroid).add(l);
			}
			//Recompute centroid
			for(int i = 0 ; i < k ; i++)
			{
				Double averageX = 0.0;
				Double averageY = 0.0;
				for(Livraison l : clusters.get(i))
				{
					averageX += l.getAdresse().getX();
					averageY += l.getAdresse().getY();
				}
				averageX /= clusters.get(i).size();
				averageY /= clusters.get(i).size();
				
				delta += averageX-centroidX.get(i) + averageY-centroidY.get(i);
				
				centroidX.set(i, averageX);
				centroidY.set(i,averageY);
			}
			eps = delta;
		}
		
		return clusters;
	}
}
