import java.util.List;
import java.util.Scanner;

/*Questions:
same input files all the time?
undirected weighted graph so adjacency list?
repeat path finding if path doesnt exist given 2 existing countries?
 */


/*TODO:
   handle countries with an alias and duplicate countries in statename
   handle for diff input files i guess?
   import all object arraylists into file
 */

public class IRoadTrip {

    Graph graph;
    public IRoadTrip (String [] args) {
        // Replace with your code
        graph = new Graph(args[0], args[1], args[2]);
        System.out.println();
    }

    public int getDistance (String country1, String country2) {
        // Replace with your code
        return -1;
    }


    public List<String> findPath (String country1, String country2) {
        // Replace with your code
        return null;
    }


    public void acceptUserInput() {
        Scanner in = new Scanner(System.in);
        String country1;
        String country2;
        while (true) {
            while (true) {
                System.out.print("Enter the name of the first country (type EXIT to quit): ");
                country1 = in.nextLine();
                if (checkValidity(country1))
                    break;
            }
            while (true) {
                System.out.print("Enter the name of the second country (type EXIT to quit): ");
                country2 = in.nextLine();
                if (checkValidity(country2))
                    break;
            }
            break;
        }

        List<String> path = findPath(country1, country2);
        if (path != null) {
            for (String p : path) {
                System.out.println(p);
            }
        } else {
            System.out.println("Path does not exist between these two countries.");
        }
    }

    private boolean checkValidity (String country) {
        if (country.equals("EXIT"))
            System.exit(0);
        if (!graph.checkExistence(country)) {
            System.out.println("Invalid country name. Please enter a valid country name.");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);

        a3.acceptUserInput();
    }

}

