package com.h4313.optitournee.vue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.h4313.optitournee.modele.Noeud;
import com.h4313.optitournee.outils.Constantes;

/**
 * La classe <code>ZoneDessin</code> repr�sente une zone graphique
 * o� l'on dessine un plan et un itin�raire qui passe par
 * des points de ce plan.
 *
 * @author H4403
 *
 */

// Pas de s�rialisation
@SuppressWarnings("serial")
public class ZoneDessin extends JPanel {


	// Pointeur vers la VueApplication dans laquelle est contenu la ZoneDessin
	private VueApplication appli_mere;
	// Le plan que l'on doit afficher
	private VuePlan mon_plan;
	// L'itin�raire que l'on doit afficher sur le plan
	protected HashMap<Integer, VueItineraire> mes_itineraires;

	// Autorise le zoom
	private boolean haveZoom = Constantes.AUTORISE_ZOOM;
	// Autorise la translation
	private boolean haveTranslation = Constantes.AUTORISE_TRANSLATION;

	// Pas du zoom
	private float dzoom = Constantes.DELTA_ZOOM;

	// Liste des noeuds selectionne, associ� � un boolean qui dit si le noeud
	// appartient � l'itin�raire
	private ArrayList<Noeud> selectedNodes = new ArrayList<Noeud>();
	// Liste de boolean associes a la liste de noeud selectionne
	// et qui dit si le noeud selectionne associe appartient a l'itineraire
	private ArrayList<Boolean> node_are_on_itineraire = new ArrayList<Boolean>();

	/*
	 * Si vrai, tout les noeuds selectionne seront garde Si faux seul le dernier
	 * noeud selectionne est garde Mettre remanent a "true" alors qu'il �tait �
	 * "faux" supprime la liste des noeuds auparant selecionne
	 */
	private boolean remanent = false;

	// Coordonne du point ou a commencer le "drag" de la souris
	private int debut_drag_x, debut_drag_y;

	// Offset du dessin au moment ou a commenc� le "drag" de la souris"

	private float current_offset_x, current_offset_y;

	// Couleur du fond
	private final Color c_back = Constantes.COULEUR_FOND;
	// Couleur du cadre
	private final Color c_cadre = Constantes.COULEUR_CADRE;
	// Largeur du cadre
	private int larg_paint = Constantes.LARGEUR_CADRE;
	// Arrondi sur le cadre
	private boolean cadre_arrondi = Constantes.CADRE_ARRONDI;

	// Dis si la touche control est appuye
	private boolean control_key = false;


	/**
	 * Cr�er une zone de dessin
	 *
	 * @param mere
	 *            la VueApplication auquel appartient la zone de dessin
	 */
	public ZoneDessin(VueApplication mere) {
		
		this.mes_itineraires = new HashMap<Integer, VueItineraire>();
		
		this.appli_mere = mere;

		this.setFocusable(false);

		// Cr�ation de la bordure
		this.setBorder(BorderFactory.createLineBorder(c_cadre, larg_paint,
				cadre_arrondi));
	}

	// On surcharge la m�thode paintComponent de JPanel pour dessiner ce qui
	// nous int�resse
	public void paintComponent(Graphics g) {

		Set<Map.Entry<Integer, VueItineraire>> setItineraires = this.mes_itineraires.entrySet();
		VueItineraire mon_itineraire = null;
		
		// On augmente d'abord la qualit� du rendu (suppresion de l'aliasing
		// /ex)
		Graphics2D g2 = (Graphics2D) g;		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		// Puis on dessine le fond
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(c_back);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// On dessine ensuite les troncons puis les noeuds pour �viter
		// qu'un troncon ne recouvre un noeud.

		int size_selection = this.selectedNodes.size();

		// dessine tous les tron�ons
		// 1) Du plan
		if (mon_plan != null)
			mon_plan.dessineTousTroncon(g);
		// 2) Des itin�raires
		for(Map.Entry<Integer, VueItineraire> entry : setItineraires)
		{
			mon_itineraire = entry.getValue();
			
			if (mon_itineraire != null)
				mon_itineraire.dessineTousTroncon(g);
			// 3) Du noeud selectionne
			for (int i = 0; i < size_selection; i++) {
				if (node_are_on_itineraire.get(i)) {
					mon_itineraire.dessineTronconDeNoeudSelectionne(
							selectedNodes.get(i), g);
				} else {
					mon_plan.dessineTronconDeNoeudSelectionne(selectedNodes.get(i),
							g);
				}
			}
		}

		// dessine tous les noeuds :
		// 1) ...Du plan
		if (mon_plan != null)
			mon_plan.dessineTousNoeud(g);
		// 2) ...Des itin�raires
		for(Map.Entry<Integer, VueItineraire> entry : setItineraires)
		{
			mon_itineraire = entry.getValue();
			
			if (mon_itineraire != null) {
				mon_itineraire.dessineTousNoeud(g);
			}
			// 3) ...Destinations du noeud selectionne
			for (int i = 0; i < size_selection; i++) {
				if (node_are_on_itineraire.get(i)) {
					mon_itineraire.dessineDestinationsDeSelection(g,
							selectedNodes.get(i));
				} else {
					mon_plan.dessineDestinationsDeSelection(g, selectedNodes.get(i));
				}
			}
			// 3') ...de la selection
			/*
			 * en dernier, pour eviter que un noeud selectionne ne soit colorier
			 * dans la couleur d'un noeud destination s'il est lui m�me destination
			 * d'un autre noeud selectionne
			 */
			for (int i = 0; i < size_selection; i++) {
				if (node_are_on_itineraire.get(i)) {
					mon_itineraire.dessineNoeudSelectionne(g, selectedNodes.get(i));
				} else {
					mon_plan.dessineNoeudSelectionne(g, selectedNodes.get(i));
				}
			}
		}
	}


	/**
	 * Deslectionne le "i-�me" noeud selectionn� de la selection courrante La
	 * methode n'a aucune effet si l'indice i ne correspond pas � un noeud
	 * selectionne
	 *
	 * @param i
	 */
	public void removeSelection(int i) {
		if (i < 0 || i >= selectedNodes.size())
			return;
		selectedNodes.remove(i);
		node_are_on_itineraire.remove(i);
	}

	/**
	 * Ajoute une noeud a selectionne
	 *
	 * @param n
	 * @param is_on_itineraire
	 *            precise si le noeud appartient � l'itineraire
	 */
	public void addSelection(Noeud n, boolean is_on_itineraire) {
		// on ajoute le noeud � la liste des noeud selectionne
		if (!remanent && !control_key) {
			// En ayant auparavant supprimer l'ancienne selection
			// si le parametre remanent n'est pas active
			// (ou si la touche control n'est pas presse)
			selectedNodes.clear();
			node_are_on_itineraire.clear();
			// Rq : Ceci �vite qu'un noeud selectionne ne soit deselectionne si
			// l'utilisateur ne selectionne qu'un noeud � la fois (c'est � dire
			// pas ctrl)
		}

		if (n != null)// on verifie quand meme si il n'est pas null
		{
			ajtSel(n, is_on_itineraire);// ajoute le noeud n � la selection
		}
	}

	/**
	 * Selectionne un noeud ou le deselectionne si il est deja selectionne.
	 *
	 * @param n
	 * @param is_on_itineraire
	 */
	private void ajtSel(Noeud n, boolean is_on_itineraire) {
		for (int i = 0; i < selectedNodes.size(); i++) {
			if (selectedNodes.get(i).equals(n)) {
				selectedNodes.remove(i);
				node_are_on_itineraire.remove(i);
				return;
			}
		}
		selectedNodes.add(n);// ajout d'une selection � la liste des selections
		node_are_on_itineraire.add(is_on_itineraire);// dit si la selection est
														// sur l'itineraire
	}

	/**
	 * Change le plan qui sera affiche
	 *
	 * @param p
	 *            une instance de VuePlan
	 */
	public void setVuePlan(VuePlan p) {
		this.mon_plan = p;
		mon_plan.setZone(this);
	}

	/**
	 * Change l'itin�raire qui sera affiche
	 *
	 * @param itineraire
	 *            une instance de VueItineraire
	 */
	public void ajouterVueItineraire(VueItineraire vue)
	{
//		System.out.println("SetVueItineraire");
		vue.setZone(this);
		this.mes_itineraires.put(Integer.valueOf(vue.getItineraireId()), vue);
	}
	
//	public void ajouterVuesItineraire(HashMap<Integer, VueItineraire> itineraires) {
//		this.mes_itineraires = itineraires;
//
//		Set<Map.Entry<Integer, VueItineraire>> set = itineraires.entrySet();
//
//		for(Map.Entry<Integer, VueItineraire> entry : set)
//		{
//			(entry.getValue()).setZone(this);
//		}
//	}


	/**
	 * @return a taille de la selection
	 */
	public int getSelectionSize() {
		return selectedNodes.size();
	}

	/**
	 * @return la vue du plan qui est actuellement affich�e
	 */
	public VuePlan getVuePlan() {
		return mon_plan;
	}

	/**
	 * @return la vue de l'itin�raire qui est actuellement affich�e
	 */
	public VueItineraire getVueItineraire(int vueitineraireId) {
		System.out.println("id : " + Integer.valueOf(vueitineraireId));
		System.out.println(mes_itineraires);
		return mes_itineraires.get(Integer.valueOf(vueitineraireId));
	}

}
