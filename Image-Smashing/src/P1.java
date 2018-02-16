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

        Grid<Integer> g = new Grid<Integer>(new Integer[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12}});

        P1 another = new P1();
        System.out.println(another.energy(p1));
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

        ArrayList<Point> returnList = new ArrayList<>();
        ArrayList<Point> currentList = new ArrayList<>();
        //Go through each bottom node getting total energy for the path
        //botStart is the starting bottom row node (for each iteration)
        //Sum is the energies sum generated from current path. optEnergy is optimal energy found thus far
        int sumEnergy = 0, optEnergy = 0, energyLeft, energyRight;
        row = energies.height()-1;
        while(col < energies.width()){
            while(row > 0){
                //Get current
                int energyCurrent = energies.get(row, col);
                //Get Left
                if(col == 0){
                    energyLeft = energies.get(row, energies.width()-1);
                }
                else{
                    energyLeft = energies.get(row, col - 1);
                }
                //Get Right
                if(col == energies.width()-1){

                }
                else{

                }

                row--;
            }
            row = energies.height()-1;
            col++;
        }
        return returnList;
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