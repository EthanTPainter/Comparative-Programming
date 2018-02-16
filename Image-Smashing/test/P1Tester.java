/** Example of using unit tests for this assignment.  This is
 * partially how your code will be graded.  Later in the class we will
 * write our own unit tests.  To run them on the command line, make
 * sure that the junit-4.12.jar is in the same directory.
 *
 * This file just finds and collects all the tests from all the listed
 * classes below for an overall test suite.
 *
 *
 * (demo$ is the prompt for the terminal; don't type it in)
 *
 *  demo$ javac -cp .:junit-4.12.jar *.java  # compile everything
 *  demo$ java -cp .:junit-4.12.jar P1Tests  # run ALL tests
 *
 * On windows replace colons with semicolons: (: with ;)
 *  demo$ javac -cp .;junit-4.12.jar *.java  # compile everything
 *  demo$ java -cp .;junit-4.12.jar P1Tests  # run tests
 */

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

public class P1Tester {
    public static void main(String args[]){
        org.junit.runner.JUnitCore.main("P1Tester");
    }


    // ------------------------------------------------------------------------------------------------
  /* Helper functions for testing. */

    public String readFile(String filename){
        try {
            return new Scanner(new FileInputStream(filename), "UTF-8").useDelimiter("\\A").next();
        }
        catch (FileNotFoundException e){
            throw new RuntimeException(String.format("couldn't read from that file(\"%s\").",filename));
        }
    }

    public void writeFile(String content, String filename){
        try {
            PrintWriter pw = new PrintWriter(filename);
            pw.write(content);
            pw.close();
        }
        catch (FileNotFoundException e){
            throw new RuntimeException(String.format("couldn't write that string to that file(\"%s\").",filename));
        }
    }


    // ------------------------------------------------------------------------------------------------
  /* Sample grids for testing. */

    public static Grid<RGB> p1(){
        return new Grid<RGB>( new RGB[][]{
                {new RGB(100, 75,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(200,125,200)},
                {new RGB(150, 30,180),new RGB(150, 50,180),new RGB(100,120,180),new RGB(100,120,180),new RGB(100,120,180)},
                {new RGB(100, 75,100),new RGB(100, 80,100),new RGB(100, 85,100),new RGB(100, 95,100),new RGB(100,110,100)},
                {new RGB(200,100, 10),new RGB(200,100, 10),new RGB(200,100, 10),new RGB(210,200, 10),new RGB(255,  0, 10)}
        });
    }

    public static Grid<RGB> p2() {
        return new Grid<RGB>( new RGB[][]{
                {new RGB( 78, 209,  79), new RGB( 63, 118, 247), new RGB( 92, 175,  95), new RGB(243,  73, 183), new RGB(210, 109, 104), new RGB(252, 101, 119)},
                {new RGB(224, 191, 182), new RGB(108,  89,  82), new RGB( 80, 196, 230), new RGB(112, 156, 180), new RGB(176, 178, 120), new RGB(142, 151, 142)},
                {new RGB(117, 189, 149), new RGB(171 ,231, 153), new RGB(149, 164, 168), new RGB(107, 119,  71), new RGB(120, 105, 138), new RGB(163, 174, 196)},
                {new RGB(163, 222, 132), new RGB(187 ,117, 183), new RGB( 92, 145,  69), new RGB(158, 143,  79), new RGB(220,  75, 222), new RGB(189,  73, 214)},
                {new RGB(211, 120, 173), new RGB(188 ,218, 244), new RGB(214, 103,  68), new RGB(163, 166, 246), new RGB( 79, 125, 246), new RGB(211, 201,  98)}
        });
    }


    public static Grid<RGB> p3() {
        return new Grid<RGB>(new RGB[][]{
                {new RGB(  0, 100, 200), new RGB(  0,  80, 200), new RGB(  0, 100, 200)},
                {new RGB(100,  25, 200), new RGB(100,  15, 200), new RGB(100,  25, 200)},
                {new RGB(200,  95, 255), new RGB(200, 110, 255), new RGB(200, 100, 255)},
                {new RGB(200, 100, 255), new RGB(200,  95, 255), new RGB(200, 100, 255)},
                {new RGB(255,  70, 200), new RGB(255, 100, 200), new RGB(255, 100, 200)}
        });
    }


    public static Grid<RGB> p4() {
        return new Grid<RGB>(new RGB[][]{
                {new RGB(255, 101, 51), new RGB(255, 101, 153), new RGB(255, 101, 255)},
                {new RGB(255, 153, 51), new RGB(255, 153, 153), new RGB(255, 153, 255)},
                {new RGB(255, 203, 51), new RGB(255, 204, 153), new RGB(255, 205, 255)},
                {new RGB(255, 255, 51), new RGB(255, 255, 153), new RGB(255, 255, 255)}
        });
    }



    public static Grid<RGB> g1(){
        return new Grid<RGB>(new RGB[][]{
                {new RGB(100, 75,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(100,100,200),new RGB(200,125,200)},
                {new RGB(150, 30,180),new RGB(150, 50,180),new RGB(100,120,180),new RGB(100,120,180),new RGB(100,120,180)},
                {new RGB(100, 75,100),new RGB(100, 80,100),new RGB(100, 85,100),new RGB(100, 95,100),new RGB(100,110,100)},
                {new RGB(200,100, 10),new RGB(200,100, 10),new RGB(200,100, 10),new RGB(210,200, 10),new RGB(255,  0, 10)}
        });
    }

    public static Grid<RGB> g2(){
        return new Grid<RGB>(new RGB[][]{
                {new RGB( 78, 209,  79), new RGB( 63, 118, 247), new RGB( 92, 175,  95), new RGB(243,  73, 183), new RGB(210, 109, 104), new RGB(252, 101, 119)},
                {new RGB(224, 191, 182), new RGB(108,  89,  82), new RGB( 80, 196, 230), new RGB(112, 156, 180), new RGB(176, 178, 120), new RGB(142, 151, 142)},
                {new RGB(117, 189, 149), new RGB(171 ,231, 153), new RGB(149, 164, 168), new RGB(107, 119,  71), new RGB(120, 105, 138), new RGB(163, 174, 196)},
                {new RGB(163, 222, 132), new RGB(187 ,117, 183), new RGB( 92, 145,  69), new RGB(158, 143,  79), new RGB(220,  75, 222), new RGB(189,  73, 214)},
                {new RGB(211, 120, 173), new RGB(188 ,218, 244), new RGB(214, 103,  68), new RGB(163, 166, 246), new RGB( 79, 125, 246), new RGB(211, 201,  98)}
        });
    }

    public static Grid<RGB> g3(){
        return new Grid<RGB>(new RGB[][]{
                {new RGB(  0, 100, 200), new RGB(  0,  80, 200), new RGB(  0, 100, 200)},
                {new RGB(100,  25, 200), new RGB(100,  15, 200), new RGB(100,  25, 200)},
                {new RGB(200,  95, 255), new RGB(200, 110, 255), new RGB(200, 100, 255)},
                {new RGB(200, 100, 255), new RGB(200,  95, 255), new RGB(200, 100, 255)},
                {new RGB(255,  70, 200), new RGB(255, 100, 200), new RGB(255, 100, 200)}
        });
    }

    public static Grid<RGB> g4(){
        return new Grid<RGB>(new RGB[][]{
                {new RGB(255, 101, 51), new RGB(255, 101, 153), new RGB(255, 101, 255)},
                {new RGB(255, 153, 51), new RGB(255, 153, 153), new RGB(255, 153, 255)},
                {new RGB(255, 203, 51), new RGB(255, 204, 153), new RGB(255, 205, 255)},
                {new RGB(255, 255, 51), new RGB(255, 255, 153), new RGB(255, 255, 255)}
        });
    }
    // position new RGB(1,2) is interesting I guess.
    public static Grid<RGB> g5(){
        return new Grid<RGB>(new RGB[][]{
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(10,20,30),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(2,3,4),new RGB( 1, 1, 1),new RGB(5,6,7),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(60,50,40),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(0,0,0),new RGB( 0, 0, 0),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0)}
        });
    }
    // the new RGB(1,1,1) nodes comprise the best vertical path to remove.
    public static Grid<RGB> g6(){
        return new Grid<RGB>(new RGB[][]{
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0),new RGB(0,0,0)}
        });
    }
    // the new RGB(1,1,1) nodes comprise the best horizontal path to remove.
    public static Grid<RGB> g7(){
        return new Grid<RGB>(new RGB[][]{
                {new RGB(1,1,1),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0),new RGB(0,0,0)},
                {new RGB(1,1,1),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0),new RGB(1,1,1),new RGB(0,0,0)},
                {new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0),new RGB(1,1,1)}
        });
    }

    // ------------------------------------------------------------------------------------------------
  /* Point tests. */

    @Test public void test_Point_1() {
        Point p = new Point(3,4);
        assertEquals(3,p.x);
        assertEquals(4,p.y);
        assertNotEquals(4, p.x);
        assertNotEquals(3, p.y);
    }

    // invert checks
    @Test public void test_Point_2() {
        Point p1 = new Point(3,4);
        assertEquals(3,p1.x);
        assertEquals(4,p1.y);
        // creating an inverted version has opposite values
        Point p2 = p1.invert();
        assertEquals(3,p1.x);
        assertEquals(4,p1.y);
        // ... but the original point wasn't affected.
        assertEquals(4, p2.x);
        assertEquals(3, p2.y);
        // only when we re-assign the output does it get changed.
        p2 = p2.invert();
        assertEquals(3, p2.x);
        assertEquals(4, p2.y);
    }

    // equality checks
    @Test public void test_Point_3() {
        Point p1a = new Point(3,4);
        Point p1b = new Point(3,4);
        Point p2 = new Point(5,6);
        assertEquals(p1a,p1b);
        assertEquals(p1b,p1a);
        assertNotEquals(p1a,p2);
        assertNotEquals(p2,p1a);
        // we don't just compare strings as a lazy shortcut.
        assertNotEquals(p2,String.format("(%s,%s)",p2.x,p2.y));
    }

    // toString checks
    @Test public void test_Point_4() {
        Point p1 = new Point(8,9);
        Point p2 = new Point(100,99);
        assertEquals("(8,9)",p1.toString());
        assertEquals("(100,99)",p2.toString());
    }

    // ------------------------------------------------------------------------------------------------
  /* RGB tests. */

    // constructor checks
    @Test public void test_rgb_1() {
        RGB v = new RGB(1,2,3);
        assertEquals(1,v.r);
        assertEquals(2,v.g);
        assertEquals(3,v.b);
        v.r=4;
        v.g=5;
        v.b=6;
        assertEquals(4,v.r);
        assertEquals(5,v.g);
        assertEquals(6,v.b);
    }

    // equality checks
    @Test public void test_rgb_2() {
        RGB v1a = new RGB(1,2,3);
        RGB v1b = new RGB(1,2,3);
        RGB v2  = new RGB(255,255,255);

        assertEquals(v1a,v1b);
        assertEquals(v1b,v1a);
        assertNotEquals(v1a,v2);
        assertNotEquals(v1b,v2);
        assertNotEquals(v2,v1a);

        assertNotEquals(v2,String.format("(%s,%s,%s)",v2.r,v2.g,v2.b));
    }

    // ------------------------------------------------------------------------------------------------
  /* Grid tests. */

    @Test public void test_grid_1() { assertEquals(4,p1().height());}
    @Test public void test_grid_2() { assertEquals(5,p1().width());}

    @Test public void test_grid_3() {
        assertEquals(5,p2().height());
        assertEquals(5,p3().height());
        assertEquals(4,p4().height());
        assertEquals(4,g1().height());
        assertEquals(5,g2().height());
        assertEquals(5,g3().height());
        assertEquals(4,g4().height());
        assertEquals(4,g5().height());
        assertEquals(4,g6().height());
        assertEquals(4,g7().height());
    }
    @Test public void test_grid_4() {
        assertEquals(6,p2().width());
        assertEquals(3,p3().width());
        assertEquals(3,p4().width());
        assertEquals(5,g1().width());
        assertEquals(6,g2().width());
        assertEquals(3,g3().width());
        assertEquals(3,g4().width());
        assertEquals(6,g5().width());
        assertEquals(6,g6().width());
        assertEquals(6,g7().width());
    }

    @Test public void test_grid_6() {
        Grid<RGB> g = new Grid<RGB>(2,5);
        for (int i=0;i<2;i++){
            for (int j=0;j<5;j++){
                assertEquals(g.values.get(i).get(j),null);
            }
        }
    }

    @Test public void test_grid_7() {
        Grid<RGB> g = new Grid<RGB>(10,8);
        for (int i=0;i<10;i++){
            for (int j=0;j<8;j++){
                assertEquals(g.values.get(i).get(j),null);
            }
        }
    }

    @Test public void test_grid_8() {
        Grid<Integer> g = new Grid<Integer>(new Integer[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12}});
        for (int i=0;i<3;i++){
            for (int j=0;j<4;j++){
                assertEquals((Integer)g.values.get(i).get(j),(Integer)(1+i*4+j));
            }
        }
    }

    @Test public void test_grid_9() {
        Grid<Integer> g = new Grid<Integer>(new Integer[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12}});
        for (int i=0;i<3;i++){
            for (int j=0;j<4;j++){
                assertEquals((Integer)g.get(i,j),(Integer)(1+i*4+j));
            }
        }
    }

    @Test public void test_grid_10() {
        Grid<Integer> g = new Grid<Integer>(new Integer[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12}});
        // reset everything
        for (int i=0;i<3;i++){
            for (int j=0;j<4;j++){
                g.set(i,j,255);
            }
        }
        // check everything
        for (int i=0;i<3;i++){
            for (int j=0;j<4;j++){
                assertEquals((Integer)255,g.get(i,j));
            }
        }
    }

    // some minimalistic removal testing - just remove one row, one item at a time (from right to left).
    @Test public void test_grid_11() {
        Grid<Integer> g = new Grid<Integer>(new Integer[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12}});
        assertEquals((Integer)12,g.remove(2,3));
        assertEquals((Integer)11,g.remove(2,2));
        assertEquals((Integer)10,g.remove(2,1));
        assertEquals((Integer) 9,g.remove(2,0));
        for (int i=0;i<2;i++){
            for (int j=0;j<4;j++){
                assertEquals((Integer)g.values.get(i).get(j),(Integer)(1+i*4+j));
            }
        }
    }

    // transpose testing.
    @Test public void test_grid_12() {
        Grid<Integer> g = new Grid<Integer>(new Integer[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12}});
        Grid<Integer> g2 = g.transpose();
        // g should be unchanged.
        for (int i=0;i<3;i++){
            for (int j=0;j<4;j++){
                assertEquals((Integer)g.values.get(i).get(j),(Integer)(1+i*4+j));
            }
        }

        // g2 should be the transposed version.
        for (int i=0;i<4;i++){
            for (int j=0;j<3;j++){
                assertEquals((Integer)g2.values.get(i).get(j),(Integer)(1+j*4+i));
            }
        }
    }

    // toString
    @Test public void test_grid_13() {
        assertEquals(p1().toString(),p1().values.toString());
        assertEquals(p2().toString(),p2().values.toString());
        assertEquals(p3().toString(),p3().values.toString());
        assertEquals(p4().toString(),p4().values.toString());
        assertEquals(g1().toString(),g1().values.toString());
        assertEquals(g2().toString(),g2().values.toString());
        assertEquals(g3().toString(),g3().values.toString());
        assertEquals(g4().toString(),g4().values.toString());
        assertEquals(g5().toString(),g5().values.toString());
        assertEquals(g6().toString(),g6().values.toString());
        assertEquals(g7().toString(),g7().values.toString());
    }

    // equals
    @Test public void test_grid_14() {
        assertEquals(p1(),p1());
        assertEquals(p2(),p2());
        assertEquals(p3(),p3());
        assertEquals(p4(),p4());
        assertEquals(g1(),g1());
        assertEquals(g2(),g2());
        assertEquals(g3(),g3());
        assertEquals(g4(),g4());
        assertEquals(g5(),g5());
        assertEquals(g6(),g6());
        assertEquals(g7(),g7());
        assertNotEquals(g1(),p3());
        assertNotEquals(g7(),p2());
        assertNotEquals(g3(),p4());
        assertNotEquals(g3(),p1());
    }

    // ------------------------------------------------------------------------------------------------
  /* Node tests. */


    /*
     * Implied grid has these weights:
     25  35  45   5
     50  50  50  10
     10  30  50  70
     50  25  75 100
    */
    @Test public void test_node_1() {
        Node n30 = new Node(3,0,null, 50);
        Node n31 = new Node(3,1,null, 25);
        Node n32 = new Node(3,2,null, 75);
        Node n33 = new Node(3,3,null,100);

        Node n20 = new Node(2,0,n31, 35);
        Node n21 = new Node(2,1,n31, 55);
        Node n22 = new Node(2,2,n31, 75);
        Node n23 = new Node(2,3,n32,145);

        Node n10 = new Node(1,0,n20, 85);
        Node n11 = new Node(1,1,n20, 85);
        Node n12 = new Node(1,2,n21,105);
        Node n13 = new Node(1,3,n22, 85);


        Node n00 = new Node(0,0,n10,110);
        Node n01 = new Node(0,1,n10,120);
        Node n02 = new Node(0,2,n11,130);
        Node n03 = new Node(0,3,n13, 90);

        // arbitrary sampling of nodes.
        assertEquals(3,n30.p.x);
        assertEquals(0,n30.p.y);
        assertEquals(null,n30.bestHop);
        assertEquals(50,n30.cost);

        assertEquals(2,n21.p.x);
        assertEquals(1,n21.p.y);
        assertEquals(n31,n21.bestHop);
        assertEquals(55,n21.cost);

        assertEquals(1,n13.p.x);
        assertEquals(3,n13.p.y);
        assertEquals(n22,n13.bestHop);
        assertEquals(85,n13.cost);

        assertEquals(0,n02.p.x);
        assertEquals(2,n02.p.y);
        assertEquals(n11,n02.bestHop);
        assertEquals(130,n02.cost);

        assertEquals(90,n03.cost);
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
  /* Overall-Program tests. */


    // ------------------------------------------------------------------------------------------------
  /* width of a few different grids. We generate some large ones for the last couple of tests. */

    //@Test public void test_width_() { assertEquals(expected,actual); } }
    @Test public void test_width_1() { assertEquals(5,g1().width()); }
    @Test public void test_width_2() { assertEquals(6,g2().width()); }
    @Test public void test_width_3() { assertEquals(3,g3().width()); }
    @Test public void test_width_4() { assertEquals(3,g4().width()); }
    @Test public void test_width_5() { assertEquals(1000,new Grid<Integer>(31,1000).width()); }
    @Test public void test_width_6() { assertEquals(  27,new Grid<Integer>(35,  27).width()); }

    // ------------------------------------------------------------------------------------------------

  /* height of a few different grids. We generate some large ones for the last couple of tests. */

    @Test public void test_height_1() { assertEquals(4,g1().height()); }
    @Test public void test_height_2() { assertEquals(5,g2().height()); }
    @Test public void test_height_3() { assertEquals(5,g3().height()); }
    @Test public void test_height_4() { assertEquals(4,g4().height()); }
    @Test public void test_height_5() { assertEquals(31,new Grid<Integer>( 31,1000).height()); }
    @Test public void test_height_6() { assertEquals(123,new Grid<Integer>(123,  27).height()); }

    // ------------------------------------------------------------------------------------------------

    /* energy_at: we consider corners, edges, and interior pieces. After looking at a couple of grids, not too much extra is worth testing for our assignment. */
    @Test public void test_energy_at_1 (){ assertEquals (46925, P1.energyAt(g1(),0,0));} // corner
    @Test public void test_energy_at_2 (){ assertEquals (67950, P1.energyAt(g1(),0,4));} // corner
    @Test public void test_energy_at_3 (){ assertEquals (23025, P1.energyAt(g1(),3,0));} // corner
    @Test public void test_energy_at_4 (){ assertEquals (30325, P1.energyAt(g1(),3,4));} // corner
    @Test public void test_energy_at_5 (){ assertEquals (17400, P1.energyAt(g1(),1,0));} // left edge
    @Test public void test_energy_at_6 (){ assertEquals (39300, P1.energyAt(g1(),0,2));} // top edge
    @Test public void test_energy_at_7 (){ assertEquals (67725, P1.energyAt(g1(),2,4));} // right edge
    @Test public void test_energy_at_8 (){ assertEquals (23050, P1.energyAt(g1(),3,3));} // bottom edge
    @Test public void test_energy_at_9 (){ assertEquals (21000, P1.energyAt(g1(),1,1));} // interior
    @Test public void test_energy_at_10(){ assertEquals (17625, P1.energyAt(g1(),1,2));} // interior
    @Test public void test_energy_at_11(){ assertEquals (48025, P1.energyAt(g1(),2,3));} // interior

    @Test public void test_energy_at_12(){ assertEquals (  29, P1.energyAt(g5(),1,0));}
    @Test public void test_energy_at_13(){ assertEquals (1429, P1.energyAt(g5(),0,1));}
    @Test public void test_energy_at_14(){ assertEquals (3527, P1.energyAt(g5(),1,2));}
    @Test public void test_energy_at_15(){ assertEquals (   3, P1.energyAt(g5(),2,2));}
    @Test public void test_energy_at_16(){ assertEquals (   0, P1.energyAt(g5(),2,4));}
    @Test public void test_energy_at_17(){ assertEquals (3500, P1.energyAt(g5(),3,2));}
    @Test public void test_energy_at_18(){ assertEquals (   0, P1.energyAt(g5(),3,3));}


    // ------------------------------------------------------------------------------------------------

  /* energy function is really just a combo usage of energy_at */

    @Test public void test_energy_1(){
        Grid<Integer> ans = new Grid<Integer>(new Integer[][]{
                {46925, 34525, 39300, 58025, 67950},
                {17400, 21000, 17625, 10025, 30825},
                {37200, 34000, 39525, 48025, 67725},
                {23025, 10400, 20325, 23050, 30325}});
        assertEquals (ans, P1.energy(g1()));
    }

    @Test public void test_energy_2(){
        Grid<Integer> ans = new Grid<Integer>(new Integer[][]{
                {57685, 50893, 91370, 25418, 33055, 37246},
                {15421, 56334, 22808, 54796, 11641, 25496},
                {12344, 19236, 52030, 17708, 44735, 20663},
                {17074, 23678, 30279, 80663, 37831, 45595},
                {32337, 30796, 4909, 73334, 40613, 36556}});
        assertEquals (ans, P1.energy(g2()));
    }

    @Test public void test_energy_3(){
        Grid<Integer> ans = new Grid<Integer>(new Integer[][]{
                {26450, 31250, 30050},
                {43150, 43925, 43125},
                {18750, 19450, 18875},
                {6700, 6150, 6075},
                {43025, 44150, 43925}});
        assertEquals (ans, P1.energy(g3()));
    }

    @Test public void test_energy_4(){
        Grid<Integer> ans = new Grid<Integer>(new Integer[][]{
                {20808, 52020, 20808},
                {20808, 52225, 21220},
                {20809, 52024, 20809},
                {20808, 52225, 21220}});
        assertEquals (ans, P1.energy(g4()));
    }

    @Test public void test_energy_5(){
        Grid<Integer> ans = new Grid<Integer>(new Integer[][]{
                {0, 1429, 3, 1510, 0, 0},
                {29, 3, 3527, 3, 110, 0},
                {0, 7729, 3, 7810, 0, 0},
                {0, 0, 3500, 0, 0, 0}});
        assertEquals (ans, P1.energy(g5()));
    }

    @Test public void test_energy_6(){
        Grid<Integer> ans = new Grid<Integer>(new Integer[][]{
                {0, 3, 0, 3, 0, 0},
                {0, 0, 6, 0, 6, 0},
                {0, 0, 0, 3, 0, 3},
                {0, 0, 6, 0, 6, 0}});
        assertEquals (ans, P1.energy(g6()));
    }

    // ------------------------------------------------------------------------------------------------

    @Test public void test_findVerticalPath_1(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(0, 1), new Point(1, 0), new Point(2, 1), new Point(3, 1)}));
        assertEquals (ans,P1.findVerticalPath(g1()));
    }

    @Test public void test_findVerticalPath_2(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(0, 3), new Point(1, 4), new Point(2, 3), new Point(3, 2), new Point(4, 2)}));
        assertEquals (ans,P1.findVerticalPath(g2()));
    }

    @Test public void test_findVerticalPath_3(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 1), new Point(4, 0)}));
        assertEquals (ans,P1.findVerticalPath(g3()));
    }

    @Test public void test_findVerticalPath_4(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0)}));
        assertEquals (ans,P1.findVerticalPath(g4()));
    }

    @Test public void test_findVerticalPath_5(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(0, 4), new Point(1, 5), new Point(2, 4), new Point(3, 3)}));
        assertEquals (ans,P1.findVerticalPath(g5()));
    }

    @Test public void test_findVerticalPath_6(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0)}));
        assertEquals (ans,P1.findVerticalPath(g6()));
    }

    @Test public void test_findVerticalPath_7(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(0, 0), new Point(1, 1), new Point(2, 0), new Point(3, 1)}));
        assertEquals (ans,P1.findVerticalPath(g7()));
    }

    // I want those seven cases to be worth two points each. Run 'em again.
    @Test public void test_findVerticalPath_1again(){ test_findVerticalPath_1();}
    @Test public void test_findVerticalPath_2again(){ test_findVerticalPath_2();}
    @Test public void test_findVerticalPath_3again(){ test_findVerticalPath_3();}
    @Test public void test_findVerticalPath_4again(){ test_findVerticalPath_4();}
    @Test public void test_findVerticalPath_5again(){ test_findVerticalPath_5();}
    @Test public void test_findVerticalPath_6again(){ test_findVerticalPath_6();}
    @Test public void test_findVerticalPath_7again(){ test_findVerticalPath_7();}

    //---------------------------------------------------------------------

    @Test public void test_findHorizontalPath_1(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3), new Point(1, 4)}));
        assertEquals (ans,P1.findHorizontalPath(g1()));
    }
    @Test public void test_findHorizontalPath_2(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(2, 0), new Point(2, 1), new Point(1, 2), new Point(2, 3), new Point(1, 4), new Point(2, 5)}));
        assertEquals (ans,P1.findHorizontalPath(g2()));
    }
    @Test public void test_findHorizontalPath_3(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(3, 0), new Point(3, 1), new Point(3, 2)}));
        assertEquals (ans,P1.findHorizontalPath(g3()));
    }
    @Test public void test_findHorizontalPath_4(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(0, 0), new Point(0, 1), new Point(0, 2)}));
        assertEquals (ans,P1.findHorizontalPath(g4()));
    }
    @Test public void test_findHorizontalPath_5(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(2, 0), new Point(3, 1), new Point(2, 2), new Point(3, 3), new Point(2, 4), new Point(1, 5)}));
        assertEquals (ans,P1.findHorizontalPath(g5()));
    }
    @Test public void test_findHorizontalPath_6(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(0, 0), new Point(1, 1), new Point(0, 2), new Point(1, 3), new Point(0, 4), new Point(0, 5)}));
        assertEquals (ans,P1.findHorizontalPath(g6()));
    }
    @Test public void test_findHorizontalPath_7(){
        List<Point> ans = new ArrayList<>(Arrays.asList(new Point[]{new Point(0, 0), new Point(1, 1), new Point(0, 2), new Point(1, 3), new Point(0, 4), new Point(1, 5)}));
        assertEquals (ans,P1.findHorizontalPath(g7()));
    }
    @Test public void test_findHorizontalPath_1again(){ test_findHorizontalPath_1();}
    @Test public void test_findHorizontalPath_2again(){ test_findHorizontalPath_2();}
    @Test public void test_findHorizontalPath_3again(){ test_findHorizontalPath_3();}
    @Test public void test_findHorizontalPath_4again(){ test_findHorizontalPath_4();}
    @Test public void test_findHorizontalPath_5again(){ test_findHorizontalPath_5();}

    // ------------------------------------------------------------------------------------------------


    @Test public void test_removeVerticalPath_1(){
        Grid<RGB> orig = g1();
        List<Point> path = new ArrayList<>(Arrays.asList(new Point[]{new Point(0,0),new Point(1,0),new Point(2,0),new Point(3,0)}));
        Grid<RGB> next = P1.removeVerticalPath(orig,path);
        // just check that they are returning a reference to the original.
        // we don't actually care in this test if they properly removed.
        assertEquals(orig, next);
    }



    // remove a manually created path (not the "best" path).
    @Test public void test_removeVerticalPath_2(){
        // an 8x4 grid of (1,1,1)'s.
        Grid<RGB> g = new Grid<RGB>(8,4);
        for (int i=0;i<g.height(); i++){
            for (int j=0;j<g.width(); j++){
                g.set(i,j,new RGB(9,9,9));
            }
        }
        // we'll consider these spots as the path,
        List<Point> path = new ArrayList<>(Arrays.asList(new Point[]{new Point(0,0),new Point(1,1),new Point(2,2),new Point(3,1),new Point(4,0),new Point(5,0),new Point(6,1),new Point(7,2)}));
        // so we put other values at those spots.
        for (Point p : path){
            g.set(p.x,p.y,new RGB(9,9,9));
        }
        // let's remove all the 9's
        P1.removeVerticalPath(g,path);
        // and then we expect a narrower grid.
        Grid<RGB> expected = new Grid<RGB>(8,3);
        for (int i=0;i<expected.height(); i++){
            for (int j=0;j<expected.width(); j++){
                expected.set(i,j,new RGB(9,9,9));
            }
        }

        assertEquals(expected, g);
    }

    // remove a manually created path (not the "best" path).
    @Test public void test_removeVerticalPath_3(){
        // an 8x4 grid of (1,1,1)'s.
        Grid<RGB> g = new Grid<RGB>(8,4);
        // we'll consider these spots as the path{ the far left edge
        List<Point> path = new ArrayList<>(Arrays.asList(new Point[]{new Point(0,0),new Point(1,0),new Point(2,0),new Point(3,0),new Point(4,0),new Point(5,0),new Point(6,0),new Point(7,0)}));
        // so we put other values at those spots.
        for (Point p : path){
            g.set(p.x,p.y,new RGB(9,9,9));
        }
        // let's remove all the 9's
        P1.removeVerticalPath(g,path);
        // and then we expect a narrower grid.
        Grid<RGB> expected = new Grid<RGB>(8,3);
        assertEquals(expected, g);
    }
    // remove a manually created path (not the "best" path).
    @Test public void test_removeVerticalPath_4(){
        // an 8x4 grid of (1,1,1)'s.
        Grid<RGB> g = new Grid<RGB>(8,4);
        // we'll consider these spots as the path{ wandering around
        List<Point> path = new ArrayList<>(Arrays.asList(new Point[]{new Point(0,2),new Point(1,1),new Point(2,2),new Point(3,3),new Point(4,3),new Point(5,2),new Point(6,1),new Point(7,2)}));
        // so we put other values at those spots.
        for (Point p : path){
            g.set(p.x,p.y,new RGB(9,9,9));
        }
        // let's remove all the 9's
        P1.removeVerticalPath(g,path);
        // and then we expect a narrower grid.
        Grid<RGB> expected = new Grid<RGB>(8,3);
        assertEquals(expected, g);
    }
    // test our given grids
    @Test public void test_removeVerticalPath_5(){
        Grid<RGB> g = g1();
        Grid<RGB> got = P1.removeVerticalPath(g,P1.findVerticalPath(g));
        Grid<RGB> expected = new Grid<>(new RGB[][]{{new RGB(100, 75, 200), new RGB(100, 100, 200), new RGB(100, 100, 200), new RGB(200, 125, 200)}, {new RGB(150, 50, 180), new RGB(100, 120, 180), new RGB(100, 120, 180), new RGB(100, 120, 180)}, {new RGB(100, 75, 100), new RGB(100, 85, 100), new RGB(100, 95, 100), new RGB(100, 110, 100)}, {new RGB(200, 100, 10), new RGB(200, 100, 10), new RGB(210, 200, 10), new RGB(255, 0, 10)}});
        assertEquals(expected, got);
    }
    @Test public void test_removeVerticalPath_6(){
        Grid<RGB> g = g2();
        Grid<RGB>got = P1.removeVerticalPath(g,P1.findVerticalPath(g));
        Grid<RGB> expected = new Grid<>(new RGB[][]{{new RGB(78, 209, 79), new RGB(63, 118, 247), new RGB(92, 175, 95), new RGB(210, 109, 104), new RGB(252, 101, 119)}, {new RGB(224, 191, 182), new RGB(108, 89, 82), new RGB(80, 196, 230), new RGB(112, 156, 180), new RGB(142, 151, 142)}, {new RGB(117, 189, 149), new RGB(171, 231, 153), new RGB(149, 164, 168), new RGB(120, 105, 138), new RGB(163, 174, 196)}, {new RGB(163, 222, 132), new RGB(187, 117, 183), new RGB(158, 143, 79), new RGB(220, 75, 222), new RGB(189, 73, 214)}, {new RGB(211, 120, 173), new RGB(188, 218, 244), new RGB(163, 166, 246), new RGB(79, 125, 246), new RGB(211, 201, 98)}});
        assertEquals(expected, got);
    }
    @Test public void test_removeVerticalPath_7(){
        Grid<RGB> g = g3();
        Grid<RGB>got = P1.removeVerticalPath(g,P1.findVerticalPath(g));
        Grid<RGB> expected = new Grid<>(new RGB[][]{{new RGB(0, 80, 200), new RGB(0, 100, 200)}, {new RGB(100, 15, 200), new RGB(100, 25, 200)}, {new RGB(200, 110, 255), new RGB(200, 100, 255)}, {new RGB(200, 100, 255), new RGB(200, 100, 255)}, {new RGB(255, 100, 200), new RGB(255, 100, 200)}});
        assertEquals(expected, got);
    }
    @Test public void test_removeVerticalPath_8(){
        Grid<RGB> g = g4();
        Grid<RGB>got = P1.removeVerticalPath(g,P1.findVerticalPath(g));
        Grid<RGB> expected = new Grid<>(new RGB[][]{{new RGB(255, 101, 153), new RGB(255, 101, 255)}, {new RGB(255, 153, 153), new RGB(255, 153, 255)}, {new RGB(255, 204, 153), new RGB(255, 205, 255)}, {new RGB(255, 255, 153), new RGB(255, 255, 255)}});
        assertEquals(expected, got);
    }

    // I want those eight cases to be worth two points each. Run 'em again.
    @Test public void test_removeVerticalPath_1again(){ test_removeVerticalPath_1();}
    @Test public void test_removeVerticalPath_2again(){ test_removeVerticalPath_2();}
    @Test public void test_removeVerticalPath_3again(){ test_removeVerticalPath_3();}
    @Test public void test_removeVerticalPath_4again(){ test_removeVerticalPath_4();}
    @Test public void test_removeVerticalPath_5again(){ test_removeVerticalPath_5();}
    @Test public void test_removeVerticalPath_6again(){ test_removeVerticalPath_6();}
    @Test public void test_removeVerticalPath_7again(){ test_removeVerticalPath_7();}
    @Test public void test_removeVerticalPath_8again(){ test_removeVerticalPath_8();}


    //---------------------------------------------------------------------

    // remove a manually created path (not the "best" path).
    @Test public void test_removeHorizontalPath_1(){
        // an 8x4 grid of (1,1,1)'s.
        Grid<RGB> g = new Grid<RGB>(4,7);
        // we'll consider these spots as the path{ top line
        List<Point> path = new ArrayList<>(Arrays.asList(new Point[]{new Point(0,0),new Point(0,1),new Point(0,2),new Point(0,3),new Point(0,4),new Point(0,5),new Point(0,6)}));
        // so we put other values at those spots.
        for (Point p : path){
            g.set(p.x,p.y,new RGB(9,9,9));
        }
        // let's remove all the 9's
        g = P1.removeHorizontalPath(g,path);
        // and then we expect a narrower grid.
        Grid<RGB> expected = new Grid<RGB>(3,7);
        assertEquals(expected, g);
    }
    // remove a manually created path (not the "best" path).
    @Test public void test_removeHorizontalPath_2(){
        // an 8x4 grid of (1,1,1)'s.
        Grid<RGB> g = new Grid<RGB>(4,7);
        // we'll consider these spots as the path{ bottom line
        List<Point> path = new ArrayList<>(Arrays.asList(new Point[]{new Point(3,0),new Point(3,1),new Point(3,2),new Point(3,3),new Point(3,4),new Point(3,5),new Point(3,6)}));
        // so we put other values at those spots.
        for (Point p : path){
            g.set(p.x,p.y,new RGB(9,9,9));
        }
        // let's remove all the 9's
        g = P1.removeHorizontalPath(g,path);
        // and then we expect a narrower grid.
        Grid<RGB> expected = new Grid<RGB>(3,7);
        assertEquals(expected, g);
    }
    // remove a manually created path (not the "best" path).
    @Test public void test_removeHorizontalPath_3(){
        // an 8x4 grid of (1,1,1)'s.
        Grid<RGB> g = new Grid<RGB>(4,7);
        // we'll consider these spots as the path{ toggling rows
        List<Point> path = new ArrayList<>(Arrays.asList(new Point[]{new Point(0,0),new Point(1,1),new Point(0,2),new Point(1,3),new Point(1,4),new Point(0,5),new Point(1,6)}));
        // so we put other values at those spots.
        for (Point p : path){
            g.set(p.x,p.y,new RGB(9,9,9));
        }
        // let's remove all the 9's
        g = P1.removeHorizontalPath(g,path);
        // and then we expect a narrower grid.
        Grid<RGB>expected = new Grid<RGB>(3,7);
        assertEquals(expected, g);
    }
    // remove a manually created path (not the "best" path).
    @Test public void test_removeHorizontalPath_4(){
        // an 8x4 grid of (1,1,1)'s.
        Grid<RGB> g = new Grid<RGB>(4,7);
        // we'll consider these spots as the path{ toggling interior
        List<Point> path = new ArrayList<>(Arrays.asList(new Point[]{new Point(1,0),new Point(2,1),new Point(1,2),new Point(2,3),new Point(1,4),new Point(2,5),new Point(1,6)}));
        // so we put other values at those spots.
        for (Point p : path){
            g.set(p.x,p.y,new RGB(9,9,9));
        }
        // let's remove all the 9's
        g = P1.removeHorizontalPath(g,path);
        // and then we expect a narrower grid.
        Grid<RGB>expected = new Grid<RGB>(3,7);
        assertEquals(expected, g);
    }
    // remove a manually created path (not the "best" path).
    @Test public void test_removeHorizontalPath_5(){
        // an 8x4 grid of (1,1,1)'s.
        Grid<RGB> g = new Grid<RGB>(7,7);
        // we'll consider these spots as the path{ all in unique rows
        List<Point> path = new ArrayList<>(Arrays.asList(new Point[]{new Point(0,0),new Point(1,1),new Point(2,2),new Point(3,3),new Point(4,4),new Point(5,5),new Point(6,6)}));
        // so we put other values at those spots.
        for (Point p : path){
            g.set(p.x,p.y,new RGB(9,9,9));
        }
        // let's remove all the 9's
        g = P1.removeHorizontalPath(g,path);
        // and then we expect a narrower grid.
        Grid<RGB>expected = new Grid<RGB>(6,7);
        assertEquals(expected, g);
    }
    // test our given grids
    @Test public void test_removeHorizontalPath_6(){
        Grid<RGB> g = g1();
        Grid<RGB>got = P1.removeHorizontalPath(g,P1.findHorizontalPath(g));
        Grid<RGB> expected = new Grid<>(new RGB[][]{{new RGB(100, 75, 200), new RGB(100, 100, 200), new RGB(100, 100, 200), new RGB(100, 100, 200), new RGB(200, 125, 200)}, {new RGB(100, 75, 100), new RGB(100, 80, 100), new RGB(100, 85, 100), new RGB(100, 95, 100), new RGB(100, 110, 100)}, {new RGB(200, 100, 10), new RGB(200, 100, 10), new RGB(200, 100, 10), new RGB(210, 200, 10), new RGB(255, 0, 10)}});
        assertEquals(expected, got);
    }
    // test our given grids
    @Test public void test_removeHorizontalPath_7(){;
        Grid<RGB> g = g2();
        Grid<RGB>got = P1.removeHorizontalPath(g,P1.findHorizontalPath(g));
        Grid<RGB> expected = new Grid<>(new RGB[][]{{new RGB(78, 209, 79), new RGB(63, 118, 247), new RGB(92, 175, 95), new RGB(243, 73, 183), new RGB(210, 109, 104), new RGB(252, 101, 119)}, {new RGB(224, 191, 182), new RGB(108, 89, 82), new RGB(149, 164, 168), new RGB(112, 156, 180), new RGB(120, 105, 138), new RGB(142, 151, 142)}, {new RGB(163, 222, 132), new RGB(187, 117, 183), new RGB(92, 145, 69), new RGB(158, 143, 79), new RGB(220, 75, 222), new RGB(189, 73, 214)}, {new RGB(211, 120, 173), new RGB(188, 218, 244), new RGB(214, 103, 68), new RGB(163, 166, 246), new RGB(79, 125, 246), new RGB(211, 201, 98)}});
        assertEquals(expected, got);
    }
    // test our given grids
    @Test public void test_removeHorizontalPath_8(){
        Grid<RGB> g = g3();
        Grid<RGB>got = P1.removeHorizontalPath(g,P1.findHorizontalPath(g));
        Grid<RGB> expected = new Grid<>(new RGB[][]{{new RGB(0, 100, 200), new RGB(0, 80, 200), new RGB(0, 100, 200)}, {new RGB(100, 25, 200), new RGB(100, 15, 200), new RGB(100, 25, 200)}, {new RGB(200, 95, 255), new RGB(200, 110, 255), new RGB(200, 100, 255)}, {new RGB(255, 70, 200), new RGB(255, 100, 200), new RGB(255, 100, 200)}});
        assertEquals(expected, got);
    }

    // test our given grids
    @Test public void test_removeHorizontalPath_9(){
        Grid<RGB> g = g4();
        Grid<RGB> got = P1.removeHorizontalPath(g,P1.findHorizontalPath(g));
        Grid<RGB> expected = new Grid<>(new RGB[][]{{new RGB(255, 153, 51), new RGB(255, 153, 153), new RGB(255, 153, 255)}, {new RGB(255, 203, 51), new RGB(255, 204, 153), new RGB(255, 205, 255)}, {new RGB(255, 255, 51), new RGB(255, 255, 153), new RGB(255, 255, 255)}});
        assertEquals(expected, got);
    }
    @Test public void test_removeHorizontalPath_1again(){ test_removeHorizontalPath_1();}
    @Test public void test_removeHorizontalPath_6again(){ test_removeHorizontalPath_6();}
    @Test public void test_removeHorizontalPath_7again(){ test_removeHorizontalPath_7();}
    @Test public void test_removeHorizontalPath_8again(){ test_removeHorizontalPath_8();}
    @Test public void test_removeHorizontalPath_9again(){ test_removeHorizontalPath_9();}



    // ------------------------------------------------------------------------------------------------

    @Test public void test_grid2ppm_1() throws FileNotFoundException{
        String filename = ".temp_file.ppm";
        P1.grid2ppm(g1(),filename);
        String s = readFile(filename);
        String[] tokens = s.split("\\s+");
        String[] expected = {"P3", "5", "4", "255", "100", "75", "200", "100", "100", "200", "100", "100", "200", "100", "100", "200", "200", "125", "200", "150", "30", "180", "150", "50", "180", "100", "120", "180", "100", "120", "180", "100", "120", "180", "100", "75", "100", "100", "80", "100", "100", "85", "100", "100", "95", "100", "100", "110", "100", "200", "100", "10", "200", "100", "10", "200", "100", "10", "210", "200", "10", "255", "0", "10"};
        assertArrayEquals(String.format("comparing \"%s\" and \"%s\".",expected,tokens),expected, tokens);
    }
    // ------------------------------------------------------------------------------------------------




    @Test public void test_grid2ppm_2() throws FileNotFoundException{
        String filename = ".temp_file.ppm";
        P1.grid2ppm(g2(),filename);
        String s = readFile(filename);
        String[] tokens = s.split("\\s+");
        String[] expected = {"P3", "6", "5", "255", "78", "209", "79", "63", "118", "247", "92", "175", "95", "243", "73", "183", "210", "109", "104", "252", "101", "119", "224", "191", "182", "108", "89", "82", "80", "196", "230", "112", "156", "180", "176", "178", "120", "142", "151", "142", "117", "189", "149", "171", "231", "153", "149", "164", "168", "107", "119", "71", "120", "105", "138", "163", "174", "196", "163", "222", "132", "187", "117", "183", "92", "145", "69", "158", "143", "79", "220", "75", "222", "189", "73", "214", "211", "120", "173", "188", "218", "244", "214", "103", "68", "163", "166", "246", "79", "125", "246", "211", "201", "98"};
        assertArrayEquals(expected, tokens);
    }

    @Test public void test_grid2ppm_3()  throws FileNotFoundException{
        String filename = ".temp_file.ppm";
        P1.grid2ppm(g3(),filename);
        String s = readFile(filename);
        String[]tokens = s.split("\\s+");
        String[] expected = {"P3", "3", "5", "255", "0", "100", "200", "0", "80", "200", "0", "100", "200", "100", "25", "200", "100", "15", "200", "100", "25", "200", "200", "95", "255", "200", "110", "255", "200", "100", "255", "200", "100", "255", "200", "95", "255", "200", "100", "255", "255", "70", "200", "255", "100", "200", "255", "100", "200"};
        assertArrayEquals(expected, tokens);
    }

    @Test public void test_grid2ppm_4() throws FileNotFoundException{
        String filename = ".temp_file.ppm";
        P1.grid2ppm(g4(),filename);
        String s = readFile(filename);
        String[] tokens = s.split("\\s+");
        String[] expected = {"P3", "3", "4", "255", "255", "101", "51", "255", "101", "153", "255", "101", "255", "255", "153", "51", "255", "153", "153", "255", "153", "255", "255", "203", "51", "255", "204", "153", "255", "205", "255", "255", "255", "51", "255", "255", "153", "255", "255", "255"};
        assertArrayEquals(expected, tokens);
    }
    //---------------------------------------------------------------------

    @Test public void test_ppm2grid_1() throws FileNotFoundException{
        // we want to have an existing ppm file, and read it to a grid,
        // lastly checking that the grid we got matches what is expected.

        String filename = ".temp_file.ppm";
        Grid<RGB> expected = g1();

        // put a ppm file's contents into a string.
        String s = "P3\n5\n4\n255\n100\n75\n200\n100\n100\n200\n100\n100\n200\n100\n100\n200\n200\n125\n200\n150\n30\n180\n150\n50\n180\n100\n120\n180\n100\n120\n180\n100\n120\n180\n100\n75\n100\n100\n80\n100\n100\n85\n100\n100\n95\n100\n100\n110\n100\n200\n100\n10\n200\n100\n10\n200\n100\n10\n210\n200\n10\n255\n0\n10\n";

        // put that string into a file.
        writeFile(s,filename);

        // call student's code.
        Grid<RGB> got = P1.ppm2grid(filename);

        // check that they read it successfully
        assertEquals(expected, got);
    }

    @Test public void test_ppm2grid_2() throws FileNotFoundException {
        // we want to have an existing ppm file, and read it to a grid,
        // lastly checking that the grid we got matches what is expected.

        String filename = ".temp_file.ppm";
        Grid<RGB> expected = g2();

        // put a ppm file's contents into a string.
        String s = "P3\n6\n5\n255\n78\n209\n79\n63\n118\n247\n92\n175\n95\n243\n73\n183\n210\n109\n104\n252\n101\n119\n224\n191\n182\n108\n89\n82\n80\n196\n230\n112\n156\n180\n176\n178\n120\n142\n151\n142\n117\n189\n149\n171\n231\n153\n149\n164\n168\n107\n119\n71\n120\n105\n138\n163\n174\n196\n163\n222\n132\n187\n117\n183\n92\n145\n69\n158\n143\n79\n220\n75\n222\n189\n73\n214\n211\n120\n173\n188\n218\n244\n214\n103\n68\n163\n166\n246\n79\n125\n246\n211\n201\n98\n";

        // put that string into a file.
        writeFile(s,filename);

        // call student's code.
        Grid<RGB> got = P1.ppm2grid(filename);

        // check that they read it successfully
        assertEquals(expected, got);
    }

    @Test public void test_ppm2grid_3() throws FileNotFoundException {
        // we want to have an existing ppm file, and read it to a grid,
        // lastly checking that the grid we got matches what is expected.

        String filename = ".temp_file.ppm";
        Grid<RGB> expected = g3();

        // put a ppm file's contents into a string.
        String s = "P3\n3\n5\n255\n0\n100\n200\n0\n80\n200\n0\n100\n200\n100\n25\n200\n100\n15\n200\n100\n25\n200\n200\n95\n255\n200\n110\n255\n200\n100\n255\n200\n100\n255\n200\n95\n255\n200\n100\n255\n255\n70\n200\n255\n100\n200\n255\n100\n200\n";

        // put that string into a file.
        writeFile(s,filename);

        // call student's code.
        Grid<RGB> got = P1.ppm2grid(filename);

        // check that they read it successfully
        assertEquals(expected, got);
    }

    @Test public void test_ppm2grid_4() throws FileNotFoundException {
        // we want to have an existing ppm file, and read it to a grid,
        // lastly checking that the grid we got matches what is expected.

        String filename = ".temp_file.ppm";
        Grid<RGB> expected = g4();

        // put a ppm file's contents into a string.
        String s = "P3\n3\n4\n255\n255\n101\n51\n255\n101\n153\n255\n101\n255\n255\n153\n51\n255\n153\n153\n255\n153\n255\n255\n203\n51\n255\n204\n153\n255\n205\n255\n255\n255\n51\n255\n255\n153\n255\n255\n255\n";


        // put that string into a file.
        writeFile(s,filename);

        // call student's code.
        Grid<RGB> got = P1.ppm2grid(filename);

        // check that they read it successfully
        assertEquals(expected, got);
    }
}