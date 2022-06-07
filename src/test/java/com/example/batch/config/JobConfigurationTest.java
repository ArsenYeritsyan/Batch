package com.example.batch.config;

import com.example.batch.BatchApplicationTests;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(JobConfiguration.class)
@ContextConfiguration(classes = {BatchApplicationTests.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, StepScopeTestExecutionListener.class})
class JobConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void testInjections() {
        assertThat(jobLauncherTestUtils).isNotNull();
    }

    @Test
    public void resourceLoopJob() throws Exception {
        assertThat(jobLauncherTestUtils.launchJob().getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

}