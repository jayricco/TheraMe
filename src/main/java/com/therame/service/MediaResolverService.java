package com.therame.service;

import jdk.internal.util.xml.impl.Input;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;

@Service(value = "MediaResolverService")
public class MediaResolverService extends ResourceHttpRequestHandler {

    private static final String VIDEO_EXTENSION = ".mp4";
    private static final String IMAGE_EXTENSION = ".jpg";

    @Value("${therame.media.root-media-location}")
    public Path rootLocation;

    @Override
    protected FileSystemResource getResource(HttpServletRequest request) {
        String mediaId = (String) request.getAttribute("resource-name");
        String mediaType = (String) request.getAttribute("resource-type");


        String extension = mediaType.equals("image") ? IMAGE_EXTENSION : VIDEO_EXTENSION;
        FileSystemResource fsr = new FileSystemResource(rootLocation.resolve(mediaId + extension).toFile());

        return fsr;
    }
}
