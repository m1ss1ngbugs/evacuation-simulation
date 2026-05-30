package evacuation.sim.routing;

import evacuation.sim.model.Cell;

import java.util.List;

@FunctionalInterface
public interface PathfindingStrategy {
    List<Cell> findPath(Cell Start, Cell End, List<Cell> map);
}
