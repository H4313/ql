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
import java.util.ArrayList;

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
	private VueItineraire mon_itineraire;

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
		this.appli_mere = mere;

		this.setFocusable(true);

		// Cr�ation de la bordure
		this.setBorder(BorderFactory.createLineBorder(c_cadre, larg_paint,
				cadre_arrondi));

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					control_key = true;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					control_key = false;
				}

			}

		});

		// Ajout d'un �couteur sur le drag de la souris pour translater plan et
		// itin�raire
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {

				// is_on_drag = true;
				// if(control_key)
				// {
				// current_drag_x = e.getX();
				// current_drag_y = e.getY();
				//
				// .addSelection(n, is_on_itineraire);
				// repaint();
				// return;
				// }

				// Dans le cas ou les translation sont permises...
				if (haveTranslation) {

					if (mon_plan != null) {
						// On modifie l'offset du plan de la valeur
						// du d�placement de la souris
						mon_plan.setOffset_x(current_offset_x
								+ (e.getX() - debut_drag_x)
								/ mon_plan.getZoom());
						mon_plan.setOffset_y(current_offset_y
								+ (e.getY() - debut_drag_y)
								/ mon_plan.getZoom());
					}
					if (mon_itineraire != null) {
						// et on fait la m�me chose pour l'itin�raire
						mon_itineraire.setOffset_x(current_offset_x
								+ (e.getX() - debut_drag_x)
								/ mon_plan.getZoom());
						mon_itineraire.setOffset_y(current_offset_y
								+ (e.getY() - debut_drag_y)
								/ mon_plan.getZoom());
					}
					repaint();
				}
			}

			// non n�cessaire ici, mais demand� par l'interface
			// MouseMotionListener
			@Override
			public void mouseMoved(MouseEvent e) {
			}

		});

		// On place un �couteur sur la roulette de la souris pour impl�menter le
		// zoom
		this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// Si le zoom est autoris�
				if (haveZoom) {

					float former_zoom = mon_plan.getZoom();

					// on d�termine le sens de la rotation pour savoir
					// si l'utilisateur zoom ou d�zoom
					int sng = -e.getWheelRotation()
							/ Math.abs(e.getWheelRotation());

					// On incr�mente ensuite le zoom du plan...
					if (mon_plan != null) {
						mon_plan.setZoom(mon_plan.getZoom() + sng * dzoom);
					}
					// et de l'itin�raire...
					if (mon_itineraire != null) {
						mon_itineraire.setZoom(mon_itineraire.getZoom() + sng
								* dzoom);
						// ...de la quantit� dzoom ( multipli� par -1 si d�zoom)
					}

					// On translate ensuite la vue pour que le point vis� par la
					// souris
					// reste � la m�me place m�me avec le zoom

					float zoom = mon_plan.getZoom();
					float x = e.getX();
					float y = e.getY();
					float move_x, move_y, move_x_former, move_y_former;

					// ...On commence par calculer l'ancien vecteur de
					// translation effectu� par le zoom pr�c�dent
					move_x_former = (x - x * former_zoom);
					move_y_former = (y - y * former_zoom);// qui vaut zero si il
															// n'y a eu aucun
															// zoom

					// On calcul ensuite le nouveau vecteur de translation
					// n�cessaire pour recadrer
					// la vue avec le nouveau zoom
					move_x = (x - x * zoom);
					move_y = (y - y * zoom);

					// On applique ensuite au plan une translation du nouveau
					// vecteur moins l'ancien
					mon_plan.setOffset_x(mon_plan.getOffset_x() + move_x / zoom
							- move_x_former / former_zoom);
					mon_plan.setOffset_y(mon_plan.getOffset_y() + move_y / zoom
							- move_y_former / former_zoom);

					mon_itineraire.setOffset_x(mon_plan.getOffset_x());
					mon_itineraire.setOffset_y(mon_plan.getOffset_y());

					// ...et on oublie pas de repeindre la vue
					repaint();
				}
			}

		});

		/*
		 * On place un �couteur sur les cliques de la souris pour :
		 * ->s�lectionner un noeud ->d�tecter le d�but d'un "drag" lorsque la
		 * souris est press� ->recentrer le plan lors d'un double clic ->charger
		 * un plan avec un clic si il n'y a aucun plan
		 */
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// Plage le focus sur la zone de dessin
				requestFocus();
				Noeud selectedNode = null;
				boolean node_is_on_itineraire = false;
				// Dans le cas ou la souris � �t� cliqu�, on regarde d'abord
				// le nombre de click
				if (e.getClickCount() == 1) {
					// S'il y a eu un clic, il peut s'agit :

					// 1) d'une op�ration de s�l�ction
					if (mon_itineraire != null) {
						// ...pou une s�l�ction,
						// on regarde d'abord si le point appartient �
						// l'itin�raire
						selectedNode = mon_itineraire.getClicked(e);

						if (selectedNode == null) {
							// sinon, alors on regarde si le point appartient au
							// plan
							if (mon_plan != null)
								selectedNode = mon_plan.getClicked(e);
						} else {
							node_is_on_itineraire = true;
						}
					} else {
						// Dans le cas ou aucun itin�raire n'est affich�,
						// on regarde directement si le point appartient au plan
						if (mon_plan != null)
							selectedNode = mon_plan.getClicked(e);
						// a note que la fonction getClicked renvoie null si
						// aucun point n'est
						// selectionne
					}

					// On ajoute la selection � la liste des selection
					addSelection(selectedNode, node_is_on_itineraire);

					// Et pour finir, on previent l'application mere qu'un point
					// a �t� selectionn�
					// (ou deselectionn�)
					appli_mere.appendNode(selectedNodes);

					// 2)D'une action pour charger un plan
					if (mon_plan.getPlan() == null) {
						appli_mere.chargerPlan();
					}

					// ...on oublie pas de repeindre la vue
					repaint();
				}

				// ..Si l'utilisateur a click� deux fois, alors il faut
				// recentrer la vue
				if (e.getClickCount() == 2) {

					dezoomEtRecentre();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// Dans le cas ou un boutton de la souris est press�,
				// On sauvegarde :
				debut_drag_x = e.getX();// Les coordonn�es de la souris
				debut_drag_y = e.getY();
				current_offset_x = mon_plan.getOffset_x();// Ainsi que l'offset
															// du plan et de
															// l'itin�raire
				current_offset_y = mon_plan.getOffset_y();
				// qui sont n�cessaire pour l'op�ration de "drag" (voir plus
				// haut addMouseMotionListener)
			}

			// non n�cessaire ici, mais demand� par l'interface MouseListener
			public void mouseReleased(MouseEvent e) {
				// if(is_on_drag)
				// {
				// is_on_drag = false;
				// repaint();
				// }
			}

			// non n�cessaire ici, mais demand� par l'interface MouseListener
			public void mouseEntered(MouseEvent e) {

			}

			// non n�cessaire ici, mais demand� par l'interface MouseListener
			public void mouseExited(MouseEvent e) {

			}

		});
	}


	/**
	 * Redonne au dessin sa taille initial et le recentre.
	 */
	public void dezoomEtRecentre() {
		// On applique deux fois le m�me traitement :
		// une fois pour l'itin�raire...
		if (mon_itineraire != null) {
			mon_itineraire.setOffset_x(0);
			mon_itineraire.setOffset_y(0);
			mon_itineraire.setZoom(1);
		}
		// ...et une fois pour le plan
		if (mon_plan != null) {
			// l'op�ration consiste � :
			mon_plan.setOffset_x(0);// remettre les offsets � 0
			mon_plan.setOffset_y(0);
			mon_plan.setZoom(1);// et le zoom � 1
		}

		// ...et on n'oublie pas de repeindre la vue
		repaint();
	}

	// On surcharge la m�thode paintComponent de JPanel pour dessiner ce qui
	// nous int�resse
	public void paintComponent(Graphics g) {

		// On augmente d'abord la qualit� du rendu (suppresion de l'aliasing
		// /ex)
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		// Puis on dessine le fond
		g.setColor(c_back);
		g.fillRect(0, 0, getWidth(), getHeight());

		// On dessine ensuite les troncons puis les noeuds pour �viter
		// qu'un troncon ne recouvre un noeud.

		int size_selection = this.selectedNodes.size();

		// dessine tous les tron�ons
		// 1) Du plan
		if (mon_plan != null)
			mon_plan.dessineTousTroncon(g);
		// 2) De l'itin�raire
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

		// dessine tous les noeuds :
		// 1) ...Du plan
		if (mon_plan != null)
			mon_plan.dessineTousNoeud(g);
		// 2) ...De l'itin�raire
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


	/**
	 * remanent a vraie permet de garder tous les noeuds selectionne. remanent a
	 * faux ne garde que le dernier noeud selectionne (tout les noeuds auparant
	 * selectionne sont enlever de la liste)
	 *
	 * @param remanent
	 */
	public void setRemanent(boolean remanent) {
		this.remanent = remanent;
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
	 * D�selectionne les points selectionn�s
	 */
	public void resetSelection() {
		appli_mere.appendNode(null);
		this.selectedNodes.clear();
		this.node_are_on_itineraire.clear();
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
	public void setVueItineraire(VueItineraire itineraire) {
		this.mon_itineraire = itineraire;
		mon_itineraire.setZone(this);
	}

	public boolean isRemanent() {
		return this.remanent;
	}

	public boolean isControlPressed() {
		return this.control_key;
	}

	/**
	 * @return les pointeurs vers les noeuds selectionnes
	 */
	public ArrayList<Noeud> getSelection() {
		ArrayList<Noeud> res = new ArrayList<Noeud>();
		int size = selectedNodes.size();
		for (int i = 0; i < size; i++) {
			res.add(getSelection(i));
		}
		return res;
	}

	/**
	 * @return le dernier point selectionne. Renvoie null si aucun point
	 *         selectionne
	 */
	public Noeud getLastSelection() {
		return getSelection(selectedNodes.size() - 1);
	}

	/**
	 * @return le i-�me point selectionne de la selection
	 */
	public Noeud getSelection(int i) {
		Noeud res = (i >= selectedNodes.size() || i < 0) ? null : selectedNodes
				.get(i);
		if (res != null) {
			boolean is_on_itineraire = this.node_are_on_itineraire.get(i);
			if (is_on_itineraire) {
				for (Noeud n : mon_plan.getPlan().getNoeuds()) {
					if (n.equals(res)) {
						res = n;
						break;
					}
				}
			}
		}
		return res;
	}

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
	public VueItineraire getVueItineraire() {
		return mon_itineraire;
	}

}
