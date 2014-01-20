//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.controleur;

/**
 * L'interface Commande est une interface pour le pattern Commander
 * Toutes les commandes pouvant �tre annul�es ou refaites doivent
 * impl�menter cette interface.
 *
 * @author H4403
 */
interface Commande {

	/**
	 * La m�thode <code>annuler</code> pour annuler la commande (d�faire ce qu'elle a fait)
	 * @return <code>true</code> si l'op�ration a r�ussie
	 */
	public boolean annuler();

	/**
	 * La m�thode <code>refaire</code> pour refaire la commande si elle a �t� annul�e
	 * @return <code>true</code> si l'op�ration a r�ussie
	 */
	public boolean refaire();

	/**
	 * La m�thode <code>faire</code> ex�cute la commande pour la premi�re fois
	 * @return <code>true</code> si l'op�ration a r�ussie
	 */
	public boolean faire();

}

