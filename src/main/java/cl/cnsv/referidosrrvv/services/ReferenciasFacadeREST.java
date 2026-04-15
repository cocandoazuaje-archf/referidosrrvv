/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.services;

import cl.cnsv.referidosrrvv.clases.ActualizarDatosReferencias;
import cl.cnsv.referidosrrvv.clases.EntidadDeCargaJs;
import cl.cnsv.referidosrrvv.clases.ReferenciasExport;
import cl.cnsv.referidosrrvv.clases.TotalesPanelEjecutivo;
import cl.cnsv.referidosrrvv.models.Referencias;
import java.math.BigDecimal;
import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.UserTransaction;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author cow
 */
@Stateless
@Path("cl.cnsv.referidosrrvv.models.referencias")
public class ReferenciasFacadeREST extends AbstractFacade<Referencias> {

  @PersistenceContext(unitName = "com.cox_referidos_war_1.0PU")
  private transient EntityManager em;

  private UserTransaction utx;

  @Context
  private HttpServletRequest req;

  public ReferenciasFacadeREST() {
    super(Referencias.class);
  }

  @POST
  @Override
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void create(Referencias entity) {
    super.create(entity);
  }

  @POST
  @Path("/derivarmasivolist")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void derivarMasivoList(List<EntidadDeCargaJs> entity)
      throws Exception {
    super.derivarMasivoList(entity, getUtx(), getEntityManager());
  }

  @PUT
  @Path("{id}")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void edit(@PathParam("id") BigDecimal id, Referencias entity)
      throws Exception {
    super.edit(entity);
  }

  @DELETE
  @Path("{id}")
  public void remove(@PathParam("id") BigDecimal id) {
    super.remove(super.find(id));
  }

  @GET
  @Path("{id}")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Referencias find(@PathParam("id") BigDecimal id) {
    Referencias result = super.find(id);
    return result;
  }

  @GET
  @Path("/porreferido/{id}")
  @Override
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Referencias> findByReferido(@PathParam("id") int id) {
    return super.findByReferido(id);
  }

  @GET
  @Path("/exportarreferidos/{id}")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Referencias> exportarReferidos(@PathParam("id") String id)
      throws Exception {
    return super.exportarReferidos(getUtx(), getEntityManager(), id);
  }

  @POST
  @Path("/actualizardatosreferencias")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<ActualizarDatosReferencias.ErrorActualizaDatosReferenciasOut> actualizarDatosReferencias(
      List<EntidadDeCargaJs> entity) throws Exception {
    return super.actualizarDatosReferencias(
        entity,
        getUtx(),
        getEntityManager());
  }

  @GET
  @Path("/reagendada/{id}/{tipo}")
  @Override
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Referencias> findByReagendada(
      @PathParam("id") String id,
      @PathParam("tipo") String tipo) {
    return super.findByReagendada(id, tipo);
  }

  @GET
  @Path("/reagendada/full")
  @Override
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Referencias> findByReagendadaFull() {
      List<Referencias> result = super.findByReagendadaFull();
      return result;
  }

  @GET
  @Path("/paneltotales/{sup}/{anio}/{mes}/{suc}")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public TotalesPanelEjecutivo getTotalesPanel(
      @PathParam("sup") String sup,
      @PathParam("anio") String anio,
      @PathParam("mes") String mes,
      @PathParam("suc") String suc) {
    return super.getTotalesPanel(suc, sup, anio, mes);
  }

  @GET
  @Path("/nocerrado/{id}/{tipo}")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Override
  public List<Referencias> findAllNotCerrado(
      @PathParam("id") String id,
      @PathParam("tipo") String tipo) {
    return super.findAllNotCerrado(id, tipo);
  }
  
  @GET
  @Path("/nocerrado/{tipo}")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Override
  public List<Referencias> findAllNotCerrado2(
      @PathParam("tipo") String tipo) {
    return super.findAllNotCerrado2(tipo);
  }

  @GET
  @Path("/canales")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Referencias> getCanales() {
    return super.getCanales(getEntityManager());
  }

  @GET
  @Path("/nocerrado/fullajax")
  @Produces({ MediaType.APPLICATION_JSON })
  public Response findAllNotCerradoFullAjax() throws Exception {
    String result = super.findAllNotCerradoFullAjax3(req, getEntityManager());
    return Response.ok(result).build();
  }

  @GET
  @Path("{from}/{to}")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Referencias> findRange(
      @PathParam("from") Integer from,
      @PathParam("to") Integer to) {
    return super.findRange(new int[] { from, to });
  }

  @GET
  @Path("/derivarmasivo/{usr}/{id}/{eje}/{sup}/{anio}/{mes}/{suc}")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void derivarMasivo(
      @PathParam("usr") String usr,
      @PathParam("id") String id,
      @PathParam("eje") String eje,
      @PathParam("sup") String sup,
      @PathParam("anio") String anio,
      @PathParam("mes") String mes,
      @PathParam("suc") String suc) throws Exception {
    super.derivarMasivo(
        suc,
        anio,
        mes,
        sup,
        usr,
        id,
        eje,
        getUtx(),
        getEntityManager());
  }

  @GET
  @Path("/exportardatatable/{id}/{sup}/{anio}/{mes}/{suc}")
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<ReferenciasExport> exportarDataTable(
      @PathParam("id") String id,
      @PathParam("sup") String sup,
      @PathParam("anio") String anio,
      @PathParam("mes") String mes,
      @PathParam("suc") String suc) throws Exception {
    return super.exportarDataTable(
        suc,
        anio,
        mes,
        sup,
        id,
        getUtx(),
        getEntityManager());
  }

  @GET
  @Path("count")
  @Produces(MediaType.TEXT_PLAIN)
  public String countREST() {
    return String.valueOf(super.count());
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  protected UserTransaction getUtx() {
    return utx;
  }
}
