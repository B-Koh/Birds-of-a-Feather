package com.example.birds_of_a_feather_team_20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to read in a file and parse the file into something useable
 */
public class CSVParser {

    private InputStream inputStream;

    public CSVParser(InputStream inputStream){
        this.inputStream = inputStream;
    }

    /**
     * Reads in a file and parses it.
     * @return list of rows from the csv file (each index in the list is a row)
     * Cited work: https://stackoverflow.com/questions/38415680/how-to-parse-csv-file-into-an-array-in-android-studio/38415815
     */
    public List read(){
        List outputList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line;
            //read each line
            while((line = reader.readLine()) != null){
                //split the line with commas
                String[] row = line.split(",");
                outputList.add(row);
            }
        }
        catch (IOException e){
            throw new RuntimeException("Error reading CSV file" + e);
        }
        finally{
            try{
                inputStream.close();
            }
            catch (IOException ee){
                throw new RuntimeException("Error closing input stream" + ee);
            }
        }


        return outputList;
    }

}
