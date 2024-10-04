package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import ar.edu.utn.dds.k3003.model.Metrica;
import ar.edu.utn.dds.k3003.model.Vianda;
import ar.edu.utn.dds.k3003.repositories.MetricaRepository;
import ar.edu.utn.dds.k3003.repositories.ViandaMapper;
import ar.edu.utn.dds.k3003.repositories.ViandaRepository;
/*import ar.edu.utn.dds.k3003.service.DDMetricsUtils;
import io.micrometer.datadog.DatadogMeterRegistry;*/

import java.util.List;
import java.util.NoSuchElementException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Fachada implements FachadaViandas {
  private final ViandaMapper viandaMapper;
  private final ViandaRepository viandaRepository;
  private final MetricaRepository metricaRepository;
  private FachadaHeladeras fachadaHeladeras;
  private EntityManagerFactory entityManagerFactory;
  private EntityManager entityManager;
  
  /*final DDMetricsUtils metricsUtils = new DDMetricsUtils("vianda");
  final DatadogMeterRegistry registry = metricsUtils.getRegistry();*/

  public Fachada() {
    this.entityManagerFactory = Persistence.createEntityManagerFactory("viandas");
    this.entityManager = entityManagerFactory.createEntityManager();
    this.viandaMapper = new ViandaMapper();
    this.viandaRepository = new ViandaRepository(entityManager);
    this.metricaRepository = new MetricaRepository(entityManager);
  }

  @Override
  public ViandaDTO agregar(ViandaDTO viandaDTO) {
    Vianda vianda =
        new Vianda(viandaDTO.getCodigoQR(),
            viandaDTO.getColaboradorId(),
            viandaDTO.getHeladeraId(),
            EstadoViandaEnum.PREPARADA,
            viandaDTO.getFechaElaboracion());
    vianda = this.viandaRepository.save(vianda);
    return viandaMapper.map(vianda);
  }
  
  public Metrica agregarMetrica(Metrica metrica) {
      metrica = this.metricaRepository.save(metrica);
      return metrica;
  }

  @Override
  public ViandaDTO modificarEstado(String qr, EstadoViandaEnum estadoViandaEnum)
      throws NoSuchElementException {
    Vianda viandaEncontrada = viandaRepository.buscarXQR(qr);
    viandaEncontrada.setEstado(estadoViandaEnum);
    viandaEncontrada = viandaRepository.save(viandaEncontrada);
    return viandaMapper.map(viandaEncontrada);
  }

  @Override
  public List<ViandaDTO> viandasDeColaborador(Long colaboradorId, Integer mes, Integer anio)
      throws NoSuchElementException {
    return viandaRepository.obtenerXColIDAndAnioAndMes(colaboradorId, mes, anio).stream()
        .map(viandaMapper::map)
        .toList();
  }

  @Override
  public ViandaDTO buscarXQR(String qr) throws NoSuchElementException {
    Vianda viandaEncontrada = viandaRepository.buscarXQR(qr);
    return viandaMapper.map(viandaEncontrada);
  }
  
  public Metrica buscarMetricaXQR(String qrMetrica) throws NoSuchElementException {
      Metrica metrica = metricaRepository.findByQR(qrMetrica);

      return metrica;
  }

  @Override
  public void setHeladerasProxy(FachadaHeladeras fachadaHeladeras) {
    this.fachadaHeladeras = fachadaHeladeras;
  }

  @Override
  public boolean evaluarVencimiento(String qr) throws NoSuchElementException {
    Vianda viandaEncontrada = viandaRepository.buscarXQR(qr);
    return fachadaHeladeras.obtenerTemperaturas(viandaEncontrada.getHeladeraId()).stream()
        .anyMatch(temperaturaDTO -> temperaturaDTO.getTemperatura() >= 5);
   
  }

  @Override
  public ViandaDTO modificarHeladera(String qr, int nuevaHeladera) {
    Vianda viandaEncontrada = viandaRepository.buscarXQR(qr);
    viandaEncontrada.setHeladeraId(nuevaHeladera);
    viandaEncontrada = viandaRepository.save(viandaEncontrada);
    return viandaMapper.map(viandaEncontrada);
  }

  public void clearDB(){
    viandaRepository.clearDB();
  }
  
  public void borrarMetricas(){
      this.metricaRepository.borrarMetricas();
  }

}