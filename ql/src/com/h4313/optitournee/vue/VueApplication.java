//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import com.h4313.optitournee.controleur.Controleur;
import com.h4313.optitournee.modele.Itineraire;
import com.h4313.optitournee.modele.Noeud;
import com.h4313.optitournee.outils.Constantes;
import com.h4313.optitournee.outils.GestionCalendar;


/**
 * La classe <code>VueApplication</code> impl�mente
 * l'interface <code>Vue</code> et constitue la vue
 * principale de l'application.
 *
 * @author H4403
 */

// Objet non serialise -> on n'a pas besoin de num�ro de version
@SuppressWarnings("serial")
public class VueApplication extends JFrame implements Vue {
	/**
	 * Pointeur vers le controleur
	 */
	private Controleur manager;

	/**
	 * Objets graphiques
	 */
	private JPanel header;
	private JToolBar controls;
	private JPanel center;
	private JPanel bottom;

	// Informations du header
//	private JImage logo;
	private JLabel titre;
	private Box boxLogin;
	private JLabel infosLogin;
	private JButton boutonLogout;

	// Boutons de la barre des controles
	private JButton boutonChargerPlan;
	private JButton boutonChargerLivraisons;
	private JButton boutonCalculerItineraire;
//	private JButton boutonUndo;
//	private JButton boutonRedo;
//	private JButton boutonAjouter;
//	private JButton boutonSupprimer;
//	private JButton boutonImprimer;

	// Zone de dessin
	private ZoneDessin zone;
//	private LabelSelection infoSelection;

	// Informations de la zone du bas;
//	private JTextPane console;
//	private JScrollPane scrolling;

	private enum Mode {
		NORMAL, AJOUT
	};

	private Mode mode;
	protected Noeud noeudAAjouter = null;

	private boolean former_remanent;

	// Constructeur param�tr�, seul celui-ci devrait etre appel�
	// Cree la fenetre et ses elements puis l'affiche
	public VueApplication(Controleur controleur) {

		this.mode = Mode.NORMAL;
		// Controleur
		manager = controleur;

		// Panels des 4 lignes de la fenetre
		header = new JPanel(new GridLayout(1, 0));
		controls = new JToolBar();
		center = new JPanel();
		bottom = new JPanel();

		// Layout de la fenetre
		this.getContentPane().setLayout(new GridBagLayout());
		header.setLayout(new BorderLayout());
		center.setLayout(new GridBagLayout());
		bottom.setLayout(new GridLayout());

		// Configuration de header
		GridBagConstraints cHeader = createBagConstraints(0, 0, 1, 0);

		// Configuration de controls
		GridBagConstraints cControls = createBagConstraints(0, 1, 1, 0);

		// Configuration de center
		GridBagConstraints cCenter = createBagConstraints(0, 2, 1,
				Constantes.GRAPH_PROPORTION_VERTICAL);

		// Configuration de bottom
		GridBagConstraints cBottom = createBagConstraints(0, 3, 1,
				1 - Constantes.GRAPH_PROPORTION_VERTICAL);

		// Elements de header
//		logo = new JImage(Constantes.PATH_IMAGE_LOGO);
//		logo.setPreferredSize(new Dimension(Constantes.LOGO_X,
//				Constantes.LOGO_Y));

		titre = new JLabel(Constantes.TITLE);
		titre.setHorizontalAlignment(SwingConstants.CENTER);

		boxLogin = Box.createHorizontalBox();

		Calendar calendrier = new GregorianCalendar();

		infosLogin = new JLabel("<html>" + Constantes.WELCOME + "<br>"
				+ GestionCalendar.calendarToDateString(calendrier) + "</html>");

		ImageIcon logoutIcon = new ImageIcon(Constantes.PATH_ICON_LOGOUT);
		boutonLogout = new JButton(logoutIcon);
		boutonLogout.setToolTipText(Constantes.TOOL_LOGOUT);
		boutonLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fermerApplication();
			}
		});

		boxLogin.add(infosLogin);
		boxLogin.add(Box.createHorizontalStrut(Constantes.LOGIN_SEPARATOR_SIZE));
		boxLogin.add(boutonLogout);

		header.add(titre, BorderLayout.CENTER);
		header.add(boxLogin, BorderLayout.EAST);

		// Elements de controls
		controls.setFloatable(false);

		boutonChargerPlan = new JButton();
		boutonChargerPlan.setText(Constantes.BUTTON_LOADPLAN);
		boutonChargerPlan.setToolTipText(Constantes.TOOL_LOADPLAN);
		boutonChargerPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chargerPlan();
			}
		});

		boutonChargerLivraisons = new JButton();
		boutonChargerLivraisons.setText(Constantes.BUTTON_LOADDELIVERY);
		boutonChargerLivraisons.setToolTipText(Constantes.TOOL_LOADDELIVERY);
		boutonChargerLivraisons.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chargerLivraisons();
			}
		});

		boutonCalculerItineraire = new JButton();
		boutonCalculerItineraire.setText(Constantes.BUTTON_COMPUTE);
		boutonCalculerItineraire.setToolTipText(Constantes.TOOL_COMPUTE);
		boutonCalculerItineraire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manager.calculerTournee();
			}
		});
		
		controls.add(boutonChargerPlan);
		controls.add(boutonChargerLivraisons);
		controls.add(boutonCalculerItineraire);

		// Elements de center
		GridBagConstraints cGraph = createBagConstraints(0, 0,
				Constantes.GRAPH_PROPORTION_HORIZONTAL, 1);
		GridBagConstraints cInfos = createBagConstraints(1, 0,
				1 - Constantes.GRAPH_PROPORTION_HORIZONTAL, 1);

		zone = new ZoneDessin(this);

		zone.setVuePlan(new VuePlan(null));
		zone.ajouterVueItineraire(new VueItineraire(null, 0, 0, 0, 0, null));

//		infoSelection = new LabelSelection(manager);
//		infoSelection.setPreferredSize(new Dimension(Constantes.SELECTION_SIZE,
//				Constantes.SELECTION_SIZE));

		center.add(zone, cGraph);
//		center.add(infoSelection, cInfos);

		// Elements de bottom
//		console = new JTextPane();
//		console.setEditable(false);
//		Style styleO = console.addStyle("Warning", null);
//		Style styleR = console.addStyle("Error", null);
//		StyleConstants.setForeground(styleO, Color.orange);
//		StyleConstants.setForeground(styleR, Color.red);
//
//		scrolling = new JScrollPane(console);
//		scrolling.setPreferredSize(new Dimension(1, 1));
//		bottom.add(scrolling);

		// On colle les elements
		this.add(header, cHeader);
		this.add(controls, cControls);
		this.add(center, cCenter);
		this.add(bottom, cBottom);

		// Parametres de la fenetre
		this.setBounds(100, 100, Constantes.DEFAULT_WIDTH,
				Constantes.DEFAULT_HEIGHT);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				fermerApplication();
			}
		});

		this.rafraichir();
		this.setVisible(true);
	}
	
	public void resetDessin()
	{
		this.zone.mes_itineraires = new HashMap<Integer, VueItineraire>();
	}

	/**
	 * Cr�e un gridBagConstraint, qui prend toute la place disponible en x et y.
	 *
	 * @param gridx
	 *            index de l'�lement sur les abscisse
	 * @param gridy
	 *            index de l'�l�ements sur les ordonn�es
	 * @param weightx
	 *            poids de l'�l�ment en abscisse
	 * @param weighty
	 *            poids de l'�l�ment en ordonne
	 * @return un objet GridBagConstraints
	 */
	private GridBagConstraints createBagConstraints(int gridx, int gridy,
			float weightx, float weighty) {
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = gridx;
		c.gridy = gridy;
		c.weightx = weightx;
		c.weighty = weighty;
		c.fill = GridBagConstraints.BOTH;

		return c;
	}

	public void afficherCurseurAttente() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	public void afficherCurseurNormal() {
		setCursor(null);
	}

	public boolean demanderCalculPlusLong() {

		int reponse = JOptionPane.showConfirmDialog(this,
				Constantes.TEXTE_FENETRE_TEMPS_TSP,
				Constantes.TITRE_FENETRE_TEMPS_TSP,
				JOptionPane.OK_CANCEL_OPTION);
		return (reponse == JOptionPane.YES_OPTION);

	}

	protected void fermerApplication() {
		int reponse = JOptionPane.showConfirmDialog(this,
				Constantes.TEXTE_FENETRE_QUITTER,
				Constantes.TITRE_FENETRE_QUITTER, JOptionPane.OK_CANCEL_OPTION);
		if (reponse == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	protected void chargerPlan() {
		OpenChooserDialog o = new OpenChooserDialog(Constantes.ADRESSE_DEFAUT_PLAN);
		o.setSavingMode(false);
		o.ouvrirFichier();
		String cheminFichier = o.getCheminFichier();
		if (cheminFichier != null && !cheminFichier.equals("")) {
			manager.chargerPlan(cheminFichier);
//			zone.dezoomEtRecentre();
		}
	}

	protected void chargerLivraisons() {
		OpenChooserDialog o = new OpenChooserDialog(Constantes.ADRESSE_DEFAUT_LIVRAISON);
		o.setSavingMode(false);
		o.ouvrirFichier();
		String cheminFichier = o.getCheminFichier();
		if (cheminFichier != null && !cheminFichier.equals("")) {
			manager.chargerLivraisons(cheminFichier);
		}
	}

	public void setItineraireInitial(Itineraire iti)
	{
		VueItineraire vi = zone.getVueItineraire(0);
		vi.setItineraireInitial(iti);
	}
	
	/**
	 * La m�thode <code>rafraichir</code>
	 * rafraichit l'affichage de la vue.
	 *
	 */
	public void rafraichir() {
		Color[] colors = Constantes.COLORS;
		int colorInc = 0;
		boutonChargerLivraisons.setEnabled(false);
		boutonCalculerItineraire.setEnabled(false);

		HashMap<Integer, Itineraire> itineraires = manager.getItineraires();
		Set<Map.Entry<Integer, Itineraire>> set = itineraires.entrySet();

		for(Map.Entry<Integer, Itineraire> entry : set)
		{
			Itineraire itineraire = entry.getValue();
		
			if (itineraire != null)
			{
				VueItineraire vi = new VueItineraire(null, 0, 0, 0, 0, colors[colorInc]);
				zone.ajouterVueItineraire(vi);
				
				VuePlan vp = zone.getVuePlan();
//				VueItineraire vi = zone.getVueItineraire();
				vi.setCalculated(false);
				vi.setItineraire(null);
	
				if (itineraire.testPlanCharger()) {
					vp.setPlan(itineraire.getPlan());
					boutonChargerLivraisons.setEnabled(true);
	
					if (itineraire.testItineraireCharger()) {
						vi.setItineraire(itineraire);
						boutonCalculerItineraire.setEnabled(true);
						
						vi.setMinMax(vp.getMinX(), vp.getMaxX(), vp.getMinY(),
								vp.getMaxY());
	
						if (itineraire.testTourneeCalculee()) {
							vi.setCalculated(true);
						}
					}
				}
			}
			zone.repaint();
			colorInc = (colorInc + 1) % Constantes.NB_COLORS;
		}
	}
}
