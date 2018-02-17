package com.therame.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MediaStorageService {

    void store(MultipartFile file) throws IOException;

}
