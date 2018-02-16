import java.util.ArrayList;
import java.util.List;

public class P1
{

    public static void main(String[] args)
    {
        Grid<RGB> p1 = new Grid<RGB>( new RGB[][]{
                {new RGB(100, 75,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(200,125,200)},
                {new RGB(150, 30,180),new RGB(150, 50,180),new RGB(100,120,180),new RGB(100,120,180),new RGB(100,120,180)},
                {new RGB(100, 75,100),new RGB(100, 80,100),new RGB(100, 85,100),new RGB(100, 95,100),new RGB(100,110,100)},
                {new RGB(200,100, 10),new RGB(200,100, 10),new RGB(200,100, 10),new RGB(210,200, 10),new RGB(255,  0, 10)}
        });

        Grid<Integer> g = new Grid<Integer>(new Integer[][]{
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12}});

        P1 another = new P1();
        Node again = new Node();
        Grid<Node> gn = again.setNodeGrid(g);
    }

    //Get Energy at Grid location
    //energy = h_energy + v_energy
    //h_energy = (R_left-R_right)^2 + (G_left-G_right)^2 + (B_left-B_right)^2
    //v_energy = (R_above-R_below)^2 + (G_above-G_below)^2 + (B_above-B_below)^2
    public static int energyAt(Grid<RGB> grid, int r, int c)
    {
        RGB left, right, above, below;
        int energy, h_energy, v_energy;

        //Horizontal Energy
        //node found on left column
        if(c == 0) {
            left = grid.get(r, grid.width()-1);
            right = grid.get(r,c+1);
        }
        //node found on right column
        else if(c == grid.width()-1) {
            left = grid.get(r, c-1);
            right = grid.get(r, 0);
        }
        //node not on left or right column
        else {
            left = grid.get(r,c-1);
            right = grid.get(r, c+1);
        }
        h_energy = (int) Math.pow(left.getR() - right.getR(), 2);
        h_energy += (int) Math.pow(left.getG() - right.getG(), 2);
        h_energy += (int) Math.pow(left.getB() - right.getB(), 2);

        //Vertical Energy
        if(r == 0){
            above = grid.get(grid.height()-1, c);
            below = grid.get(r+1, c);
        }
        else if(r == grid.height()-1){
            above = grid.get(r-1, c);
            below = grid.get(0, c);
        }
        else {
            above = grid.get(r-1, c);
            below = grid.get(r+1, c);
        }
        v_energy = (int) Math.pow(above.getR() - below.getR(), 2);
        v_energy += (int) Math.pow(above.getG() - below.getG(), 2);
        v_energy += (int) Math.pow(above.getB() - below.getB(), 2);
        //Sum all horizontal energy and vertical energy
        energy = h_energy + v_energy;
        return energy;
    }

    //Generate a grid of energies
    public static Grid<Integer> energy(Grid<RGB> grid)
    {
        Grid<Integer> newGrid = new Grid<Integer>(grid.height(), grid.width());
        for(int a = 0; a < grid.height(); a++){
            for(int b = 0; b < grid.width(); b++){
                newGrid.set(a, b, energyAt(grid,a,b));
            }
        }
        return newGrid;
    }

    //Find list of points from cheapest path from top to bottom
    //Tips: Start at the bottom and work your way to the top
    //Tips: Lowest value at the bottom guarantees Min path found
    public static List<Point> findVerticalPath(Grid<RGB> grid)
    {
        P1 p = new P1();
        Grid<Integer> energies = new Grid<>();
        int row, col = 0;
        energies = p.energy(grid);
        ArrayList<Point> optList = new ArrayList<>();
        ArrayList<Point> loopList = new ArrayList<>();
        //SavedEnergy is the energies sum generated from current path. optEnergy is optimal energy found thus far
        int loopEnergy = 0, optimalEnergy = 0, energyLeft, energyRight, energyMiddle;
        //Loop Column Left, Middle, and Right are for getting the Next Energies from Left, Middle, and Right points
        int loopColumnLeft = 0, loopColumnMiddle = 0, loopColumnRight = 0;
        //Loop Column is for setting the new loop column (may differentiate from column in loop)
        int loopColumn = 0;
        row = energies.height()-1;
        while(col < energies.width()){
            while(row >= 0) {
                //If row is bottom row (Bottom to top), then grab first element and increment row
                //If grabbing first element of the bottom row
                if(row == energies.height()-1){
                    loopEnergy = energies.get(row, col);
                    loopList.add(new Point(row, col));
                    row--;
                    energyMiddle = energies.get(row, col);
                }
                else{
                    //Get Middle/current node energy from previous loop
                    energyMiddle = energies.get(row, loopColumn);
                    loopColumnMiddle = loopColumn;
                }
                //Get Left node energy
                if (loopColumn == 0) {
                    energyLeft = energies.get(row, energies.width() - 1);
                    loopColumnLeft = energies.width()-1;
                } else {
                    energyLeft = energies.get(row, loopColumn - 1);
                    loopColumnLeft = loopColumn - 1;
                }
                //Get Right node energy
                if (loopColumn == energies.width() - 1) {
                    energyRight = energies.get(row, 0);
                    loopColumnRight = 0;
                } else {
                    energyRight = energies.get(row, loopColumn + 1);
                    loopColumnRight = loopColumn + 1;
                }

                //Compare the energies to determine which node should be selected
                //Select Left if left is less than middle and right
                if (energyLeft <= energyMiddle){
                    if (energyLeft <= energyRight) {
                        //Set energyLeft as new node to branch off from
                        loopList.add(0,new Point(row, loopColumnLeft));
                        loopColumn = loopColumnLeft;
                        loopEnergy += energyLeft;
                    }
                }
                //Select Middle if middle is less than left and right
                if(energyMiddle < energyLeft){
                    if(energyMiddle <= energyRight){
                        //Set energyMiddle as new node to branch off from
                        loopList.add(0,new Point(row, loopColumnMiddle));
                        loopColumn = loopColumnMiddle;
                        loopEnergy += energyMiddle;
                    }
                }
                //Select right if right is less than left and middle
                if(energyRight < energyLeft){
                    if(energyRight < energyMiddle){
                        //Set energyRight as new node to branch off from
                        loopList.add(0, new Point(row, loopColumnRight));
                        loopColumn = loopColumnRight;
                        loopEnergy += energyRight;
                    }
                }
                //Check for equals
                //Decrement row
                row--;
            }
            System.out.println("loopList: " + loopList.toString() + ", optList: " + optList.toString());
            //Reset row and add 1 to column
            row = energies.height()-1;
            col++;
            loopColumn = col;
            //If optimal energy not set, set to loop energy
            //Set optimal List as loop list
            if(optimalEnergy == 0){
                optimalEnergy = loopEnergy;
                //Add all values in loop List to optList
                int i = 0;
                while(i < loopList.size()){
                    optList.add(i,loopList.get(i));
                    i++;
                }
                loopList.clear();
            }
            //If optimal energy and optimal list set, compare to loop energy and list
            else if(loopEnergy < optimalEnergy){
                //Remove current values in optList and add all values in loop List to optList
                optList.clear();
                int i = 0;
                while(i < loopList.size()){
                    optList.add(i,loopList.get(i));
                    i++;
                }
                loopList.clear();
            }
            //If loop energy is greater reset the loop Energy and list
            else{
                loopList.clear();
                loopEnergy = 0;
            }
        }
        return optList;
    }

    //Find list of points from cheapest path from left to right
    //Tips: Use grid transpose method to build it in terms of findVerticalPath definition
    public static List<Point> findHorizontalPath(Grid<RGB> grid)
    {
        P1 p = new P1();
        Grid<Integer> energies = new Grid<>();
        int rows = 0, cols = 0;
        energies = p.energy(grid);
        ArrayList<Point> returnList = new ArrayList<>();

        return returnList;
    }

    //Remove each location of the path from the grid
    //Return a reference to the grid even though it was modified in place
    public static Grid<RGB> removeVerticalPath(Grid<RGB> grid, List<Point> path)
    {

        return null;
    }

    //Remove each location of the path from the grid
    //If a row is empty (all removed), should remove the entire row from the grid
    public static Grid<RGB> removeHorizontalPath(Grid<RGB> grid, List<Point> path)
    {

        return null;
    }

    //Open indicated file, assumed to be in PPM P3 format
    //Read its contents and create and return the grid
    public static Grid<RGB> ppm2grid(String filename)
    {

        return null;
    }

    //Correctly store given grid in PPM P3 format in a file with the indicated name
    //In the current working directory
    public static void grid2ppm(Grid<RGB> grid, String filename)
    {


    }
}