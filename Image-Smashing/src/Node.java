/*
 * @Author: Ethan Painter
 * Version: Java JDK 1.8
 * Date Started: 2/6/2018
 * Last updated: 2/6/2018
 */

public class Node{

    public Point p;
    public Node bestHop;
    public int cost;

    //Default Constructor
    public Node(){ }

    //Constructor creates a Point from row r and column c
    //Quite common for bestHop to be null
    //Cost indicates the full cost of the cheapest path from this point
    // inclusive to the end
    public Node(int r, int c, Node bestHop, int cost){
        p = new Point(r,c);
        this.bestHop = bestHop;
        this.cost = cost;
    }

    //Given a Grid of Integer values
    //Create a grid of nodes with set bestHop and costs for each node
    //Follows a bottom-up structure
    public Grid<Node> setNodeGrid(Grid<Integer> grid){
        Grid<Node> nodeGrid = new Grid<>(grid.height(), grid.width());
        int row, col;
        Node sampleNode;
        //Start with the bottom row because we want to build from bottom to top, left to right
        row = nodeGrid.height()-1;
        col = 0;
        //Initialize the bottom row to given bottom row values from Integer Grid(bestHop and cost)
        while(col < nodeGrid.width()){
            sampleNode = new Node(row, col,null, grid.get(row, col));
            nodeGrid.set(row, col, sampleNode);
            col++;
        }
        col = 0;
        int middleEnergy, leftEnergy, rightEnergy;
        int summedEnergy;
        while(row > 0){
            while(col < nodeGrid.width()){
                //Get middle energy
                middleEnergy = grid.get(row - 1, col);
                //Get Left energy
                if(col == 0){
                    leftEnergy = grid.get(row - 1, grid.width() - 1);
                }
                else{
                    leftEnergy = grid.get(row - 1, col - 1);
                }
                //Get Right Energy
                if(col == grid.width() - 1){
                    rightEnergy = grid.get(row - 1, 0);
                }
                else{
                    rightEnergy = grid.get(row - 1, col + 1);
                }
                int smallest = getSmallestEnergy(leftEnergy, middleEnergy, rightEnergy);
                summedEnergy = smallest + nodeGrid.get(row, col).cost;
                nodeGrid.set(row - 1, col, new Node(row - 1, col, null, summedEnergy));
                col++;
            }
            col = 0;
            row--;
        }
        System.out.println("Node Grid: " + nodeGrid);
        return nodeGrid;
    }

    //Set bestHops for minimum path in the grid
    //Only sets ONE path. Doesn't set any other path to follow
    //This path is immediately relied on for vertical and horizontal paths
    public Grid<Node> setBestHop(Grid<Node> nodeGrid){
        //Analyze best path from cost on top row
        int row = 0, col = 1, savedCol = 0;
        int smallestCost = nodeGrid.get(0,0).cost;
        while(col < nodeGrid.width()) {
            if (smallestCost > nodeGrid.get(0, col).cost) {
                smallestCost = nodeGrid.get(0, col).cost;
                savedCol = col;
            }
            col++;
        }
        //Given smallestCost, Find path from row 0 and savedCol #


        return nodeGrid;
    }

    public int getSmallestEnergy(int leftEnergy, int middleEnergy, int rightEnergy){
        if(leftEnergy <= middleEnergy){
            if(leftEnergy <= rightEnergy){
                return leftEnergy;
            }
        }
        else if(middleEnergy < leftEnergy){
            if(middleEnergy <= rightEnergy){
                return middleEnergy;
            }
        }
        return rightEnergy;
    }

    @Override
    public String toString() {
            String result;
            if(bestHop == null) {
                result = "(null)|" + cost;
            }
            else{
                result =  bestHop.p.getX() + "," + bestHop.p.getY() + "|" + cost;
        }
        return result;
    }
}