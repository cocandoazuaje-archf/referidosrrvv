package cl.cnsv.referidosrrvv.clases;

/**
 * Compatibilidad para llamadas históricas desde {@link UpProspeccionTh}.
 *
 * La versión previa de este archivo quedó comentada y rompía la compilación
 * (el archivo no contenía la clase esperada). Esta implementación delega al
 * cliente HTTP simple usado en {@link Prospection}.
 */
public class Prospection2 {

    private Prospection2() {
        // constructor vacío
    }

    public static String consultar(String rut, int timeout) {
        return Prospection.consultar(rut, timeout);
    }
}
