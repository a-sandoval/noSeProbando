package models.entities.domain.notificaciones.tiempoDeEnvio;

import models.repositories.datos.RepositorioUsuarios;
import models.entities.domain.registro.Usuario;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalTime;
import java.util.List;

public class NotificacionAsincronicaScheduler {

    public static void main(String[] args) throws SchedulerException {

        List<Usuario> usuarios = RepositorioUsuarios.getInstance().usuariosConNotificacionesAsincronicas();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        int i = 0;

        for (Usuario usuario : usuarios) {

            List<LocalTime> horarios = usuario.getHorariosDisponibles();

            for (LocalTime horario : horarios) {

                JobDetail job = JobBuilder.newJob(EnviarNotificacionAsincronica.class)
                        .withIdentity("recepcionJob_" + i + "_" + horario.hashCode(), "group1") // Identificador único para cada tarea
                        .build();

                // Define el disparador (trigger) basado en el horario del usuario
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity("recepcionTrigger_" + i + "_" + horario.hashCode(), "group1") // Identificador único para cada trigger
                        .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(horario.getHour(), horario.getMinute())) // Horario específico del usuario
                        .build();

                // Programa la tarea con el Scheduler
                scheduler.scheduleJob(job, trigger);
            }

            i++;
        }

        scheduler.start();

    }
}
