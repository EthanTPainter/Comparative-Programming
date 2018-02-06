/*
 * @Author: Ethan Painter
 * Version: Java JDK 1.8
 * Date Started: 2/6/2018
 * Last updated: 2/6/2018
 */

//Represents a pair of int alues (x and y)
public class Point{

    int x, y;

    //Default Constructor
    public Point(){ }

    //Constructor that accepts two ints
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    //Getters and setters for x
    public int getX(){
        return x;
    }
    public void setX(int newValue){
        x = newValue;
    }

    //Getters and setters for y
    public int getY(){
        return y;
    }
    public void setY(int newValue){
        y = newValue;
    }

    //Creates an inverted point (x and y are swapped)
    public Point invert(){
        int z;
        z = x;
        x = y;
        y = z;
        return this
    }

    public String toString(){
        //Return format: "(3,4)"
        return "(" + x + "," + y + ")";
    }

}