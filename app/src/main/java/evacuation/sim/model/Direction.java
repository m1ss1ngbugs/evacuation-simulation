package evacuation.sim.model;

/**
 * Enum representing the four basic directions on a 2D grid.
 * Used to calculate the positions of neighbors Cells (relative dX and dY offsets).
 * @author Heorhii Yartsev (293562)
 */
public enum Direction {
    /** North direction (up the Y axis). */
    NORTH(0, 1),
    /** East direction (right the X axis). */
    EAST(1, 0),
    /** South direction (down the Y axis). */
    SOUTH(0, -1),
    /** West direction (left the X axis). */
    WEST(-1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }

    /** @return X-axis offset for the given direction. */
    public int getDx() {
        return dx;
    }

    /** @return Y-axis offset for the given direction. */
    public int getDy() {
        return dy;
    }
}
