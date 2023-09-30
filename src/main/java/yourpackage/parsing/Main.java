import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<DataField> dataFieldArrayList = new ArrayList<>();   // ArrayList of data fields

        //Read from console or specific file for now
        Scanner consoleScanner = new Scanner(System.in);    // declare new Scanner for console input
        System.out.println("Enter a the name of the input data file:");
        String filename = "flightrecord.csv";
        //String filename = consoleScanner.nextLine();


        BufferedReader br;  // Reader for file info
        String line;        // Current line that buffered reader is on
        int i = 0;
        int j;
        int MAX_ROWS, MAX_COLS;
        try {
            br = new BufferedReader(new FileReader(filename));
            ArrayList<String[]> dataLines = new ArrayList<String[]>();
            while ((line = br.readLine()) != null) {
                dataLines.add(line.split(",", -1));
            }
            MAX_ROWS = dataLines.size();
            String[][] arr = new String[MAX_ROWS][0];   // dataLines.size() = number of rows
            dataLines.toArray(arr);
            MAX_COLS = arr[0].length;

            while (i < MAX_COLS) {  // Iterate through columns and grab the name of each field
                DataField df = new DataField(arr[0][i]);    // Instantiate a new object with name found in the top of each column
                ArrayList<String> dataElements = new ArrayList<>(); // Create a new arraylist for that data field
                dataFieldArrayList.add(df);                         // Add the new DataField to dataFieldArraylist
                j = 1;      // Set to 1 to ensure that the names of each field are not also parsed
                while (j < MAX_ROWS) {  // Iterate through each row and add each data element for the given column
                    //dataElements.add(arr[j][i]);
                    df.addDataRow(arr[j][i]);
                    j++;
                }
                i++;
            }
        }
        catch (FileNotFoundException ex) {
            System.err.println("File could not be located.");
            return;
        }
        catch (IOException ex) {
            System.err.println("Error: IOException.");
            return;
        }

        // Debug thing to output all the elements of column 1 in the file
        System.out.println(dataFieldArrayList.get(0).getDataRows());
    }
}
