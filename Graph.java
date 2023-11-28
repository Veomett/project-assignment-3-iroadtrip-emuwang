import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.LinkedList;

class capDistDetails {
    public int numa;
    public String ida;
    public int numb;
    public String idb;
    public int kmdist;
}

class stateNameDetails {
    public int stateNum;
    public String stateID;
    public String countryName;
    public String start;
    public String end;
    public LocalDate date;
}

class bordersDetails {
    public String country;
    public String[] conCountry;
    public int[] dist;
}

public class Graph { //implementation for graph structure as well as its functions like dijkstras
    //use adjacency list for undirected weighted graph
    private LinkedList<String>[] adjacencyList;
    private capDistDetails[] cdDetails;
    private stateNameDetails[] snDetails;
    bordersDetails[] bDetails;

    /**
     * constructor that reads in the data and creates the graph
     */
    public Graph (String bordersFile, String capdistFile, String statenameFile) {
        adjacencyList = new LinkedList[216];
        makeBorderDetails(bordersFile);
        makeCapDistDetails(capdistFile);
        makeStateNameDetails(statenameFile);
    }

    /**
     * @param country to check for existence
     * @return true/false depending on if the country is on the list or does not exist anymore
     */
    public boolean checkExistence(String country) {
        for (int i = 0; i < snDetails.length; i++) {
            if (country.equalsIgnoreCase(snDetails[i].countryName)) {
                if (snDetails[i].end.equalsIgnoreCase("2020-12-31"))
                    return true;
            }
        }
        return false;
    }

    /**
     * helper function to parse through given file and input data into a capDistDetails array
     * @return capDistDetails[]
     */
    private void makeCapDistDetails(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("Given file for Capital Distance does not exist. Proceeding with default file.");
            System.exit(0);
        }
        try {
            cdDetails = new capDistDetails[41006];
            int count = 0;
            BufferedReader bfReader = new BufferedReader(new FileReader(f));
            String str = bfReader.readLine(); //read first line
            while (true) {
                str = bfReader.readLine(); //read second line
                if (str == null) //loop until next line is null/empty
                    break;
                String[] strArr = str.split(","); //split lines from csv file using ","
                cdDetails[count] = new capDistDetails(); //initialize object
                for (int i = 0; i < strArr.length; i++) { //loop through strArr and input into values
                    switch (i) {
                        case 0:
                            cdDetails[count].numa = Integer.parseInt(strArr[i]);
                            break;
                        case 1:
                            cdDetails[count].ida = strArr[i];
                            break;
                        case 2:
                            cdDetails[count].numb = Integer.parseInt(strArr[i]);
                            break;
                        case 3:
                            cdDetails[count].idb = strArr[i];
                            break;
                        case 4:
                            cdDetails[count].kmdist = Integer.parseInt(strArr[i]);
                            break;
                    }
                }
                count++;
            }
            bfReader.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    /**
     * helper function to parse through given file and input data into a stateNameDetails array
     * @return stateNameDetails[]
     */
    private void makeStateNameDetails(String fileName) {

        snDetails = new stateNameDetails[216];
        try {
            int count = 0;
            BufferedReader bfReader = new BufferedReader(new FileReader("state_name.tsv"));
            String str = bfReader.readLine(); //read first line
            while (true) {
                str = bfReader.readLine(); //read second line
                if (str == null) //loop until next line is null/empty
                    break;
                String[] strArr = str.split("\t"); //split lines from csv file using the tab character
                snDetails[count] = new stateNameDetails(); //initialize object
                for (int i = 0; i < strArr.length; i++) { //loop through strArr and input into values
                    switch (i) {
                        case 0:
                            snDetails[count].stateNum = Integer.parseInt(strArr[i]);
                            break;
                        case 1:
                            snDetails[count].stateID = strArr[i];
                            break;
                        case 2:
                            snDetails[count].countryName =  strArr[i];
                            break;
                        case 3:
                            snDetails[count].start = strArr[i];
                            break;
                        case 4:
                            snDetails[count].end = strArr[i];
                            break;
                    }
                }
                count++;
            }
            bfReader.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private void makeBorderDetails(String fileName) {
        System.out.println("made it into makeBorderDetails");
    }
}
