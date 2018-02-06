/*
 * @Author: Ethan Painter
 * Version: Java JDK 1.8
 * Date Started: 2/6/2018
 * Last updated: 2/6/2018
 */

public class RGB{

    //assumed values in each between (0,255)
    int r, g, b;

    //Default Constructor
    public RGB(){ }

    //Constructor accepts three ints
    public RGB(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    //Getter and setter for r
    public int getR(){
        return r;
    }
    public void setR(int newValue){
        r = newValue;
    }

    //Getter and setter for g
    public int getG(){
        return g;
    }
    public void setG(int newValue){
        g = newValue;
    }

    //Getter and setter for b
    public int getB(){
        return b;
    }
    public void setB(int newValue){
        b = newValue;
    }

    //Other is also an instance of RGB
    //Check if they have the same values for r, g, and b.
    // Return true if all instance varaibles equal
    public boolean equals(Object other){
        if(this.getR() == (RGB) other.getR() && this.getG() == (RGB) other.getG() && this.getB() == (RGB) other.getB()){
            return true;
        }
        else{
            return false;
        }
    }

}