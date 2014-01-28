package com.h4313.optitournee.modele;

import java.util.HashMap;

public class GestItineraire
{
	HashMap<Integer, Itineraire> itineraires;
	
	public GestItineraire()
	{
		this.reset();
	}
	
	public void ajouterItineraire(Itineraire itineraire)
	{
		itineraires.put(Integer.valueOf(itineraire.getItineraireId()), itineraire);
	}
	
	public void supprimerItineraire(Itineraire itineraire)
	{
		itineraires.remove(Integer.valueOf(itineraire.getItineraireId()));
	}
	
	public Itineraire getItineraire(int itineraireId)
	{
		return itineraires.get(Integer.valueOf(itineraireId));
	}
	
	public HashMap<Integer, Itineraire> getItineraires()
	{
		return itineraires;
	}
	
	public void reset()
	{
		itineraires = new HashMap<Integer, Itineraire>();
	}
}
