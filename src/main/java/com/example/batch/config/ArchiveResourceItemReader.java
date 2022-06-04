package com.example.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.zip.ZipFile;

@Slf4j
@Component
public class ArchiveResourceItemReader<T> extends MultiResourceItemReader<T> {

  private ZipFile zipFile;
  private Resource resource;

  @Override
  public void open(@NonNull ExecutionContext context) {
    if (resource != null) {
      try {
        zipFile = new ZipFile(resource.getFile());
        Resource[] resources = extractFiles(zipFile);
        this.setResources(resources);
      } catch (IOException ex) {
        log.error(
                "Error reading archive file {}: Error message: {}",
                resource.getFilename(),
                ex.getMessage());
      }
    } else {
      log.warn("Cannot open the resource");
      return;
    }
    super.open(context);
  }

  public void setResource(Resource resource) {
    this.resource = resource;
  }

  private Resource[] extractFiles(final ZipFile zipFile) {
    return zipFile.stream()
            .filter(zipEntry -> !zipEntry.isDirectory())
            .map(
                    zipEntry -> {
                      try {
                        return new InputStreamResource(
                                zipFile.getInputStream(zipEntry), zipEntry.getName());
                      } catch (IOException e) {
                        log.error(
                                "Error reading archive file {}: Error message: {}",
                                e.getMessage(),
                                zipEntry.getName());
                        return null;
                      }
                    })
            .toArray(Resource[]::new);
  }

  @Override
  public void close() throws ItemStreamException {
    try {
      if (zipFile != null) {
        zipFile.close();
      }
    } catch (IOException ex) {
      log.info("Error during closing the archive file: {}", ex.getMessage());
      throw new ItemStreamException(ex);
    } finally {
      if (zipFile != null) {
        try {
          zipFile.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }


}
