import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class P1
{
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
        Grid<Integer> energies;
        int col = 0;
        energies = p.energy(grid);
        ArrayList<Point> optList = new ArrayList<>();

        //Create node and setNodeGrid with node path
        Node n1 = new Node();
        Grid<Node> gn = n1.setNodeGrid(energies);
        gn = n1.setBestHop(gn);

        //Follow node path created
        //ASSUMING: We have at least one node.bestHop set (not null) in the first row
        boolean found = false;
        while(found == false){
            if(gn.get(0, col).bestHop !=  null){
                n1 = gn.get(0, col);
                found = true;
            }
            else{
                col++;
            }
        }
        //Loop through nodes until we reach the bottom
        while(n1.bestHop != null){
            optList.add(n1.p);
            n1 = n1.bestHop;
        }
        //Add last node to the end because it's the node on the bottom row
        optList.add(n1.p);
        //.out.println("Path: " + optList);
        return optList;
    }

    //Find list of points from cheapest path from left to right
    //Tips: Use grid transpose method to build it in terms of findVerticalPath definition
    public static List<Point> findHorizontalPath(Grid<RGB> grid)
    {
        P1 p = new P1();
        Grid<Integer> energies = new Grid<>();
        int col = 0;
        energies = p.energy(grid);
        ArrayList<Point> optList = new ArrayList<>();

        int newHeight = energies.width();
        int newWidth = energies.height();
        //Transpose energy grid
        energies = energies.transpose();
        //Set height and width
        energies.width = newWidth;
        energies.height = newHeight;
        //System.out.println("Trans: " + energies);

        //Create node and setNodeGrid with node path
        Node n1 = new Node();
        Grid<Node> gn = n1.setNodeGrid(energies);
        //System.out.println("Pre Node Grid: " + gn);
        gn = n1.setBestHop(gn);
        //System.out.println("Node Grid: " + gn);
        //Follow node path created
        //ASSUMING: We have at least one node.bestHop set (not null) in the first row
        boolean found = false;
        while(found == false){
            if(gn.get(0, col).bestHop !=  null){
                n1 = gn.get(0, col);
                found = true;
            }
            else{
                col++;
            }
        }
        //Loop through nodes until we reach the bottom
        while(n1.bestHop != null){
            //Inverse the points from (X,Y) -> (Y,X) because the grid was transposed
            optList.add(new Point(n1.p.getY(), n1.p.getX()));
            n1 = n1.bestHop;
        }
        //Add last node to the end because it's the node on the bottom row
        //Remember to swap (X,Y) -> (Y,X) for this one too
        optList.add(new Point(n1.p.getY(), n1.p.getX()));
        //System.out.println("Path: " + optList);
        return optList;
    }

    //Remove each location of the path from the grid
    //Return a reference to the grid even though it was modified in place
    public static Grid<RGB> removeVerticalPath(Grid<RGB> grid, List<Point> path)
    {
        //Assuming list goes from (0,0), (0,1), (0,2) ... in that order
        //This means we can work from back of the list to the front because each removal changes indexing (ArrayList)
        int count = path.size()-1;
        while(count != -1){
            int xVal = path.get(count).getX();
            int yVal = path.get(count).getY();
            grid.remove(xVal,yVal);
            count--;
        }
        //Set width (Check if changed)
        grid.width = grid.values.get(0).size();
        //System.out.println("Height: " + grid.height);
        //System.out.println("Width: " + grid.width);
        return grid;
    }

    //Remove each location of the path from the grid
    //If a row is empty (all removed), should remove the entire row from the grid
    public static Grid<RGB> removeHorizontalPath(Grid<RGB> grid, List<Point> path)
    {
        //Counter
        int starter = 0;
        grid = grid.transpose();
        //Get new Path with (X,Y) -> (Y,X)
        ArrayList<Point> newPath = new ArrayList<>();
        while(starter < path.size()){
            Point p1 = new Point(path.get(starter).getY(), path.get(starter).getX());
            newPath.add(p1);
            starter++;
        }
        /*Print out different paths
        System.out.println("Grid: "+ grid);
        System.out.println("Path: " + path);
        System.out.println("Height: " + grid.height);
        System.out.println("Width: " + grid.width);
        */

        //Remove all points from the grid
        int count = path.size()-1;
        while(count != -1){
            int xVal = newPath.get(count).getX();
            int yVal = newPath.get(count).getY();
            grid.remove(xVal,yVal);
            count--;
        }

        //Set width (Check if changed)
        grid.width = grid.values.get(0).size();
        //Transpose Grid
        grid = grid.transpose();
        return grid;
    }

    //Open indicated file, assumed to be in PPM P3 format
    //Read its contents and create and return the grid
    public static Grid<RGB> ppm2grid(String filename)
    {
        Grid<RGB> returnList = new Grid<RGB>();
        String line;
        try{
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            bufferedReader.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return returnList;
    }

    //Correctly store given grid in PPM P3 format in a file with the indicated name
    //In the current working directory
    public static void grid2ppm(Grid<RGB> grid, String filename)
    {
        //Assume Grid<RGB> is filled
        String output = "P3\n";
        int row = 0, col = 0;
        //Get output string from Grid values
        while(row < grid.height){
            while(col < grid.width){
                output += grid.get(row, col).getR() + "\n";
                output += grid.get(row, col).getG() + "\n";
                output += grid.get(row, col).getB() + "\n";
                col++;
            }
            row++;
        }
        //Store output string in file

        
        //System.out.println(output);
    }
}