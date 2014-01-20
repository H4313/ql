package com.h4313.optitournee.outils;

import java.awt.Color;


/**
 * Classe contenant l'ensemble des constantes necessaire � l'application
 *
 * @author H4403
 */
public abstract class Constantes {

	/******* QL ********/
	public static final float COUT_AU_METRE_EN_EURO = (float) 0.01;
	public static final float AUTONOMIE_DRONE_EN_METRE = (float) 25000.0;
	public static final float VOLUME_STOCK_DRONE_EN_L = (float) 50.0;
	public static final float POIDS_STOCK_DRONE_EN_G = (float) 50000.0;
	
	
	/***************/
	
	public static final Integer TAILLE_MAX_HISTORIQUE = 20;

	public static enum TypeFichier {
		PLAN, LIVRAISONS, TEXTE
	};

	// Duree d'attente a chaque point de livraison en secondes. 600 = 10 minutes
	public static final Integer DUREE_ATTENTE_LIVRAISON = 600;

	//Temps de calcul du TSP
	public static final int TEMPS_MAX_TSP_DEFAUT = 150;
	public static final int COEF_AUGM_TEMPS_TSP = 2;

	// Parametres de l'interface
	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 600;

	public static final float GRAPH_PROPORTION_HORIZONTAL = (float) 0.9;
	public static final float GRAPH_PROPORTION_VERTICAL = (float) 0.9;

	public static final int LOGO_X = 100;
	public static final int LOGO_Y = 50;

	public static final int LOGIN_SEPARATOR_SIZE = 10;
	public static final int SELECTION_SIZE = 100;

	public static final String ADRESSE_DEFAUT_PLAN = "ressources/exemples/exemples_plan";
	public static final String ADRESSE_DEFAUT_LIVRAISON = "ressources/exemples/exemples_livraison";
	public static final String ADRESSE_DEFAUT_SORTIE_FEUILLE = "bin";
	public static final String EXTENSION_DEFAUT_SORTIE_FEUILLE = "txt";


	// Chemins d'acces images
	public static final String PATH_IMAGE_LOGO = "ressources/images/logo.png";
	public static final String PATH_ICON_LOGOUT = "ressources/images/logout.png";
	public static final String PATH_ICON_UNDO = "ressources/images/undo.png";
	public static final String PATH_ICON_REDO = "ressources/images/redo.png";
	public static final String PATH_ICON_ADD = "ressources/images/add.png";
	public static final String PATH_ICON_REMOVE = "ressources/images/remove.png";
	public static final String PATH_ICON_PRINT = "ressources/images/print.png";

	//Chemons d'acc�s schemas XML
	public static final String PLANSCHEMAPATH="ressources/schemasXML/planSchema.xsd";
	public static final String LIVRAISONSCHEMAPATH="ressources/schemasXML/livraisonSchema.xsd";


	// Tooltips
	public static final String TOOL_LOGOUT = "Se d�connecter";
	public static final String TOOL_LOADPLAN = "Charge le plan � partir d'un fichier XML";
	public static final String TOOL_LOADDELIVERY = "Charge les points de livraison � partir d'un fichier XML";
	public static final String TOOL_COMPUTE = "Calcule l'itin�raire id�al pour les livraisons pr�sentes";
	public static final String TOOL_UNDO = "Annule la derni�re action effectu�e";
	public static final String TOOL_REDO = "Refait la derni�re action annul�e";
	public static final String TOOL_ADD = "Ajoute un point de livraison � l'endroit selectionn�";
	public static final String TOOL_REMOVE = "Supprime le point de livraison selectionn�";
	public static final String TOOL_VALIDE_AJOUT = "Valide l'ajout en cours";
	public static final String TOOL_ANNULER_AJOUT = "Annule l'ajout en cours";
	public static final String TOOL_PRINT = "Imprime un r�capitulatif des instructions pour le livreur";

	// Texte des boutons
	public static final String BUTTON_LOADPLAN = "Charger Plan";
	public static final String BUTTON_LOADDELIVERY = "Charger Livraisons";
	public static final String BUTTON_COMPUTE = "Calculer Itineraire";
	public static final String TEXTE_VALIDE_AJOUT = "Valider";
	public static final String TEXTE_ANNULER_AJOUT = "Annuler";


	// Messages de l'interface
	public static final String TITLE = "Pr�paration pour le lendemain";
	public static final String WELCOME = "Bonjour M. Henry";

	// Types de noeuds
	public static final String TEXT_SIMPLENODE = "Noeud libre";
	public static final String TEXT_DELIVERYNODE = "Point de livraison";
	public static final String TEXT_WAREHOUSE = "Entrep�t";
	public static final String TEXT_NOSELECTION = "Aucun point s�lectionn�";

	// Messages d'erreur
	public static final String CHARGEMENT_PLAN_IMPOSSIBLE = "Impossible de charger un plan � partir de ce fichier XML";
	public static final String CHARGEMENT_ITINERAIRE_IMPOSSIBLE = "Impossible de charger les livraisons � partir de ce fichier XML";
	public static final String ECHEC_CALCUL = "Le calcul a �chou�";
	public static final String SUCCES_CALCUL = "Le calcul a r�ussi";
	public static final String SOLU_NON_OPTI_CALCUL = "La solution trouv�e n'est pas optimale";
	public static final String ANNULER_IMPOSSIBLE = "Impossible d'annuler";
	public static final String REFAIRE_IMPOSSIBLE = "Impossible de refaire";
	public static final String GENERATION_FDR_IMPOSSIBLE = "Impossible de g�n�rer la feuille de route du livreur";

	// Messages
	public static final String CHARGEMENT_PLAN_REUSSI = "Chargement du plan r�ussi";
	public static final String CHARGEMENT_LIVRAISONS_REUSSI = "Chargement des livraisons r�ussi";
	public static final String GENERATION_FDR_REUSSI = "La g�n�ration de la feuille de route du livreur a r�ussie";
	public static final String AUCUN_NOEUD_SELECT = "Aucun point du plan n'est s�lectionn�";
	public static final String SUPPRESSION_REUSSIE = "Suppression r�ussie";
	public static final String SUPPRESSION_ECHEC = "Echec suppression";
	public static final String AJOUT_REUSSI = "Ajout r�ussi";
	public static final String AJOUT_ECHEC = "Echec ajout";
	public static final String AJOUT_SUITE = "Selectionnez la livraison pr�c�dente ou l'entrep�t et validez";
	public static final String AJOUT_NOEUD_NON_VIDE = "Le noeud choisi n'est pas vide";
	public static final String AJOUT_NOEUD_EST_VIDE = "Le noeud choisi est vide";


	// Fenetre Quitter
	public static final String TITRE_FENETRE_QUITTER = "Confirmation";
	public static final String TEXTE_FENETRE_QUITTER = "Voulez-vous vraiment fermer l'application ?";

	// Fenetre Demande temp TSP plus long
	public static final String TITRE_FENETRE_TEMPS_TSP = "Augmentation du temps";
	public static final String TEXTE_FENETRE_TEMPS_TSP = "Voulez-vous relancer le calcul avec un temps plus long ?";

	//************//
	// ZoneDessin //
	//************//

	public static final boolean AUTORISE_ZOOM = true;
	public static final boolean AUTORISE_TRANSLATION = true;
	public static final float DELTA_ZOOM = 0.1f;
	public static final Color COULEUR_FOND = Color.WHITE;
	public static final Color COULEUR_CADRE = Color.BLACK;
	public static final int LARGEUR_CADRE = 5;
	public static final boolean CADRE_ARRONDI = true;



	//************//
	//  VuePlan   //
	//************//

	public static final float TAILLE_NOEUD = 16;
	//Distance minimal entre un troncon sortant et un troncon entrant
	public static final float LARGEUR_CHAUSEE = TAILLE_NOEUD / 5;
	//Decalage entre deux passages d'un troncon
	public static final float DECALAGE_PASSAGE = 4f;
	//Decaler les troncons
	public static final boolean DECALAGE_TRONCON = true;
	public static final float LARGEUR_TRONCON = TAILLE_NOEUD / 6;
	// Couleur d'un noeud
	public static final Color COUL_NOEUD = Color.BLACK;
	// Couleur d'un tron�on
	public static final Color COUL_TRONCON = Color.GRAY;
	//Couleur du noeud selectionnne
	public static final Color COUL_NOEUD_SEL = Color.decode("#FF4000");//orange
	// Couleur des troncon d'un noeud selectionne
	public static final Color COUL_TONCON_SEL = Color.decode("#FA8258");
	// Couleur des noeuds destination des troncon d'un noeud selectionne
	public static final Color COUL_TERM_SEL = Color.decode("#FA8258");
	public static final float MIN_ZOOM = 0.2f;
	public static final float MAX_ZOOM = 10f;
	// Affiche les tron�on du noeud selectionne
	public static final boolean AFF_TRONCON_SEL = true;
	// Affiche les noeuds destination des troncons du noeuds selectionne
	public static final boolean AFF_TERM_SEL = true;
	//Si vrai, ne se contente pas d'agrandir les distances, mais resize tout les element
	//du graphe
	public static final boolean ZOOM_ELEMENT = false;

	//****************//
	//  VueItineaire  //
	//****************//

	/**
	 * Permet de r�soudre les conflits si un noeud est parcouru par deux plages horaires differente
	 * (et qu'il n'est pas un noeud de livraison) :
	 * ->FIRST_WIN : Le noeud garde la couleur de la premi�re plage horaire auquel il appartient
	 * ->LAST_WIN : Le noeud garde la couleur de la derni�re plage horaire auquel il appartient
	 * ->BOTH_WIN : Le noeud prend la couleur des deux plages horaires (camembert)
	 * ->PORPOTIONATE : Le noeud prend la couleur des deux plages horaires. Plus les trajets d'une
	 * plage horaire passe par ce noeud, plus sa part de couleur est importante.
	 */
	public static enum DrawStrategy {
		FIRST_WIN, LAST_WIN, BOTH_WIN, PROPORTIONATE
	};

	public static final DrawStrategy STRATEGY = DrawStrategy.BOTH_WIN;

	public static final DrawStrategy STRATEGY_LIVRAISON = DrawStrategy.BOTH_WIN;

	
	/**
	 * Colorie les noeuds de de passage
	 * (i.e les noeuds qui ne sont pas des livraisons)
	 * dans la couleur de la plage horaire auquel ils appartiennent
	 * */
	public static final boolean COLORIE_NOEUD_COMME_PLAGE = true;
	//Couleur des plages horaires
	public static final Color[] COLORS = {
										   Color.decode("#0021FF"),//Blue
										   Color.decode("#04B404"),//Green
										   Color.decode("#FFFF00"),//Yellow
										   Color.decode("#04B4AE"),//Cyan
										   Color.decode("#5F04B4"),//Purple
										   Color.decode("#04B45F"),//Teal
										 };

	public static final int NB_COLORS = COLORS.length;


	//Couleur de l'entrepot
	public static final Color COUL_ENTREPOT = Color.MAGENTA;
	//Couleur d'un noeud hors plage horaire
	public static final Color COUL_HORS_PLAGE = Color.RED;

	// Couleur d'un noeud
	public static final Color COUL_NOEUD_IT = Color.PINK;
	// Couleur d'un tron�on
	public static final Color COUL_TRONCON_IT = Color.PINK;
	// Couleur d'un noeud selectionne
	public static final Color COUL_NOEUD_SEL_IT = Color.GREEN;
	// Couleur des troncon d'un noeud selectionne
	public static final Color COUL_TONCON_SEL_IT = Color.GREEN;
	// Couleur des noeuds destination des troncon d'un noeud selectionne
	public static final Color COUL_TERM_SEL_IT = Color.GREEN;

}
