package evacuation.sim.routing;

import evacuation.sim.model.Cell;
import evacuation.sim.model.Board;

import java.util.List;

@FunctionalInterface
public interface PathfindingStrategy {
    List<Cell> findPath(Cell Start, List<Cell> ends, Board board);
}
