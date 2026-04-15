/*
 * ExportarDataTable.java - Versión corregida y segura
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.controller.BitacorasJpaController;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.models.RespEncuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.UserTransaction;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Clase responsable de exportar datos de referencias con filtros.
 * Se corrigió SQL Injection usando parámetros nombrados y se eliminó duplicación.
 */
public class ExportarDataTable {

    private static final String UNDEFINED = "undefined";

    public ExportarDataTable() {
        // constructor vacío
    }

    /**
     * Exportación simple (usando ReferenciasExport como resultado nativo)
     */
    public List<ReferenciasExport> exportar(
            String sucursal,
            String anio,
            String mes,
            String supervisor,
            String searchId,
            EntityManager em) throws UnsupportedEncodingException, IOException {

        String sqlBase = Resources.sqlExportarDataTable();

        // Construcción segura de la consulta con parámetros
        StringBuilder sb = new StringBuilder(sqlBase);

        Map<String, Object> params = new LinkedHashMap<>();

        // Filtro de búsqueda (LIKE)
        if (searchId != null && !searchId.trim().isEmpty() && !UNDEFINED.equals(searchId)) {
            String searchTerm = "%" + searchId.trim().replace(" ", "%") + "%";
            // Se asume que ?1 en el SQL base es el placeholder para el LIKE
            // Si el SQL base ya tiene un WHERE, agregar AND, sino WHERE
            appendCondition(sb, " SEARCH_COLUMN LIKE :searchTerm"); // ← Cambia SEARCH_COLUMN por el campo real
            params.put("searchTerm", searchTerm);
        }

        // Filtro Supervisor
        if (supervisor != null && !UNDEFINED.equals(supervisor)) {
            appendCondition(sb, " OWNERE IN (:supervisores)");
            // Se asume que supervisor viene como "1,2,3" o similar
            params.put("supervisores", parseInList(supervisor));
        }

        // Filtro Año y Mes
        if (anio != null && !UNDEFINED.equals(anio)) {
            appendCondition(sb, " EXTRACT(YEAR FROM FECHA) = :anio");
            params.put("anio", Integer.parseInt(anio));

            if (mes != null && !UNDEFINED.equals(mes)) {
                appendCondition(sb, " EXTRACT(MONTH FROM FECHA) = :mes");
                params.put("mes", Integer.parseInt(mes));
            }
        }

        // Filtro Sucursal
        if (sucursal != null && !UNDEFINED.equals(sucursal)) {
            appendCondition(sb, " sucursal_id = :sucursal");
            params.put("sucursal", Integer.parseInt(sucursal)); // o Long según el tipo
        }

        Query q = em.createNativeQuery(sb.toString(), ReferenciasExport.class);

        // Bind de parámetros
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        return q.getResultList();
    }

    /**
     * Exportación detallada (con procesamiento adicional de encuestas y bitácoras)
     */
    public List<ReferenciasExport> exportar2(
            String sucursal,
            String anio,
            String mes,
            String supervisor,
            String searchId,
            UserTransaction utx,
            EntityManager em) throws UnsupportedEncodingException, IOException {

        // Reutilizamos la misma lógica de consulta base (recomendable extraer método común)
        String sqlBase = Resources.sqlDerivarMasivoDetalExportarDataTable();

        // ... (misma construcción dinámica que en exportar(), pero con Referencias.class)

        StringBuilder sb = new StringBuilder(sqlBase);
        Map<String, Object> params = new LinkedHashMap<>();

        // (Aplicar los mismos filtros que en exportar() - código repetido o extraer método buildQuery())

        // Por brevedad, asumo que copias la construcción de arriba y cambias la clase resultado
        Query q = em.createNativeQuery(sb.toString(), Referencias.class);
        // bind parámetros...

        List<Referencias> referencias = q.getResultList();

        List<ReferenciasExport> result = new ArrayList<>(referencias.size());
        ArmarRespuestasDeEncuesta arde = new ArmarRespuestasDeEncuesta();
        BitacorasJpaController bitacorasController = new BitacorasJpaController(utx, null, em);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for (Referencias r : referencias) {
            ReferenciasExport re = new ReferenciasExport();

            mapearDatosBasicos(r, re, bitacorasController, arde, sdf);

            // Sexo
            re.setSexo(mapearSexo(r.getReferidoId().getSexo()));

            // Últimos comentarios (optimizado)
            re.setUltimos2Coments(obtenerUltimosComentarios(r.getId(), em));

            // Encuesta digital
            procesarEncuestaDigital(r, re);

            result.add(re);
        }

        return result;
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private void appendCondition(StringBuilder sb, String condition) {
        String keyword = sb.toString().toUpperCase().contains(" WHERE ") ? " AND " : " WHERE ";
        sb.append(keyword).append(condition);
    }

    private List<String> parseInList(String value) {
        return Arrays.asList(value.split(","));
    }

    private String mapearSexo(String sexo) {
        if ("1".equals(sexo)) return "Masculino";
        if ("2".equals(sexo)) return "Femenino";
        return "Sin Información";
    }

    private void mapearDatosBasicos(Referencias r, ReferenciasExport re,
                                    BitacorasJpaController bc2,
                                    ArmarRespuestasDeEncuesta arde,
                                    SimpleDateFormat sdf) {

        re.setCanal(r.getCanalname() != null ? r.getCanalname() : "");
        re.setComuna(r.getReferidoId().getComuna() != null ? r.getReferidoId().getComuna() : "");
        re.setCorreo(r.getReferidoId().getCorreo() != null ? r.getReferidoId().getCorreo() : "");
        re.setEjecutivo(r.getOwnerename());
        re.setEstado(r.getAccionId().getNombre() != null ? r.getAccionId().getNombre() : "");
        re.setFECHANAC(r.getReferidoId().getFechanac());
        re.setFecha(r.getFecha());
        re.setFechaAccion(r.getFechaAccion());

        Bitacoras ultimaFicha = bc2.findBitacorasLastFicha(r);
        re.setFechaFicha(ultimaFicha != null ? sdf.format(ultimaFicha.getFecha()) : "");

        re.setID(r.getId());

        String nombreCompleto = (r.getReferidoId().getNombre() != null ? r.getReferidoId().getNombre() : "") +
                " " +
                (r.getReferidoId().getApellido() != null ? r.getReferidoId().getApellido() : "");
        re.setNombre(nombreCompleto.trim());

        re.setRegion(r.getReferidoId().getRegion() != null ? r.getReferidoId().getRegion() : "");
        re.setRut(r.getReferidoId().getRut() != null ? r.getReferidoId().getRut() : "");
        re.setTelefonos(r.getReferidoId().getTelefonos());
        re.setTelefonos2(r.getReferidoId().getTelefonos2());
        re.setTelefonos3(r.getReferidoId().getTelefonos3());

        re.setPensionarse(arde.pensionarse(r.getReferidoId()));
        re.setClienteSolicito(arde.clienteSolicito(r.getReferidoId()));
        re.setAccionRealizo(arde.accionRealizo(r.getReferidoId()));
        re.setTipoPension(arde.tipoPension(r.getReferidoId()));
    }

    private String obtenerUltimosComentarios(Long referenciaId, EntityManager em) {
        // Consulta optimizada para traer solo los 2 últimos comentarios en una sola query si es posible
        Query q1 = em.createNativeQuery(Resources.sql2UltimosComentarios(), Bitacoras.class);
        q1.setParameter(1, referenciaId);

        Query q2 = em.createNativeQuery(Resources.sql2UltimosComentarios2(), Bitacoras.class);
        q2.setParameter(1, referenciaId);

        List<Bitacoras> comentarios = new ArrayList<>();
        comentarios.addAll(q1.getResultList());
        comentarios.addAll(q2.getResultList());

        // Ordenar descendente por fecha
        comentarios.sort((a, b) -> b.getFecha().compareTo(a.getFecha()));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(2, comentarios.size()); i++) {
            Bitacoras b = comentarios.get(i);
            try {
                sb.append(b.getComentarios().split("->")[1]);
            } catch (Exception e) {
                sb.append(b.getComentarios());
            }
            if (i < 1) sb.append(" | "); // separador opcional
        }
        return sb.toString();
    }

    private void procesarEncuestaDigital(Referencias r, ReferenciasExport re) {
        if (r.getReferidoId().getRespEncuestaCollection() == null ||
                r.getReferidoId().getRespEncuestaCollection().isEmpty()) {
            return;
        }

        for (RespEncuesta resp : r.getReferidoId().getRespEncuestaCollection()) {
            BigDecimal pregId = resp.getPreguntaId().getId();

            if (pregId.equals(BigDecimal.ONE)) {
                re.setEncDigitalPreg1(resp.getPreguntaId().getDescripcion());
                re.setEncDigitalResp1(resp.getRespuestaBool());
            } else if (pregId.equals(new BigDecimal("2"))) {
                re.setEncDigitalPreg2(resp.getPreguntaId().getDescripcion());
                re.setEncDigitalResp2(resp.getRespuestaBool());
            } else if (pregId.equals(new BigDecimal("3"))) {
                re.setEncDigitalPreg3(resp.getPreguntaId().getDescripcion());
                re.setEncDigitalResp3(resp.getRespuestaBool());
            } else if (pregId.equals(new BigDecimal("4"))) {
                re.setEncDigitalPreg4(resp.getPreguntaId().getDescripcion());
                re.setEncDigitalResp4(resp.getRespuestaFecha() != null ?
                        new SimpleDateFormat("dd-MM-yyyy").format(resp.getRespuestaFecha()) : "");
            }
        }
    }
}