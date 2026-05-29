package evacuation.sim.agent;

import evacuation.sim.model.Board;

public abstract class Agent {
    protected int id;
    protected int logicalX;
    protected int logicalY;
    protected float renderX;
    protected float renderY;
    protected boolean isActive;

    public abstract void update(Board board, float df);

    public int getId() {
        return id;
    }

    public int getLogicalX() {
        return logicalX;
    }

    public int getLogicalY() {
        return logicalY;
    }

    public float getRenderX() {
        return renderX;
    }

    public float getRenderY() {
        return renderY;
    }

    public boolean isActive() {
        return isActive;
    }
}
