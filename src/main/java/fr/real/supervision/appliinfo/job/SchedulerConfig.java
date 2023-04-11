package fr.real.supervision.appliinfo.job;

import fr.real.supervision.appliinfo.job.maintenance.MaintenanceJob;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import fr.real.supervision.appliinfo.job.alert.AlertJob;
import fr.real.supervision.appliinfo.job.astreinte.AstreinteJob;
import fr.real.supervision.appliinfo.job.mail.EmailJob;
import fr.real.supervision.appliinfo.job.purge.PurgeJob;
import fr.real.supervision.appliinfo.job.sms.SmsJob;

@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {

	private static final int POOL_SIZE = 10;

	@Bean
	@ConditionalOnProperty(value = "jobs.alert.enabled", matchIfMissing = true, havingValue = "true")
	public AlertJob alertJob() {
		return new AlertJob();
	}

	@Bean
	@ConditionalOnProperty(value = "jobs.email.enabled", matchIfMissing = true, havingValue = "true")
	public EmailJob emailJob() {
		return new EmailJob();
	}

	@Bean
	@ConditionalOnProperty(value = "jobs.sms.enabled", matchIfMissing = true, havingValue = "true")
	public SmsJob smsJob() {
		return new SmsJob();
	}
	
	@Bean
	@ConditionalOnProperty(value = "jobs.astreinte.enabled", matchIfMissing = true, havingValue = "true")
	public AstreinteJob astreinteJob() {
		return new AstreinteJob();
	}
	
	@Bean
	@ConditionalOnProperty(value = "jobs.purge.enabled", matchIfMissing = true, havingValue = "true")
	public PurgeJob purgeJob() {
		return new PurgeJob();
	}

	@Bean
	@ConditionalOnProperty(value = "jobs.maintenance.alertActivationStatus.update.enabled" , matchIfMissing = true, havingValue = "true")
	public MaintenanceJob maintenanceJob(){ return new MaintenanceJob(); }
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

		threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
		threadPoolTaskScheduler.setThreadNamePrefix("appliinfo-task-pool-");
		threadPoolTaskScheduler.initialize();

		scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
	}
}