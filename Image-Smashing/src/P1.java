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

        System.out.println(g);
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

    //Generate a grid with
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

    public static List<Point> findVerticalPath(Grid<RGB> grid)
    {

        return null;
    }

    public static List<Point> findHorizontalPath(Grid<RGB> grid)
    {

        return null;
    }

    public static Grid<RGB> removeVerticalPath(Grid<RGB> grid, List<Point> path)
    {

        return null;
    }

    public static Grid<RGB> removeHorizontalPath(Grid<RGB> grid, List<Point> path)
    {

        return null;
    }

    public static Grid<RGB> ppm2grid(String filename)
    {

        return null;
    }

    public static void grid2ppm(Grid<RGB> grid, String filename)
    {


    }
}