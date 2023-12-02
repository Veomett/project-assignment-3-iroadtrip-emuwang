import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.*;

public class Graph {
    private final int MAX_VALUE = Integer.MAX_VALUE;

    private class Edge implements Comparable<Edge>{
        String source;
        String dest;
        int dist;
        boolean visited;
        int distFromSource;
        int cost;
        Edge(String v1, String v2, int d) {
            source = v1;
            dest = v2;
            dist = d;
            visited = false;
        }
        @Override
        public int compareTo(Edge e) {
            return this.cost - e.cost;
        }
    }

    class costNode implements Comparable<costNode>{
        String source;
        String dest;
        int cost;
        costNode(String s, String d, int c) {
            source = s;
            dest = d;
            cost = c;
        }
        @Override
        public int compareTo(costNode n) {
            return Integer.compare(this.cost, n.cost);
        }
    }

    public void dijkstra(PriorityQueue<Edge> edgeQueue, String[] path, int[] finalDistances, String source) {
        for (int i = 0; i < snDetails.size(); i++) {
            path[i] = null;
            finalDistances[i] = MAX_VALUE;
        }

        //add Edges from source
        for (Edge e : adjacencyList[getIndexWithCountry(source)]) {
            if (!edgeQueue.contains(e)) {
                e.cost += e.dist;
                edgeQueue.add(e);
                e.visited = true;
                e.distFromSource = 0;
            }
        }

        //loop while the PQueue is not empty to handle all connections
        while (!edgeQueue.isEmpty()) {
            Edge toHandle = edgeQueue.poll();
            if (toHandle.cost < finalDistances[getIndexWithCountry(toHandle.dest)]) {
                finalDistances[getIndexWithCountry(toHandle.dest)] = toHandle.cost;
                path[getIndexWithCountry(toHandle.dest)] = toHandle.source;
            }

            for (Edge e : adjacencyList[getIndexWithCountry(toHandle.dest)]) {
                if (!edgeQueue.contains(e) && !e.visited) {
                    e.cost = finalDistances[getIndexWithCountry(toHandle.dest)] + e.dist;
                    e.visited = true;
                    edgeQueue.add(e);
                }
            }
        }
    }

    public List<String> findShortestPath(String source, String dest) {
        int max = snDetails.size();
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>();
        String[] path = new String[snDetails.size()];
        int[] finalDistances = new int[snDetails.size()];

        dijkstra(edgeQueue, path, finalDistances, source);

        String currCountry = dest;
        Stack<String> pathStack = new Stack<>();
        while (!currCountry.equals(source)) {
            int index = getIndexWithCountry(currCountry);
            String toAdd = "* " + path[getIndexWithCountry(currCountry)] + " --> " + currCountry + " (" + getDistBetweenCountries(path[getIndexWithCountry(currCountry)], currCountry) + " km.)";
            pathStack.add(toAdd);
            currCountry = path[getIndexWithCountry(currCountry)];
        }

        //reset cost and visited for next search
        for (int i = 0; i < adjacencyList.length; i++) {
            for (Edge e : adjacencyList[i]) {
                e.cost = 0;
                e.visited = false;
            }
        }

        LinkedList<String> toReturn = new LinkedList<>();
        while (!pathStack.isEmpty()) {
            toReturn.add(pathStack.pop());
        }
        return toReturn;
    }

    private int getDistBetweenCountries(String source, String dest) {
        for (capDistDetails c: cdDetails) {
            if (checkAliases(c.ida, source) && checkAliases(c.idb, dest)) {
                return c.kmdist;
            }
        }
        return -1;
    }

    private String[] countryInAdjList;
    private LinkedList<Edge>[] adjacencyList;
    private costNode[] costs;
    public List<capDistDetails> cdDetails = new ArrayList<>();
    private List<stateNameDetails> snDetails = new ArrayList<>();
    private List<bordersDetails> bDetails = new ArrayList<>();

    public Graph (String bordersFile, String capdistFile, String statenameFile) {
        makeBorderDetails(bordersFile);
        makeCapDistDetails(capdistFile);
        makeStateNameDetails(statenameFile);
        countryInAdjList = new String[snDetails.size()];
        adjacencyList = new LinkedList[snDetails.size()];
        costs = new costNode[snDetails.size()];
        for (int i = 0; i < snDetails.size(); i++) {
            adjacencyList[i] = new LinkedList<>();
            countryInAdjList[i] = snDetails.get(i).countryName;
            costs[i] = new costNode(countryInAdjList[i], null, MAX_VALUE);
        }
        for (int i = 0; i < cdDetails.size(); i++) {
            String source = getCountryWithID(cdDetails.get(i).ida);
            String dest = getCountryWithID(cdDetails.get(i).idb);
            if (source != null && dest != null) {
                for (bordersDetails b: bDetails) {
                    if (source.equals(b.country) || checkAliases(b.country, source)) {
                        for (int j = 0; j < b.connectingCountry.size(); j++) {
                            if (b.connectingCountry.get(j).equals(dest) || checkAliases(b.connectingCountry.get(j), dest)) {
                                addEdge(source, dest, cdDetails.get(i).kmdist);
                                break;
                            }
                        }
                    }
                }
            }
        }

        /*
        //DELETE LATER
        for (int i = 0; i < adjacencyList.length; i++) {
            System.out.print(countryInAdjList[i] + " has " + adjacencyList[i].size() + " connections in graph: ");

            for (Edge e: adjacencyList[i]) {
                System.out.print(e.dest + ", dist: " + e.dist + "| ");
            }
            System.out.println();
        }

         */

    }

    private int getIndexWithCountry(String country) {
        int count = 0;
        for (stateNameDetails s: snDetails) {
            if (s.countryName.equals(country))
                return count;
            count++;
        }
        return -1;
    }

    private String getCountryWithID(String ID) {
        for (stateNameDetails s: snDetails) {
            if (s.stateID.equals(ID))
                return s.countryName;
        }
        return null;
    }

    private void addEdge(String v1, String v2, int weight) {
        Edge e = new Edge(v1, v2, weight);
        int index = -1;
        int count = 0;
        for (stateNameDetails s: snDetails) {
            if (s.countryName.equals(v1)) {
                index = count;
                break;
            }
            count++;
        }
        adjacencyList[index].add(e);
    }

    private boolean checkAliases(String country, stateNameDetails s) {
        for (int i = 0; i < s.alias.size(); i++) {
            if (s.alias.get(i).equalsIgnoreCase(country))
                return true;
        }
        return false;
    }

    private boolean checkAliases(String country, String source) {
        for (stateNameDetails s: snDetails) {
            if (s.countryName.equals(source)) {
                if (checkAliases(country, s))
                    return true;
            }
        }
        return false;
    }

    /**
     * checks if a country exists or not in snDetails
     * @param country to be searched
     * @return true/false depending on its existence
     */
    public boolean checkExistence(String country) {
        for (stateNameDetails s: snDetails) {
            if (country.equalsIgnoreCase(s.countryName) || checkAliases(country, s))
                return true;
        }
        return false;
    }


    /**
     * helper method used in makeBorderDetails() to parse second part of input string for country, dist pair
     * @param str second part of input string
     * @return HashMap of country and distance pairs to put into bDetails arraylist
     */
    private ArrayList<String> getConInfo (String str) {
        ArrayList<String> temp = new ArrayList<>();
        String[] fullStr = str.split(";");
        for (int i = 0; i < fullStr.length; i++) {
            String country = (fullStr[i].split("[0-9]")[0]).strip();
            temp.add(country);
        }
        return temp;
    }

    /**
     * helper method to create bDetails arraylist
     * @param fileName to read from
     */
    private void makeBorderDetails(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("Given file for Border Details does not exist. Proceeding with default file.");
            f = new File("borders.txt");
        }
        try {
            BufferedReader bfReader = new BufferedReader(new FileReader(f));
            while (true) {
                String str = bfReader.readLine(); //read second line
                if (str == null) //loop until next line is null/empty
                    break;
                String[] strArr = str.split("="); //split lines from csv file using "="
                bordersDetails temp = new bordersDetails();
                for (int i = 0; i < strArr.length; i++) { //loop through strArr and input into values
                    switch (i) {
                        case 0:
                            temp.country = strArr[i].strip();
                            break;
                        case 1:
                            if (!strArr[i].isBlank()) {
                                temp.connectingCountry = getConInfo(strArr[i]);
                            }
                            break;
                    }
                }
                bDetails.add(temp);
            }
            bfReader.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    /**
     * helper method to create cdDetails arraylist
     * @param fileName to read from
     */
    private void makeCapDistDetails(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("Given file for Capital Distance does not exist. Proceeding with default file.");
            f = new File("capdist.csv");
        }
        try {
            BufferedReader bfReader = new BufferedReader(new FileReader(f));
            String str = bfReader.readLine(); //read first line
            while (true) {
                str = bfReader.readLine(); //read second line
                if (str == null) //loop until next line is null/empty
                    break;
                String[] strArr = str.split(","); //split lines from csv file using ","
                capDistDetails temp = new capDistDetails();
                for (int i = 0; i < strArr.length; i++) { //loop through strArr and input into values
                    switch (i) {
                        case 0:
                            temp.numa = Integer.parseInt(strArr[i]);
                            break;
                        case 1:
                            temp.ida = strArr[i];
                            break;
                        case 2:
                            temp.numb = Integer.parseInt(strArr[i]);
                            break;
                        case 3:
                            temp.idb = strArr[i];
                            break;
                        case 4:
                            temp.kmdist = Integer.parseInt(strArr[i]);
                            break;
                    }
                }
                cdDetails.add(temp);
            }
            bfReader.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private void addExtraAliases(stateNameDetails temp) {
        temp.alias.add(temp.stateID);
        switch (temp.stateID) {
            case "DRC":
                temp.alias.add("Congo, Democratic Republic of");
                temp.alias.add("Congo, Democratic Republic of the");
                temp.alias.add("Democratic Republic of the Congo");
                break;
            case "PRK":
                temp.alias.add("North Korea");
                temp.alias.add("Korea, North");
                break;
            case "ROK":
                temp.alias.add("South Korea");
                temp.alias.add("Korea, South");
                break;
            case "DRV":
                temp.alias.add("Vietnam");
                temp.alias.add("Annam");
                temp.alias.add("Cochin China");
                temp.alias.add("Tonkin");
                temp.alias.add("VNM");
                temp.alias.add("RVN");
                break;
            case "TAZ":
                temp.alias.add("Tanzania");
                temp.alias.add("Tanganyika");
                temp.alias.add("Zanzibar");
                temp.alias.add("ZAN");
                temp.alias.add("TAN");
                break;
            case "USA":
                temp.alias.add("United States");
                temp.alias.add("US");
                break;
            case "DEN":
                temp.alias.add("Greenland");
                temp.alias.add("Denmark (Greenland)");
                break;
            case "BHM":
                temp.alias.add("Bahamas, The");
                temp.alias.add("The Bahamas");
                break;
        }
    }

    /**
     * helper method to create snDetails arraylist
     * @param fileName to read from
     */
    private void makeStateNameDetails(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("Given file for State Name does not exist. Proceeding with default file.");
            f = new File("state_name.csv");
        }
        try {
            BufferedReader bfReader = new BufferedReader(new FileReader(f));
            String str = bfReader.readLine(); //read first line
            while (true) {
                str = bfReader.readLine(); //read second line
                if (str == null) //loop until next line is null/empty
                    break;
                String[] strArr = str.split("\t"); //split lines from csv file using the tab character
                stateNameDetails temp = new stateNameDetails(); //initialize object
                for (int i = 0; i < strArr.length; i++) { //loop through strArr and input into values
                    if (strArr[4].equals("2020-12-31")) {
                        switch (i) {
                            case 0:
                                temp.stateNum = Integer.parseInt(strArr[i]);
                                break;
                            case 1:
                                temp.stateID = strArr[i];
                                break;
                            case 2:
                                if (strArr[i].contains("(")) {
                                    String[] smallStr = strArr[i].split("\\(");
                                    temp.countryName = smallStr[0].trim();
                                    temp.alias.add(smallStr[1].replace(")", "").trim());
                                } else {
                                    temp.countryName = strArr[i];
                                }
                                break;
                            case 4:
                                temp.end = strArr[i];
                                break;
                        }
                    }
                }

                if (temp.countryName != null) {
                    addExtraAliases(temp);
                    snDetails.add(temp);
                }
            }
            bfReader.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}

/**
 * object with details from Borders file
 */
class bordersDetails {
    public String country;
    public List<String> connectingCountry = new ArrayList<>();
}

/**
 * object with details from Capital Distance file
 * removed distance in miles
 */
class capDistDetails {
    protected int numa;
    protected String ida;
    protected int numb;
    protected String idb;
    protected int kmdist;
}

/**
 * object with details from State Name file
 * removed start date of countries
 */
class stateNameDetails {
    protected int stateNum;
    protected String stateID;
    protected String countryName;
    protected List<String> alias = new ArrayList<>();
    protected String end;
}

