package com.therame.service;

import com.therame.model.Exercise;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MediaStorageService {

    Exercise store(Exercise exercise, MultipartFile file) throws IOException;

}
