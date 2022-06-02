package com.example.batch.utils;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class UnzipUtility {

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

    public static void unzipSource(Path sourceFolder, Path targetFolder) throws IOException {

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(sourceFolder.toFile()))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                Path newUnzipPath = zipSlipVulnerabilityProtect(zipEntry, targetFolder);
                boolean isDirectory = false;
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }
                if (isDirectory) {
                    Files.createDirectories(newUnzipPath);
                } else {
                    if (newUnzipPath.getParent() != null) {
                        if (Files.notExists(newUnzipPath.getParent())) {
                            Files.createDirectories(newUnzipPath.getParent());
                        }
                    }
                    Files.copy(zipInputStream, newUnzipPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
        }
    }
    public static Path zipSlipVulnerabilityProtect(ZipEntry zipEntry, Path targetDir)
            throws IOException {
        Path dirResolved = targetDir.resolve(zipEntry.getName());
        Path normalizePath = dirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Invalid zip: " + zipEntry.getName());
        }

        return normalizePath;
    }

    //    Path sourcePath = Paths.get("src/main/resources/zip/data.zip");
//    Path targetPath = Paths.get("src/main/resources/data");
//        try {
//        unzipSource(sourcePath, targetPath);
//            log.info("Unzipped successfully at location : "+targetPath);
//
//    } catch (IOException e) {
//        e.printStackTrace();
//    }

//    ZipFile zip = null;
//        try {
//            zip = new ZipFile("/src/main/resources/zip/data.zip");
//
//
//        for (Enumeration<?> e = zip.entries(); e.hasMoreElements();) {
//            ZipEntry entry = (ZipEntry) e.nextElement();
//            if (!entry.isDirectory()) {
//                System.out.println(entry.getName());
//            }
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    } finally {
//        zip.close();
//    }}
}

