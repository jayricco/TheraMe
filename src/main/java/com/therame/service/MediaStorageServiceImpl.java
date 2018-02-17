package com.therame.service;

import com.therame.exception.EmptyMediaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class MediaStorageServiceImpl implements MediaStorageService {

    @Value("${therame.media.root-location}")
    private Path rootLocation;

    @Override
    public void store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (!file.isEmpty()) {
            Files.copy(file.getInputStream(), rootLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } else {
            throw new EmptyMediaException("Video file cannot be empty.");
        }
    }
}
