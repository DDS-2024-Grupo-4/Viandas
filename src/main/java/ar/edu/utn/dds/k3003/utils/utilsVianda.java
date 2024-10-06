package ar.edu.utn.dds.k3003.utils;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import ar.edu.utn.dds.k3003.repositories.ViandaMapper;
import ar.edu.utn.dds.k3003.model.Vianda;
import java.time.LocalDateTime;
import java.util.List;

public class utilsVianda {

    public static void crearViandas(Fachada fachada){
            ViandaDTO viandaNuevaDTO1 = new ViandaDTO("abc", LocalDateTime.now(),EstadoViandaEnum.PREPARADA,1L,1);
            viandaNuevaDTO1.setId(1L);
            ViandaDTO viandaNuevaDTO2 = new ViandaDTO("fgh", LocalDateTime.now(),EstadoViandaEnum.PREPARADA,2L,2);
            viandaNuevaDTO2.setId(2L);
            ViandaDTO viandaNuevaDTO3 = new ViandaDTO("jkl", LocalDateTime.now(),EstadoViandaEnum.PREPARADA,3L,3);
            viandaNuevaDTO3.setId(3L);

            fachada.agregar(viandaNuevaDTO1);
            fachada.agregar(viandaNuevaDTO2);
            fachada.agregar(viandaNuevaDTO3);
    }
}