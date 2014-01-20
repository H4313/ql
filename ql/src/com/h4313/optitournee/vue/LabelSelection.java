package com.h4313.optitournee.vue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JLabel;

import com.h4313.optitournee.controleur.Controleur;
import com.h4313.optitournee.modele.Itineraire;
import com.h4313.optitournee.modele.Livraison;
import com.h4313.optitournee.modele.Livraison.EtatLivraison;
import com.h4313.optitournee.modele.Noeud;
import com.h4313.optitournee.modele.PlageHoraire;
import com.h4313.optitournee.outils.Constantes;
import com.h4313.optitournee.outils.GestionCalendar;
import com.h4313.optitournee.outils.Pair;

/**
 * La classe <code>LabelSelection</code> g�re l'affichage
 * de donn�es textuelles des noeuds.s
 *
 * @author H4403
 */
//Objet non serialise -> on n'a pas besoin de num�ro de version
@SuppressWarnings("serial")
public class LabelSelection extends JLabel {

	private enum TypeNoeud {AUCUN, SIMPLE, ENTREPOT, LIVRAISON};

	private Controleur manager;
	private HashMap<Noeud, Pair<TypeNoeud, Pair<Livraison, PlageHoraire>>> nodes;
	private TypeNoeud type_last_node;

	public LabelSelection(Controleur mgr) {
		manager = mgr;
		nodes = new HashMap<Noeud, Pair<TypeNoeud, Pair<Livraison, PlageHoraire>>>();
		update();
	}


	public boolean testNoeudSimple(){
		return type_last_node == TypeNoeud.SIMPLE;
	}

	public boolean testNoeudEntrepot(){
		return type_last_node == TypeNoeud.ENTREPOT;
	}

	public boolean testNoeudLivraison(){
		return type_last_node == TypeNoeud.LIVRAISON;
	}

	public void setNoeud(ArrayList<Noeud> noeuds) {
		clear();
		if(noeuds == null)return;
		for(Noeud n : noeuds)
		{
			setNoeud(n);
		}
	}

	private void clear() {
		nodes.clear();
	}

	private void setNoeud(Noeud noeud) {
		TypeNoeud type;
		Pair<Livraison, PlageHoraire> livraisonSel = null;

		Itineraire itineraire = manager.getItineraire();

		if (itineraire != null && noeud != null) {
			livraisonSel = itineraire.rechercheLivraisonParNoeud(noeud);

			if (livraisonSel == null) {
				if (itineraire.getEntrepot() != null &&
					noeud.equals(itineraire.getEntrepot().getAdresse())) {
					// Le point est l'entrepot
					type = TypeNoeud.ENTREPOT;
				}
				else {
					// Si le point n'est pas sur l'itineraire
					type = TypeNoeud.SIMPLE;
				}
			}
			else{
				// Si le point est sur l'itin�raire
				type = TypeNoeud.LIVRAISON;
			}
		}
		else{
			type = TypeNoeud.AUCUN;
		}
		nodes.put(noeud, new Pair<TypeNoeud, Pair<Livraison, PlageHoraire>>(type, livraisonSel));
		this.type_last_node = type;
	}

	public void update() {
		String message = "<html>";
		for (Entry<Noeud, Pair<TypeNoeud, Pair<Livraison, PlageHoraire>>> entry : nodes.entrySet())
		{
			message += getText(entry.getKey(), entry.getValue().getFirst(), entry.getValue().getSecond());
		}

		message += "</html>";
		this.setText(message);

	}

	private String getText(Noeud n, TypeNoeud type, Pair<Livraison, PlageHoraire> livraisonSel) {
	
		String message = "<br> <font color=\"green\">";

		if (type == TypeNoeud.AUCUN) {
			// Si aucun point n'est selectionn�

			message += Constantes.TEXT_NOSELECTION+"</font>";

		}
		else if (type == TypeNoeud.ENTREPOT) {
			// Si le point est l'entrepot

			message += Constantes.TEXT_WAREHOUSE + "</font>"+"<br>";
			message += "X:" + n.getX() + " Y:"
					+ n.getY();
			if(manager.getItineraire().testTourneeCalculee()){
				message += "<br>";
				message += "Heure de d�part: "
						+ GestionCalendar.calendarToHeureString(manager.getItineraire().getEntrepot().getHeure());
			}
		}
		else if(type == TypeNoeud.SIMPLE){

			// Si le point n'est pas sur l'itineraire

			message += Constantes.TEXT_SIMPLENODE + "</font>"+"<br>";
			message += "X:" + n.getX() + " Y:"
					+ n.getY();
		}
		else if(type == TypeNoeud.LIVRAISON) {
			// Si le point est sur l'itin�raire

			message += Constantes.TEXT_DELIVERYNODE + "</font>" + "<br>";
			message += "X:" + n.getX() + " Y:" + n.getY();
			message += "<br>" + livraisonSel.getSecond().textePlage();

			if (livraisonSel.getFirst().getEtat() != EtatLivraison.NON_CALCULE) {
				Calendar datePassage = livraisonSel.getFirst().getHeure();

				message += "<br>";

				if (livraisonSel.getFirst().getEtat() == EtatLivraison.HORS_PLAGE_HORAIRE) {
					message += "<font color=\"red\">";
				}

				message += "Heure de passage: "
						+ GestionCalendar.calendarToHeureString(datePassage);

				if (livraisonSel.getFirst().getEtat() == EtatLivraison.HORS_PLAGE_HORAIRE) {
					message += "</font>";
				}
			}
		}

		message += "</br>";
		return message;
	}

}
