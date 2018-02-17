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

        Grid<RGB> g1 = new Grid<RGB>(new RGB[][]{
                {new RGB(100, 75,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(200,125,200)},
                {new RGB(150, 30,180),new RGB(150, 50,180),new RGB(100,120,180),new RGB(100,120,180),new RGB(100,120,180)},
                {new RGB(100, 75,100),new RGB(100, 80,100),new RGB(100, 85,100),new RGB(100, 95,100),new RGB(100,110,100)},
                {new RGB(200,100, 10),new RGB(200,100, 10),new RGB(200,100, 10),new RGB(210,200, 10),new RGB(255,  0, 10)}
        });

        Grid<RGB> g2 = new Grid<RGB>(new RGB[][]{
                {new RGB( 78, 209,  79), new RGB( 63, 118, 247), new RGB( 92, 175,  95), new RGB(243,  73, 183), new RGB(210, 109, 104), new RGB(252, 101, 119)},
                {new RGB(224, 191, 182), new RGB(108,  89,  82), new RGB( 80, 196, 230), new RGB(112, 156, 180), new RGB(176, 178, 120), new RGB(142, 151, 142)},
                {new RGB(117, 189, 149), new RGB(171 ,231, 153), new RGB(149, 164, 168), new RGB(107, 119,  71), new RGB(120, 105, 138), new RGB(163, 174, 196)},
                {new RGB(163, 222, 132), new RGB(187 ,117, 183), new RGB( 92, 145,  69), new RGB(158, 143,  79), new RGB(220,  75, 222), new RGB(189,  73, 214)},
                {new RGB(211, 120, 173), new RGB(188 ,218, 244), new RGB(214, 103,  68), new RGB(163, 166, 246), new RGB( 79, 125, 246), new RGB(211, 201,  98)}
        });

        Grid<RGB> g3 = new Grid<RGB>(new RGB[][]{
                {new RGB(  0, 100, 200), new RGB(  0,  80, 200), new RGB(  0, 100, 200)},
                {new RGB(100,  25, 200), new RGB(100,  15, 200), new RGB(100,  25, 200)},
                {new RGB(200,  95, 255), new RGB(200, 110, 255), new RGB(200, 100, 255)},
                {new RGB(200, 100, 255), new RGB(200,  95, 255), new RGB(200, 100, 255)},
                {new RGB(255,  70, 200), new RGB(255, 100, 200), new RGB(255, 100, 200)}
        });

        Grid<RGB> g4 = new Grid<RGB>(new RGB[][]{
                {new RGB(255, 101, 51), new RGB(255, 101, 153), new RGB(255, 101, 255)},
                {new RGB(255, 153, 51), new RGB(255, 153, 153), new RGB(255, 153, 255)},
                {new RGB(255, 203, 51), new RGB(255, 204, 153), new RGB(255, 205, 255)},
                {new RGB(255, 255, 51), new RGB(255, 255, 153), new RGB(255, 255, 255)}
        });

        // position new RGB(1,2) is interesting I guess.
        Grid<RGB> g5 = new Grid<RGB>(new RGB[][]{
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(10,20,30),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(2,3,4),new RGB( 1, 1, 1),new RGB(5,6,7),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(60,50,40),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(0,0,0),new RGB( 0, 0, 0),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0)}
        });

        // the new RGB(1,1,1) nodes comprise the best vertical path to remove.
        Grid<RGB> g6 = new Grid<RGB>(new RGB[][]{
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0),new RGB(0,0,0)}
        });

        // the new RGB(1,1,1) nodes comprise the best horizontal path to remove.
       Grid<RGB> g7 = new Grid<RGB>(new RGB[][]{
                {new RGB(1,1,1),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(1,1,1),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(1,1,1)}
        });

        Grid<Integer> g = new Grid<Integer>(new Integer[][]{
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12}});

        Grid<Integer> ans = new Grid<Integer>(new Integer[][]{
                {57685, 50893, 91370, 25418, 33055, 37246},
                {15421, 56334, 22808, 54796, 11641, 25496},
                {12344, 19236, 52030, 17708, 44735, 20663},
                {17074, 23678, 30279, 80663, 37831, 45595},
                {32337, 30796, 4909, 73334, 40613, 36556}});

        P1 another = new P1();
        Node again = new Node();
        Grid<Node> gn = again.setNodeGrid(ans);
        gn = again.setBestHop(gn);
        another.findVerticalPath(g6);
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
        System.out.println("Path: " + optList);
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
        System.out.println("Trans: " + energies);

        //Create node and setNodeGrid with node path
        Node n1 = new Node();
        Grid<Node> gn = n1.setNodeGrid(energies);
        System.out.println("Pre Node Grid: " + gn);
        gn = n1.setBestHop(gn);
        System.out.println("Node Grid: " + gn);
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
        System.out.println("Path: " + optList);
        return optList;
    }

    //Remove each location of the path from the grid
    //Return a reference to the grid even though it was modified in place
    public static Grid<RGB> removeVerticalPath(Grid<RGB> grid, List<Point> path)
    {
        //Assuming list goes from (0,0), (0,1), (0,2) ... in that order
        //This means we can work from back of the list to the front because each removal changes indexing (ArrayList)
        System.out.println("Grid: " + grid);
        System.out.println("length: " + path.size());
        System.out.println("Path: " + path);
        int count = path.size()-1;
        while(count != -1){
            int xVal = path.get(count).getX();
            int yVal = path.get(count).getY();
            grid.remove(xVal,yVal);
            count--;
        }
        System.out.println("Grid Height: " + grid.height());
        System.out.println("Grid Width:  " + grid.width());
        return grid;
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