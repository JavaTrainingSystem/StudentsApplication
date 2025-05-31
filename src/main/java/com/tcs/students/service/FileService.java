package com.tcs.students.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface FileService {

    String uploadFile(MultipartFile file);

    InputStream downloadFile(String filePath) throws FileNotFoundException;

    void deleteFile(String certFilePath);
}
