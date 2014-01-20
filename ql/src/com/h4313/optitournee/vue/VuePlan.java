//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.*;

import com.h4313.optitournee.modele.Noeud;
import com.h4313.optitournee.modele.Plan;
import com.h4313.optitournee.modele.Troncon;
import com.h4313.optitournee.outils.Constantes;

/**
 * La classe VuePlan est une vue sur un plan. Elle permet d'afficher les noeuds
 * du plan et les arcs qui les relient.
 *
 * @author H4403
 */

public class VuePlan {


	// Pointeur vers le mod�le du plan
	protected Plan plan;
	// Ponteur vers la zone de dessin sur laquel se dessine le plan
	protected ZoneDessin zone;

	// diametre d'un noeud (modifiable par le zoom)
	protected float taille_noeud = Constantes.TAILLE_NOEUD;

	/*
	 * plus cette valeur est grande, plus les tron�ons sortant seront �loign� du
	 * vecteur median (noeud source, noeud destination)
	 */
	protected float largeur_chausse = Constantes.LARGEUR_CHAUSEE;

	// Pr�cise le d�calage entre les diff�rents passage sur un m�me troncon
	// un d�calage de 0 confond les passages en un seul arc.
	protected float decalage_passage = Constantes.DECALAGE_PASSAGE;
	protected boolean decaler_troncon = Constantes.DECALAGE_TRONCON;

	// Affiche les tron�on du noeud selectionne
	protected boolean display_selected_troncon = Constantes.AFF_TRONCON_SEL;
	// Affiche les noeuds destination des troncons du noeuds selectionne
	protected boolean display_destination_from_node = Constantes.AFF_TERM_SEL;

	// Largeur du pinceau pour les troncon
	protected float larg_paint = Constantes.LARGEUR_TRONCON;

	// Couleur d'un noeud selectionne
	protected Color c_select = Constantes.COUL_NOEUD_SEL;
	// Couleur d'un noeud
	protected Color c_noeud = Constantes.COUL_NOEUD;
	// Couleur d'un tron�on
	protected Color c_troncon = Constantes.COUL_TRONCON;
	// Couleur des troncon d'un noeud selectionne
	protected Color c_sel_troncon = Constantes.COUL_TONCON_SEL;
	// Couleur des noeuds destination des troncon d'un noeud selectionne
	protected Color c_term = Constantes.COUL_TERM_SEL;

	// valeur minimal du zoom
	protected float min_zoom = Constantes.MIN_ZOOM;
	// valeur maximal du zoom
	protected float max_zoom = Constantes.MAX_ZOOM;
	// zoom de la vue
	protected float zoom = 1;

	// Translation du graphe en x et y
	protected float offset_x = 0;
	protected float offset_y = 0;

	/*
	 * Coordonn�e du point le plus en haut � droite du plan, et du point le plus
	 * en bas � gauche du plan (permet de recadrer le graphe au centre de la
	 * zone de dessin)
	 */
	protected int minX, maxX, minY, maxY;

	/*
	 * Agrandit aussi la taille des �l�ment lors du zoom si faux, alors seul les
	 * distances entre les �l�ment son agrandies
	 */
	protected boolean zoomElement = Constantes.ZOOM_ELEMENT;


	/*
	 * N�cessaire pour la translation. Il garde la largeur de la zone de dessin
	 * avant un changement d'�chelle. Ceci permet de redimensionner
	 * automatiquement l'offset si la zone de dessin change de taille
	 */
	private int w_o;
	private int h_o;


	public VuePlan() {
		this.plan = null;
	}

	public VuePlan(Plan p) {

		this.plan = p;
		// Calcul les dimension du panel
		setMinMax();
	}


	/**
	 * La m�thode <code>getClicked</code> permet
	 * de connaitre quel noeud a �t� cliqu� par l'utilisateur
	 *
	 * @param e
	 *            le click de la souris
	 * @return Le noeud selectionn� par le click de la souris
	 */
	public Noeud getClicked(MouseEvent e) {
		if (plan == null)
			return null;
		List<Noeud> noeuds = plan.getNoeuds();
		Noeud selected = null;
		// Pour tous les noeuds
		for (Noeud n : noeuds) {
			int x = screenX(n.getX());
			int y = screenY(n.getY());
			// on regarde si le click de la souris est contenu dans la zone de
			// dessin
			// (attention : l'origine est en haut � droite)
			if (e.getX() <= x + taille_noeud && e.getX() >= x
					&& e.getY() <= y + taille_noeud && e.getY() >= y) {
				selected = n;
				return selected;
			}
		}
		return selected;
	}

	/**
	 *
	 * @param deb_x coordonn�e en x du 1er angle de la selection
	 * @param deb_y coordonn�e en y du 1er angle de la selection
	 * @param fin_x coordonn�e en x du 2�me angle de la selection
	 * @param fin_y coordonn�e en y du 2�me angle de la selection
	 * @return Les noeuds contenues dans le carre
	 */
	public ArrayList<Noeud> includedInto(int deb_x, int deb_y, int fin_x,
			int fin_y) {
		if (plan == null)
			return null;
		List<Noeud> noeuds = plan.getNoeuds();
		ArrayList<Noeud> selection = new ArrayList<Noeud>();
		// Pour tous les noeuds
		for (Noeud n : noeuds) {
			int x = screenX(n.getX());
			int y = screenY(n.getY());
			// on regarde si le click de la souris est contenu dans la zone de
			// dessin
			// (attention : l'origine est en haut � droite)
			if (deb_x <= x && fin_x >= x + taille_noeud && deb_y <= y
					&& fin_y >= y + taille_noeud) {
				selection.add(n);
			}
		}
		return selection;
	}

	/**
	 * La m�thode <code>dessineTousTroncon</code>
	 * dessine tous les troncons du plan sur
	 * la zone de dessin
	 *
	 * @param g
	 *            Graphics passe par la zone de dessin
	 */
	public void dessineTousTroncon(Graphics g) {
		if (plan == null)
			return;
		List<Noeud> noeuds = plan.getNoeuds();
		for (Noeud n : noeuds) {
			dessineTronconDeNoeud(n, g, c_troncon);
		}
	}

	/**
	 * La m�thode <code>dessineTousNoeuds</code>
	 * dessine tous les noeuds du plan sur la zone de dessin
	 *
	 * @param g
	 *            Graphics passe par la zone de dessin
	 */
	public void dessineTousNoeud(Graphics g) {
		// Dessine tout les noeuds
		if (plan == null)
			return;
		for (Noeud n : plan.getNoeuds()) {
			dessineNoeud(n, g, c_noeud);
		}
	}

	/**
	 * La m�thode <code>dessineDestinationsDeSelection</code>
	 * dessine tout les noeuds auquel mene un noeud selectionne
	 *
	 * @param g
	 *            Graphics passe par la zone de dessin
	 * @param n
	 *            Le noeud selectionne
	 */
	public void dessineDestinationsDeSelection(Graphics g, Noeud n) {

		if (this.display_destination_from_node) {
			for (Troncon t : n.getTroncons_sortants()) {
				dessineNoeud(t.getDestination(), g, c_term);
			}
		}
	}

	/**
	 * La m�thode <code>dessineNoeudSelectionne</code>
	 * dessine un noeud dans la couleur de la selection
	 *
	 * @param g
	 *            Graphics passe par la zone de dessin
	 * @param n
	 *            Le noeud selectionne
	 */
	public void dessineNoeudSelectionne(Graphics g, Noeud n) {
		dessineNoeud(n, g, c_select);
	}


	/**
	 * La m�thode <code>dessineTronconDeNoeudSelectionne</code>
	 * dessine tout les noeuds sortant d'un noeud "n" dans la couleur de la
	 * selection.
	 *
	 * @param n
	 * @param g
	 *            Graphics passe par la zone de dessin
	 */
	protected void dessineTronconDeNoeudSelectionne(Noeud n, Graphics g) {
		if (display_selected_troncon)
			dessineTronconDeNoeud(n, g, c_sel_troncon);
	}

	/**
	 * La m�thode <code>dessineTronconDeNoeud</code>
	 * dessine tous les noeuds sortant d'un noeud "n".
	 *
	 * @param n
	 * @param g
	 *            Graphics passe par la zone de dessin
	 * @param c
	 *            la couleur avec laquel dessiner les troncon
	 */
	protected void dessineTronconDeNoeud(Noeud n, Graphics g, Color c) {
		for (Troncon t : n.getTroncons_sortants()) {
			Noeud dest = t.getDestination();
			dessineTroncon(n, dest, c, g, t.getNbPassage());
		}
	}

	/**
	 * La m�thode <code>dessineNoeud</code>
	 * dessine un noeud "n" avec une couleur "c" sur la zone de dessin
	 *
	 * @param n
	 * @param g
	 *            Graphics passe par la zone de dessin
	 * @param c
	 */
	protected void dessineNoeud(Noeud n, Graphics g, Color c) {
		int x1 = screenX(n.getX());
		int y1 = screenY(n.getY());
		g.setColor(c);
		g.fillOval(x1, y1, (int) taille_noeud, (int) taille_noeud);
	}

	/**
	 * La m�thode <code>dessineTroncon</code>
	 * Dessine un troncon qui part du noeud "depart" et qui arrive au noeud
	 * "arrive".
	 *
	 * @param depart
	 * @param arrive
	 * @param c
	 *            la couleur avec laquel dessine le troncon
	 * @param g
	 *            Graphics ou dessiner le troncon
	 * @param nb_passage
	 *            creer des "nb_passage" copies du troncon, decale sur sa droite
	 *            de decalage_passage.
	 */
	protected void dessineTroncon(Noeud depart, Noeud arrive, Color c,
			Graphics g, int nb_passage) {

		nb_passage++;
		nb_passage = (this.decaler_troncon ? nb_passage : 1);

		for (int i = 0; i < nb_passage; i++) {
			dessineDirection(depart, arrive, g, c, (int) (largeur_chausse + i
					* decalage_passage));
		}

	}

	/**
	 * La m�thode <code>dessineDirection</code>
	 * Dessine un segment qui part du noeud "depart" et qui arrive au noeud
	 * "arrive".
	 *
	 * @param depart
	 * @param arrive
	 * @param c
	 *            la couleur avec laquel dessine le troncon
	 * @param g
	 *            Graphics ou dessiner le troncon
	 * @param hauteur
	 *            le decalage a appliquer au segment par rapport � la droite qui
	 *            passe par les points de d�part et d'arrive.
	 */
	protected void dessineDirection(Noeud depart, Noeud arrive, Graphics g,
			Color c, int hauteur) {

		int x1 = screenX(depart.getX()) + (int) (taille_noeud / 2);
		int y1 = screenY(depart.getY()) + (int) (taille_noeud / 2);
		int x2 = screenX(arrive.getX()) + (int) (taille_noeud / 2);
		int y2 = screenY(arrive.getY()) + (int) (taille_noeud / 2);

		/*
		 * 1)On calcule le produit vectoriel entre le vecteur A = (x2 - x1, y2 -
		 * y1) et le vecteur perpendiculaire � l'�cran. 2)On translate ensuite
		 * le vecteur A par le r�sultat du produit, qui repr�sente donc le
		 * d�calage du troncon par rapport � la droite qui passe par (x1,y1) et
		 * (x2,y2). 3)La norme du vecteur de d�calage est calcul� pour placer le
		 * troncon sur le bord d'un noeud
		 */
		double pc = Math.pow(hauteur, 2)
				/ (Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
		double p = -Math.sqrt(pc);// p est la norme du d�calage
		int x1p = (int) ((y2 - y1) * p + x1);// produit vectoriel
		int y1p = (int) ((x1 - x2) * p + y1);
		int x2p = (x2 - x1 + x1p);
		int y2p = (y2 - y1 + y1p);

		// Defini la largeur du pinceau
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(larg_paint));// Ceci d�fini la largeur du
													// pinceau

		g.setColor(c);
		g.drawLine(x1p, y1p, x2p, y2p);
	}

	/**
	 * La m�thode <code>screenX</code>
	 * converti une abscisse du modele en abscisse sur le JPanel.
	 *
	 * @param x
	 * @return l'abscisse en coordonn�e de la zone de dessin
	 */
	protected int screenX(int x) {
		if (maxX == minX) {
			return (int) taille_noeud;
		}
		if (w_o == 0)
			w_o = zone.getWidth();
		// on modifie l'offset sur les x en fonction de la largeur de la zone
		// de dessin (le d�calage en abscisse doit augmenter quand la largeur
		// augmente,
		// sans modifier l'offset)
		offset_x = zone.getWidth() * offset_x / w_o;
		w_o = zone.getWidth();
		// on conveti l'abscisse en abscisse de la zone de dessin
		// On prend en compte la largeur de la zone, le zoom et l'offset
		// appliqu�.
		return (int) ((zone.getWidth() * zoom - 2 * taille_noeud)
				/ (maxX - minX) * (x - minX) + taille_noeud / 2 + offset_x
				* zoom);
	}

	/**
	 * La m�thode <code>screenY</code>
	 * converti une ordonne du modele en ordonnee sur le JPanel.
	 *
	 * @param x
	 * @return l'ordonnee en coordonn�e de la zone de dessin
	 */
	protected int screenY(int y) {
		if (maxY == minY) {
			return (int) taille_noeud;
		}
		// on modifie l'offset sur les y en fonction de la hauteur de la zone
		// de dessin (le d�calage en ordonnee doit augmenter quand la hauteur
		// augmente,
		// sans modifier l'offset)
		if (h_o == 0)
			h_o = zone.getHeight();
		offset_y = zone.getHeight() * offset_y / h_o;
		h_o = zone.getHeight();
		// on conveti l'ordonne en ordonnee de la zone de dessin
		// On prend en compte la hauteur de la zone, le zoom et l'offset
		// appliqu�.
		return (int) ((zone.getHeight() * zoom - 2 * taille_noeud)
				/ (maxY - minY) * (y - minY) + taille_noeud / 2 + offset_y
				* zoom);
	}


	/**
	 * La m�thode <code>setMinMax</code>
	 * determine l'abscisse minimum et maximum, ainsi que l'ordonne minimum et
	 * maximum, sur l'ensemble des points. Ce calcule est n�cessaire pour
	 * centrer le graphe dans la zone de dessin
	 */
	private void setMinMax() {
		if (plan == null)
			return;
		minX = -1;
		maxX = -1;
		minY = -1;
		maxY = -1;
		List<Noeud> noeuds = plan.getNoeuds();
		for (Noeud n : noeuds) {
			minX = (minX == -1 ? n.getX() : Math.min(minX, n.getX()));
			minY = (minY == -1 ? n.getY() : Math.min(minY, n.getY()));
			maxX = (maxX == -1 ? n.getX() : Math.max(maxX, n.getX()));
			maxY = (maxY == -1 ? n.getY() : Math.max(maxY, n.getY()));
		}
	}


	public Plan getPlan() {
		return plan;
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

	/**
	 * @return la valeur du vecteur de translation sur les abscisses
	 */
	public float getOffset_x() {
		return offset_x;
	}

	/**
	 * @return la valeur du vecteur de translation sur les ordonn�es
	 */
	public float getOffset_y() {
		return offset_y;
	}

	/**
	 * @return la valeur actuelle du zoom
	 */
	public float getZoom() {
		return zoom;
	}

	/**
	 * Si aff est vraie, alors dessine aussi les troncons du noeud selectionne
	 *
	 * @param aff
	 */
	public void setAffSelTroncon(boolean aff) {
		this.display_selected_troncon = aff;
	}

	/**
	 * Si aff est vraie, alors dessine aussi les destinations accessible a
	 * partir du noeud selectionne
	 *
	 * @param aff
	 */
	public void setAffSelDest(boolean aff) {
		this.display_destination_from_node = aff;
	}

	/**
	 * D�cale le plan sur les ordonnees.
	 *
	 * @param offset_y
	 *            valeur du d�calage sur les ordonnees
	 */
	public void setOffset_y(float offset_y) {
		this.offset_y = offset_y;
		this.h_o = zone.getHeight();
	}

	/**
	 * Fait un zoom sur la vue
	 *
	 * @param zoom
	 *            valeur absolue du zoom sur la vue
	 */
	public void setZoom(float zoom) {
		if (zoom > this.max_zoom || zoom < this.min_zoom)
			return;
		if (this.zoomElement) {
			this.taille_noeud = taille_noeud / this.zoom * zoom;
			this.decalage_passage = decalage_passage / this.zoom * zoom;
			this.largeur_chausse = largeur_chausse / this.zoom * zoom;
			this.larg_paint = larg_paint / this.zoom * zoom;
		}
		this.zoom = zoom;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
		this.setMinMax();
	}

	public void setZone(ZoneDessin zone) {
		this.zone = zone;
	}

	/**
	 * D�cale le plan sur les abscisses.
	 *
	 * @param offset_x
	 *            valeur du d�calage sur les abscisses
	 */
	public void setOffset_x(float offset_x) {
		this.offset_x = offset_x;
		this.w_o = zone.getWidth();
	}

	public void setMinMax(int minX, int maxX, int minY, int maxY) {
		this.maxX = maxX;
		this.minX = minX;
		this.minY = minY;
		this.maxY = maxY;
	}

}
