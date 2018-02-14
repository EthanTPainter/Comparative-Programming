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
        System.out.println(g.transpose());

    }

    public static int energyAt(Grid<RGB> grid, int r, int c)
    {

        return 0;
    }

    public static Grid<Integer> energy(Grid<RGB> grid)
    {

        return null;
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