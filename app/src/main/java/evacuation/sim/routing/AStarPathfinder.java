package evacuation.sim.routing;

import evacuation.sim.model.BaseType;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;
import evacuation.sim.model.Board;

import java.util.*;

public class AStarPathfinder implements PathfindingStrategy{

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

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.fCost, other.fCost);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(cell, node.cell);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cell);
        }
    }

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
            List<Cell> neighbors = getNeighbors(currentNode.cell, board);

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

                    // Since Java's PriorityQueue does not have a decrease-key operation, we need to remove and re-add the node to update its position in the queue
                    openSet.remove(neighborNode);
                    openSet.add(neighborNode);
                }
            }
        }

        return Collections.emptyList(); // Return empty path if no path is found
    }

    // Using Manhattan distance to the closest exit as heuristic
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

    private List<Cell> reconstructPath(Node node) {
        List<Cell> path = new ArrayList<>();
        while (node != null) {
            path.add(node.cell);
            node = node.parent;
        }
        Collections.reverse(path); // Reverse the path to get the correct order from start to end
        return path;
    }

    private List<Cell> getNeighbors(Cell cell, Board board) {
        List<Cell> neighbors = new ArrayList<>();
        int currentX = cell.getLogicalX();
        int currentY = cell.getLogicalY();

        // Define possible directions (up, down, left, right)
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int[] dir : directions) {
            int newX = currentX + dir[0];
            int newY = currentY + dir[1];

            Cell neighbor = board.getCell(newX, newY);
            if (neighbor != null) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }
}
