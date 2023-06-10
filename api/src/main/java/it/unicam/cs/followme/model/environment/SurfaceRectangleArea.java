package it.unicam.cs.followme.model.environment;

import it.unicam.cs.followme.util.DoubleRange;

public class SurfaceRectangleArea<L> extends SurfaceArea<L> {

    private final double width;
    private final double height;
    private final DoubleRange rangeX;
    private final DoubleRange rangeY;

    private static final double DEFAULT_SIZE = 1.0;

    public SurfaceRectangleArea(L label) {
        this(label, DEFAULT_SIZE, DEFAULT_SIZE);
    }

    public SurfaceRectangleArea(L label, double width, double height) {
        super(label);
        this.width = width;
        this.height = height;
        this.rangeX = new DoubleRange(-this.width/2, this.width/2);
        this.rangeY = new DoubleRange(-this.height/2, this.height/2);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public boolean includes(SurfacePosition position) {
        return this.rangeX.hasInRange(position.getX()) && this.rangeY.hasInRange(position.getY());
    }
}
