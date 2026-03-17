package logic;


import java.util.PriorityQueue;
import java.util.Stack;


public class AEstrella {

    private int[][] mapa;
//Java Program to implement A* Search Algorithm
   //Here we're creating a shortcut for (int, int) pair

    public AEstrella(int[][] mapa){
        this.mapa = new int[mapa.length][mapa[0].length];
        for (int r = 0; r < mapa.length; r++)
            this.mapa[r] = mapa[r].clone();
    }

   public static class Pair {
       int first;
       int second;
       public Pair(int first, int second){
           this.first = first;
           this.second = second;
       }

       public int getFirst(){
            return first;
       }
       public int getSecond(){
            return second;
       }

       @Override
       public boolean equals(Object obj) {
           return obj instanceof Pair && this.first == ((Pair)obj).first && this.second == ((Pair)obj).second;
       }
   }
    public static class Ret {

        double p1;
        Pair[] p2;
        public Ret(double first, Pair[] second){
           this.p1 = first;
           this.p2 = second;
       }

    }
    
   

   // Creating a shortcut for tuple<int, int, int> type
   public static class Details {
       double value;
       int i;
       int j;

       public Details(double value, int i, int j) {
           this.value = value;
           this.i = i;
           this.j = j;
       }
   }

   // a Cell (node) structure
   public static class Cell {
       public Pair parent;
       // f = g + h, where h is heuristic
       public double f, g, h;
       Cell()
       {
           parent = new Pair(-1, -1);
           f = -1;
           g = -1;
           h = -1;
       }

       public Cell(Pair parent, double f, double g, double h) {
           this.parent = parent;
           this.f = f;
           this.g = g;
           this.h = h;
       }
   }

   // method to check if our cell (row, col) is valid
   boolean isValid(int[][] mapa, int rows, int cols,
                   Pair point)
   {
       if (rows > 0 && cols > 0)
           return (point.first >= 0) && (point.first < rows)
                   && (point.second >= 0)
                   && (point.second < cols);

       return false;
   }

   //is the cell blocked?

   boolean isUnBlocked(int[][] mapa, int rows, int cols,
                       Pair point)
   {
       return isValid(mapa, rows, cols, point)
               && mapa[point.first][point.second] != 0;
   }

   //Method to check if destination cell has been already reached
   boolean isDestination(Pair position, Pair dest)
   {
       return position == dest || position.equals(dest);
   }

   // Method to calculate heuristic function
   double calculateHValue(Pair src, Pair dest)
   {
        double dx = Math.abs(src.first - dest.first);
        double dy = Math.abs(src.second - dest.second);
        
        return ((dx + dy) + (Math.sqrt(2) - 2) * Math.min(dx, dy));
   }

   // Method for tracking the path from source to destination

   Pair[] tracePath(
           Cell[][] cellDetails,
           int rows,
           int cols,
           Pair dest)
   {   //A* Search algorithm path
       //!System.out.println("The Path:  ");

       Stack<Pair> path = new Stack<>();

       int row = dest.first;
       int col = dest.second;

       Pair nextNode = cellDetails[row][col].parent;
       do {
           path.push(new Pair(row, col));
           nextNode = cellDetails[row][col].parent;
           row = nextNode.first;
           col = nextNode.second;
       } while (cellDetails[row][col].parent != nextNode); // until src

       Pair[] ret = new Pair[path.size()];
       int i = 0;
       while (!path.empty()) {
           Pair p = path.peek();
           ret[i++] = p;
           path.pop();
       }
       return ret;
   }

// A main method, A* Search algorithm to find the shortest path

   Ret aStarSearch(Pair src,
                    Pair dest)
   {
        if(src.equals(dest)){
            Pair[] path = new Pair[1];
            path[0] = src;
            return new Ret(0.0, path);
        }
        int rows = mapa.length;
        int cols = mapa[0].length;

       if (!isValid(mapa, rows, cols, src)) {
           //!System.out.println("Source is invalid...");
           return new Ret(-1.0, null);
       }


       if (!isValid(mapa, rows, cols, dest)) {
           //!System.out.println("Destination is invalid...");
           return new Ret(-1.0, null);
       }


       if (!isUnBlocked(mapa, rows, cols, src)
               || !isUnBlocked(mapa, rows, cols, dest)) {
           //!System.out.println("Source or destination is blocked...");
           return new Ret(-1.0, null);
       }


       if (isDestination(src, dest)) {
           //!System.out.println("We're already (t)here...");
           return new Ret(-1.0, null);
       }


       boolean[][] closedList = new boolean[rows][cols];//our closed list

       Cell[][] cellDetails = new Cell[rows][cols];

       int i, j;
       // Initialising of the starting cell
       i = src.first;
       j = src.second;
       cellDetails[i][j] = new Cell();
       cellDetails[i][j].f = 0.0;
       cellDetails[i][j].g = 0.0;
       cellDetails[i][j].h = 0.0;
       cellDetails[i][j].parent = new Pair( i, j );


  // Creating an open list


       PriorityQueue<Details> openList = new PriorityQueue<>((o1, o2) -> Double.compare(o1.value, o2.value));

       // Put the starting cell on the open list,   set f.startCell = 0

       openList.add(new Details(0.0, i, j));

       while (!openList.isEmpty()) {
           Details p = openList.peek();
           // Add to the closed list
           i = p.i; // second element of tuple
           j = p.j; // third element of tuple

           // Remove from the open list
           openList.poll();
           closedList[i][j] = true;

              // Generating all the 8 neighbors of the cell

          int[] dx = {-1, 1, 0, 0};
            int[] dy = {0, 0, -1, 1};

            for (int dir = 0; dir < 4; dir++) {

                int addX = dx[dir];
                int addY = dy[dir];

                Pair neighbour = new Pair(i + addX, j + addY);

                if (isValid(mapa, rows, cols, neighbour)) {

                    if(cellDetails[neighbour.first] == null){
                        cellDetails[neighbour.first] = new Cell[cols];
                    }

                    if (cellDetails[neighbour.first][neighbour.second] == null) {
                        cellDetails[neighbour.first][neighbour.second] = new Cell();
                    }

                    if (isDestination(neighbour, dest)) {
                        cellDetails[neighbour.first][neighbour.second].parent = new Pair(i, j);
                        return new Ret(cellDetails[i][j].g, tracePath(cellDetails, rows, cols, dest).clone());
                    }

                    else if (!closedList[neighbour.first][neighbour.second]
                            && isUnBlocked(mapa, rows, cols, neighbour)) {

                        double gNew, hNew, fNew;

                        gNew = cellDetails[i][j].g + mapa[neighbour.first][neighbour.second];
                        hNew = calculateHValue(neighbour, dest);
                        fNew = gNew + hNew;

                        if (cellDetails[neighbour.first][neighbour.second].f == -1
                                || cellDetails[neighbour.first][neighbour.second].f > fNew) {

                            openList.add(new Details(fNew, neighbour.first, neighbour.second));

                            cellDetails[neighbour.first][neighbour.second].g = gNew;
                            cellDetails[neighbour.first][neighbour.second].f = fNew;
                            cellDetails[neighbour.first][neighbour.second].parent = new Pair(i, j);
                        }
                    }
                }
            }
       }

       System.out.println("Failed to find the Destination Cell");
       return new Ret(-1.0, null);
   }
}