package evacuation.sim.routing;

import evacuation.sim.model.Cell;
import evacuation.sim.model.Board;

import java.util.List;

/**
 * Interface defining the pathfinding strategy for evacuees.
 * Foundation of the Strategy Pattern, enabling
 * flexible exchange of agent navigation algorithms in real time.
 *
 * @author Bartłomiej Krajewski (293439)
 */
@FunctionalInterface
public interface PathfindingStrategy {
    /**
     * Calculates the optimal path of movement from the starting point to the targets.
     * @param Start the starting cell where the agent is currently located.
     * @param ends a list of potential destination cells (e.g., emergency exits).
     * @param board the board layout (e.g., the agent's mental map) used for analyzing the environment.
     * @return an ordered list of tiles constituting the escape route,
     * or an empty list if passage is impossible.
     */
    List<Cell> findPath(Cell Start, List<Cell> ends, Board board);
}
