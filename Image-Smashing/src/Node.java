/*
 * @Author: Ethan Painter
 * Version: Java JDK 1.8
 * Date Started: 2/6/2018
 * Last updated: 2/6/2018
 */

import java.util.ArrayList;
import java.util.List;

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
    //SUMS OF ENERGIES DO NOT WRAP AROUND GRID
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
        Point leftp, midp, rightp;
        int leftSumEnergy, midSumEnergy, rightSumEnergy;
        while(row > 0){
            while(col < nodeGrid.width()){
                //Get middle energy
                middleEnergy = grid.get(row - 1, col);
                midSumEnergy = middleEnergy + nodeGrid.get(row, col).cost;
                midp = new Point(row - 1, col);
                //Get Left energy
                if(col == 0){
                    leftEnergy = grid.get(row - 1, grid.width() - 1);
                    leftSumEnergy = leftEnergy + nodeGrid.get(row, col).cost;
                    leftp = new Point(row - 1, grid.width()- 1);
                }
                else{
                    leftEnergy = grid.get(row - 1, col - 1);
                    leftSumEnergy = leftEnergy + nodeGrid.get(row, col).cost;
                    leftp = new Point(row - 1, col - 1);
                }
                //Get Right Energy
                if(col == grid.width() - 1){
                    rightEnergy = grid.get(row - 1, 0);
                    rightSumEnergy = rightEnergy + nodeGrid.get(row, col).cost;
                    rightp = new Point(row - 1, 0);
                }
                else{
                    rightEnergy = grid.get(row - 1, col + 1);
                    rightSumEnergy = rightEnergy + nodeGrid.get(row, col).cost;
                    rightp = new Point(row - 1, col + 1);
                }

                //Check nodeGrid for empty or comparable amounts
                //Start with leftEnergy location (null - NOT SET)
                //IF COL == 0 CANT SET LEFT ENERGY GRID LOCATION (NO WRAPPING)
                if(nodeGrid.get(leftp.getX(), leftp.getY()) == null && col != 0) {
                    //Set location with current sum energy
                    nodeGrid.set(leftp.getX(), leftp.getY(), new Node(leftp.getX(), leftp.getY(), null, leftSumEnergy));
                }
                //Else leftEnergy Location is not null, check if new amount is less than current amount
                else{
                    //IF COL == 0 CANT SET LEFT ENERGY GRID LOCATION (NO WRAPPING)
                    if(nodeGrid.get(leftp.getX(), leftp.getY()) != null && nodeGrid.get(leftp.getX(), leftp.getY()).cost > leftSumEnergy && col != 0){
                        nodeGrid.set(leftp.getX(), leftp.getY(), new Node(leftp.getX(), leftp.getY(), null, leftSumEnergy));
                    }
                }
                //Check for middleEnergy location (null - NOT SET)
                if(nodeGrid.get(midp.getX(), midp.getY()) == null){
                    //Set location with current sum energy
                    nodeGrid.set(midp.getX(), midp.getY(), new Node(midp.getX(), midp.getY(), null, midSumEnergy));
                }
                else{
                    if(nodeGrid.get(midp.getX(), midp.getY()).cost > midSumEnergy){
                        nodeGrid.set(midp.getX(), midp.getY(), new Node(midp.getX(), midp.getY(), null, midSumEnergy));
                    }
                }
                //Check for rightEnergy location (null - NOT SET)
                //IF COL == grid.width()-1 CANT SET LEFT ENERGY GRID LOCATION (NO WRAPPING)
                if(nodeGrid.get(rightp.getX(), rightp.getY()) == null && col != grid.width()-1){
                    nodeGrid.set(rightp.getX(), rightp.getY(), new Node(rightp.getX(), rightp.getY(), null, rightSumEnergy));
                }
                else {
                    //IF COL == grid.width()-1 CANT SET LEFT ENERGY GRID LOCATION (NO WRAPPING)
                    if(nodeGrid.get(rightp.getX(), rightp.getY()) != null && nodeGrid.get(rightp.getX(), rightp.getY()).cost > rightSumEnergy && col != grid.width()-1){
                        nodeGrid.set(rightp.getX(), rightp.getY(), new Node(rightp.getX(), rightp.getY(), null, rightSumEnergy));
                    }
                }
                col++;
            }
            col = 0;
            row--;
        }
        //System.out.println("Node Grid: " + nodeGrid);
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
        col = savedCol;
        //Save Left, Mid, and Right Node references (for clarity)
        Node leftNode, middleNode, rightNode;
        //Save energies found at left, mid, and right
        int leftEnergy, middleEnergy, rightEnergy;
        //Save leftColumn and rightColumn as they differ from mid
        int loopLeftColumn, loopRightColumn;
        while(row < nodeGrid.height()-1){
            //Get middle energy (energy below current spot)
            middleNode = nodeGrid.get(row + 1, col);
            middleEnergy = nodeGrid.get(row + 1, col).cost;
            //Get Left energy
            if(col == 0){
                leftNode = nodeGrid.get(row + 1, nodeGrid.width() - 1);
                //leftEnergy = nodeGrid.get(row + 1, nodeGrid.width() - 1).cost;
                leftEnergy = 1000000;      //Set leftEnergy to a ridiculous amount because it should be unreachable
                loopLeftColumn = nodeGrid.width() - 1;
            }
            else{
                leftNode = nodeGrid.get(row + 1, col - 1);
                leftEnergy = nodeGrid.get(row + 1, col - 1).cost;
                loopLeftColumn = col - 1;
            }
            //Get Right Energy
            if(col == nodeGrid.width() - 1){
                rightNode = nodeGrid.get(row + 1, 0);
                //rightEnergy = nodeGrid.get(row + 1, 0).cost;
                rightEnergy = 1000000;      //Set rightEnergy to a ridiculous amount because it should be unreachable
                loopRightColumn = 0;
            }
            else{
                rightNode = nodeGrid.get(row + 1, col + 1);
                rightEnergy = nodeGrid.get(row + 1, col + 1).cost;
                loopRightColumn = col + 1;
            }
            smallestCost = getSmallestEnergy(leftEnergy, middleEnergy, rightEnergy);
            //Set bestHop for current row + col
            if(smallestCost == leftEnergy){
                nodeGrid.get(row, col).bestHop = leftNode;
                col = loopLeftColumn;
            }
            else if(smallestCost == middleEnergy){
                nodeGrid.get(row, col).bestHop = middleNode;
            }
            else{
                nodeGrid.get(row, col).bestHop = rightNode;
                col = loopRightColumn;
            }
            row++;
        }
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