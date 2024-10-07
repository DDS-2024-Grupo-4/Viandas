package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import ar.edu.utn.dds.k3003.model.Vianda;
import ar.edu.utn.dds.k3003.utils.utilsVianda;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.util.NoSuchElementException;

public class ViandaController {

  private final Fachada fachada;

  public ViandaController(Fachada fachada) {
    this.fachada = fachada;
  }

  public void agregar(Context context) {
    var viandaDTO = context.bodyAsClass(ViandaDTO.class);
    var viandaDTORta = this.fachada.agregar(viandaDTO);
    context.json(viandaDTORta);
    context.status(HttpStatus.CREATED);
  }
  
  public void agregarGenericas(Context context){
      System.out.println(this.fachada);
      try {
          utilsVianda.crearViandas1(this.fachada);
          utilsVianda.crearViandas2(this.fachada);
          utilsVianda.crearViandas3(this.fachada);
          context.status(HttpStatus.CREATED).result("Viandas genericas creadas");
      } catch (Exception e) {
          context.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Error de Servidor: " + e.getMessage());
      }
  }

  public void obtenerXColIDAndAnioAndMes(Context context) {
    var colaboradorId = context.queryParamAsClass("colaboradorId", Long.class).get();
    var anio = context.queryParamAsClass("anio", Integer.class).get();
    var mes = context.queryParamAsClass("mes", Integer.class).get();
    try {
      var viandaDTOS = this.fachada.viandasDeColaborador(colaboradorId, mes, anio);
      context.json(viandaDTOS);
    } catch (NoSuchElementException ex) {
      context.result(ex.getLocalizedMessage());
      context.status(HttpStatus.NOT_FOUND);
    }
  }

  public void obtenerXQR(Context context) {
    var qr = context.pathParamAsClass("qr", String.class).get();

    try {
      var viandaDTO = this.fachada.buscarXQR(qr);
      context.json(viandaDTO);
    } catch (NoSuchElementException ex) {
      context.result(ex.getLocalizedMessage());
      context.status(HttpStatus.NOT_FOUND);
    }
  }

  public void evaluarVencimiento(Context context) {
    var qr = context.pathParamAsClass("qr", String.class).get();

    try {
      var viandaDTO = this.fachada.evaluarVencimiento(qr);
      context.json(viandaDTO);
    } catch (NoSuchElementException ex) {
      context.result(ex.getLocalizedMessage());
      context.status(HttpStatus.NOT_FOUND);
    }
  }

  public void modificarHeladeraXQR(Context context) {
    var qr = context.pathParamAsClass("qrVianda", String.class)
        .get();
    var heladeraId = context.queryParamAsClass("heladeraId",Integer.class).get();

    var viandaDTO = this.fachada.modificarHeladera(qr, heladeraId);
    context.json(viandaDTO);
  }

  public void limpiarDB(Context context){
      fachada.clearDB();
      context.status(HttpStatus.NO_CONTENT);
  }
}
