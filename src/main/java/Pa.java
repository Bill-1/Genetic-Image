public class Pa implements Comparable<Pa> {
    public double loss;
    public Individual individual;

    public Pa(double loss, Individual individual) {
        this.loss = loss;
        this.individual = individual;
    }

    @Override
    public int compareTo(Pa other) {
        return Double.compare(this.loss, other.loss);
    }
}
