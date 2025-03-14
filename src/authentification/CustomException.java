package authentification;

// Classe d'exception personnalisée pour gérer les erreurs spécifiques
public class CustomException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructeur avec message personnalisé
    public CustomException(String message) {
        super(message);
    }
}
