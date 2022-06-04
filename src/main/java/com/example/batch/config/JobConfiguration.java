package com.example.batch.config;

import com.example.batch.model.LineDto;
import com.example.batch.model.Person;
import com.example.batch.model.PersonProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.Comparator;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class JobConfiguration {

  public final JobBuilderFactory jobBuilderFactory;

  public final StepBuilderFactory stepBuilderFactory;

  public final MyCustomWriter myCustomWriter;

  public final PersonProcessor processor;

  @Value("${zip.path}")
  private Resource resource;

  @Bean
  public Step step1(MyCustomWriter myCustomWriter) {
    return stepBuilderFactory
        .get("step1")
        .<LineDto, Person>chunk(10)
        .reader(multiResourceItemReader())
        .processor(processor)
        .writer(myCustomWriter)
        .build();
  }

  @Bean
  public Job job(Step step1) {
    return jobBuilderFactory
        .get("MyJob")
        .incrementer(new RunIdIncrementer())
        .flow(step1)
        .end()
        .build();
  }

  @Bean
  public MultiResourceItemReader<LineDto> multiResourceItemReader() {
    ArchiveResourceItemReader<LineDto> resourceItemReader = new ArchiveResourceItemReader<>();
    resourceItemReader.setResource(resource);
    resourceItemReader.setDelegate(reader());
    resourceItemReader.setComparator(Comparator.comparing(Resource::getDescription));
    return resourceItemReader;
  }

  @Bean
  public FlatFileItemReader<LineDto> reader() {
    FlatFileItemReader<LineDto> reader = new FlatFileItemReader<>();
    reader.setLinesToSkip(1);
    reader.setLineMapper(
        new DefaultLineMapper<>() {
          {
            setLineTokenizer(
                new DelimitedLineTokenizer() {
                  {
                    setNames("firstName", "lastName", "date");
                  }
                });
            setFieldSetMapper(
                new BeanWrapperFieldSetMapper<>() {
                  {
                    setTargetType(LineDto.class);
                  }
                });
          }
        });
    return reader;
  }
}
