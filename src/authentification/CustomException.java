package authentification;

/**
 * Cette classe représente une exception personnalisée pour le projet d'authentification.
 * Elle étend la classe Exception et permet de personnaliser le message d'erreur.
 */
public class CustomException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8297007093511557654L;

	/**
     * Constructeur pour initialiser l'exception avec un message personnalisé.
     *
     * @param message Le message d'erreur à afficher.
     */
    public CustomException(String message) {
        super(message);
    }
}
