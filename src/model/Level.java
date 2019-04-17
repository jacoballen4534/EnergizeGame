package model;

import javafx.geometry.Point2D;
import sample.Game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//These are the different things that can be on the map
enum TileType {
    FLOOR,
    WALL,
    CAMP_FIRE,
    PROTAGONIST,
    DOOR_UP,
    DOOR_RIGHT,
    DOOR_DOWN,
    DOOR_LEFT,
    ENEMY,
    NULLTILE,
    ITEM,
    GRUNT,
    BOMBER,
    ARCHER
}

public class Level {
    private ArrayList<ArrayList<TileType>> tiles = new ArrayList<>();
    private ArrayList<ArrayList<TileType>> pathFindingTiles = new ArrayList<>(); //This is only walls and floors
    private int levelNumber; //Used for doors, to go to the correct room
    private final int levelWidth;
    private final int levelHeight;
    private int mapWidth;//Used to calculate the level number of neighboring levels
    private HashMap<TileType, Point2D> doorMap; //Store the doors and their location.
    //Make sure all the doors in the level are reachable. Default true and set to false later if that door exists
    private boolean topDoorReachable = true, rightDoorReachable = true, bottomDoorReachable = true, leftDoorReachable = true;
    private Point2D currentPoint;
    private Point2D nextPoint;
    private int nextXDirection, nextYDirection; //Used to be able to keep doing in the same direction
    private ArrayList<Point2D> floorLocation = new ArrayList<>();
    private Node[][] nodes; //2d array of all nodes
    private double[][] dist;
    private Node[][] next;



    //////////////Macros, Actual size of different sprites///////////
    private final int Tile_SPRITE_WIDTH = 32;
    private final int Tile_SPRITE_HEIGHT = 32;

    private final int PROTAGONIST_SPRITE_WIDTH = 400;
    private final int PROTAGONIST_SPRITE_HEIGHT = 296;
    //Protagonist is downscaled from x8 spreadsheet to wanted size.
    private final double PROTAGONIST_SPRITE_SCALE = 0.35;

    private final int CAMP_FIRE_SPRITE_WIDTH = 64;
    private final int CAMP_FIRE_SPRITE_HEIGHT = 64;

    private final int DOOR_SPRITE_WIDTH = 70;
    private final int DOOR_SPRITE_HEIGHT = 80;
    private final int DOOR_RENDER_WIDTH = 64;
    private final int DOOR_RENDER_HEIGHT = 90;

    private final int GRUNT_SPRITE_WIDTH = 129;
    private final int GRUNT_SPRITE_HEIGHT = 111;



    public Level(BufferedImage image, int levelNumber, int mapWidth) { //Makes a level from an image
        this.levelWidth = image.getWidth();
        this.levelHeight = image.getHeight();
        this.levelNumber = levelNumber;
        this.doorMap = new HashMap<>();
        this.mapWidth = mapWidth;
        ProcessImage(image);
    }


    //Makes a random level. Needs the current row, col and map width to find what level each door should map to
    public Level(int levelNumber, int mapWidth, int wallArrangement, HashMap<TileType, Point2D> doorMap, int levelWidth, int levelHeight) { //Pass in top and left door, if they exist
        //////////////////////////////////// DOOR SETUP /////////////////////////////////////////////////////////////
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.levelNumber = levelNumber;
        this.doorMap = doorMap; //This puts the up and left door in, if there is one
        this.mapWidth = mapWidth;

        // Place right and down door in a random location along the respective border, if there is one.
        if ((wallArrangement & (0b1 << 2)) != 0) { //Add right door
            //Put door on right wall at random height. leave 1 tile above for the horizontal walls,
            // and 2 tiles below as the door takes up 2 vertical tiles (want the bottom to be above the lower wall.
            this.doorMap.put(TileType.DOOR_RIGHT, new Point2D(this.levelWidth-1, Game.getNextRandomInt(this.levelHeight -3 , true) + 1));
        }

        if ((wallArrangement & (0b1 << 1)) != 0) { //Add bottom door
            //Put door 1 tile above the bottom wall at random width. leave 1 tile on either side,
            this.doorMap.put(TileType.DOOR_DOWN, new Point2D(Game.getNextRandomInt(this.levelWidth - 2, true) + 1, this.levelHeight - 2));
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //////////////////////////////////// INITIALIZE WALLS ///////////////////////////////////////////////////////
        for (int row = 0; row < this.levelHeight; row ++) {
            ArrayList<TileType> column = new ArrayList<>();
            for (int col = 0; col < this.levelWidth; col++) {
                column.add(TileType.WALL);
            }
            this.tiles.add(column);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////// PICK STARTING POINT FOR RANDOM WALK /////////////////////////////////////////
        for (Map.Entry<TileType, Point2D> door : this.doorMap.entrySet()) {
            //Set currentPoint each time through loop to ensure it gets set at least once.
            switch (door.getKey()){
                case DOOR_UP:
                    this.topDoorReachable = false;
                    this.currentPoint = door.getValue().add(0, +1);
                    break;
                case DOOR_RIGHT:
                    this.rightDoorReachable = false;
                    this.currentPoint = door.getValue().add(-1, 0);
                    break;
                case DOOR_DOWN:
                    this.currentPoint = door.getValue().add(0, -2);
                    this.bottomDoorReachable = false;
                    break;
                case DOOR_LEFT:
                    this.leftDoorReachable = false;
                    this.currentPoint = door.getValue().add(+1, 0);
                    break;
            }
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////

        while (this.floorLocation.size() < (this.levelWidth * this.levelHeight * 0.5)) { //Set the percentage of walls to be removed

            //place floor and add to floor list
            this.placeFloor(currentPoint, true);

            //check if a door is now reachable - update reachable boolean accordingly
            this.updateReachableDoors(currentPoint);

            //Pick next location
            this.currentPoint = this.nextLocation(this.currentPoint);
        }


        //If a door is still unreachable, make a path to it.

        int steps = 1; //Converge from each door towards the centre. Converging at the same time will allow different paths to connect, making the full map connected earlie.
        while (!this.topDoorReachable || !this.rightDoorReachable || !this.bottomDoorReachable || !this.leftDoorReachable) {
            if (!this.topDoorReachable) {
                this.nextPoint = doorMap.get(TileType.DOOR_UP).add(0, steps);
                this.topDoorReachable = ((this.tiles.get((int)this.nextPoint.getY()).get((int)this.nextPoint.getX()) == TileType.FLOOR) || steps > this.levelHeight - 4);
                this.placeFloor(this.nextPoint, false);
            }
            if (!this.rightDoorReachable) {
                this.nextPoint = doorMap.get(TileType.DOOR_RIGHT).add(-steps, 0);
                this.rightDoorReachable = ((this.tiles.get((int)this.nextPoint.getY()).get((int)this.nextPoint.getX()) == TileType.FLOOR) || steps > this.levelWidth - 3);
                this.placeFloor(this.nextPoint, true);
            }
            if (!this.bottomDoorReachable) {
                this.nextPoint = doorMap.get(TileType.DOOR_DOWN).add(0, -steps - 1);
                this.bottomDoorReachable = ((this.tiles.get((int)this.nextPoint.getY()).get((int)this.nextPoint.getX()) == TileType.FLOOR) || steps > this.levelHeight - 4);
                this.placeFloor(this.nextPoint, true);
            }
            if (!this.leftDoorReachable) {
                this.nextPoint = doorMap.get(TileType.DOOR_LEFT).add(steps, 0);
                this.leftDoorReachable = ((this.tiles.get((int)this.nextPoint.getY()).get((int)this.nextPoint.getX()) == TileType.FLOOR) || steps > this.levelWidth - 3);
                this.placeFloor(this.nextPoint, true);
            }
            steps++;
        }


        ///////////////////////////////////////// PLACE DOORS /////////////////////////////////////////////////////
        //Add all the doors to the tile list.
        for (Map.Entry<TileType, Point2D> door : this.doorMap.entrySet()) {
            this.tiles.get((int)door.getValue().getY()).set((int)door.getValue().getX(), door.getKey());
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        this.ConvertToPathFindingTiles();
        this.setupNodes();
        this.calculateFloydWarshall();

        //Find paths between tiles
    }

    private void setupNodes() {
        this.nodes = new Node[levelHeight][levelWidth];
        //////////////////Initialize all nodes /////////////////////////////
        for (int row = 0; row < levelHeight; row ++) {
            for (int col = 0; col < levelWidth; col++) {
                nodes[row][col] = new Node(col + row * levelWidth);
            }
        }
        //////////////////////////////////////////////////////////////////

        for (int row = 1; row < levelHeight - 1; row ++) {
            for (int col = 1; col < levelWidth - 1; col++) {
                //Only add neighbors if it is a floor
                if (this.pathFindingTiles.get(row).get(col) == TileType.FLOOR && this.tiles.get(row - 1).get(col) != TileType.DOOR_UP &&
                        this.pathFindingTiles.get(row + 1).get(col) == TileType.FLOOR) { //Valid tile to be on

                    if (row > 1 && pathFindingTiles.get(row - 1).get(col) == TileType.FLOOR) { //Check above
                        nodes[row][col].addNighbor(col + (row - 1) * levelWidth);
                    }

                    //Can only go left or right if there are 2 tiles below as there is the bottom wall, and the characters are 2 blocks tall
                    if (row < levelHeight - 2) {
                        if (col < levelWidth - 2 && pathFindingTiles.get(row).get(col + 1) == TileType.FLOOR && pathFindingTiles.get(row + 1).get(col + 1) == TileType.FLOOR) { //check right
                            nodes[row][col].addNighbor((col + 1) + row * levelWidth);
                        }

                        if (col > 1 && pathFindingTiles.get(row).get(col - 1) == TileType.FLOOR && pathFindingTiles.get(row + 1).get(col - 1) == TileType.FLOOR) { //check left
                            nodes[row][col].addNighbor((col - 1) + row * levelWidth);
                        }
                    }

                    if (row < levelHeight - 3 && pathFindingTiles.get(row + 2).get(col) == TileType.FLOOR) { //Check below
                        nodes[row][col].addNighbor(col + (row + 1) * levelWidth);
                    }
                }
            }
        }

        //Replace all int neighbors with actual references.
        for (int row = 0; row < levelHeight; row ++) {
            for (int col = 0; col < levelWidth; col++) {
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

    private void calculateFloydWarshall() {
        int V = levelHeight * levelWidth;
        this.dist = new double[V][V];
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
//        System.out.println("All Path's Found");
    }


    public void findAndPrintPath(int startingId, int destinationId) {
        Node startingNode = getNodeFromId(startingId);
        Node desinationNode = getNodeFromId(destinationId);
//    public ArrayList<Node> findPath(int startingNode, int destinationNode) {
        ArrayList<Node> path = new ArrayList<>();
        if (next[startingId][destinationId] == null) {
//            return path;
            System.out.println("There is no path from " + startingId + " to " + destinationId);
            return;
        }
        path.add(startingNode);
        while (!startingNode.equals(desinationNode)) {
            startingNode = next[startingNode.getId()][desinationNode.getId()];
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

    public int nextDirection(int startingId, int destinationId) {
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

    private void ConvertToPathFindingTiles() { //Convert all tiles to wall or floor. And print it out for debugging
//        System.out.println("\n\n");
        for (int row = 0; row < this.levelHeight; row ++) {
            ArrayList<TileType> pathFindingCol = new ArrayList<>();
            for (int col = 0; col < this.levelWidth; col++) {
                switch (this.tiles.get(row).get(col)) {
                    case CAMP_FIRE:
                    case WALL:
                        pathFindingCol.add(TileType.WALL);
//                        System.out.print("W");
                        break;
                    case DOOR_UP:
                    case DOOR_RIGHT:
                    case DOOR_DOWN:
                    case DOOR_LEFT:
//                        System.out.print("D");
                        pathFindingCol.add(TileType.WALL);
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

    private Point2D nextLocation (Point2D currentPoint) {

        //Pick a random direction to step.
        switch (Game.getNextRandomInt(4, true)) {
            case 0: //25 %chance of moving in x direction
                this.nextXDirection = Game.getNextRandomInt(2, true) == 0 ? 1 : -1;
                this.nextYDirection = 0;
                break;
            case 1: //25% chance of moving in y direction
                this.nextYDirection = Game.getNextRandomInt(2, true) == 0 ? 1 : -1;
                this.nextXDirection = 0;
                break;
                // 50% chance of staying in the same direction
        }

        Point2D nextPosition = currentPoint.add(this.nextXDirection, this.nextYDirection);

        //If this new position is out of bounds, move back in the opposite direction 2 steps
        if (nextPosition.getX() < 1 || nextPosition.getX() > this.levelWidth - 2 ||
                 nextPosition.getY() < 1 || nextPosition.getY() > this.levelHeight - 3) {
            nextPosition = currentPoint.add(-this.nextXDirection, -this.nextYDirection);
        }


        return nextPosition;
    }


    private void placeFloor(Point2D location, boolean doubleTile) { //Double tile indicates if the tile underneath should be added also.
        // This is sometimes needed due the protagonist being 2 tiles tall
        this.tiles.get((int)location.getY()).set((int)location.getX(), TileType.FLOOR);
        if (!this.floorLocation.contains(location)) {
            this.floorLocation.add(location);
        }

        if (doubleTile) {
            this.tiles.get((int) location.getY() + 1).set((int) location.getX(), TileType.FLOOR);
            if (!this.floorLocation.contains(location.add(0, 1))) {
                this.floorLocation.add(location.add(0, 1));
            }
        }
    }

    private void updateReachableDoors(Point2D newLocation) {
        //Only update doors that are still unreachable.
        //Check if the new location is next to a door
        if (!this.topDoorReachable) {
            this.topDoorReachable = newLocation.equals(this.doorMap.get(TileType.DOOR_UP).add(0,1));
        }
        if (!this.rightDoorReachable) {
            this.rightDoorReachable = newLocation.equals(this.doorMap.get(TileType.DOOR_RIGHT).add(-1,0));
        }
        if (!this.bottomDoorReachable) {
            this.bottomDoorReachable = newLocation.equals(this.doorMap.get(TileType.DOOR_DOWN).add(0,-2));
        }
        if (!this.leftDoorReachable) {
            this.leftDoorReachable = newLocation.equals(this.doorMap.get(TileType.DOOR_LEFT).add(1,0));
        }
    }


    public ArrayList<ArrayList<TileType>> getTiles() {
        return this.tiles;
    }

    public int getLevelNumber() {
        return this.levelNumber;
    }

    public int getLevelWidth() {
        return this.levelWidth;
    }

    public int getLevelHeight() {
        return this.levelHeight;
    }

    public HashMap<TileType,Point2D> getDoors () {
        return this.doorMap;
    }

    private void ProcessImage(BufferedImage image) {
        for (int row = 0; row < this.levelHeight; row++) {
            ArrayList<TileType> column = new ArrayList<>();
            for (int col = 0; col < this.levelWidth; col++) {
                int pixel = image.getRGB(col, row);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 128 && green == 128 && blue == 128) { //Grey = Floor
                    column.add(TileType.FLOOR);
                } else if (red == 0 && green == 0 && blue == 0) { //Black = Wall
                    column.add(TileType.WALL);
                } else if (red == 0 && green == 0 && blue == 255) { //Blue = Protagonist
                    column.add(TileType.PROTAGONIST);
                } else if (red == 0 && green == 255 && blue == 0) { //Green = Door //Add different blue if we want to have non right doors
                    column.add(TileType.DOOR_RIGHT);
                    this.doorMap.put(TileType.DOOR_RIGHT, new Point2D(col, row));
                } else if (red == 255 && green == 165 && blue == 0) { //Orange = Campfire / random chance of some other background but not solid.
                    column.add(TileType.CAMP_FIRE);
                } else if (red == 128 && green == 0 && blue == 128) { //Purple = Item
                    column.add(TileType.ITEM);
                } else if (red == 255 && green == 0 && blue == 1) { // Red = Enemy, (Blue = 1) = Grunt
                    column.add(TileType.GRUNT);
                } else if (red == 255 && green == 0 && blue == 2) { // Red = Enemy, (Blue = 2) = Bomber
                    column.add(TileType.BOMBER);
                } else if (red == 255 && green == 0 && blue == 3) { // Red = Enemy, (Blue = 3) = Archer
                    column.add(TileType.ARCHER);
                } else {
                    column.add(TileType.NULLTILE);
                }
            }
            this.tiles.add(column);
        }
        this.ConvertToPathFindingTiles();
        this.setupNodes();
        this.calculateFloydWarshall();
        System.out.println("Done tutorial Room");
    }

    public void loadLevel(Game game) {
        Handler.clearAllObjects();

        for (int row = 0; row < this.levelHeight; row++) {
            for (int col = 0; col < this.levelWidth; col++) {
                TileType tile = this.tiles.get(row).get(col);
                //May be able to move sprite width into respective class's later. Keep here for testing
                switch (tile) {
                    case CAMP_FIRE:
                        Handler.addWall(col + row * this.levelWidth, new AnimationWall(col, row, PreLoadedImages.campFireSpriteSheet, CAMP_FIRE_SPRITE_WIDTH,
                                CAMP_FIRE_SPRITE_HEIGHT, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_HEIGHT * Game.SCALE, 4,0,0));
                        break;

                    case WALL:
                        Handler.addWall(col + row * this.levelWidth, new Wall(col,row, PreLoadedImages.tileSpriteSheet, Tile_SPRITE_WIDTH,
                                Tile_SPRITE_HEIGHT,CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_HEIGHT * Game.SCALE, 0,2));
//                        continue; //Continue if wall is a solid sprite, otherwise consider break to draw tile underneath.
                        break;
                    case PROTAGONIST:
                        Protagonist tempProtagonist = new Protagonist(col, row, PreLoadedImages.protagonistSpriteSheet, PROTAGONIST_SPRITE_WIDTH,
                                PROTAGONIST_SPRITE_HEIGHT, (int) (PROTAGONIST_SPRITE_WIDTH * Game.SCALE * PROTAGONIST_SPRITE_SCALE),
                                (int) (PROTAGONIST_SPRITE_HEIGHT * Game.SCALE * PROTAGONIST_SPRITE_SCALE), this.levelWidth);
                        Handler.addPlayer(tempProtagonist);
                        game.setProtagonist(tempProtagonist);
                        this.tiles.get(row).set(col, TileType.FLOOR);
                        break;

                    case DOOR_UP:
                        Point2D upDoorLocation = this.doorMap.get(TileType.DOOR_UP);
                        Handler.addDoor(new Door((int)upDoorLocation.getX(), (int)upDoorLocation.getY(), PreLoadedImages.doorSpriteSheet,
                                DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT, DOOR_RENDER_WIDTH, DOOR_RENDER_HEIGHT, this.levelNumber - this.mapWidth,TileType.DOOR_UP));
                        break;

                    case DOOR_RIGHT:
                        Point2D rightDoorLocation = this.doorMap.get(TileType.DOOR_RIGHT);
                        Handler.addDoor(new Door((int)rightDoorLocation.getX(), (int)rightDoorLocation.getY(), PreLoadedImages.doorSpriteSheet,
                                DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT, DOOR_RENDER_WIDTH, DOOR_RENDER_HEIGHT, this.levelNumber + 1,TileType.DOOR_RIGHT));
                        break;
//
                    case DOOR_DOWN:
                        Point2D downDoorLocation = this.doorMap.get(TileType.DOOR_DOWN);
                        Handler.addDoor(new Door((int)downDoorLocation.getX(), (int)downDoorLocation.getY(), PreLoadedImages.doorSpriteSheet,
                                DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT, DOOR_RENDER_WIDTH, DOOR_RENDER_HEIGHT, this.levelNumber + this.mapWidth,TileType.DOOR_DOWN));
                        break;

                    case DOOR_LEFT:
                        Point2D leftDoorLocation = this.doorMap.get(TileType.DOOR_LEFT);
                        Handler.addDoor(new Door((int)leftDoorLocation.getX(), (int)leftDoorLocation.getY(), PreLoadedImages.doorSpriteSheet,
                                DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT, DOOR_RENDER_WIDTH, DOOR_RENDER_HEIGHT, this.levelNumber - 1,TileType.DOOR_LEFT));
                        break;

                    case FLOOR: //This is just here so if we add tiles with different textures, we can differentiate and create floor with different spreadsheet row/col
                        break;

                    case ITEM:
                        Handler.addWall(col + row * this.levelWidth, new NullTile(col, row, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, false));
                        continue;

                    case GRUNT:
                        Handler.addEnemy(new Grunt(col,row,PreLoadedImages.gruntSpriteSheet, GRUNT_SPRITE_WIDTH, GRUNT_SPRITE_HEIGHT, GRUNT_SPRITE_WIDTH * Game.SCALE,
                                GRUNT_SPRITE_HEIGHT * Game.SCALE, this.levelWidth, true));
                        break;

                    default:
                        Handler.addWall(col + row * this.levelWidth, new NullTile(col, row, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, true));
                        continue;
                }
                //Move this into its own switch section above and add a tile to handler in the constructor of the other tiles that require a tile eg protagonist, door, enemy
                Handler.addFloor(new Floor(col, row, PreLoadedImages.tileSpriteSheet, Tile_SPRITE_WIDTH, Tile_SPRITE_HEIGHT, Game.PIXEL_UPSCALE, Game.PIXEL_UPSCALE, 0, 0));
            }
        }
        Handler.updateEnemyTarget(game.getProtagonist()); //As enemies can be added before protagonist making their target null. So add at the end.
        Handler.udateCharacterLevelWidth(this.levelWidth);
    }
}