package model;

import java.util.ArrayList;

import static model.TileType.*;

public class ShortestPath {
    private Node[][] nodes; //2d array of all nodes
    private Node[][] next;
    private final int levelWidth;
    private final int levelHeight;
    private boolean pathsThreadComplete = false;
    private ArrayList<ArrayList<TileType>> pathFindingTiles = new ArrayList<>(); //This is only walls and floors


    ShortestPath(int levelWidth, int levelHeight, ArrayList<ArrayList<TileType>> tiles) {
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        //Calculate the shortest paths in a new thread. This means find shortest path will return 0 until this is finished. So give enemy's random moves at the start
//        new Thread(this::calculateFloydWarshall).start();
        new Thread(() -> {
            ConvertToPathFindingTiles(tiles);
            setupNodes(tiles);
            calculateFloydWarshall();
//            System.out.println("All paths found");
        }).start();
    }


    private void ConvertToPathFindingTiles(ArrayList<ArrayList<TileType>> tiles) { //Convert all tiles to wall or floor. And print it out for debugging
//        System.out.println("\n\n");
        for (int row = 0; row < this.levelHeight; row ++) {
            ArrayList<TileType> pathFindingCol = new ArrayList<>();
            for (int col = 0; col < this.levelWidth; col++) {
                switch (tiles.get(row).get(col)) {
                    case CAMP_FIRE:
                    case WALL:
                        pathFindingCol.add(WALL);
//                        System.out.print("W");
                        break;
                    case DOOR_UP:
                    case DOOR_RIGHT:
                    case DOOR_DOWN:
                    case DOOR_LEFT:
//                        System.out.print("D");
                        pathFindingCol.add(WALL);
                        break;
                    default:
//                        System.out.print("F");
                        pathFindingCol.add(TileType.FLOOR);
                        break;
                }
            }
//            System.out.println("");
            this.pathFindingTiles.add(pathFindingCol);
        }
    }


    private Node getNodeFromId(int id) {
        for (int row = 0; row < levelHeight; row ++) {
            for (int col = 0; col < levelWidth; col++) {
                if (nodes[row][col].getId() == id) {
                    return nodes[row][col];
                }
            }
        }
        return null;
    }

    public int nextDirection(int startingId, int destinationId) {
        if (startingId >= 0 && startingId < next.length && destinationId >= 0 && destinationId < next.length && this.pathsThreadComplete) {
            if (next[startingId][destinationId] == null) {
//            return path;
                System.out.println("There is no path from " + startingId + " to " + destinationId);
                return 0;
            } else {
                int nextNodeId = next[startingId][destinationId].getId();

                if (nextNodeId == startingId + 1) { //right
                    return 2;
                } else if (nextNodeId > startingId) { //down
                    return 3;
                } else if (nextNodeId == startingId - 1) { //left
                    return 4;
                } else { //up
                    return 1;
                }
            }
        } else {
//            System.out.println("Calculating paths");
            return 5;//Not ready yet
        }
    }


    private void calculateFloydWarshall() {
        int V = levelHeight * levelWidth;
        double[][] dist = new double[V][V];
        this.next = new Node[V][V];

        ///////////////////////////////// Initialize ///////////////////////////////////
        for (int bigRow = 0; bigRow < V; bigRow++) {
            for (int bigCol = 0; bigCol < V; bigCol++) {
                dist[bigRow][bigCol] = Double.POSITIVE_INFINITY;
                next[bigRow][bigCol] = null;
            }
        }
        /////////////////////////////////////////////////////////////////////////////////

        ////////////////////////// Set Initial weights and connections ///////////////////
        for (int row = 0; row < levelHeight; row ++) {
            for (int col = 0; col < levelWidth; col++) {
                //For each vertex v
                dist[nodes[row][col].getId()][nodes[row][col].getId()] = 0; //dist[v][v] = 0;
                next[nodes[row][col].getId()][nodes[row][col].getId()] = nodes[row][col]; //next[v][v] = 0;
                for (Node nodeNeighbor : nodes[row][col].getNodeNeighbors()) { //for each edge(u,v)
                    dist[nodes[row][col].getId()][nodeNeighbor.getId()] = 1; //dist[u][v] = w(u,v) <- this is all 1 for our case
                    next[nodes[row][col].getId()][nodeNeighbor.getId()] = nodeNeighbor; // next[u][v] = v;
                }
            }
        }
        //////////////////////////////////////////////////////////////////////////////////////

        /////////////// introduce node k between i,j to try to improve path //////////////////
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][j] > dist[i][k] + dist[k][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
        this.pathsThreadComplete = true; //To stop sending 5(not ready yet) and start sending actual path values.
//        System.out.println("All Path's Found");
    }




    private void setupNodes(ArrayList<ArrayList<TileType>> tiles) {
        //////////////////Initialize all nodes /////////////////////////////
        this.nodes = new Node[this.levelHeight][this.levelWidth];

        for (int row = 0; row < this.levelHeight; row ++) {
            for (int col = 0; col < this.levelWidth; col++) {
                nodes[row][col] = new Node(col + row * this.levelWidth);
            }
        }
        //////////////////////////////////////////////////////////////////

        for (int row = 1; row < this.levelHeight - 1; row ++) {
            for (int col = 1; col < this.levelWidth - 1; col++) {
                //Only add neighbors if it is a floor
                if (pathFindingTiles.get(row).get(col) == TileType.FLOOR && tiles.get(row - 1).get(col) != TileType.DOOR_UP &&
                        pathFindingTiles.get(row + 1).get(col) == TileType.FLOOR) { //Valid tile to be on

                    if (row > 1 && pathFindingTiles.get(row - 1).get(col) == TileType.FLOOR) { //Check above
                        nodes[row][col].addNighbor(col + (row - 1) * this.levelWidth);
                    }

                    //Can only go left or right if there are 2 tiles below as there is the bottom wall, and the characters are 2 blocks tall
                    if (row < this.levelHeight - 2) {
                        if (col < this.levelWidth - 2 && pathFindingTiles.get(row).get(col + 1) == TileType.FLOOR && pathFindingTiles.get(row + 1).get(col + 1) == TileType.FLOOR) { //check right
                            nodes[row][col].addNighbor((col + 1) + row * this.levelWidth);
                        }

                        if (col > 1 && pathFindingTiles.get(row).get(col - 1) == TileType.FLOOR && pathFindingTiles.get(row + 1).get(col - 1) == TileType.FLOOR) { //check left
                            nodes[row][col].addNighbor((col - 1) + row * this.levelWidth);
                        }
                    }

                    if (row < this.levelHeight - 3 && pathFindingTiles.get(row + 2).get(col) == TileType.FLOOR) { //Check below
                        nodes[row][col].addNighbor(col + (row + 1) * this.levelWidth);
                    }
                }
            }
        }

        //Replace all int neighbors with actual references.
        for (int row = 0; row < this.levelHeight; row ++) {
            for (int col = 0; col < this.levelWidth; col++) {
                for (int neighborId : nodes[row][col].getNeighbors()) {
                    nodes[row][col].addNodeNighbor(getNodeFromId(neighborId));
                }
            }
        }

//        for (int row = 0; row < levelHeight; row ++) { //Print all nodes
//            for (int col = 0; col < levelWidth; col++) {
//                if (nodes[row][col].getNeighbors().isEmpty()) {
//                    System.out.print((char)27 + "[31m" + "W");
//                } else {
////                    System.out.print((char)27 + "[0m" + "F");
//                    System.out.print((char)27 + "[0m" + nodes[row][col].getId());
//                }
//                System.out.print("\t");
//            }
//            System.out.println("");
//        }
    }



    ///////////Unused for now/////////////
    public void findAndPrintPath(int startingId, int destinationId) { //Findis the complete path between two points and prints it out. also draws the map with path on it.
        Node startingNode = getNodeFromId(startingId);
        Node destinationNode = getNodeFromId(destinationId);
//    public ArrayList<Node> findPath(int startingNode, int destinationNode) {
        ArrayList<Node> path = new ArrayList<>();
        if (next[startingId][destinationId] == null) {
//            return path;
            System.out.println("There is no path from " + startingId + " to " + destinationId);
            return;
        }
        path.add(startingNode);
        while (!startingNode.equals(destinationNode)) {
            startingNode = next[startingNode.getId()][destinationNode.getId()];
            path.add(startingNode);
        }

        for (Node node : path) {
            System.out.print(node.getId());
            if (node.getId() != destinationId) {
                System.out.print(" -> ");
            } else {
                System.out.println("");
            }
        }

        for (int row = 0; row < levelHeight; row ++) {
            for (int col = 0; col < levelWidth; col++) {
                if (nodes[row][col].getNeighbors().isEmpty()) {
                    System.out.print((char)27 + "[31m" + "W");
                } else {
                    if (path.contains(nodes[row][col])) {
//                        System.out.print((char) 27 + "[34m" + "F");
                        System.out.print((char) 27 + "[34m" + nodes[row][col].getId());
                    } else {
//                      System.out.print((char)27 + "[0m" + "F");
                        System.out.print((char) 27 + "[0m" + nodes[row][col].getId());
                    }
                }
                System.out.print("\t");
            }
            System.out.println("");
        }
    }

    public int shortestPathLength(int startingId, int destinationId) { //Number of times from start to end for improved proximity
        Node startingNode = getNodeFromId(startingId);
        Node destinationNode = getNodeFromId(destinationId);
        int length = 0;
        if (next[startingId][destinationId] == null || startingNode == null || destinationNode == null) {
            return -1;//No path yet
        }
        while (!startingNode.equals(destinationNode)) {
            startingNode = next[startingNode.getId()][destinationNode.getId()];
            length++;
        }
       return length;
    }
}
