import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual {
    public List<Triangle> triangles;
    public int height;
    public int width;

    public Individual(int height, int width) {
        triangles = new ArrayList<>();
        this.height = height;
        this.width = width;
    }

    public void mutate(double mutationRate, Canvas canvas) {
        if (Math.random() < mutationRate) {
            double rate = Main.current / Main.targetLoss;
            double min = 1e18;
            Triangle t = null;
            for (int i = 0; i < 10; i++) {
                Triangle tr = new Triangle(height, width, rate);
                double loss = canvas.willPaint(tr);
                if (loss < min) {
                    min = loss;
                    t = tr;
                }
            }

            triangles.add(t);
            canvas.paint(t);
        } else {
            if (!triangles.isEmpty()) {
                Random random = new Random();
                int randomIndex = random.nextInt(triangles.size());
                triangles.remove(randomIndex);
            }
        }
        if (Math.random() < mutationRate) {
            mutate(mutationRate, canvas);
        }
    }

    public Individual crossBreed(Individual other) {
        Individual offSpring = new Individual(height, width);
        int len = Math.max(triangles.size(), other.triangles.size());
        Canvas canvas = new Canvas(height, width);
        for (int i = 0; i < len; i++) {
            if (triangles.size() > i && other.triangles.size() > i) {
                Triangle triangle1 = triangles.get(i);
                Triangle triangle2 = other.triangles.get(i);
                double loss1 = canvas.willPaint(triangle1);
                double loss2 = canvas.willPaint(triangle2);
                if (loss1 > loss2) {
                    offSpring.triangles.add(triangle2);
                    canvas.paint(triangle2);
                } else {
                    offSpring.triangles.add(triangle1);
                    canvas.paint(triangle1);
                }
//                System.out.println("Loss: " + canvas.currentLoss);
            } else if (triangles.size() > i) {
                offSpring.triangles.add(triangles.get(i));
                canvas.paint(triangles.get(i));
            } else if (other.triangles.size() > i) {
                offSpring.triangles.add(other.triangles.get(i));
                canvas.paint(other.triangles.get(i));
            }
        }
//        System.out.println("Offspring loss: " + canvas.currentLoss);
        Main.current = canvas.currentLoss;
        return offSpring;
    }
}