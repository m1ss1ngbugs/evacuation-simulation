package evacuation.sim.model;

public class Cell {
    private int logicalX;
    private int logicalY;
    private BaseType baseType;
    private DynamicState dynamicState;
    private int visitCount;

    public Cell(int logicalX, int logicalY) {
        this.logicalX = logicalX;
        this.logicalY = logicalY;
        baseType = BaseType.FLOOR;
        dynamicState = DynamicState.NONE;
        visitCount = 0;
    }

    public int getLogicalX() {
        return logicalX;
    }

    public int getLogicalY() {
        return logicalY;
    }

    public BaseType getBaseType() {
        return baseType;
    }

    public DynamicState getDynamicState() {
        return dynamicState;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setDynamicState(DynamicState dynamicState) {
        this.dynamicState = dynamicState;
    }

    public void addVisit(){
        visitCount++;
    }
}
