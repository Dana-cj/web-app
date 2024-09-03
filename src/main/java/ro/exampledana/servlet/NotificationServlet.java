package ro.exampledana.servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

    @WebListener
    public class NotificationServlet implements ServletContextListener {
        private ScheduledExecutorService scheduler;

        @Override
        public void contextInitialized(ServletContextEvent event) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
//            scheduler.scheduleAtFixedRate(new SomeDailyJob(), 0, 1, TimeUnit.DAYS);
//            scheduler.scheduleAtFixedRate(new SomeHourlyJob(), 0, 1, TimeUnit.HOURS);
//            scheduler.scheduleAtFixedRate(new SomeQuarterlyJob(), 0, 15, TimeUnit.MINUTES);
            scheduler.scheduleAtFixedRate(new SomeFiveSecondelyJob(), 0, 5, TimeUnit.SECONDS);
        }

        @Override
        public void contextDestroyed(ServletContextEvent event) {
            scheduler.shutdownNow();
        }

}
 class SomeFiveSecondelyJob implements Runnable {
    @Override
    public void run() {
        // Do your quarterly job here.
    }

}
