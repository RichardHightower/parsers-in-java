package com.jenkov.parsers.round2;

import java.io.IOException;
import java.util.Map;

import org.boon.IO;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonBenchmark {



    public static void main(String[] args) throws IOException {
        String fileName = "data/webxml.json";


        String fileContents = IO.read ( fileName );

        ObjectMapper mapper = new ObjectMapper();

        int iterations = 1_000_000; //10.000.000 iterations to warm up JIT and minimize one-off overheads etc.
        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(fileContents, mapper);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(String fileContents, ObjectMapper mapper) {
        try {
            Map<String, Object> map = (Map<String, Object>) mapper.readValue ( fileContents, Map.class);
        } catch ( IOException e ) {
            e.printStackTrace ();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

}
