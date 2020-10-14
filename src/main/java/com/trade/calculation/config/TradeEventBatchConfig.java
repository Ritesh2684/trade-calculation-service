package com.trade.calculation.config;

import java.time.LocalDate;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
@EnableScheduling
public class TradeEventBatchConfig {
	
	@Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobLocator jobLocator;
	
	 @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
	    public void performTBDEventBatch(){

	       JobParameters params = new JobParametersBuilder()
	               .addLong("time", System.currentTimeMillis()).toJobParameters();

	        Job job = null;
	        try {
	           
	                job = jobLocator.getJob("Trade-Calculation-Job");
	                jobLauncher.run(job, params);
	            
	        } catch (JobExecutionException e) {
	            log.error("EventJob execution exception"+e.getLocalizedMessage());
	        }
	    }

}
