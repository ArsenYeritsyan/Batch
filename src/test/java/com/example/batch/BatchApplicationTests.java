package com.example.batch;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootTest
public class BatchApplicationTests {

  @Bean
  public JobLauncherTestUtils getJobLauncherTestUtils(){
    return new JobLauncherTestUtils();
  }

  @Test
  void contextLoads() {}
}
