package com.example.batch.config;

import com.example.batch.custom.MyCustomReader;
import com.example.batch.custom.MyCustomWriter;
import com.example.batch.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class JobConfiguration {

    public final JobBuilderFactory jobBuilderFactory;

    public final StepBuilderFactory stepBuilderFactory;

    public final MyCustomReader myCustomReader;

    public final MyCustomWriter myCustomWriter;

//    @Bean
//    public ItemReader<Person> itemReader() throws IOException, URISyntaxException {
//        MultiResourceItemReader<Person> reader = new MultiResourceItemReader<>();
//        URL res = getClass().getClassLoader().getResource("zip/data.zip");
//        File file = Paths.get(res.toURI()).toFile();
//        ZipFile zipFile = new ZipFile(file.getAbsolutePath());
//        reader.setComparator(Comparator.comparing(Resource::getDescription));
//        reader.setResources(extractFiles(zipFile));
//        reader.setDelegate(myCustomReader);
//        return reader;
//    }


    @Bean
    public MultiResourceItemReader<Person> multiResourceItemReader(){
        MultiResourceItemReader<Person> multiResourceItemReader = new MultiResourceItemReader<Person>();
        URL res = getClass().getClassLoader().getResource("zip/data.zip");
        try {
            File file = Paths.get(res.toURI()).toFile();
            ZipFile zipFile = null;
            zipFile = new ZipFile(file.getAbsolutePath());
            multiResourceItemReader.setResources(extractFiles(zipFile));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        multiResourceItemReader.setDelegate(myCustomReader);
        return multiResourceItemReader;
    }

  //    @SuppressWarnings({"rawtypes", "unchecked"})
  //    @Bean
  //    public JdbcBatchItemWriter<Person> itemWriter() {
  //        JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();
  //
  //        itemWriter.setDataSource(this.dataSource);
  //        itemWriter.setSql("INSERT INTO Person (first_name, last_name, csv_date)VALUES
  // (:firstName, :lastName, :csvDate)");
  //
  //        itemWriter.setItemSqlParameterSourceProvider(new
  // BeanPropertyItemSqlParameterSourceProvider());
  //        itemWriter.afterPropertiesSet();
  //
  //        return itemWriter;
  //    }
  @Bean
  public Step step1(MyCustomWriter myCustomWriter) {
      return stepBuilderFactory.get("step1")
              .<Person, Person>chunk(10)
              .reader(multiResourceItemReader())
              .writer(myCustomWriter)
              .build();
  }

    @Bean
    public Job job(Step step1) {
        return jobBuilderFactory.get("MyJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1).end().build();
    }

    public static Resource[] extractFiles(final ZipFile currentZipFile) throws IOException {
        List<Resource> extractedResources = new ArrayList<>();
        Enumeration<? extends ZipEntry> zipEntryEnum = currentZipFile.entries();
        while (zipEntryEnum.hasMoreElements()) {
            ZipEntry zipEntry = zipEntryEnum.nextElement();
            if (!zipEntry.isDirectory()) {
                extractedResources.add(
                        new InputStreamResource(
                                currentZipFile.getInputStream(zipEntry),
                                zipEntry.getName()));
            }
        }
        Resource[] retResources = new Resource[extractedResources.size()];
        extractedResources.toArray(retResources);
        return retResources;
    }
}
