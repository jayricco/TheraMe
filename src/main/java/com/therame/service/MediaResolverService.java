package com.therame.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;

@Service(value = "MediaResolverService")
public class MediaResolverService extends ResourceHttpRequestHandler {

    private static final String VIDEO_EXTENSION = ".mp4";
    private static final String IMAGE_EXTENSION = ".jpg";

    @Value("${therame.media.root-media-location}")
    public Path rootLocation;

    @Override
    protected Resource getResource(HttpServletRequest request) {
        String mediaId = (String) request.getAttribute("resource-name");
        String mediaType = (String) request.getAttribute("resource-type");

        String extension = mediaType.equals("image") ? IMAGE_EXTENSION : VIDEO_EXTENSION;
        return new FileSystemResource(rootLocation.resolve(mediaId + extension).toFile());
    }
}
