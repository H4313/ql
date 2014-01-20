//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.vue;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

import com.h4313.optitournee.modele.Itineraire;
import com.h4313.optitournee.modele.Livraison;
import com.h4313.optitournee.modele.Livraison.EtatLivraison;
import com.h4313.optitournee.modele.Noeud;
import com.h4313.optitournee.modele.NoeudItineraire;
import com.h4313.optitournee.modele.PlageHoraire;
import com.h4313.optitournee.modele.Plan;
import com.h4313.optitournee.modele.Trajet;
import com.h4313.optitournee.modele.Troncon;
import com.h4313.optitournee.outils.Constantes;
import com.h4313.optitournee.outils.Constantes.DrawStrategy;

/**
 * La classe VueItineraire dessine un itineraire sur une zone de dessin. Du
 * point de vue graphique, il s'agit d'un plan particulier, qui fait la
 * distinction entre livraison, entrepot et noeud dit "de passage"
 *
 * @author H4403
 */
public class VueItineraire extends VuePlan {


	/**
	 * pointeur vers le mod�le de l'itin�raire
	 *
	 * @see Itineraire
	 */
	private Itineraire itineraire;

	private DrawStrategy strategy = Constantes.STRATEGY;
	/*
	 * Colorie les noeuds de de passage (i.e les noeuds qui ne sont pas des
	 * livraisons) dans la couleur de la plage horaire auquel ils appartiennent
	 */
	private boolean color_node_comme_plage = Constantes.COLORIE_NOEUD_COMME_PLAGE;
	private Color[] couleurs = Constantes.COLORS;

	// Couleur de l'entrepot
	private Color c_entrepot = Constantes.COUL_ENTREPOT;

	private Color c_hors_plage = Constantes.COUL_HORS_PLAGE;

	/*
	 * Si l'itin�raire n'est pas calcul�, alors les points donn� sont en fait
	 * des points de livraison
	 */
	private boolean calculated = false;

	private DrawStrategy strategy_livraison = Constantes.STRATEGY_LIVRAISON;


	public VueItineraire(Itineraire it, int minX, int maxX, int minY, int maxY) {

		super(it != null ? it.getPlan() : null);
		this.itineraire = it;
		this.c_noeud = Constantes.COUL_NOEUD_IT;
		this.c_troncon = Constantes.COUL_TRONCON_IT;
		this.c_select = Constantes.COUL_NOEUD_SEL_IT;
		this.c_sel_troncon = Constantes.COUL_TONCON_SEL_IT;
		this.c_term = Constantes.COUL_TERM_SEL_IT;
		this.setMinMax(minX, maxX, minY, maxY);
	}


	public void dessineTousTroncon(Graphics g) {
		// Si l'itin�raire n'est pas calcule, alors
		// on sait qu'il ne peut pas y avoir de troncon
		if (!calculated)
			return;// ...donc on quitte
		/*
		 * Map qui contient pour chaque troncon dessine, Le nombre de fois ou il
		 * faut encore le dessiner. (Un troncon ayant deux passages sera dessine
		 * deux fois avec un decalage)
		 */
		HashMap<String, Integer> nb_passages = new HashMap<String, Integer>();
		NoeudItineraire entrepot = itineraire.getEntrepot();
		int i = 0;
		// On commence par dessiner le trajet qui part de l'entrepot
		// dans la couleur de la premi�re plage horraire
		dessineTrajetSansNoeud(entrepot, g, couleurs[i % Constantes.NB_COLORS],
				nb_passages);

		// Ensuite, pour chaque livraison d'une plage horraire
		List<PlageHoraire> plages = itineraire.getPlagesHoraire();
		for (PlageHoraire plage : plages) {
			List<Livraison> livraisons = plage.getAllLivraison();
			for (Livraison livraison : livraisons) {
				// on dessine le trajet qui part de la livraison
				// dans la couleur de la plage horraire
				dessineTrajetSansNoeud(livraison, g, couleurs[i
						% Constantes.NB_COLORS], nb_passages);
			}
			i++;
		}

	}

	public void dessineTousNoeud(Graphics g) {
		// Si il n'y a pas d'itineraire d�fini, alors
		// il n'y a pas de noeud
		if (itineraire == null)
			return;// ...donc on quitte

		/*
		 * Cette liste contient toutes les adresse de livraison deja livre qui
		 * ne doivent pas etre redessine car leur couleur doit rester celle de
		 * leur plage horaire (y compris si elle sont de simple point de passage
		 * pour d'autre trajet)
		 */
		HashMap<String, Color[]> adresse_deja_livre = new HashMap<String, Color[]>();

		/*
		 * Cette liste contient pour chaque noeud le nombre de couleur
		 * differentes (donc de plage horaire) auquel il appartient
		 */
		HashMap<String, Color[]> noeud_passage_passe = new HashMap<String, Color[]>();

		NoeudItineraire entrepot = itineraire.getEntrepot();
		int i = 0;

		// On commence par dessiner tout les noeuds sur le trajet qui part de
		// l'entrepot
		if (calculated) {
			dessineNoeudsDeTrajetSuivantNI(entrepot, g, couleurs[i
					% Constantes.NB_COLORS], adresse_deja_livre,
					noeud_passage_passe);
		}

		// Ensuite, pour chaque livraison de chaque plage horraire
		List<PlageHoraire> plages = itineraire.getPlagesHoraire();
		for (PlageHoraire plage : plages) {
			List<Livraison> livraisons = plage.getAllLivraison();
			for (Livraison livraison : livraisons) {
				/*
				 * ...on dessine les noeuds appartenant au trajet qui part du
				 * point de livraison parcourus (si ce ne sont pas des point de
				 * livraion d�j� parcourus)
				 */
				if (calculated) {
					dessineNoeudsDeTrajetSuivantNI(livraison, g,
							(color_node_comme_plage ? couleurs[i
									% Constantes.NB_COLORS] : c_noeud),
							adresse_deja_livre, noeud_passage_passe);
				}
				// ...puis on dessine le point de livraison lui m�me
				dessineNoeudItineraire(livraison, g, couleurs[i
						% Constantes.NB_COLORS], adresse_deja_livre);
				// adresse_deja_livre.add(livraison.getAdresse().toString());
			}
			i++;
		}

		// On dessine l'entrepot en dernier pour eviter qu'il ne soit colorier
		// comme un point de passage d'un trajet
		dessineNoeudItineraire(entrepot, g, c_entrepot, adresse_deja_livre);

	}


	/**
	 * Dessine un NoeudItineraire - c'est � dire un Entrepot ou une livraison
	 *
	 * @param ni
	 *            le noeud itin�raire � dessiner
	 * @param g
	 *            Graphics passe par la zone de dessin
	 * @param c
	 *            la couleur du noeud
	 */
	private void dessineNoeudItineraire(NoeudItineraire ni, Graphics g,
			Color c, HashMap<String, Color[]> adresse_deja_livre) {

		// Un noeud itineraire est dessine differement selon qu'il est
		// ->un entrepot (un noeud cercl� avec un plus a l'interieur)
		// ->une livraison (un noeud cercl�)
		// ->une livraison hors plage horaire (un noeud cercle avec un "+" �
		// l'int�rieur)

		// On commence par regarder si le noeud est une livraison hors plage
		// horaire
		Noeud adresse = ni.getAdresse();
		if (ni instanceof Livraison
				&& ((Livraison) ni).getEtat() == EtatLivraison.HORS_PLAGE_HORAIRE) {
			c = c_hors_plage;// Dans ce cas la, on remplace la couleur 'c' par
								// une couleur speciale
		}

		// on dessine ensuite un noeud "normal" (quel que soit le type de noeud
		// itin�raire)

		this.dessineCamembert(adresse, g, adresse_deja_livre,
				this.strategy_livraison, c);

		// puis on le cercle de noire pour signifier qu'il s'agit d'un noeud
		// itineraire
		dessineCercleNoir(g, adresse);

		// Si le noeud est en plus un entrepot,
		// on dessine un "+" � l'int�rieur du cercle
		if (!(ni instanceof Livraison)) {
			g.drawLine(screenX(adresse.getX()), screenY(adresse.getY())
					+ (int) taille_noeud / 2, screenX(adresse.getX())
					+ (int) taille_noeud, screenY(adresse.getY())
					+ (int) taille_noeud / 2);
			g.drawLine(screenX(adresse.getX()) + (int) taille_noeud / 2,
					screenY(adresse.getY()), screenX(adresse.getX())
							+ (int) taille_noeud / 2, screenY(adresse.getY())
							+ (int) taille_noeud);
		}

	}

	/**
	 * dessine un cercle noir autour d'un noeud n
	 *
	 * @param g
	 * @param n
	 */
	private void dessineCercleNoir(Graphics g, Noeud n) {
		g.setColor(Color.black);
		g.drawOval(screenX(n.getX()), screenY(n.getY()), (int) taille_noeud,
				(int) taille_noeud);
	}

	/**
	 * dessine un camembert de toutes les couleurs des plages horaires auquel
	 * appartient le noeud
	 *
	 * @param n
	 *            le noeud que l'on souhaite colorie
	 * @param g
	 * @param historique
	 *            une table des noeuds deja parcourus avec les couleurs des
	 *            plages horaires auquel ils appartiennent
	 * @param strategy
	 *            la strategie de dessin (both, first, last, proportionate)
	 * @param couleur_demande
	 *            la couleur de la derniere plage horaire auquel appartient le
	 *            noeud
	 */
	private void dessineCamembert(Noeud n, Graphics g,
			HashMap<String, Color[]> historique, DrawStrategy strategy,
			Color couleur_demande) {
		// on dessine le noeud de destination
		// differement selon la strategie defini
		if (strategy == DrawStrategy.LAST_WIN) {
			// Si la strategy est LAST_WIN, il suffit
			// de dessiner le noeud dans la couleur
			// de la derniere plage horaire (donc c)
			dessineNoeud(n, g, couleur_demande);
		} else if (strategy == DrawStrategy.FIRST_WIN) {
			// Si la strategy est FIRST_WIN...
			// On ne dessine le noeud que si c'est
			// la premi�re fois qu'on le parcours

			// En pratique, on verifie qu'il n'est pas dans
			// la liste des noeuds de passage parcourus
			if (!historique.containsKey(n.toString())) {
				// et s'il n'y est pas,
				dessineNoeud(n, g, couleur_demande);
				Color[] couleurs = { couleur_demande };// on le dessine
				// et on l'ajoute a la liste des noeuds de passage parcourus
				historique.put(n.toString(), couleurs);
			}
		} else {
			// Si la strategy est BOTH_WIN ou proportionnate,
			// On regarde si le noeud a deja ete dessine
			if (historique.containsKey(n.toString())) {
				// Si oui,
				Color[] couleurs = historique.get(n.toString());
				boolean deja_peinte = false;
				if (strategy == DrawStrategy.BOTH_WIN) {
					// et que la strategy est BOTH_WIN,
					// on regarde si le noeud a deja ete parcourus par
					// un trajet de la m�me plage horaire
					for (Color coul : couleurs) {
						if (coul == couleur_demande) {
							deja_peinte = true;
							break;
						}
					}
				}
				if (!deja_peinte) {
					// Si non, on ajoute la couleur de la plage horaire actuel
					Color[] couleurs2 = new Color[couleurs.length + 1];
					System.arraycopy(couleurs, 0, couleurs2, 0, couleurs.length);
					couleurs2[couleurs.length] = couleur_demande;
					// puis on dessine le point
					dessineNoeud(n, g, couleurs2);
					historique.put(n.toString(), couleurs2);
				}
				/*
				 * Remarque : Pour la strategy PROPORTIONATE, on ne verifie
				 * jamais is un point a �t� parcourus par un trajet de la m�me
				 * plage horaire. Par consequent plus un point est parcourus
				 * dans un meme plage horaire, plus il sera dessine dans la
				 * couleur de cette plage horaire (sa part de camembert
				 * augmente)
				 */
			} else {
				// Enfin, si le noeud n'a jamais ete dessine
				dessineNoeud(n, g, couleur_demande);// on le dessine une
													// premiere fois
				Color[] couleurs = { couleur_demande };
				// puis on met � jour la liste des noeuds deja dessine
				historique.put(n.toString(), couleurs);
			}
		}

	}

	/**
	 * Dessine tous les noeuds de passage entre le NoeudItineraire "depart" et
	 * le noeud itin�raire suivant. Le dernier point du trajet qui est donc un
	 * point de livraison, sera aussi dessine. (il est redessiner dans la
	 * methode dessineToutNoeud grace a la methode dessineNoeudItineraire)
	 *
	 * @param depart
	 * @param g
	 *            Graphics passe par la zone de dessin
	 * @param c
	 *            la couleur du noeud
	 * @param adresse_deja_livre
	 *            liste des Livraisons d�j� livr�. Les point de livraison ont
	 *            toujours la couleur de leur plage horaire, m�me s'ils sont des
	 *            noeuds de passage pour d'autre trajet.
	 */
	private void dessineNoeudsDeTrajetSuivantNI(NoeudItineraire depart,
			Graphics g, Color c, HashMap<String, Color[]> adresse_deja_livre,
			HashMap<String, Color[]> noeud_passage_passe) {
		Trajet t = depart.getTrajet();
		List<Troncon> troncons_du_trajet = t.getTroncons();
		// Pour chaque troncon du trajet qui part du
		// noeud itineraire de d�part (entrepot ou livraison)
		Noeud n;
		for (Troncon troncon : troncons_du_trajet) {
			if (!adresse_deja_livre.containsKey(troncon.getDestination()
					.toString())) {
				n = troncon.getDestination();
				// on dessine le noeud de destination
				// differement selon la strategie defini
				this.dessineCamembert(n, g, noeud_passage_passe, strategy, c);
			}

		}
	}

	private void dessineNoeud(Noeud n, Graphics g, Color[] couleurs) {
		float start_angle, arc_angle;
		int size = couleurs.length;
		for (int i = 0; i < couleurs.length; i++) {
			start_angle = (float) (360 / size * (i + 1));
			arc_angle = (float) (360 / size);
			g.setColor(couleurs[i]);
			g.fillArc(screenX(n.getX()), screenY(n.getY()), (int) taille_noeud,
					(int) taille_noeud, (int) start_angle, (int) arc_angle);

		}
	}

	/**
	 * Dessine tout les troncons qui compose un trajet, sans noeuds de d�part et
	 * d'arrive.
	 *
	 * @param depart
	 *            le NoeudItineraire d'ou part le trajet
	 * @param g
	 *            Graphics de la zone de dessin
	 * @param c
	 *            couleur des troncons
	 * @param nb_passages
	 *            map qui contient pour chaque troncon deja dessine, le nombre
	 *            de fois ou il faut encore le dessine.
	 */
	private void dessineTrajetSansNoeud(NoeudItineraire depart, Graphics g,
			Color c, HashMap<String, Integer> nb_passages) {
		// Si l'itin�raire n'est pas calcul�, alors il n'y a pas de troncon
		if (!calculated)
			return;// donc on quitte

		// Sinon, pour tout les troncon du trajet qui part du NoeudItineraire
		// depart...
		Trajet t = depart.getTrajet();
		List<Troncon> troncons_du_trajet = t.getTroncons();

		Noeud depart_troncon, arrive_troncon;
		depart_troncon = depart.getAdresse();
		for (Troncon troncon : troncons_du_trajet) {
			arrive_troncon = troncon.getDestination();
			String key = depart_troncon + " " + arrive_troncon;
			int hauteur;
			// On cherche s'il a deja ete dessine
			if (nb_passages.containsKey(key)) {
				// Si non, on recupere le nombre de passage
				// qu'il faudra dessiner
				hauteur = nb_passages.get(key);
			} else {
				// Si oui, on recupere le nombre de passage
				// encore a dessiner
				hauteur = troncon.getNbPassage();
			}
			/*
			 * On dessine un segment parall�le � la droite qui passe par les
			 * points de d�part et d'arriver, mais qui est decaler suivant le
			 * nombre de passage du troncon
			 */

			this.dessineDirection(depart_troncon, arrive_troncon, g, c,
					(int) (largeur_chausse + (troncon.getNbPassage() - hauteur)
							* decalage_passage));

			// On met a jour le nombre de passage restant � dessiner pour le
			// troncon
			nb_passages.put(key, hauteur - 1);// ajoute aussi le troncon s'il
												// n'�tait pas dans la map

			depart_troncon = arrive_troncon;
		}
	}


	/**
	 * @return le plan forme par les noeuds et les arcs de l'itin�raire.
	 */
	private Plan getPlanFromItineraire() {


		// Cr�ation d'une nouvelle liste qui va contenir tous les noeuds de
		// l'itin�raire
		List<Noeud> res = new ArrayList<Noeud>();

		// On commence par ajouter l'adresse de l'entrepot � la liste
		res.add(new Noeud(itineraire.getEntrepot().getAdresse()));
		if (calculated) {
			// ...puis tous les noeuds qui compose le trajet partant de
			// l'entrepot
			Trajet t = itineraire.getEntrepot().getTrajet();
			List<Troncon> troncons_du_trajet = t.getTroncons();
			for (Troncon troncon : troncons_du_trajet) {
				res.add(new Noeud(troncon.getDestination()));
			}
		}

		// On fait la m�me chose pour chaque Livraison d'une plage horaire...
		List<PlageHoraire> plages = itineraire.getPlagesHoraire();
		for (PlageHoraire plage : plages) {
			List<Livraison> livraisons = plage.getAllLivraison();
			for (Livraison livraison : livraisons) {
				res.add(new Noeud(livraison.getAdresse()));
				if (calculated) {
					Trajet t = livraison.getTrajet();// On recup�re le trajet de
														// la livraison
					List<Troncon> troncons_du_trajet = t.getTroncons();
					for (Troncon troncon : troncons_du_trajet) {
						res.add(new Noeud(troncon.getDestination()));// On
																		// ajoute
																		// les
																		// noeuds
																		// du
																		// trajet
					}
				}

			}

		}

		// Pour finir on enl�ve tout les troncons qui m�ne a des noeuds qui ne
		// font pas partis
		// de l'itin�raire
		for (Noeud n : res) {
			if (calculated) {
				// Pour chaque troncons sortant d'un noeud
				for (int i = 0; i < n.getTroncons_sortants().size(); i++) {
					// on v�rifie qu'il m�ne � un noeud de l'itin�raire
					if (!res.contains(n.getTroncons_sortants().get(i)
							.getDestination())) {
						n.getTroncons_sortants().remove(i);// Si non, on
															// l'enl�ve
						i--;// Sans oublier de prendre en compte que la liste se
							// d�cale vers la gauche
					}
				}
			} else {
				n.getTroncons_sortants().clear();
			}

		}

		// On renvoie le plan forme par ses noeuds
		// Chaque noeud ayant en attribut ses troncon sortant
		return new Plan(res);
	}

	public Itineraire getItineraire() {
		return itineraire;
	}

	/**
	 * Permet de specifier si l'itineraire a deja ete calcule ou pas.
	 *
	 * @param calculated
	 */
	public void setCalculated(boolean calculated) {
		this.calculated = calculated;
		if (calculated) {
			plan = getPlanFromItineraire();
		}
	}

	/**
	 * @return vraie si l'itin�raire a �t� calcul�
	 */
	public boolean isCalculated() {
		return calculated;
	}

	/**
	 * Change l'itineraire que l'on dessine.
	 *
	 * precondition si l'itineraire est deja calcule, on doit appeller avant
	 *      setCalculated(true). Si l'itineraire n'est pas calcule, on doit
	 *      appeller avant setCalculated(false)
	 * @param it
	 *            l'itineraire a dessine
	 */
	public void setItineraire(Itineraire it) {
		itineraire = it;
		plan = (itineraire == null ? null : getPlanFromItineraire());
	}


}
