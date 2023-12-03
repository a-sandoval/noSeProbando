package models.entities.domain.comunidad;

import models.entities.domain.Persistente;
import models.entities.domain.entidades.Establecimiento;
import models.entities.domain.incidentes.Incidente;
import models.entities.domain.registro.Usuario;
import models.entities.domain.services.gradoDeConfianza.GradoDeConfianzaAPI;
import models.entities.domain.services.gradoDeConfianza.entities.GradoDeConfianzaComunidad;
import models.entities.domain.services.gradoDeConfianza.entities.requests.ComunidadRequest;
import models.entities.domain.servicios.PrestacionDeServicio;
import lombok.Getter;
import lombok.Setter;
import models.repositories.datos.RepositorioIncidentes;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="comunidad")
public class Comunidad extends Persistente {

    @Column(name="nombre")
    private String nombre;
    @Column(name="descripcion")
    private String descripcion;
    @Nullable
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Usuario> miembros;

    @Nullable
    @ManyToMany(fetch = FetchType.LAZY)
    private List<PrestacionDeServicio> serviciosDeInteres;
    @Nullable
    @OneToMany(mappedBy = "comunidadDondeSeReporta", cascade = { CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Incidente> incidentesReportados;


    public Comunidad() {
       miembros = new ArrayList<>();
       serviciosDeInteres = new ArrayList<>();
       incidentesReportados = new ArrayList<>();

    }

    public void agregarServiciosDeInteres(PrestacionDeServicio ... servicios) { this.serviciosDeInteres.addAll(List.of(servicios));}

    public void darDeBaja(PrestacionDeServicio servicio){
        serviciosDeInteres.remove(servicio);
    }

    public void seInformoUnIncidente(Incidente unIncidente){
        if(unIncidente!=null) incidentesReportados.add(unIncidente);
    }

    public void agregarUsuarios(Usuario ... usuario) {  this.miembros.addAll(List.of(usuario));
    }

    public List<Establecimiento> getEstablecimientosObservados() {

        List<Establecimiento> establecimientosObservados = new ArrayList<>();

        for(PrestacionDeServicio servicio:serviciosDeInteres) {

            establecimientosObservados.add(servicio.getEstablecimiento());
        }

        return establecimientosObservados;
    }

    public Float gradoDeConfianzaActual() throws IOException {

       GradoDeConfianzaComunidad gradoDeConfianzaComunidad= new GradoDeConfianzaAPI().obtenerGradoDeConfianzaPara(new ComunidadRequest(this, new RepositorioIncidentes().filtrarUltimaSemana()));

       return gradoDeConfianzaComunidad.nuevoPuntaje;

    }


}
