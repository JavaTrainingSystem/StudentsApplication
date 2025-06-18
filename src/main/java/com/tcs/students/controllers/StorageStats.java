package com.tcs.students.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/storage")
public class StorageStats {

    @Value("${local.file.path}")
    private String localFilePath;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStorageStats() {
        File root = new File(localFilePath);
        File[] folders = root.listFiles(File::isDirectory);

        if (folders == null) return ResponseEntity.ok(Collections.emptyMap());

        List<Map<String, Object>> folderSummary = new ArrayList<>();
        List<Map<String, Object>> allFiles = new ArrayList<>();

        for (File folder : folders) {
            String fullPath = folder.getAbsolutePath();
            String relativePath = fullPath.replaceFirst(Pattern.quote(localFilePath), "").replaceFirst("^/", "");
            long size = FileUtils.sizeOfDirectory(folder);
            int fileCount = countFiles(folder);
            int subfolderCount = countFolders(folder);
            String lastModified = formatDate(folder.lastModified());

            folderSummary.add(Map.of(
                    "encodedPath", Base64.getEncoder().encodeToString(fullPath.getBytes()),
                    "relativePath", relativePath,
                    "size", size / 1024 / 1024,
                    "files", fileCount,
                    "subfolders", subfolderCount,
                    "lastModified", lastModified
            ));

            collectFilesRecursively(folder, allFiles);
        }

        // Top 5 consumers
        List<Map<String, Object>> topConsumers = folderSummary.stream()
                .sorted((a, b) -> Long.compare((long) b.get("size"), (long) a.get("size")))
                .limit(5)
                .collect(Collectors.toList());

        // Top 5 recent uploads
        allFiles.sort((a, b) -> ((String) b.get("modified")).compareTo((String) a.get("modified")));
        List<Map<String, Object>> recentUploads = allFiles.stream().limit(5).collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "basePath", localFilePath,
                "folderSummary", folderSummary,
                "topConsumers", topConsumers,
                "recentUploads", recentUploads
        ));
    }

    @GetMapping("/download-folder")
    public void downloadZip(@RequestParam("encodedPath") String encodedPath, HttpServletResponse response) throws IOException {
        String decodedPath = new String(Base64.getDecoder().decode(encodedPath));
        File folder = new File(decodedPath);

        if (!folder.exists() || !folder.isDirectory()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + folder.getName() + ".zip");

        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            zipDirectory(folder, folder.getName(), zipOut);
        }
    }


    // Helper methods
    private void zipDirectory(File folder, String parent, ZipOutputStream zos) throws IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                zipDirectory(file, parent + "/" + file.getName(), zos);
            } else {
                zos.putNextEntry(new ZipEntry(parent + "/" + file.getName()));
                Files.copy(file.toPath(), zos);
                zos.closeEntry();
            }
        }
    }

    private int countFiles(File folder) {
        File[] files = folder.listFiles();
        if (files == null) return 0;
        int count = 0;
        for (File f : files) {
            if (f.isFile()) count++;
            else count += countFiles(f);
        }
        return count;
    }

    private int countFolders(File folder) {
        File[] files = folder.listFiles();
        if (files == null) return 0;
        int count = 0;
        for (File f : files) {
            if (f.isDirectory()) count += 1 + countFolders(f);
        }
        return count;
    }

    private void collectFilesRecursively(File folder, List<Map<String, Object>> list) {
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isFile()) {
                String fullPath = f.getAbsolutePath();
                String relativePath = fullPath.replaceFirst(Pattern.quote(localFilePath), "").replaceFirst("^/", "");
                list.add(Map.of(
                        "name", f.getName(),
                        "size", f.length() / 1024,
                        "modified", formatDate(f.lastModified()),
                        "encodedPath", Base64.getEncoder().encodeToString(fullPath.getBytes()),
                        "relativePath", relativePath
                ));
            } else {
                collectFilesRecursively(f, list);
            }
        }
    }


    private String formatDate(long timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(new Date(timestamp));
    }


    @GetMapping("/view-folder")
    public ResponseEntity<List<Map<String, Object>>> viewFolder(@RequestParam String encodedPath) {
        String decodedPath = new String(Base64.getDecoder().decode(encodedPath));
        File folder = new File(decodedPath);

        if (!folder.exists() || !folder.isDirectory()) {
            return ResponseEntity.notFound().build();
        }

        File[] files = folder.listFiles();
        if (files == null) return ResponseEntity.ok(Collections.emptyList());

        List<Map<String, Object>> content = new ArrayList<>();
        for (File f : files) {
            content.add(Map.of(
                    "name", f.getName(),
                    "isDirectory", f.isDirectory(),
                    "path", Base64.getEncoder().encodeToString(f.getAbsolutePath().getBytes())
            ));
        }

        return ResponseEntity.ok(content);
    }


}
