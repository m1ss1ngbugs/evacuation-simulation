package evacuation.sim.agent;

import evacuation.sim.model.Board;

public abstract class Agent {
    private int id;
    private int logicalX;
    private int logicalY;
    private float renderX;
    private float renderY;
    private boolean isActive;

    public Agent(int id, int logicalX, int logicalY) {
        this.id = id;
        this.logicalX = logicalX;
        this.logicalY = logicalY;
        this.renderX = logicalX;
        this.renderY = logicalY;
        this.isActive = true;
    }

    public abstract void update(Board board, float dt);

    // deactivates (in purpose to delete) agent
    public void deactivate() {
        this.isActive = false;
    }

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

    protected void setLogicalX(int logicalX) {
        this.logicalX = logicalX;
    }

    protected void setLogicalY(int logicalY) {
        this.logicalY = logicalY;
    }

    protected void setRenderX(float renderX) {
        this.renderX = renderX;
    }

    protected void setRenderY(float renderY) {
        this.renderY = renderY;
    }

    protected void setActive(boolean active) {
        isActive = active;
    }
}
