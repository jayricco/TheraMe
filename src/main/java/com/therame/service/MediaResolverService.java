package com.therame.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Path;

@Service
public class MediaResolverService extends ResourceHttpRequestHandler {

    private static final String DEFAULT_FILE_EXTENSION = ".mp4";

    @Value("${therame.media.root-location}")
    public Path rootLocation;

    @Override
    protected Resource getResource(HttpServletRequest request) {
        String mediaName = (String) request.getAttribute("video-request");
        return new FileSystemResource(rootLocation.resolve(mediaName + DEFAULT_FILE_EXTENSION).toFile());
    }

}
