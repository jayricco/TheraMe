package com.therame.service;

import com.therame.exception.EmptyMediaException;
import com.therame.exception.InvalidMediaException;
import com.therame.model.Exercise;
import com.therame.util.Base64Converter;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service(value = "MediaStorageService")
public class MediaStorageServiceImpl implements MediaStorageService {

    @Value("${therame.media.root-media-location}")
    private Path rootMediaLocation;

    @Value("${therame.media.root-upload-location}")
    private Path rootUploadLocation;

    private ExerciseService exerciseService;
    private MediaEncoderService encoderService;

    public MediaStorageServiceImpl(ExerciseService exerciseService, MediaEncoderService encoderService) {
        this.exerciseService = exerciseService;
        this.encoderService = encoderService;
    }

    @Override
    @Transactional
    public Exercise store(Exercise exercise, MultipartFile file) throws IOException {
        Files.createDirectories(rootMediaLocation);
        Files.createDirectories(rootUploadLocation);

        // Initialize with fake time, will be updated later
        exercise.setRunTime("0:00");
        Exercise createdExercise = exerciseService.createExercise(exercise);

        // File name will be base64 encoded exercise UUID
        String fileName = Base64Converter.toUrlSafeString(createdExercise.getId());

        // Attempt to store the file temporarily for conversion
        if (!file.isEmpty()) {
            Files.copy(file.getInputStream(), rootUploadLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } else {
            throw new EmptyMediaException("Video file cannot be empty.");
        }

        try {
            // Update original record with correct runtime
            FFmpegProbeResult videoInfo = encoderService.getVideoDetails(rootUploadLocation.resolve(fileName).toAbsolutePath().toString());

            long durationMillis = ((long) Math.ceil(videoInfo.getFormat().duration)) * 1000L;
            String runtime = DurationFormatUtils.formatDuration(durationMillis, "mm:ss");
            createdExercise.setRunTime(runtime);

            createdExercise = exerciseService.createExercise(exercise);

            // Re-encode video to common mp4 format
            encoderService.encodeVideo(videoInfo.getFormat().filename, rootMediaLocation.resolve(fileName).toString(),
                    (long) Math.ceil(videoInfo.getFormat().duration), true);
        } catch(IOException e) {

            // Clean up the uploaded file
            Files.delete(rootUploadLocation.resolve(fileName));

            // This is usually the issue and its a bit hard to actually verify that it is... so this is 'good enough'
            throw new InvalidMediaException("Invalid video format.");
        }

        return createdExercise;
    }
}
