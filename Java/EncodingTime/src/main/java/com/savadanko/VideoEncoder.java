package com.savadanko;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.FFmpegResult;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VideoEncoder {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Please provide the path to the input video file.");
            System.exit(1);
        }

        String inputFilePath = args[0];
        File inputFile = new File(inputFilePath);

        if (!inputFile.exists()) {
            System.err.println("Input file does not exist.");
            System.exit(1);
        }

        String[] codecs = {"libvpx-vp9", "libx265"};
        for (String codec : codecs) {
            encodeVideo(inputFile.toPath(), codec);
        }
    }

    public static void encodeVideo(Path inputFilePath, String codec) {
        Path outputFilePath = Paths.get("output_" + codec + ".mp4");

        long startTime = System.currentTimeMillis();

        FFmpeg ffmpeg = FFmpeg.atPath();

        UrlOutput output = UrlOutput.toPath(outputFilePath)
                .addArguments("-c:v", codec);

        String[] codecArgs = getCodecSpecificArgs(codec);
        for (int i = 0; i < codecArgs.length; i += 2) {
            output.addArguments(codecArgs[i], codecArgs[i + 1]);
        }

        FFmpegResult result = ffmpeg
                .addInput(UrlInput.fromPath(inputFilePath))
                .addOutput(output)
                .setOverwriteOutput(true)
                .execute();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.err.printf("Encoding with %s completed in %d ms\n", codec, duration);
    }

    private static String[] getCodecSpecificArgs(String codec) {
        return switch (codec) {
            case "libvpx-vp9" -> new String[]{"-b:v", "2M"};
            case "libx265" -> new String[]{"-preset", "slow", "-crf", "28"};
            default -> new String[]{};
        };
    }
}


