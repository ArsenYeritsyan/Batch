package com.example.batch.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@ContextConfiguration(classes = JobConfiguration.class)
class JobConfigurationTest {

    private final JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();


    @Test
    public void testMyJob() throws Exception {

        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addString("date", UUID.randomUUID().toString())
                        .addLong("JobId", System.currentTimeMillis())
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();

        JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }

    @Test
    public void should_process_input_csv_successfully() throws Exception {
        var jobExecution = jobLauncherTestUtils.launchJob(buildJobParameters());
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    }

    @Test
    public void testInjections() {
        assertThat(jobLauncherTestUtils).isNotNull();
    }

    @Test
    public void resourceLoopJob() throws Exception {
        assertThat(jobLauncherTestUtils.launchJob().getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    private JobParameters buildJobParameters() {
        return buildJobParameters("classpath:zip/data.zip");
    }

    private JobParameters buildJobParameters(String filePath) {
        return new JobParametersBuilder().addString("pathToFile", filePath)
                .addLong("currentTimeInMillis", System.currentTimeMillis()).toJobParameters();
    }
}