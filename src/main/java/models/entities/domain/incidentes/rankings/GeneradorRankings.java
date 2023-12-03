package models.entities.domain.incidentes.rankings;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class GeneradorRankings {
    public static void main(String[] args) throws SchedulerException {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            JobDetail jobDetail = JobBuilder.newJob(ArchivoDeRankings.class)
                    .withIdentity("Generar Ranking de Entidades", "Rankings")
                    .build();


            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("Fin de periodo semanal", "Trigger")
                    .withSchedule(CronScheduleBuilder.weeklyOnDayAndHourAndMinute(DateBuilder.SUNDAY, 23, 59))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);

            scheduler.start();

    }
}
