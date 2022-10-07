package app;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Picture implements Serializable {

    private final String name;
    private final int dotCount;
    private final double proportion;
    private final int width;
    private final int height;
    private final List<Dot> startingDots;

    public String getName() {
        return name;
    }

    public int getDotCount() {
        return dotCount;
    }

    public double getProportion() {
        return proportion;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Dot> getStartingDots() {
        return startingDots;
    }

    public Picture(String name, int dot_count, double proportion, int width, int height, List<Dot> startingDots) {
        this.name = name;
        this.dotCount = dot_count;
        this.proportion = proportion;
        this.width = width;
        this.height = height;
        this.startingDots = startingDots;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "name='" + name + '\'' +
                ", dot_count=" + dotCount +
                ", proportion=" + proportion +
                ", width=" + width +
                ", height=" + height +
                ", startingDots=" + startingDots +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return name.equals(picture.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
