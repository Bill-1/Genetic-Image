import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class GeneticAlgo {
    public int height;
    public int width;
    public int[][][] rgbTarget;

    public void run(BufferedImage image, String outputPath, int population, int chromosomeNumber) {
        Path path = Paths.get(outputPath);
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path);
                System.out.println("Directory created at: " + outputPath);
            } else if (Files.isDirectory(path)) {
                System.out.println("Directory exists at: " + outputPath);
            } else {
                System.out.println("A file with the same name already exists at: " + outputPath);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while checking or creating the directory: " + e.getMessage());
        }
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

        System.out.println("Generating individuals...");
        for (int i = 0; i < population; i++) {
            individuals[i] = new Individual(height, width, chromosomeNumber);
        }
//        System.out.println("Generating individuals...");
        int itr = 0;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("../resources/data.ser"))) {
                oos.writeObject(individuals);
                System.out.println("Serialization successful: Array written to data.ser");
            } catch (IOException e) {
                System.err.println("Serialization failed:");
                e.printStackTrace();
            }
        }));
        while(true) {
            List<Pa> scores = new ArrayList<>();
            System.out.println("Process begins...");
            for (int i = 0; i < population; i++) {
                Individual individual = individuals[i];
                individual.mutate();
                scores.add(new Pa(individual.getLoss(), individual));
            }
            Collections.sort(scores);
            Individual[] parents = new Individual[population / 2];
            for (int i = 0; i < population / 2; i++) {
                parents[i] = scores.get(i).individual;
            }
            System.out.println("..." + scores.get(0).loss + "..." + scores.get(1).loss);
            for (int i = 0; i < population; i++) {
                Individual parent1 = parents[rand.nextInt(parents.length)];
                Individual parent2 = parents[rand.nextInt(parents.length)];
                Individual child = parent1.crossBreed(parent2);
                individuals[i] = child;
            }
            itr++;
            System.out.println(itr);
            if (itr % 5 == 0) {
                Individual bestSpeciman = scores.get(0).individual;
                Canvas canvas = new Canvas(height, width);
                canvas.render(bestSpeciman);
                System.out.println("Current loss: " + bestSpeciman.getLoss());
                System.out.println("Number of triangles: " + bestSpeciman.triangles.size());
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