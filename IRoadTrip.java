import java.util.List;
import java.util.Scanner;

public class IRoadTrip {

    Graph graph; //graph of countries and their connections to each other

    /**
     * default constructor to make a graph object
     * @param args containing files to be read
     */
    public IRoadTrip (String [] args) {
        if (args.length == 3) {
            graph = new Graph(args[0], args[1], args[2]);
        } else {
            System.out.println("Invalid Arguments, must have the names of files.");
            System.exit(-1);
        }
    }

    /**
     * @param country1 source
     * @param country2 destination
     * @return distance between two given countries' capital
     */
    public int getDistance (String country1, String country2) {
        return graph.getDistBetweenCountries(country1, country2);
    }

    /**
     * finds the path between two given countries
     * @param country1 source
     * @param country2 destination
     * @return a List of string containing the path
     */
    public List<String> findPath (String country1, String country2) {
        return graph.findShortestPath(country1, country2);
    }

    /**
     * main loop that handles user input and returns appropriate responses
     */
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
            System.out.println("Route from " + country1 + " to " + country2 + ":");
            for (String p : path) {
                System.out.println(p);
            }
            acceptUserInput();
        } else {
            System.out.println("Path does not exist between these two countries.");
            acceptUserInput();
        }
    }

    /**
     * helper function used in acceptUserInput() to check if input is valid
     * @param country
     * @return
     */
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

