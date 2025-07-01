package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SeguridadUtil {

    public static String encriptarSHA256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(texto.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error al encriptar: " + e.getMessage());
            return null;
        }
    }
}
