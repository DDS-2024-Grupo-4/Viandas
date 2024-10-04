package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.model.Metrica;
import ar.edu.utn.dds.k3003.repositories.MetricaRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.NoSuchElementException;

public class MetricaController {

    private Fachada fachada;
    public MetricaController(Fachada fachada) {
        this.fachada = fachada;
    }

    public void agregar(Context context) {
        Metrica metrica = context.bodyAsClass(Metrica.class);
        this.fachada.agregarMetrica(metrica);
    }

    public void obtenerMetrica(Context context) {
        var qr = context.queryParamAsClass("qr", String.class).get();
        try {
            var metrica = this.fachada.buscarMetricaXQR(qr);
            context.json(metrica);
        } catch (NoSuchElementException ex) {
            context.result(ex.getLocalizedMessage());
            context.status(HttpStatus.NOT_FOUND);
        }
    }


    public void deleteAllMetricas(Context context) {
        try {
            this.fachada.borrarMetricas();
            context.result("Metricas eliminados con éxito.");
            context.status(HttpStatus.OK);
        } catch (Exception e) {
            context.result("Error al eliminar las metricas: " + e.getMessage());
            context.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}