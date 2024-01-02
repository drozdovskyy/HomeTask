package com.softserve.framework.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVReader extends ExternalFileReader {
    private final String CSV_SPLIT_BY = ",";
    private final String CSV_SPLIT_BY_REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";


    public CSVReader(String filename) {
        super(filename);
        //System.out.println("***PATH = " + getPath());
    }

    @Override
    public List<List<String>> getAllCells(String path) {
        List<List<String>> allCells = new ArrayList<>();
        String row;


        //
        // try with resources
        try (Scanner sc = new Scanner(new File(path))) {
            while (sc.hasNextLine()) {
                row = sc.nextLine();
                List<String> rowSplitList = new ArrayList<>();
                //System.out.println("\t\trow = " + row);
                for (String rowSplit : List.of(row.split(CSV_SPLIT_BY_REGEX))) {
                    rowSplitList.add(rowSplit.replace("\"", ""));
                }

                allCells.add(rowSplitList);
            }
        } catch (Exception e) {
            // loggin.error(); // TODO
            //e.printStackTrace();
        }
        return allCells;
    }
}
