package evacuation.sim.routing;

import evacuation.sim.model.BaseType;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;
import evacuation.sim.model.Board;

import java.util.*;

/**
 * Klass responsible for pathfinding strategy for simulation evacuees.
 * Realize AStar pathfinding strategy.
 * Manages methods to transform cells into nodes to sort them more efficiently
 * in PriorityQueue and find the shortest path.
 * @author Bartłomiej Krajewski (293439)
 */
public class AStarPathfinder implements PathfindingStrategy{

    /**
     * It is a wrapper for cells, the entire algorithm works with nodes.
     */
    private static class Node implements Comparable<Node> {
        Cell cell;
        Node parent;
        double gCost; // Cost from start to current node (A -> X)
        double hCost; // Heuristic cost from current node to end (X -> B)
        double fCost; // Total cost (gCost + hCost) 

        public Node(Cell cell, Node parent, double gCost, double hCost) {
            this.cell = cell;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
        }

        /**
         * The method compares the value of this.fCost (the cost of the current node)
         * with other.fCost (the cost of the second node).
         * @param other specific object of the Node class with which we want to compare.
         * @return int (-1, 0, or 1) comparison result.
         */
        @Override
        public int compareTo(Node other) {
            return Double.compare(this.fCost, other.fCost);
        }

        /**
         * Checks whether the object on which the method was called is identical to the object passed as an argument.
         * @param o the object with which to compare.
         * @return boolean value (same tile or not).
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(cell, node.cell);
        }

        /**
         * The method takes the coordinates from the Cell tile and generates a unique number based on them.
         * @return int Cell id.
         */
        @Override
        public int hashCode() {
            return Objects.hash(cell);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * It calculates the shortest and most cost-effective path to the nearest available exit.
     * The algorithm dynamically takes into account penalties for passing through fire and smoke.
     */
    @Override
    public List<Cell> findPath(Cell Start, List<Cell> ends, Board board){

        if (Start == null || ends == null || ends.isEmpty() || board == null) {
            return Collections.emptyList(); // Return empty path if start or end is null
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(); // Upcoming cells
        Map<Cell, Node> closedSet = new HashMap<>(); // Visited cells
        Map<Cell, Node> allNodes = new HashMap<>(); // For checking if a cell already has a Node

        Node startNode = new Node(Start, null, 0.0, heuristic(Start, ends));
        openSet.add(startNode);
        allNodes.put(Start, startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll(); 

            if (ends.contains(currentNode.cell)) {
                return reconstructPath(currentNode);
            }

            closedSet.put(currentNode.cell, currentNode);
            List<Cell> neighbors = board.getNeighbors(currentNode.cell);

            for (Cell neighbor : neighbors) {
                if (closedSet.containsKey(neighbor)) {
                    continue; // Ignore the neighbor which is already evaluated
                }

                if (neighbor.getBaseType() == BaseType.OBSTACLE && !ends.contains(neighbor)) {
                    continue; // Ignore obstacles
                }

                if (neighbor.getBaseType() == BaseType.WALL) {
                    continue; // Ignore walls
                }

                double MoveCost = 1.0; 
                if(neighbor.getDynamicState() == DynamicState.FIRE){
                    MoveCost += 75.0; 
                } else if(neighbor.getDynamicState() == DynamicState.SMOKE){
                    MoveCost += 7.5; 
                }

                double tentativeGCost = currentNode.gCost + MoveCost; // Calculate tentative gCost
                Node neighborNode = allNodes.get(neighbor);

                if(neighborNode == null) {
                    neighborNode = new Node(neighbor, currentNode, tentativeGCost, heuristic(neighbor, ends));
                    allNodes.put(neighbor, neighborNode);
                    openSet.add(neighborNode); 
                } else if (tentativeGCost < neighborNode.gCost) {
                    neighborNode.parent = currentNode;
                    neighborNode.gCost = tentativeGCost;
                    neighborNode.fCost = tentativeGCost + neighborNode.hCost;

                    // Since Java's PriorityQueue does not have a decrease-key operation,
                    // we need to remove and re-add the node to update its position in the queue
                    openSet.remove(neighborNode);
                    openSet.add(neighborNode);
                }
            }
        }

        return Collections.emptyList(); // Return empty path if no path is found
    }

    /**
     * Using Manhattan distance to the closest exit as heuristic.
     * @param a the {@link Cell} object on which the algorithm is currently located.
     * @param ends list of all exits cells (objects of class {@link Cell} with EXIT {@link BaseType}).
     * @return double minDistance - estimated, shortest distance (measured in tiles)
     * from cell to the nearest of all available exits.
     */
    private double heuristic(Cell a, List<Cell> ends) {
        double minDistance = Double.MAX_VALUE;
        for (Cell end : ends) {
            double dist = Math.abs(a.getLogicalX() - end.getLogicalX()) + Math.abs(a.getLogicalY() - end.getLogicalY());
            if (dist < minDistance) {
                minDistance = dist;
            }
        }
        return minDistance;
    }

    /**
     * Recreates the final evacuation path, by getting cells from the parents nodes and reversing the path.
     * @param node The end node (object of class {@link Node}), i.e. the one representing the exit from the building.
     * @return List<Cell> - an ordered list of cells (objects of class {@link Cell}) tiles arranged chronologically.
     */
    private List<Cell> reconstructPath(Node node) {
        List<Cell> path = new ArrayList<>();
        while (node != null) {
            path.add(node.cell);
            node = node.parent;
        }
        Collections.reverse(path); // Reverse the path to get the correct order from start to end
        return path;
    }
}
