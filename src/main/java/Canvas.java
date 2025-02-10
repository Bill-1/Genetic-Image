import static java.lang.Math.round;

import java.util.List;

public class Canvas implements Cloneable {

    public int height, width;
    public Pixel[][] pixels;
    public double currentLoss;

    public Canvas(int height, int width) {
        this.height = height;
        this.width = width;
        pixels = new Pixel[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i][j] = new Pixel(255, 255, 255);
            }
        }
        currentLoss = Main.targetLoss;
    }

    public Canvas(int height, int width, int[][][] painted) {
        this.height = height;
        this.width = width;
        pixels = new Pixel[height][width];
        double targetLoss = 0;
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                pixels[i][j] = new Pixel(painted[i][j][0], painted[i][j][1], painted[i][j][2]);
                int redDiff = 255 - painted[i][j][0];
                int greenDiff = 255 - painted[i][j][1];
                int blueDiff = 255 - painted[i][j][2];
                targetLoss += (redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
            }
        }
        Main.targetLoss = targetLoss / (this.height * this.width * 3);
        Main.current = Main.targetLoss;
    }

    public void render(Individual individual) {
        currentLoss = Main.targetLoss;
        for (Triangle triangle : individual.triangles) {
            this.paint(triangle);
        }
    }

    public void paint(Triangle t) {
        double score = currentLoss * this.height * this.width * 3;
        int red = t.color.getRed();
        int green = t.color.getGreen();
        int blue = t.color.getBlue();
        int alpha = t.color.getAlpha();

        List<Point> filling = t.fill();
        for (Point p : filling) {
            int redDiff = pixels[p.x][p.y].r - Main.target.pixels[p.x][p.y].r;
            int greenDiff = pixels[p.x][p.y].g - Main.target.pixels[p.x][p.y].g;
            int blueDiff = pixels[p.x][p.y].b - Main.target.pixels[p.x][p.y].b;
            score -= (redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
            pixels[p.x][p.y].r = (pixels[p.x][p.y].r * (255 - alpha) + alpha * red) / 255;
            pixels[p.x][p.y].g = (pixels[p.x][p.y].g * (255 - alpha) + alpha * green) / 255;
            pixels[p.x][p.y].b = (pixels[p.x][p.y].b * (255 - alpha) + alpha * blue) / 255;
            redDiff = pixels[p.x][p.y].r - Main.target.pixels[p.x][p.y].r;
            greenDiff = pixels[p.x][p.y].g - Main.target.pixels[p.x][p.y].g;
            blueDiff = pixels[p.x][p.y].b - Main.target.pixels[p.x][p.y].b;
            score += (redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
        }

        currentLoss = score / (this.height * this.width * 3);
    }
}