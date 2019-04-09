package com.github.yankee42.courseconvert;

import net.npe.dds.DDSReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BackgroundImageExtractor {

    public static void extract(final Path modOrDds, final Path outputImage, final Path outputCal) throws IOException {
        final byte[] dds = readDds(modOrDds);
        ImageIO.write(ddsToBufferedImage(dds), "png", outputImage.toFile());
        try(InputStream calibrationFile = BackgroundImageExtractor.class.getResourceAsStream("/map-calibration.cal");
            OutputStream out = Files.newOutputStream(outputCal)) {
            copy(calibrationFile, out);
        }
    }

    private static BufferedImage ddsToBufferedImage(final byte[] dds) {
        int [] pixels = DDSReader.read(dds, DDSReader.ARGB, 0);
        int width = DDSReader.getWidth(dds);
        int height = DDSReader.getHeight(dds);
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }

    private static byte[] readDds(final Path modOrDds) throws IOException {
        if (modOrDds.getFileName().toString().toLowerCase().endsWith("zip")) {
            try(ZipFile zipFile = new ZipFile(modOrDds.toFile())) {
                final ZipEntry entry = zipFile.getEntry("maps/pda_map_H.dds");
                if (entry == null) {
                    throw new IllegalArgumentException("File <maps/pda_map_H.dds> not found in <" + modOrDds + ">");
                }
                try (InputStream in = zipFile.getInputStream(entry)) {
                    return toBytes(in);
                }
            }
        }
        return Files.readAllBytes(modOrDds);
    }

    private static byte[] toBytes(final InputStream in) throws IOException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        copy(in, result);
        return result.toByteArray();
    }

    private static void copy(final InputStream in, final OutputStream result) throws IOException {
        final byte[] buffer = new byte[4096];
        int bytesRead;
        while((bytesRead = in.read(buffer)) != -1) {
            result.write(buffer, 0, bytesRead);
        }
    }
}
