package com.h4313.optitournee.outils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Classe contenant des methodes pour g�rer des Calendar
 *
 * @author H4403
 *
 */
public abstract class GestionCalendar {

	/**
	 * formatDate format de sortie pour afficher une date en String
	 */
	private static final SimpleDateFormat formatDate = new SimpleDateFormat("d MMMMM yyyy");

	/**
	 * formatHeure format de sortie pour afficher une heure en String
	 */
	private static final SimpleDateFormat formatHeure = new SimpleDateFormat("HH:mm");

	/**
	 * calendarToDateString retourne la date en String d'un calendar
	 * @param date Calendar � afficher
	 * @return texte repr�sentant la date
	 */
	public static String calendarToDateString(Calendar date){
        return formatDate.format(date.getTime());
    }

	/**
	 * calendarToHeureString retourne l'heure en String d'un calendar
	 * @param date Calendar � afficher
	 * @return texte repr�sentant l'heure
	 */
	public static String calendarToHeureString(Calendar date){
        return formatHeure.format(date.getTime());
    }

	/**
	 * Retourne le jour de la semaine en fran�ais d'un calendar
	 * @param date Calendar
	 * @return test repr�sentant le jour de la semaine
	 */
	public static String getJour(Calendar date){
        return date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.ALL_STYLES, Locale.FRENCH);
    }

	/**
	 * Ajoute � un Calendar un certain nombre de seconde
	 * @param date calendar � modifier
	 * @param nbSeconde nombre de seconde � rajouter
	 */
	public static void ajouteSecondes(Calendar date, int nbSeconde){
		date.add(Calendar.SECOND, nbSeconde);
	}

	/**
	 * Test si une date est entre 2 autres
	 * @param ajdh
	 * @param dateDebut
	 * @param dateFin
	 * @return si une date est entre 2 autres
	 */
	public static boolean entreDate(Calendar ajdh, Calendar dateDebut, Calendar dateFin)
    {
        return ajdh.equals(dateDebut) || ajdh.equals(dateFin) || (ajdh.after(dateDebut) && ajdh.before(dateFin));
    }
}
