import static java.lang.Math.round;

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

    public double willPaint(Triangle t) {
        double score = currentLoss * this.height * this.width * 3;
        long R = 0, G = 0, B = 0;
        for (Point p : t.filling) {
            if (p.x >= 0 && p.x < height && p.y >= 0 && p.y < width) {
                R += pixels[p.x][p.y].r;
                G += pixels[p.x][p.y].g;
                B += pixels[p.x][p.y].b;
                int redDiff = pixels[p.x][p.y].r - Main.target.pixels[p.x][p.y].r;
                int greenDiff = pixels[p.x][p.y].g - Main.target.pixels[p.x][p.y].g;
                int blueDiff = pixels[p.x][p.y].b - Main.target.pixels[p.x][p.y].b;
                score -= (redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
            }
        }
        long n = t.filling.size();
        long C1_1 = R;
        long C1_2 = G;
        long C1_3 = B;
        long C2_1 = R - t.red;
        long C2_2 = G - t.green;
        long C2_3 = B - t.blue;
        double A = 0.5;
        double Re = (C1_1 * A - C2_1) / (n * A);
        double Gr = (C1_2 * A - C2_2) / (n * A);
        double Bl = (C1_3 * A - C2_3) / (n * A);
        int alpha = (int) (A * 255);
        int red = (int) Math.min(255, Math.max(0, round(Re)));
        int green = (int) Math.min(255, Math.max(0, round(Gr)));
        int blue = (int) Math.min(255, Math.max(0, round(Bl)));
        for (Point p : t.filling) {
            if (p.x >= 0 && p.x < height && p.y >= 0 && p.y < width) {
                int r = (alpha * red + (255 - alpha) * pixels[p.x][p.y].r) / 255;
                int g = (alpha * green + (255 - alpha) * pixels[p.x][p.y].g) / 255;
                int b = (alpha * blue + (255 - alpha) * pixels[p.x][p.y].b) / 255;
                int redDiff = r - Main.target.pixels[p.x][p.y].r;
                int greenDiff = g - Main.target.pixels[p.x][p.y].g;
                int blueDiff = b - Main.target.pixels[p.x][p.y].b;
                score += (redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
            }
        }
        return (score / (this.height * this.width * 3));
    }

    public void paint(Triangle t) {
        double score = currentLoss * this.height * this.width * 3;
        long R = 0, G = 0, B = 0;
        for (Point p : t.filling) {
            if (p.x >= 0 && p.x < height && p.y >= 0 && p.y < width) {
                R += pixels[p.x][p.y].r;
                G += pixels[p.x][p.y].g;
                B += pixels[p.x][p.y].b;
                int redDiff = pixels[p.x][p.y].r - Main.target.pixels[p.x][p.y].r;
                int greenDiff = pixels[p.x][p.y].g - Main.target.pixels[p.x][p.y].g;
                int blueDiff = pixels[p.x][p.y].b - Main.target.pixels[p.x][p.y].b;
                score -= (redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
            }
        }
        long n = t.filling.size();
        long C1_1 = R;
        long C1_2 = G;
        long C1_3 = B;
        long C2_1 = R - t.red;
        long C2_2 = G - t.green;
        long C2_3 = B - t.blue;
        double A = 0.5;
        double Re = (C1_1 * A - C2_1) / (n * A);
        double Gr = (C1_2 * A - C2_2) / (n * A);
        double Bl = (C1_3 * A - C2_3) / (n * A);
        int alpha = (int) (A * 255);
        int red = (int) Math.min(255, Math.max(0, round(Re)));
        int green = (int) Math.min(255, Math.max(0, round(Gr)));
        int blue = (int) Math.min(255, Math.max(0, round(Bl)));
        for (Point p : t.filling) {
            if (p.x >= 0 && p.x < height && p.y >= 0 && p.y < width) {
                pixels[p.x][p.y].r = (alpha * red + (255 - alpha) * pixels[p.x][p.y].r) / 255;
                pixels[p.x][p.y].g = (alpha * green + (255 - alpha) * pixels[p.x][p.y].g) / 255;
                pixels[p.x][p.y].b = (alpha * blue + (255 - alpha) * pixels[p.x][p.y].b) / 255;
                int redDiff = pixels[p.x][p.y].r - Main.target.pixels[p.x][p.y].r;
                int greenDiff = pixels[p.x][p.y].g - Main.target.pixels[p.x][p.y].g;
                int blueDiff = pixels[p.x][p.y].b - Main.target.pixels[p.x][p.y].b;
                score += (redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
            }
        }
        currentLoss = score / (this.height * this.width * 3);
    }
}