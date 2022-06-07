package com.example.batch;

import com.example.batch.model.LineDto;
import com.example.batch.model.Person;
import com.example.batch.model.PersonProcessor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@EnableBatchProcessing
@SpringBootTest
public class BatchApplicationTests {

  @InjectMocks
  FlatFileItemReader<LineDto> itemReader;
  @InjectMocks
  PersonProcessor itemProcessor;


  @Test
  public void test() throws UnexpectedInputException, ParseException, Exception {
    itemReader.open(new ExecutionContext());
    LineDto person = itemReader.read();
    System.out.println(person);
    Person tranformedPerson = itemProcessor.process(person);
    System.out.println(tranformedPerson);
    assertThat(person.getDate()).isEqualTo(LocalDate.of(2021,1,26));
  }


  @Bean
  public JobLauncherTestUtils getJobLauncherTestUtils(){
    return new JobLauncherTestUtils();
  }
}
