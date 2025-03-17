package authentification;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;
import org.sqlite.SQLiteDataSource;

public class Database {
	private Connection conn;
	private static final String DB_URL = "jdbc:sqlite:auth.db";
	private static final int MAX_RETRIES = 3;
	private static final int RETRY_DELAY = 1000; // 1 seconde

	public void connect() throws SQLException {
		if (conn != null && !conn.isClosed()) {
			disconnect();
		}

		int retryCount = 0;
		while (retryCount < MAX_RETRIES) {
			try {
				SQLiteDataSource ds = new SQLiteDataSource();
				ds.setUrl(DB_URL);
				conn = ds.getConnection();
				return;
			} catch (SQLException e) {
				if (e.getSQLState().equals("HY000") && e.getMessage().contains("database is locked")) {
					retryCount++;
					try {
						Thread.sleep(RETRY_DELAY * retryCount);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
						throw new SQLException("Interruption pendant la tentative de reconnexion", ex);
					}
				} else {
					throw e;
				}
			}
		}
		throw new SQLException("Impossible de se connecter après " + MAX_RETRIES + " tentatives");
	}

	public void disconnect() throws SQLException {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// Ignorer les erreurs de fermeture
			} finally {
				conn = null;
			}
		}
	}

	public void createUsersTable() throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS users (" + "email TEXT PRIMARY KEY," + "password TEXT NOT NULL)";

		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
		}
	}

	public boolean registerUser(String email, String password) throws SQLException {
		if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("Email et mot de passe ne peuvent pas être vides");
		}

		String hashedPassword = hashPassword(password);
		String query = "INSERT INTO users (email, password) VALUES (?, ?)";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, email);
			pstmt.setString(2, hashedPassword);
			return pstmt.executeUpdate() == 1;
		}
	}

	public boolean authenticate(String email, String password) throws SQLException {
		if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("Email et mot de passe ne peuvent pas être vides");
		}

		String hashedPassword = hashPassword(password);
		String query = "SELECT * FROM users WHERE email = ? AND password = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, email);
			pstmt.setString(2, hashedPassword);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		}
	}

	private String hashPassword(String password) {
		try {
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[16];
			random.nextBytes(salt);

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] passwordBytes = password.getBytes();
			byte[] saltedPassword = new byte[salt.length + passwordBytes.length];
			System.arraycopy(salt, 0, saltedPassword, 0, salt.length);
			System.arraycopy(passwordBytes, 0, saltedPassword, salt.length, passwordBytes.length);

			byte[] hashBytes = md.digest(saltedPassword);
			byte[] finalHash = new byte[salt.length + hashBytes.length];
			System.arraycopy(salt, 0, finalHash, 0, salt.length);
			System.arraycopy(hashBytes, 0, finalHash, salt.length, hashBytes.length);

			return Base64.getEncoder().encodeToString(finalHash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 n'est pas disponible", e);
		}
	}
}
