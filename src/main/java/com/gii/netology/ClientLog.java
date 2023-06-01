package com.gii.netology;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;

public class ClientLog {
    static ArrayList<String> productArray = new ArrayList<>();
    static ArrayList<String> counterItem = new ArrayList<>();
    void log(String productNum, String amount) {
        productArray.add(productNum);
        counterItem.add(amount);
    }

    void exportAsCVS(File textFile) throws IOException {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(textFile))){
            for (int i = 0; i < productArray.size(); i++) {
                StringJoiner stringJoiner = new StringJoiner("-")
                        .add(productArray.get(i))
                        .add(counterItem.get(i));

                String[] concatForCVS = stringJoiner.toString().split("-");

                csvWriter.writeNext(concatForCVS);
            }

        }
    }
}