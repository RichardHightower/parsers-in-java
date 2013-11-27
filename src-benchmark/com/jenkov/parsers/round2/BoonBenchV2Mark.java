package com.jenkov.parsers.round2;

import org.boon.IO;
import org.boon.json.JsonLazyEncodeParser;

import java.io.IOException;
import java.util.Map;


/**
 */
public class BoonBenchV2Mark {

    public static void main(String[] args) throws IOException {
        String fileName = "data/webxml.json";


        String fileContents = IO.read ( fileName );


        int iterations = 1_000_000; //1.000.000 iterations to warm up JIT and minimize one-off overheads etc.
        long startTime = System.currentTimeMillis();

        final char[] chars = fileContents.toCharArray ();

        for(int i=0; i<iterations; i++) {
            parse(chars);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(char [] fileContents) {
        Map<String, Object> map =  JsonLazyEncodeParser.parseMap ( fileContents );


    }


}
