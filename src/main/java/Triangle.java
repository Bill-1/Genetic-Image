import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Triangle {

    public Color color;
    public Point p1, p2, p3;
    public List<Point> filling;
    public int red;
    public int green;
    public int blue;

    Random rand = new Random();

    public Triangle(int height, int width, double rate) {
        int he = (int) Math.floor(height * rate);
        int wi = (int) Math.floor(width * rate);
//        System.out.println(" " + he + ' ' + wi + ' ' + rate);
        int h = rand.nextInt(he);
        int w = rand.nextInt(wi);
        p1 = new Point(h, w);
        h = rand.nextInt(he);
        w = rand.nextInt(wi);
        p2 = new Point(h, w);
        h = rand.nextInt(he);
        w = rand.nextInt(wi);
        p3 = new Point(h, w);
        int xDiff = rand.nextInt(height - he + 1);
        int yDiff = rand.nextInt(width - wi + 1);
        displace(xDiff, yDiff);
        color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
    }

    public Triangle(Point p1, Point p2, Point p3, Color color) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.color = color;
    }

    public Triangle(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.color = Color.WHITE;
    }

    public void displace(int X, int Y) {
        p1.displace(X, Y);
        p2.displace(X, Y);
        p3.displace(X, Y);
    }

    public boolean isIn(Point p) {
        Triangle smallerTriangle1 = new Triangle(p, p2, p3);
        Triangle smallerTriangle2 = new Triangle(p1, p, p3);
        Triangle smallerTriangle3 = new Triangle(p1, p2, p);

        int area = getTwiceTheArea();
        int smallArea1 = smallerTriangle1.getTwiceTheArea();
        int smallArea2 = smallerTriangle2.getTwiceTheArea();
        int smallArea3 = smallerTriangle3.getTwiceTheArea();

        return (area == smallArea1 + smallArea2 + smallArea3);
    }

    public int getTwiceTheArea() {
        return Math.abs(p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y));
    }

    public void mutate() {
        Random rand = new Random();

        p1.x += rand.nextInt(11) - 5;
        p1.y += rand.nextInt(11) - 5;
        p2.x += rand.nextInt(11) - 5;
        p2.y += rand.nextInt(11) - 5;
        p3.x += rand.nextInt(11) - 5;
        p3.y += rand.nextInt(11) - 5;

        int newRed = Math.min(255, Math.max(0, color.getRed() + rand.nextInt(11) - 5));
        int newGreen = Math.min(255, Math.max(0, color.getGreen() + rand.nextInt(11) - 5));
        int newBlue = Math.min(255, Math.max(0, color.getBlue() + rand.nextInt(11) - 5));
        int newAlpha = Math.min(255, Math.max(0, color.getAlpha() + rand.nextInt(11) - 5));
        color = new Color(newRed, newGreen, newBlue, newAlpha);
    }

    public Triangle blendTriangle(Triangle other) {
        Random rand = new Random();

        int x1 = (int)(rand.nextDouble() * p1.x + (1 - rand.nextDouble()) * other.p1.x);
        int y1 = (int)(rand.nextDouble() * p1.y + (1 - rand.nextDouble()) * other.p1.y);
        Point blendedP1 = new Point(x1, y1);

        int x2 = (int)(rand.nextDouble() * p2.x + (1 - rand.nextDouble()) * other.p2.x);
        int y2 = (int)(rand.nextDouble() * p2.y + (1 - rand.nextDouble()) * other.p2.y);
        Point blendedP2 = new Point(x2, y2);

        int x3 = (int)(rand.nextDouble() * p3.x + (1 - rand.nextDouble()) * other.p3.x);
        int y3 = (int)(rand.nextDouble() * p3.y + (1 - rand.nextDouble()) * other.p3.y);
        Point blendedP3 = new Point(x3, y3);

        double rWeight = rand.nextDouble();
        int red = (int)(rWeight * color.getRed() + (1 - rWeight) * other.color.getRed());
        double gWeight = rand.nextDouble();
        int green = (int)(gWeight * color.getGreen() + (1 - gWeight) * other.color.getGreen());
        double bWeight = rand.nextDouble();
        int blue = (int)(bWeight * color.getBlue() + (1 - bWeight) * other.color.getBlue());
        double aWeight = rand.nextDouble();
        int alpha = (int)(aWeight * color.getAlpha() + (1 - aWeight) * other.color.getAlpha());

        Color blendedColor = new Color(red, green, blue, alpha);

        return new Triangle(blendedP1, blendedP2, blendedP3, blendedColor);
    }

    public List<Point> fill() {
        List<Point> fillingArea = new ArrayList<>();
        Stack<Point> stack = new Stack<>();
        stack.push(p1);
        stack.push(p2);
        stack.push(p3);
        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int i = p.x;
            int j = p.y;
            if (i < 0 || i >= Main.target.height || j < 0 || j >= Main.target.width) {
                continue;
            }
            if (!Main.visited[p.x][p.y] && isIn(p)) {
                red += Main.target.pixels[p.x][p.y].r;
                green += Main.target.pixels[p.x][p.y].g;
                blue += Main.target.pixels[p.x][p.y].b;
                Main.visited[p.x][p.y] = true;
                fillingArea.add(p);
                stack.push(new Point(i + 1, j));
                stack.push(new Point(i, j + 1));
                stack.push(new Point(i, j - 1));
                stack.push(new Point(i - 1, j));
                stack.push(new Point(i + 1, j - 1));
                stack.push(new Point(i - 1, j + 1));
                stack.push(new Point(i - 1, j - 1));
                stack.push(new Point(i + 1, j + 1));

            }
        }
        for (Point p : fillingArea) {
            Main.visited[p.x][p.y] = false;
        }
        return fillingArea;
    }
}
