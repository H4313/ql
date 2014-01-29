//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.controleur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.h4313.optitournee.outils.Pair;
import com.h4313.optitournee.controleur.parserXML.ParserXML;
import com.h4313.optitournee.controleur.parserXML.XmlParserException;
import com.h4313.optitournee.modele.*;
import com.h4313.optitournee.modele.Itineraire.EtatItineraire;
import com.h4313.optitournee.outils.Constantes.TypeFichier;
import com.h4313.optitournee.vue.VueApplication;

/**
 * La classe <code>Controleur</code> contr�le et organise l'ensemble de l'application.
 *
 * @author H4403
 */
public class Controleur {


	/**
	 * L'attribut <code>historique</code> permet de g�rer les commandes r�alis�es par l'utilisateur pour qu'il
	 * puisse les annuler et les refaire
	 *
	 * @see Historique
	 */
	private Historique historique;

	/**
	 * L'attribut <code>itineraire</code> repr�sente l'objet itin�raire du mod�le, c'est le point d'entr�e du mod�le
	 *
	 * @see Itineraire
	 */
	private Itineraire itineraireInitial;
	protected GestItineraire gestItineraire;

	/**
	 * L'attribut <code>vueAppli</code> est la vue principale de l'application et c'est avec elle que le controleur
	 * communique
	 */
	protected VueApplication vueAppli;


	/**
	 *	Objet <code>Controleur</code> unique qui est un singleton
	 */
	private static Controleur controleurSingleton = null;

	/**
	 * La m�thode <code>getControleur</code> permet de r�cup�rer l'instance unique du contr�leur
	 *
	 * @return le contr�leur
	 */
	public static Controleur getControleur(){
		if(controleurSingleton == null){
			controleurSingleton = new Controleur();
		}
		return controleurSingleton;
	}

	/**
	 * La m�thode <code>Controleur</code> est le constructeur priv� de la classe <code>Controleur</code>.
	 * Pour obtenir l'objet <code>controleur</code>, il faut r�cup�rer le singleton avec la m�thode <code>getControleur</code>
	 */
	private Controleur(){
		this.historique = new Historique();

		this.gestItineraire = new GestItineraire();

		this.vueAppli = new VueApplication(this);

	}

	/**
	 * La m�thode <code>chargerPlan</code> charge un fichier pass� en param�tre et cr�e les objets du mod�le
	 * pour repr�senter le plan en m�moire.
	 * Si le chargement �choue, un message est envoy� � l'utilisateur
	 *
	 * @param adresse Chemin d'acc�s du fichier XML d'entr�e
	 */
	public void chargerPlan(String adresse)
	{
		this.vueAppli.afficherCurseurAttente();
		try
		{
			ParserXML planParser= new ParserXML(adresse, TypeFichier.PLAN);
			this.itineraireInitial= new Itineraire(planParser.genererPlanDepuisXML());
			this.itineraireInitial.setEtat(EtatItineraire.ITINERAIRE_NON_CHARGE);

			// TEST
			this.vueAppli.setItineraireInitial(itineraireInitial);
			this.gestItineraire.ajouterItineraire(this.itineraireInitial);
			// TEST
			
			this.historique.viderHistorique();

			this.vueAppli.rafraichir();
//			this.vueAppli.afficherMessageInfo(Constantes.CHARGEMENT_PLAN_REUSSI);
		}
		catch (XmlParserException e)
		{
			e.printStackTrace();
			//this.vueAppli.afficherMessageErreur(Constantes.CHARGEMENT_PLAN_IMPOSSIBLE);
//			this.vueAppli.afficherMessageErreur(e.getMsgUtilisateur());
			//this.vueAppli.afficherMessageErreur(e.getMsgUtilisateur());
		}
		this.vueAppli.afficherCurseurNormal();
		
	}



	/**
	 * La m�thode <code>calculerTournee</code> cr�e et lance une commande <code>CalculerTournee</code>
	 * qui va calculer la toun�e entre les livraisons d�j� charg�es.
	 * Si le calcul �choue, un message est envoy� � l'utilisateur.
	 */
	public void calculerTournee()
	{
		this.vueAppli.afficherCurseurAttente();
		
		HashMap<Integer, Itineraire> itineraires = this.gestItineraire.getItineraires();
		Set<Map.Entry<Integer, Itineraire>> set = itineraires.entrySet();

		for(Map.Entry<Integer, Itineraire> entry : set)
		{
			Itineraire itineraire = entry.getValue();
			
			Commande cmd = new CalculerTournee(this, itineraire);
			this.historique.faire(cmd);
		}
		
		this.vueAppli.afficherCurseurNormal();
		this.vueAppli.rafraichir();
	}

	/**
	 * La m�thode <code>augmenterTempsTSP</code> permet d'augmenter le temps de calcul
	 * du <code>TSP</code> si ce dernier est insuffisant.
	 */
	public void augmenterTempsTSP(){
		//this.itineraire.augmenterTempsTSP();
	}


	/**
	 * La m�thode <code>chargerLivraisons</code> charge un fichier XML et cr�e les objets du mod�le
	 * pour repr�senter les livraisons en m�moire.
	 * Si le chargement �choue, un message est envoy� � l'utilisateur
	 *
	 * @param adresse Chemin d'acc�s du fichier XML d'entr�e
	 */
	public void chargerLivraisons(String adresse)
	{
		this.vueAppli.afficherCurseurAttente();
		try
		{
			if (this.itineraireInitial != null)
			{
				ParserXML livraisonParser= new ParserXML(adresse,TypeFichier.LIVRAISONS);

				Pair<NoeudItineraire,ArrayList<PlageHoraire> > dataLIvr = 
						livraisonParser.genererLivraisonsDepuisXML(this.itineraireInitial.getPlan().getNoeuds());
				this.itineraireInitial.setPlagesHoraire(dataLIvr.getSecond());
				this.itineraireInitial.setEntrepot(dataLIvr.getFirst());
				this.itineraireInitial.setEtat(EtatItineraire.NON_CALCULE);

				/********* AJOUT QL ***********/
				this.vueAppli.resetDessin();
				this.gestItineraire.reset();
				this.gestItineraire.supprimerItineraire(this.itineraireInitial);
				Plan plan = this.itineraireInitial.getPlan();
				NoeudItineraire entrepot = this.itineraireInitial.getEntrepot();
				List<PlageHoraire> plagesHoraireInitiales = this.itineraireInitial.getPlagesHoraire();
				PlageHoraire plageHoraireInitiale = plagesHoraireInitiales.get(0);
				
				AffectationLivraison.affecter(this.gestItineraire, plan, entrepot, plageHoraireInitiale);
				
				/********** AJOUT QL **********/
				
				this.historique.viderHistorique();

				this.vueAppli.rafraichir();
//				this.vueAppli.afficherMessageInfo(Constantes.CHARGEMENT_LIVRAISONS_REUSSI);
			}
		}
		catch (XmlParserException e)
		{
			e.printStackTrace();
			//this.vueAppli.afficherMessageErreur(Constantes.CHARGEMENT_ITINERAIRE_IMPOSSIBLE);
//			this.vueAppli.afficherMessageErreur(e.getMsgUtilisateur());
			//this.vueAppli.afficherMessageErreur(e.getMsgUtilisateur());
		}
		this.vueAppli.afficherCurseurNormal();
	}

	/**
	 * La m�thode <code>getItineraire</code> est un getter pour obtenir le point d'entr�e du mod�le
	 * @return l'itin�raire, point d'entr�e du mod�le
	 */
	public Itineraire getItineraireInitial()
	{
		return this.itineraireInitial;
	}
	
	public HashMap<Integer, Itineraire> getItineraires() {
		return gestItineraire.getItineraires();
	}


}

