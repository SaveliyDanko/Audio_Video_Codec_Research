package com.savadanko;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;

import java.nio.file.Path;
import java.nio.file.Paths;

public class VideoQualityMetrics {

    public static void main(String[] args) {
        Path originalVideo = Path.of("path to origin");
        Path compressedVideoVP9 = Path.of("vp-9 output");

        Path compressedVideoX265 = Path.of("h265 output");
        calculatePSNR(originalVideo, compressedVideoVP9, "/absolute/path/to/psnr_vp9_output.txt");
        calculateSSIM(originalVideo, compressedVideoVP9, "/absolute/path/to/ssim_vp9_output.txt");

        calculatePSNR(originalVideo, compressedVideoX265, "/absolute/path/to/psnr_x265_output.txt");
        calculateSSIM(originalVideo, compressedVideoX265, "/absolute/path/to/ssim_x265_output.txt");
    }

    private static void calculatePSNR(Path originalVideo, Path compressedVideo, String outputFileName) {
        Path psnrOutput = Paths.get(outputFileName);

        FFmpeg.atPath()
                .addInput(UrlInput.fromPath(originalVideo))
                .addInput(UrlInput.fromPath(compressedVideo))
                .addArguments("-filter_complex", "[0:v][1:v]psnr")
                .addArguments("-f", "null")
                .addOutput(UrlOutput.toPath(psnrOutput))
                .setOverwriteOutput(true)
                .execute();

        System.out.println("PSNR calculation completed. Results are saved to " + psnrOutput);
    }

    private static void calculateSSIM(Path originalVideo, Path compressedVideo, String outputFileName) {
        Path ssimOutput = Paths.get(outputFileName);

        FFmpeg.atPath()
                .addInput(UrlInput.fromPath(originalVideo))
                .addInput(UrlInput.fromPath(compressedVideo))
                .addArguments("-filter_complex", "[0:v][1:v]ssim")
                .addArguments("-f", "null")
                .addOutput(UrlOutput.toPath(ssimOutput))
                .setOverwriteOutput(true)
                .execute();

        System.out.println("SSIM calculation completed. Results are saved to " + ssimOutput);
    }
}









