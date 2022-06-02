package com.example.batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;


import java.util.UUID;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class CustomJobLauncher implements CommandLineRunner {

    private final JobLauncher jobLauncher;

    private final Job job;

    public void scheduleByFixedRate() throws Exception {
        log.info("Batch job starting");
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", UUID.randomUUID().toString())
                .addLong("JobId", System.currentTimeMillis())
                .addLong("time", System.currentTimeMillis()).toJobParameters();

        JobExecution execution = jobLauncher.run(job, jobParameters);
        log.info("JobExecution:.. " + execution.getStatus());
    }

    @Override
    public void run(String... args) throws Exception {
        scheduleByFixedRate();
    }
}
