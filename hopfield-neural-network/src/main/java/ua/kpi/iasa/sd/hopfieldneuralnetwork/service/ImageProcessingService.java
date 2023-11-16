package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageProcessingService {

    public int[][] processImages(MultipartFile[] images) {
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
        return img.stream().map(i -> convertToBinaryArray(i, min)).toArray(int[][]::new);
    }

    private int[] convertToBinaryArray(BufferedImage image, int dimension) {
        int[] binaryArray = new int[dimension * dimension];
        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                int color = image.getRGB(x, y);
                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = color & 0xFF;
                int grayscale = (red + green + blue) / 3; // Simple grayscale conversion
                // Convert grayscale to binary: 1 represents black, 0 represents white
                binaryArray[y * dimension + x] = (grayscale < 128) ? 1 : 0;
            }
        }
        return binaryArray;
    }
}

