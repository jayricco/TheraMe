package com.therame.service;

import com.therame.model.Exercise;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface MediaStorageService {

    Exercise store(Exercise exercise, MultipartFile file) throws IOException;
    Exercise store(Exercise exercise, File videoFile) throws IOException;
}
