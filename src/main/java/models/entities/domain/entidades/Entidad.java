package models.entities.domain.entidades;


import models.entities.domain.Persistente;
import models.entities.domain.services.georef.entities.Ubicacion;
import models.entities.domain.servicios.PrestacionDeServicio;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="entidad")
public class Entidad extends Persistente {
    @Override
    public String toString() {
        return "Entidad nombre=" + nombre ;
    }
    @Column(name="nombre")
    private String nombre;
    @Column(name="cantidad_reportes")
    private Integer cantidadReportes;
    @Column(name = "promedio_de_cierre")
    private Double promedioCierre;
    @OneToMany(mappedBy="entidad", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Establecimiento> establecimientosAsociados;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="prestadora_de_servicio_id", referencedColumnName = "id")
    private PrestadoraDeServicio prestadora;

    public Entidad() {
        establecimientosAsociados = new ArrayList<>();
    }

    public static String[] obtenerEncabezadosCSV(String opcion) {

        switch (opcion) {
            case "INFORME_POR_PROMEDIO_CIERRE":
                return new String[]{"Nombre", "Promedio de Cierre"};
            case "INFORME_POR_CANTIDAD_INCIDENTES":
                return new String[] {"Nombre","Cantidad de Reportes"};
            default:
                throw new RuntimeException("No es valida la opcion de archivo CSV");
        }
    }

    public String[] obtenerDatosCSV(String opcion) {

        switch(opcion) {
            case "INFORME_POR_PROMEDIO_CIERRE":
                return new String[]{"Nombre", String.valueOf(this.promedioCierre)};
            case "INFORME_POR_CANTIDAD_INCIDENTES":
                return new String[] {"Nombre",String.valueOf(this.cantidadReportes)};
            default:
                throw new RuntimeException("No es valida la opcion de archivo CSV");
        }
    }

    public void agregarEstablecimientos(Establecimiento... unEstablecimiento) {

        establecimientosAsociados.addAll(List.of(unEstablecimiento));

        for(Establecimiento establecimiento: unEstablecimiento) {

            establecimiento.setEntidad(this);
        }
    }


    public List<PrestacionDeServicio> serviciosConIncidente() {
        return establecimientosAsociados.stream().flatMap(establecimiento -> establecimiento.obtenerServiciosIncidentados()).toList();
    }

    public ArrayList<Ubicacion> dondeOpera() {
        return establecimientosAsociados.stream().map(establecimiento -> establecimiento.getUbicacionGeografica()).collect(Collectors.toCollection(ArrayList::new));
    }

}




