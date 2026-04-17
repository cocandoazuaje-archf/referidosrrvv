package cl.cnsv.crypto.encryption;

/**
 * Implementación mínima para compatibilidad de compilación.
 *
 * El proyecto referencia {@code cl.cnsv.crypto.encryption.CryptoUtil} pero la
 * dependencia no está incluida en el repositorio. Esta clase mantiene la misma
 * firma utilizada por el código existente.
 */
public class CryptoUtil {

    public CryptoUtil(String unusedKey, String unusedKeyPath) {
        // intencionalmente vacío
    }

    public String decryptData(String cipherText) {
        // Fallback conservador: si no hay librería de cifrado disponible,
        // retornamos el valor tal cual para no bloquear la ejecución.
        return cipherText;
    }
}
