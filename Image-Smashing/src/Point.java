/*
 * @Author: Ethan Painter
 * Version: Java JDK 1.8
 * Date Started: 2/6/2018
 * Last updated: 2/6/2018
 */

//Represents a pair of int values (x and y)
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
    //Doesn't affect current point
    public Point invert(){
        Point p = new Point();
        p.setX(y);
        p.setY(x);
        return p;
    }

    //Custom equals method
    @Override
    public boolean equals(Object object){
        return (object instanceof Point) && (((Point) object).getX() == this.getX()) && (((Point) object).getY() == this.getY());
    }

    //Custom toString method
    @Override
    public String toString(){
        //Return format: "(3,4)"
        return "(" + x + "," + y + ")";
    }

}