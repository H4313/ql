/**
 *
 */
package com.h4313.optitournee.vue;

import java.io.File;
import javax.swing.JFileChooser;

import com.h4313.optitournee.controleur.parserXML.MonFileFilter;

/**
 * La classe <code>OpenChooserDialog</code> g�re l'ouverture
 * d'explorateurs de fichiers
 *
 * @author H4403
 *
 */
public class OpenChooserDialog {

	//Chemin du r�pertoire de d�part
	private String starting_directory  = "C:\\";


	//Titre du dialog
	private String title = "";

	private String cheminFichier="";

	//Mettre � "vrai" pour ouvrir la boite de dialog
	//en mode sauvegarde
	private boolean is_saving_mode = true;


	public OpenChooserDialog(String starting_directory, String title) {
		this.cheminFichier=null;
		this.starting_directory = starting_directory;
		this.title = title;
	}

	public OpenChooserDialog(String starting_directory) {
		this.cheminFichier=null;
		this.starting_directory = starting_directory;
	}

	public void setStartingDirectory(String d) {
		this.starting_directory = d;
	}

	public String getCheminFichier() {
		return this.cheminFichier;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSavingMode(boolean b) {
		is_saving_mode = b;
	}

	/**
	 * 	Cette m�thode permet d'ouvrir un gestionnaire de fichier
	 * 	et de choisir un fichier XML d�sir�.
	 */
	public void ouvrirFichier() {

		JFileChooser jFileChooserXML = new JFileChooser();
		jFileChooserXML.setDialogTitle(title);
		jFileChooserXML.setCurrentDirectory(new File(starting_directory));

		if(is_saving_mode) {
			jFileChooserXML.setDialogType(JFileChooser.SAVE_DIALOG);
	        if (jFileChooserXML.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

	        	this.cheminFichier=jFileChooserXML.getSelectedFile().getAbsolutePath();
	        }
		}
		else {
			MonFileFilter filter = new MonFileFilter();
			filter.addExtension("xml");
			filter.setDescription("Fichier XML");
			jFileChooserXML.setFileFilter(filter);
			jFileChooserXML.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        if (jFileChooserXML.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

	        	this.cheminFichier=jFileChooserXML.getSelectedFile().getAbsolutePath();
	        }
		}


	}

}
