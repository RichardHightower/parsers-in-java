package com.jenkov.parsers.round2;

import com.jenkov.parsers.FileUtil;
import com.jenkov.parsers.core.DataCharBuffer;
import org.boon.IO;
import org.boon.json.JSONParser2;

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
        for(int i=0; i<iterations; i++) {
            parse(fileContents);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(String fileContents) {
        Map<String, Object> map =  JSONParser2.parseMap ( fileContents );


    }


}
