package com.therame.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class MediaEncoderService {

    @Async
    public void encodeVideo(String inputFile, String outputFile, boolean cleanUp) throws IOException {
        FFmpeg ffmpeg = new FFmpeg();
        FFprobe ffprobe = new FFprobe();

        FFmpegBuilder ffmpegBuilder = new FFmpegBuilder()
                .setInput(inputFile)
                .overrideOutputFiles(true)
                .addOutput(outputFile)
                .setFormat("mp4")
                .disableSubtitle()
                .setAudioChannels(1) // Mono - unsure if this is the best choice
                .setAudioCodec("aac")
                .setAudioSampleRate(FFmpeg.AUDIO_SAMPLE_48000) // Good enough imo
                .setAudioBitRate(32768)
                .setVideoCodec("libx264")
                .setVideoFrameRate(FFmpeg.FPS_23_976)
                .setVideoResolution(1280, 720) // 720p for now, may want to scale this with input video res in the future
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // Its possible to register a progress listener if we ever want to provide that to the client
        executor.createJob(ffmpegBuilder).run();

        if (cleanUp) {
            // Clean up input file
            Files.deleteIfExists(Paths.get(inputFile));
        }
    }

    public FFmpegProbeResult getVideoDetails(String inputFile) throws IOException {
        return new FFprobe().probe(inputFile);
    }
}
