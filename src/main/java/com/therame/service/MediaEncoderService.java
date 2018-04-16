package com.therame.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.options.VideoEncodingOptions;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Service(value="MediaEncoderService")
public class MediaEncoderService {

    @Async
    public void encodeVideo(String inputFile, String outputFile, long runtime,
                            boolean cleanUp) throws IOException {
        FFmpeg ffmpeg = new FFmpeg();
        FFprobe ffprobe = new FFprobe();
        FFmpegOutputBuilder output1 = new FFmpegOutputBuilder()
                .setFilename(outputFile.concat(".mp4"))
                .setFormat("mp4")
                .disableSubtitle()
                .disableAudio()
                .setVideoCodec("libx264")
                .setVideoMovFlags("faststart")
                .setVideoFrameRate(FFmpeg.FPS_29_97)
                .setVideoResolution(1280, 720)
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .addExtraArgs("-profile:v", "baseline")
                .addExtraArgs("-level", "4.0");

        FFmpegBuilder ffmpegBuilder = new FFmpegBuilder()
                .setInput(inputFile)
                .overrideOutputFiles(true)
                .addOutput(output1);

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // Its possible to register a progress listener if we ever want to provide that to the client
        executor.createJob(ffmpegBuilder).run();

        if (cleanUp) {
            // Clean up input file
            Files.deleteIfExists(Paths.get(inputFile));
        }


        // Generate a thumbnail
        FFmpegBuilder ssBuilder = new FFmpegBuilder()
                .addExtraArgs("-ss", Objects.toString(runtime / 2)) // Keep this before input, its much faster
                .addInput(outputFile.concat(".mp4"))
                .overrideOutputFiles(true)
                .addOutput(outputFile.concat(".jpg"))
                .addExtraArgs("-vframes", "1")
                .addExtraArgs("-q:v", "2") // JPEG quality, lower is better quality
                .addExtraArgs("-s", "640x360") // 1/2 720p
                .done();

        executor.createJob(ssBuilder).run();
    }

    public FFmpegProbeResult getVideoDetails(String inputFile) throws IOException {
        return new FFprobe().probe(inputFile);
    }
}
