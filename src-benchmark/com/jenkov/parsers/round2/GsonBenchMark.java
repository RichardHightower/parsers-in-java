package com.jenkov.parsers.round2;

import com.google.gson.Gson;
import org.boon.IO;

import java.io.IOException;
import java.util.Map;

public class GsonBenchMark {


    public static void main(String[] args) throws IOException {
        String fileName = "data/webxml.json";


        String fileContents = IO.read ( fileName );
        Gson gson = new Gson();

        int iterations = 1_000_000; //10.000.000 iterations to warm up JIT and minimize one-off overheads etc.
        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(fileContents, gson);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(String fileContents, Gson gson) {
        Map<String, Object> map = (Map<String, Object>) gson.fromJson (fileContents, Map.class );



    }

}
