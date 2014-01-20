package com.h4313.optitournee.controleur;

import com.h4313.optitournee.outils.Constantes;



/**
 * La classe <code>CalculerTournee</code> est une commande pour calculer la tourn�e entre les livraisons,
 * sur un itin�raire
 *
 * @author H4403
 */
public class CalculerTournee implements Commande {

	/**
	 * Controleur de l'application qui connait l'itin�raire sur lequel la commande
	 * est �x�cut�e et o� le calcul est r�alis�
	 */
	private Controleur controleur;

	/**
	 * Constructeur de la commande
	 * @param contr <code>Controleur</code> de l'application sur lequel la commande est �x�cut�e
	 */
	public CalculerTournee(Controleur contr){
		this.controleur = contr;
	}

	/**
	 * La m�thode <code>annuler</code> annule le calcul en supprimant les trajets calcul�s entre les livraisons
	 *
	 * @return <code>false</code>
	 */
	public boolean annuler() {
		this.controleur.itineraire.annulerCalcul();
		return false;
	}

	/**
	 * La m�thode <code>refaire</code> relance le calcul
	 *
	 * @return	<code>true</code> si le calcul a abouti normalement
	 */
	public boolean refaire() {
		int res = this.controleur.itineraire.calculerTournee();
		return res != 0;
	}

	/**
	 * La m�thode <code>faire</code> lance le calcul pour la premi�re fois
	 *
	 * @return	<code>true</code> si le calcul a abouti normalement
	 */
	public boolean faire() {
		int res = this.controleur.itineraire.calculerTournee();

		if(res == 0){ //Solu opti trouv�e
			this.controleur.vueAppli.afficherMessageInfo(Constantes.SUCCES_CALCUL);
			return true;
		}
		else if(res == 1){ //Solu non optimal trouv�e
			this.controleur.vueAppli.afficherMessageAvertissement(Constantes.SOLU_NON_OPTI_CALCUL);
			if(this.controleur.vueAppli.demanderCalculPlusLong()){
				this.controleur.augmenterTempsTSP();
				this.controleur.calculerTournee();
				return false;
			}
			else{
				return true;
			}
		}
		else if(res == -2){ //Pas de resultat dans le temps
			this.controleur.vueAppli.afficherMessageErreur(Constantes.ECHEC_CALCUL);
			if(this.controleur.vueAppli.demanderCalculPlusLong()){
				this.controleur.augmenterTempsTSP();
				this.controleur.calculerTournee();
			}
			return false;
		}
		else{ //Echec calcul
			this.controleur.vueAppli.afficherMessageErreur(Constantes.ECHEC_CALCUL);
			return false;
		}
	}

}
