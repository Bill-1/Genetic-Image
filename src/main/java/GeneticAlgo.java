import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.util.Pair;

public class GeneticAlgo {
    public int height;
    public int width;
    public int[][][] rgbTarget;

    public void run(BufferedImage image, String outputPath, int population) {
        Random rand = new Random();
        this.height = image.getHeight();
        this.width = image.getWidth();
        System.out.println(height);
        System.out.println(width);
        this.rgbTarget = new int[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = image.getRGB(j, i);
                rgbTarget[i][j][0] = (pixel >> 16) & 0xff; // Red
                rgbTarget[i][j][1] = (pixel >> 8) & 0xff;  // Green
                rgbTarget[i][j][2] = pixel & 0xff; // Blue
            }
        }
        Main.target =  new Canvas(height, width, rgbTarget);
        Main.visited = new boolean[Main.target.height][Main.target.width];
        Individual[] individuals = new Individual[population];
        for (int i = 0; i < population; i++) {
            individuals[i] = new Individual(height, width);
        }
        int itr = 0;
        Pair<Double, Individual> bestParent1 = null;
        Pair<Double, Individual> bestParent2 = null;
        while(true) {
            System.out.println("Process begins...");
            for (Individual individual : individuals) {
                Canvas canvas = new Canvas(height, width);
                canvas.render(individual);
                individual.mutate(0.5, canvas);
                if (bestParent1 == null) {
                    bestParent1 = new Pair<>(canvas.currentLoss, individual);
                } else if (bestParent2 == null) {
                    if (canvas.currentLoss < bestParent1.getKey()) {
                        bestParent2 = bestParent1;
                        bestParent1 = new Pair<>(canvas.currentLoss, individual);
                    } else {
                        bestParent2 = new Pair<>(canvas.currentLoss, individual);
                    }
                } else {
                    if (canvas.currentLoss < bestParent1.getKey()) {
                        bestParent2 = bestParent1;
                        bestParent1 = new Pair<>(canvas.currentLoss, individual);
                    } else if (canvas.currentLoss < bestParent2.getKey()) {
                        bestParent2 = new Pair<>(canvas.currentLoss, individual);
                    }
                }
                canvas = null;
            }
            Individual offSpring = bestParent1.getValue().crossBreed(bestParent2.getValue());
            Arrays.fill(individuals, offSpring);
            itr++;
            System.out.println(itr);
            if (itr % 5 == 0) {
                Canvas canvas = new Canvas(height, width);
                canvas.render(offSpring);
                System.out.println("Current loss: " + canvas.currentLoss);
                System.out.println("Number of triangles: " + offSpring.triangles.size());
                BufferedImage paintedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        try {
                            int r = canvas.pixels[i][j].r;
                            int g = canvas.pixels[i][j].g;
                            int b = canvas.pixels[i][j].b;
                            Color color = new Color(r, g, b);
                            paintedImage.setRGB(j, i, color.getRGB());
                        } catch (Exception e) {
                            System.out.println("Error: " + canvas.pixels[i][j].r + " " + canvas.pixels[i][j].g + " " + canvas.pixels[i][j].b);
                        }
                    }
                }
                try {
                    String newPath = outputPath + (itr / 5 + ".png");
                    File outputFile = new File(newPath);
                    ImageIO.write(paintedImage, "png", outputFile);
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}