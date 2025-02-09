import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {

    public static Canvas target;
    public static double targetLoss;
    public static double current;
    public static boolean[][] visited;

    public static void main(String[] args) throws IOException {
        String name = "lisa";
        String filePath = "./src/main/resources/images/" + name + ".png";
        String outputPath = "./src/main/resources/images/" + name + "/";
        int frameLimit = 10;
        int areaLimit = 50;
        int numberOfIterations = 30000;

        File imageFile = new File(filePath);
        if (!imageFile.exists()) {
            System.out.println("Error: File not found! Check the file path.");
            return;
        }

        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            System.out.println("Failed to load the image");
            return;
        }

        GeneticAlgo ga = new GeneticAlgo();
        ga.run(image, outputPath, 50);
    }
}