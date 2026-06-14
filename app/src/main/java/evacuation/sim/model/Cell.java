package evacuation.sim.model;

/**
 * A class responsible for cells. Storing their properties (coordinates, states, types and so on).
 * @author Heorhii Yartsev (293562)
 */
public class Cell {
    private int logicalX;
    private int logicalY;
    private BaseType baseType;
    private DynamicState dynamicState;
    private int visitCount;

    public Cell(int logicalX, int logicalY, BaseType baseType) {
        this.logicalX = logicalX;
        this.logicalY = logicalY;
        this.baseType = baseType;
        this.dynamicState = DynamicState.NONE;
        this.visitCount = 0;
    }

    public int getLogicalX() {
        return logicalX;
    }

    // standard getters and setters

    public int getLogicalY() {
        return logicalY;
    }

    public BaseType getBaseType() {
        return baseType;
    }

    public void setBaseType(BaseType baseType) {
        this.baseType = baseType;
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
