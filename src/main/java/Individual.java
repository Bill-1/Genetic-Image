import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual {
    public List<Triangle> triangles;
    public int height;
    public int width;
    public double loss = Main.targetLoss;
    public boolean dirty = false;

    public Individual(int height, int width, int size) {
        triangles = new ArrayList<>();
        this.height = height;
        this.width = width;
        for (int i = 0; i < size; i++) {
            triangles.add(new Triangle(height, width, 1));
        }
    }

    public void mutate() {
        double chance = Math.random();
        if (chance < 0.1) {
            triangles.add(new Triangle(height, width, 1));
            dirty = true;
        } else if (chance > 0.9) {
            if (!this.triangles.isEmpty()) {
                int indexToRemove = new Random().nextInt(this.triangles.size());
                this.triangles.remove(indexToRemove);
                dirty = true;
            }
        }
        for (Triangle triangle : triangles) {
            chance = Math.random();
            if (chance < 0.2) {
                triangle.mutate();
            }
        }
    }

    public double getLoss() {
        if (!dirty) {
            return loss;
        } else {
            dirty = false;
            Canvas canvas = new Canvas(height, width);
            canvas.render(this);
            loss = canvas.currentLoss;
            return loss;
        }
    }

    public Individual crossBreed(Individual other) {
        Individual child = new Individual(height, width, 0);
        double chance = Math.random();
        int numTriangles;
        if (chance < 0.5) {
            numTriangles = Math.max(this.triangles.size(), other.triangles.size());
            for (int i = 0; i < numTriangles; i++) {
                if (i >= other.triangles.size()) {
                    child.triangles.add(triangles.get(i));
                } else if (i >= this.triangles.size()) {
                    child.triangles.add(other.triangles.get(i));
                } else {
                    Triangle triangle1 = this.triangles.get(i);
                    Triangle triangle2 = other.triangles.get(i);
                    Triangle blendedTriangle = triangle1.blendTriangle(triangle2);
                    child.triangles.add(blendedTriangle);
                }
            }
        } else {
            numTriangles = Math.max(this.triangles.size(), other.triangles.size());
            for (int i = 0; i < numTriangles; i++) {
                if (Math.random() < 0.5) {
                    if (i < this.triangles.size()) {
                        child.triangles.add(this.triangles.get(i));
                    }
                } else {
                    if (i < other.triangles.size()) {
                        child.triangles.add(other.triangles.get(i));
                    }
                }
            }
        }
        return child;
    }
}