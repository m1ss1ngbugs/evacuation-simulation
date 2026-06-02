package evacuation.sim.routing;

import evacuation.sim.model.BaseType;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;

import java.util.*;

public class AStarPathfinder implements PathfindingStrategy{

    private static class Node implements Comparable<Node> {
        Cell cell;
        Node parent;
        double gCost; // Cost from start to current node
        double hCost; // Heuristic cost from current node to end
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
    }
    @Override
    public List<Cell> findPath(Cell Start, Cell End, List<Cell> map){
        // List<Cell> path;

        if (Start == null || End == null || map == null) {
            return Collections.emptyList(); // Return empty path if start or end is null
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<Cell, Node> closedSet = new HashMap<>();
        Map<Cell, Node> allNodes = new HashMap<>();

        Node startNode = new Node(Start, null, 0.0, heuristic(Start, End));
        openSet.add(startNode);
        allNodes.put(Start, startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

            if (currentNode.cell.equals(End)) {
                return reconstructPath(currentNode);
            }

            closedSet.put(currentNode.cell, currentNode);
            List<Cell> neighbors = getNeighbors(currentNode.cell, map);

            for (Cell neighbor : neighbors) {
                if (closedSet.containsKey(neighbor)) {
                    continue; // Ignore the neighbor which is already evaluated.
                }

                if (neighbor.getBaseType() == BaseType.OBSTACLE) {
                    continue; // Ignore obstacles
                }

                if (neighbor.getBaseType() == BaseType.WALL) {
                    continue; // Ignore walls
                }

                double MoveCost = 1.0; // Assuming uniform cost for moving to a neighbor
                if(neighbor.getDynamicState() == DynamicState.FIRE){
                    MoveCost += 75.0; // Example of increased cost for fire
                } else if(neighbor.getDynamicState() == DynamicState.SMOKE){
                    MoveCost += 7.5; // Example of increased cost for smoke
                }

                double tentativeGCost = currentNode.gCost + MoveCost; // Calculate tentative gCost
                Node neighborNode = allNodes.get(neighbor);

                if(neighborNode == null) {
                    neighborNode = new Node(neighbor, currentNode, tentativeGCost, heuristic(neighbor, End));
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

    private double heuristic(Cell a, Cell b) {
        // Using Manhattan distance as heuristic
        return Math.abs(a.getLogicalX() - b.getLogicalX()) + Math.abs(a.getLogicalY() - b.getLogicalY());
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
}
