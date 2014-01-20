package com.h4313.optitournee.outils;

import java.text.DecimalFormat;



/**
 * <p>Classe repr�sentant un temps.</p>
 * @author S�bastien ESTIENNE , Stephane KI
 */
public class Temps
{
	/**
	 * Le nombre d'heures.
	 */
   private int heures;

   /**
    * Le nombre de minutes.
    */
   private int minutes;

   /**
    * Le nombre de secondes.
    */
   private int secondes;


   /**
    * <p>Constructeur par d�faut d'un temps.</p>
    */
   public Temps()
   {
      this(0, 0, 0);
   }


   /**
    * <p>Constructeur de temps avec le nombre d'heures sp�cifi�s.</p>
    * @param heures Le nombre d'heures.
    */
   public Temps(int heures)
   {
      this(heures, 0, 0);
   }


   /**
    * <p>Constructeur de temps avec le nombre d'heures et de minutes sp�cifi�s.</p>
    * @param heures Le nombre d'heures.
    * @param minutes Le nombre de minutes.
    */
   public Temps(int heures, int minutes)
   {
      this(heures, minutes, 0);
   }


   /**
    * <p>Constructeur de temps avec le nombre d'heures, de minutes et de secondes sp�cifi�s.</p>
    * @param heures Le nombre d'heures.
    * @param minutes Le nombre de minutes.
    * @param secondes Le nombre de secondes.
    */
   public Temps(int heures, int minutes, int secondes)
   {
      this.setHeures(heures);
      this.setMinutes(minutes);
      this.setSecondes(secondes);
   }


   /**
    * <p>Constructeur de temps � partir d'un temps existant.</p>
    * @param temps Un temps.
    */
   public Temps(Temps temps)
   {
      this(temps.getHeures(), temps.getMinutes(), temps.getSecondes());
   }


   /**
    * <p>Retourne le nombre d'heures.</p>
    * @return Renvoie le nombre d'heures.
    */
   public int getHeures()
   {
      return this.heures;
   }


   /**
    * <p>Modifie le nombre d'heures.</p>
    * @param heures Le nombre d'heures.
    */
   public void setHeures(int heures)
   {
      if(heures < 0 || heures > 23)
         this.heures = 0;
      else
         this.heures = heures;
   }


   /**
    * <p>Retourne le nombre de minutes.</p>
    * @return Renvoie le nombre de minutes.
    */
   public int getMinutes()
   {
      return this.minutes;
   }


   /**
    * <p>Modifie le nombre de minutes.</p>
    * @param minutes Le nombre de minutes.
    */
   public void setMinutes(int minutes)
   {
      if(minutes < 0 || minutes > 60)
         this.minutes = 0;
      else
         this.minutes = minutes;
   }


   /**
    * <p>Retourne le nombre de secondes.</p>
    * @return Renvoie le nombre de secondes.
    */
   public int getSecondes()
   {
      return this.secondes;
   }


   /**
    * <p>Modifie le nombre de secondes.</p>
    * @param secondes Le nombre de secondes.
    */
   public void setSecondes(int secondes)
   {
      this.secondes = secondes;
   }


   /**
    * <p>Ajoute au temps courant un nombre d'heures.</p>
    * @param heures_ Le nombre d'heures.
    */
   public void ajouterHeures(int heures_)
   {
      this.setHeures((this.heures + heures_) % 24);
   }


   /**
    * <p>Ajoute au temps courant un nombre de minutes.</p>
    * @param minutes_ Le nombre de minutes.
    */
   public void ajouterMinutes(int minutes_)
   {
      this.ajouterHeures((this.minutes + minutes_) / 60);
      this.setMinutes((this.minutes + minutes_) % 60);
   }


   /**
    * <p>Ajoute au temps courant un nombre de secondes.</p>
    * @param secondes_ Le nombre de secondes.
    */
   public void ajouterSecondes(int secondes_)
   {
      this.ajouterMinutes((this.secondes + secondes_) / 60);
      this.setSecondes((this.secondes + secondes_) % 60);
   }

   /**
    * Verifie si l'objet temps courant est apres l'objet t
    * Si les deux temps sont �gaux la m�thode renvoi false
    *
    * @param t
    * @return si this est apr�s t
    */
   public boolean after(Temps t)
   {
	   boolean aft=true;

	   if (this.heures<t.heures)
	   {
		   aft=false;
	   }
	   else
	   {
		   if (this.heures==t.heures)
		   {
			   if (this.minutes<t.minutes)
			   {
				   aft=false;
			   }
			   else
			   {
				   if (this.minutes==t.minutes)
				   {
					   if (this.secondes<= t.secondes)
					   {
						   aft=false;
					   }
				   }
			   }
		   }
	   }

	   return aft;
   }


   /**
    *
    * @param t
    * @return si this est avant t
    */
   public boolean before(Temps t)
   {
	   boolean egal= ((this.heures==t.heures) &&( this.minutes==t.minutes) && ( this.secondes==t.secondes));

		return (t.after(this) && !egal) ;

   }

   /**
    * <p>Retourne une repr�sentation du temps.</p>
    * @return Renvoie une repr�sentation du temps.
    */
   @Override
   public String toString()
   {
      String resultat;
      DecimalFormat dFormat = new DecimalFormat("00");

      resultat = "Temps: " + dFormat.format(this.getHeures()) + "h "
            + dFormat.format(this.getMinutes()) + "min " + dFormat.format(this.getSecondes())
            + "sec.";

      return resultat;
   }
}
