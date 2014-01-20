/**
 * 
 */
package com.h4313.optitournee.controleur.parserXML;

/**
 * La classe <code>XmlParserException</code> r???pr???sente les exceptions qui peuvent ???tre g???n???r???es
 * lors du traitement des fichier XML. 
 * Elle permet de regrouper toutes les autres exceptions qui peuvent ???tre g???n???r???e
 * ( ie <code>SAXException</code>, <code>IOException</code>, etc) 
 * et de n'en produire qu'une seule. Cette classe fournit ???galement un message destin??? ???
 * l'utilisateur afin qu'il puisse savoir les raisons de l'???chec du traitement du fichier.
 * @author H4403
 * @see Exception
 */
public class XmlParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Message ??? destination de l'utilisateur afin de connaitre la cause de l'???chec du traitement du 
	 * fichier XML ??? traiter.
	 */
	private String msgUtilisateur;
	
	/**
	 * Constructeur de base avec un message d'erreur.
	 * @param message Message d'erreur g???n???r???.
	 */
	public XmlParserException (String message)
	{
		super(message);
		this.msgUtilisateur= " Le fichier N'EST PAS VALIDE \n"+ "Cause: "+message;
		
	}
	

	/**
	 * Constructeur qui permet de connaitre la v???ritable exception qui ??? ???t??? lev???e
	 * ainsi que le message d'erreur g???n???r???.
	 * @param message Message d'erreur associ??? ??? l'exception lev???e.
	 * @param e Exception lev???e lors du parsage
	 */
	public XmlParserException (String message,Exception e)
	{
		super(e.getClass().getSimpleName()+"--->"+message);
		
		String [] cause= e.getLocalizedMessage().split(":");
		String [] msg= message.split("\n");
		this.msgUtilisateur= msg[0] +"\n"+ "Cause: "+cause[cause.length-1] +"\n";
	}
	
	/**
	 * la m???thode <code>getMsgUtilisateur</code> permet d'obtenir le message ??? destination de l'utilisateur.
	 * @return Le message ??? destination de l'utilisateur.
	 */
	public String getMsgUtilisateur(){
		return this.msgUtilisateur;
	}

}
