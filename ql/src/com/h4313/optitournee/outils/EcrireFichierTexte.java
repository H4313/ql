package com.h4313.optitournee.outils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe abstraite contenant une m�thode permettant d'ecrire une chane de caract�res
 * dans un fichier
 * @author H4403
 *
 */
public abstract class EcrireFichierTexte {

	/**
	 * ecrireFichierTexte est une m�thode qui va �crire du texte dans un fichier
	 * @param adresseFichier adresse du fichier o� �crire
	 * @param texte texte qui sera �crit dans le fichier
	 * @return -1 en cas de probl�me, 0 sinon
	 */
	public static int ecrireFichierTexte(String adresseFichier, String texte){
		try
		{
			FileWriter fw = new FileWriter(adresseFichier, true);
			BufferedWriter fluxSortie = new BufferedWriter(fw);

			fluxSortie.write(texte);
			fluxSortie.flush();

			fluxSortie.close();
		}
		catch(IOException ioe){
			return -1;
		}

		return 0;
	}
}
