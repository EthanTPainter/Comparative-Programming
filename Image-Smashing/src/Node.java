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
}