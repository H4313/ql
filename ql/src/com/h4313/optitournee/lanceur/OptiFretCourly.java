package com.h4313.optitournee.lanceur;


import com.h4313.optitournee.controleur.Controleur;


/**
 * Classe OptiFretCourly
 * Point d'entr�e de l'application avec la methode main qui lance le controleur
 *
 * @author H4403
 *
 */
public class OptiFretCourly {

	/**
	 * M�thode main qui lance le controleur et donc l'application
	 *
	 * @param args lors du lancement de l'application (non utilis�)
	 */
	public static void main(String[] args) {
		Controleur.getControleur();
	}

}