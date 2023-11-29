import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/*Questions:
same input files all the time?
inconsistent naming: "Korea, People's Republic of" and "Macedonia (Former Yugoslav Republic of)"
size of adjacency list? how many vertices
 */

/*TODO:
   import all object arraylists into graph: how to make an adjacency list of strings?? have edge n stuff
   handle countries with weird alias and commas? example: Korea, People's Republic of
   handle for diff input files i guess?
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
        List<String> path = new LinkedList<>();
        return path;
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
        if (path.size() > 0) {
            for (String p : path) {
                System.out.println(p);
            }
            acceptUserInput();
        } else {
            System.out.println("Path does not exist between these two countries.");
            acceptUserInput();
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

