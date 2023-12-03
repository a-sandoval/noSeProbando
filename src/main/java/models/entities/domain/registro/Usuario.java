package models.entities.domain.registro;


import converters.MedioNotificacionAttributeConverter;
import models.entities.domain.Persistente;
import models.entities.domain.comunidad.Comunidad;
import models.entities.domain.config.Config;
import models.entities.domain.incidentes.Incidente;
import models.entities.domain.notificaciones.Notificador;
import models.entities.domain.notificaciones.medioEnvio.Mail;
import models.entities.domain.notificaciones.medioEnvio.MedioNotificacion;
import models.entities.domain.notificaciones.medioEnvio.WhatsApp;
import models.entities.domain.notificaciones.tiempoDeEnvio.ModoRecepcion;
import models.entities.domain.notificaciones.tiempoDeEnvio.PreferenciaEnvioNotificacion;
import models.entities.domain.roles.Permiso;
import models.entities.domain.roles.Rol;
import models.entities.domain.services.georef.entities.Ubicacion;
import models.entities.domain.servicios.PrestacionDeServicio;
import models.entities.domain.servicios.RolFrenteAPrestacion;
import models.entities.domain.entidades.Entidad;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.mail.MessagingException;
import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="usuario")
public class Usuario extends Persistente {
    @Column(name="nombreDeUsuario")
    private String nombreDeUsuario;
    @Column(name="email")
    private String email;
    @Embedded
    private Contrasenia contrasenia; //hasheada
    @Column(name="telefono")
    private String numeroTelefono;
    @Column(name="bloqueado")
    private Boolean bloqueado;
    @Transient
    private Ubicacion localizacion;
    @Convert(converter = MedioNotificacionAttributeConverter.class)
    @Column(name = "medioNotificacion")
    @Nullable
    private MedioNotificacion medioPreferido; // Email o Wpp
    @Nullable
    @Enumerated(EnumType.STRING)
    private ModoRecepcion modoRecepcion; // Sincronico o asincronico

    @Nullable
    @ElementCollection
    @CollectionTable(name = "usuario_horario_disponible", joinColumns = @JoinColumn(name="usuario_id"))
    private List<LocalTime> horariosDisponibles;

    @Nullable
    @OneToMany(fetch = FetchType.LAZY)
    private List<Rol> roles;

    @Nullable
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Entidad> entidadesDeInteres;


    @Nullable
    @OneToMany(mappedBy="usuario", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<RolFrenteAPrestacion> rolFrenteAPrestaciones;

    @Nullable
    @OneToOne
    private Permiso permisoEspecialDeDesignado;

    @Transient
    private Float gradoDeConfianza;

    public Usuario() {
        horariosDisponibles = new ArrayList<>();
        roles = new ArrayList<>();
        entidadesDeInteres = new ArrayList<>();
        rolFrenteAPrestaciones = new ArrayList<>();
        gradoDeConfianza = Config.GRADO_DE_CONFIANZA_INICIAL;
        this.bloqueado = false;
    }

    public Usuario(String usuario, String email, Contrasenia contrasenia) {
        this.nombreDeUsuario = usuario;
        this.email = email;
        this.contrasenia = contrasenia;

        horariosDisponibles = new ArrayList<>();
        roles = new ArrayList<>();
        entidadesDeInteres = new ArrayList<>();
        rolFrenteAPrestaciones = new ArrayList<>();

    }

    public void configurarHorariosDisponibles(LocalTime... horario) {
        horariosDisponibles.addAll(List.of(horario));
    }



    public void setPreferencias(PreferenciaEnvioNotificacion preferencia) {
        medioPreferido = preferencia.getMedioNotificacion();
        modoRecepcion = preferencia.getModoRecepcion();

    }

    public void setMedioDeNotificacion(String medio){

        switch(medio) {
            case "WhatsApp": this.medioPreferido = new WhatsApp(); break;
            default: this.medioPreferido = new Mail();
        }
    }

    public void setModo(String modo) {

        switch(modo) {
            case "Asincronico": this.modoRecepcion = ModoRecepcion.ASINCRONICA; break;
            case "Sincronico": this.modoRecepcion = ModoRecepcion.SINCRONICA; break;
            default: this.modoRecepcion = ModoRecepcion.SINCRONICA; break;
        }
    }

    public void agregarEntidadesDeInteres(Entidad... entidad) {

        entidadesDeInteres.addAll(List.of(entidad));
    }

    public ArrayList<PrestacionDeServicio> prestacionDeServiciosDeInteres() {

        ArrayList<PrestacionDeServicio> serviciosDeInteres = new ArrayList<>();

        for(Entidad entidad: entidadesDeInteres) {

            serviciosDeInteres.addAll(entidad.serviciosConIncidente());

        }

        return serviciosDeInteres;
    }


    public boolean teInteresa(PrestacionDeServicio servicioAfectado) {

        return this.prestacionDeServiciosDeInteres().contains(servicioAfectado);
    }

    public boolean teInteresa(Incidente incidente) {

        return this.teInteresa(incidente.getServicioAfectado()) || this.estasEn(incidente.getComunidadDondeSeReporta());
    }

    public boolean estasEn(Comunidad comunidad) {

        return roles.stream().anyMatch(rol ->
                (rol.getComunidad() != null && rol.getComunidad().equals(comunidad)));
    }

    public void modificarLocalizacion(Ubicacion nuevaLocalizacion) throws MessagingException {
        this.localizacion = nuevaLocalizacion;
        Notificador notificadorRevisiones = new Notificador();
        notificadorRevisiones.enviarSugerenciasRevisionA(this);
    }

    public void aniadirRol(Rol rol) {

        roles.add(rol);
    }

    public void quitarRol(Rol rol){
        roles.remove(rol);
    }
    public void setContra(String contrasenia) {

        String contraEncriptada = new Encriptador().encriptarContrasenia(contrasenia);

        this.setContrasenia(new Contrasenia(contraEncriptada));
    }

    public void agregarEntidadesDeInteres(List<Entidad> entidades) {

        this.entidadesDeInteres.addAll(entidades);
    }

    public boolean tenesPermiso(String nombreInternoDePermiso) {
        if(this.permisoEspecialDeDesignado!=null) {
            return this.permisoEspecialDeDesignado.coincideConNombreInterno(nombreInternoDePermiso) || this.roles.stream().anyMatch(r->r.tenesPermiso(nombreInternoDePermiso));

        }

        else
            return this.roles.stream().anyMatch(r->r.tenesPermiso(nombreInternoDePermiso));
    }



    public boolean tiene(Rol rol) {
        return this.roles.contains(rol);
    }
}



