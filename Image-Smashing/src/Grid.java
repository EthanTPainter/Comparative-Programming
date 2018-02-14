/*
 * @Author: Ethan Painter
 * Version: Java JDK 1.8
 * Date Started: 2/6/2018
 * Last updated: 2/6/2018
 */

import java.util.ArrayList;

public class Grid<T>{

    public ArrayList<ArrayList<T>> values = new ArrayList<ArrayList<T>>();

    //Local variables
    int height, width;

    //Default Constructor
    public Grid(){ }

    //Creates a grid with indicated size
    //makes each location null
    public Grid(int numRows, int numCols)
    {
        //Assign height and width
        height = numRows;
        width = numCols;

        //Couple for loops to initialize values
        int row, col;
        for(row = 0; row < numRows; row++){
            ArrayList<T> a = new ArrayList<T>();
            values.add(a);
            for(col = 0; col < numCols; col++){
                a.add(null);
            }
        }
    }

    //Assumes input is a rectangle
    //Creates and fills values from its contents
    public Grid(T[][] inGrid)
    {
        //Set height and width
        height = inGrid.length;
        width = inGrid[0].length;

        //Couple for loops to check for values
        int rows,cols;
        for(rows = 0; rows < inGrid.length; rows++){
            ArrayList<T> a = new ArrayList<T>();
            values.add(a);
            for(cols = 0; cols < inGrid[rows].length; cols++){
                a.add(inGrid[rows][cols]);
            }
        }
    }

    //Returns number of rows
    public int height()
    {
        return height;
    }

    //Returns the number of columns
    public int width()
    {
        return width;
    }

    //Returns item in row i and column j
    public T get(int i , int j)
    {
        T found = values.get(i).get(j);
        return found;
    }

    //Assumes location (i,j) exists in Grid
    //Sets the location's value to given value
    public void set(int i, int j, T value)
    {
        values.get(i).set(j,value);
        return;
    }

    //Remove item at location (i,j)
    //If row is now empty, remove row
    //Removed value is returned
    public T remove(int i, int j)
    {
        T output = values.get(i).get(j);
        values.get(i).remove(j);
        if(values.get(i).isEmpty()){
            values.remove(i);
            height = height - 1;
        }
        return output;
    }

    //Creates a transposed version of the Grid
    //ie. Leftmost column is the new first row
    //ie. Second-leftmost column is the new second row
    //DOESN'T MODIFY THE ORIGINAL GRID
    public Grid<T> transpose()
    {
        Grid<T> grid = new Grid<>();
        int newHeight = this.width();
        int newWidth = this.height();
        //Couple for loops to initialize values
        int row, col;
        for(row = 0; row < newHeight; row++){
            ArrayList<T> a = new ArrayList<T>();
            grid.values.add(a);
            for(col = 0; col < newWidth; col++){
                a.add(this.get(col, row));
            }
        }
        //return grid
        return grid;
    }

    //Custom toString method (lazy method)
    @Override
    public String toString()
    {
        return values.toString();
    }

    //When object is also a grid
    //AND both grids are the same size
    //AND every single location holds items that are equal to each other
    //this method returns true
    @Override
    public boolean equals(Object object){
        return (object instanceof Grid) && (((Grid)object).height() == this.height()) && (((Grid)object).width() == this.width()) && (((Grid)object).toString().equals(this.toString()));
    }
}