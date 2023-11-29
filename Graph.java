import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class Edge {
    String source;
    String dest;
    int dist;

    Edge(String v1, String v2, int d) {
        source = v1;
        dest = v2;
        dist = d;
    }
}

public class Graph { //implementation for graph structure as well as its functions like dijkstras
    //use adjacency list for undirected weighted graph

    private LinkedList<Edge>[] adjacencyList;
    private List<capDistDetails> cdDetails = new ArrayList<>();
    private List<stateNameDetails> snDetails = new ArrayList<>();
    private List<bordersDetails> bDetails = new ArrayList<>();

    public Graph (String bordersFile, String capdistFile, String statenameFile) {
        makeBorderDetails(bordersFile);
        makeCapDistDetails(capdistFile);
        makeStateNameDetails(statenameFile);
        adjacencyList = new LinkedList[snDetails.size()];
        for (int i = 0; i < snDetails.size(); i++) {
            adjacencyList[i] = new LinkedList<>();
        }
        for (int i = 0; i < cdDetails.size(); i++) {
            String source = getCountryWithID(cdDetails.get(i).ida);
            String dest = getCountryWithID(cdDetails.get(i).idb);
            if (source != null && dest != null) {
                addEdge(source, dest, cdDetails.get(i).getKmdist());
                System.out.println("going to add edge - countryA: " + getCountryWithID(cdDetails.get(i).ida) + ", countryB: " + getCountryWithID(cdDetails.get(i).idb) + ", dist: " + cdDetails.get(i).getKmdist());
            }
        }

        for (int i = 0; i < adjacencyList.length; i++) {
            for (Edge e: adjacencyList[i]) {
                System.out.print(e.source + " connects to " + e.dest + ", dist: " + e.dist + "| ");
            }
            System.out.println();
        }
    }

    private String getCountryWithID(String ID) {
        for (stateNameDetails s: snDetails) {
            if (s.getStateID().equals(ID))
                return s.getCountryName();
        }
        System.out.println("Country not found, returning null: " + ID);
        return null;
    }

    private void addEdge(String v1, String v2, int weight) {
        Edge e = new Edge(v1, v2, weight);
        int index = -1;
        int count = 0;
        for (stateNameDetails s: snDetails) {
            if (s.getCountryName().equals(v1)) {
                index = count;
                break;
            }
            count++;
        }
        adjacencyList[index].add(e);
    }

    /**
     * checks if a country exists or not in snDetails
     * @param country to be searched
     * @return true/false depending on its existence
     */
    public boolean checkExistence(String country) {
        for (stateNameDetails s: snDetails) {
            System.out.println("Checking for " + country + " in " + s.getCountryName() + " and " + s.getAlias());
            if (country.equalsIgnoreCase(s.getCountryName()) || country.equalsIgnoreCase(s.getAlias())) {
                //if (s.end.equals("2020-12-31")) //check if country still exists
                    return true;
            }
        }
        return false;
    }

    /**
     * helper method used in makeBorderDetails() to parse second part of input string for country, dist pair
     * @param str second part of input string
     * @return HashMap of country and distance pairs to put into bDetails arraylist
     */
    private HashMap<String, Integer> getConInfo (String str) {
        HashMap<String, Integer> temp = new HashMap<>();
        String[] fullStr = str.split(";");
        for (int i = 0; i < fullStr.length; i++) {
            String country = (fullStr[i].split("[0-9]")[0]).strip();
            String dist = fullStr[i].replaceAll("[^0-9]", "");
            temp.put(country, Integer.parseInt(dist));
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
                            temp.country = strArr[i];
                            break;
                        case 1:
                            if (!strArr[i].isBlank()) {
                                temp.conInfo = getConInfo(strArr[i]);
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
                                    temp.alias = smallStr[1].replace(")", "").trim();
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
                    snDetails.add(temp);
                    System.out.println("Added country: " + temp.getCountryName() + ", alias: " + temp.getAlias());
                }
            }
            System.out.println("Size of snDetails: " + snDetails.size());
            bfReader.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

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

    public int getNuma() {
        return numa;
    }

    public String getIda() {
        return ida;
    }

    public int getNumb() {
        return numb;
    }

    public String getIdb() {
        return idb;
    }

    public int getKmdist() {
        return kmdist;
    }
}

/**
 * object with details from State Name file
 * removed start date of countries
 */
class stateNameDetails {
    protected int stateNum;
    protected String stateID;
    protected String countryName;
    protected String alias;
    protected String end;
    protected int indexInGraph;

    public int getStateNum() {
        return stateNum;
    }

    public String getStateID() {
        return stateID;
    }

    public String getCountryName() {
        return countryName;
    }
    public String getAlias() {
        return alias;
    }

    public String getEnd() {
        return end;
    }
    public int getIndexInGraph() {
        return indexInGraph;
    }
}

/**
 * object with details from Borders file
 */
class bordersDetails {
    public String country;
    public HashMap<String, Integer> conInfo = new HashMap<>();
}