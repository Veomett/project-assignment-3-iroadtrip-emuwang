import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
    protected String end;

    public int getStateNum() {
        return stateNum;
    }

    public String getStateID() {
        return stateID;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getEnd() {
        return end;
    }
}

/**
 * object with details from Borders file
 */
class bordersDetails {
    public String country;
    public HashMap<String, Integer> conInfo = new HashMap<>();
}

public class Graph { //implementation for graph structure as well as its functions like dijkstras
    //use adjacency list for undirected weighted graph

    private LinkedList<String>[] adjacencyList;
    private List<capDistDetails> cdDetails = new ArrayList<>();
    private List<stateNameDetails> snDetails = new ArrayList<>();
    private List<bordersDetails> bDetails = new ArrayList<>();

    public Graph (String bordersFile, String capdistFile, String statenameFile) {
        adjacencyList = new LinkedList[216];
        makeBorderDetails(bordersFile);
        makeCapDistDetails(capdistFile);
        makeStateNameDetails(statenameFile);
    }

    /**
     * checks if a country exists or not in snDetails
     * @param country to be searched
     * @return true/false depending on its existence
     */
    public boolean checkExistence(String country) {
        for (stateNameDetails s: snDetails) {
            if (country.equalsIgnoreCase(s.getCountryName())) {
                if (s.end.equals("2020-12-31")) //check if country still exists
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
                    switch (i) {
                        case 0:
                            temp.stateNum = Integer.parseInt(strArr[i]);
                            break;
                        case 1:
                            temp.stateID = strArr[i];
                            break;
                        case 2:
                            temp.countryName =  strArr[i];
                            break;
                        case 4:
                            temp.end = strArr[i];
                            break;
                    }
                }
                snDetails.add(temp);
            }
            bfReader.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

}
