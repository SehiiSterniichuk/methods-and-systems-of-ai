package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.ByteArrayOutputStream;

import org.springframework.core.io.ByteArrayResource;

import static ua.kpi.iasa.sd.hopfieldneuralnetwork.service.HopfieldCalculator.flattenPattern;

@Service
public class ImageProcessingService {
    final int DIMENSION = 80;

    public byte[][] processImages(MultipartFile[] images) {
        List<BufferedImage> img = new ArrayList<>(images.length);
        for (MultipartFile image : images) {
            try {
                BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
                img.add(bufferedImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        int min = img.stream().mapToInt(i -> Math.min(i.getWidth(), i.getHeight())).min().orElse(-1);
        if (min > DIMENSION) {
            img = img.stream().map(i -> resizeAndCompressImage(i, DIMENSION, DIMENSION)).toList();
            min = DIMENSION;
        }
        int finalMin = min;
        return img.stream().map(i -> convertToBinaryArray(i, finalMin)).toArray(byte[][]::new);
    }

    public byte[] resizeAndConvertImage(MultipartFile image, int dimension) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(image.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (bufferedImage.getWidth() != DIMENSION || bufferedImage.getHeight() != DIMENSION) {
            bufferedImage = resizeAndCompressImage(bufferedImage, DIMENSION, DIMENSION);
        }
        return convertToBinaryArray(bufferedImage, dimension);
    }

    public Resource convertToBufferedImg(MultipartFile image) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(image.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int min = Math.min(bufferedImage.getWidth(), bufferedImage.getHeight());
        if (min > DIMENSION) {
            bufferedImage = resizeAndCompressImage(bufferedImage, DIMENSION, DIMENSION);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert pattern to image", e);
        }
        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }

    public BufferedImage resizeAndCompressImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private byte[] convertToBinaryArray(BufferedImage image, int dimension) {
        byte[] binaryArray = new byte[dimension * dimension];
        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                int color = image.getRGB(x, y);
                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = color & 0xFF;
                int grayscale = (red + green + blue) / 3; // Simple grayscale conversion
                // Convert grayscale to binary: 1 represents black, 0 represents white
                binaryArray[y * dimension + x] = (byte)((grayscale < 128) ? 1 : 0);
            }
        }
        return binaryArray;
    }

    public Resource convertPatternToImageBytes(Pattern pattern, int dimension) {
        BufferedImage image = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();
        byte[] flattenedPattern = flattenPattern(pattern.p());
        int[] pixelData = new int[flattenedPattern.length];
        for (int i = 0; i < flattenedPattern.length; i++) {
            int pixelValue = (flattenedPattern[i] == 1) ? 0 : 255; // 1 to black, 0 to white
            pixelData[i] = (pixelValue << 16) | (pixelValue << 8) | pixelValue;
        }
        raster.setDataElements(0, 0, dimension, dimension, pixelData);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert pattern to image", e);
        }
        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }
}

