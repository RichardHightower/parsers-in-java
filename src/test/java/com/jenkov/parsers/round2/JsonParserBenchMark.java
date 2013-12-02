package com.jenkov.parsers.round2;

import com.jenkov.parsers.FileUtil;
import com.jenkov.parsers.core.DataCharBuffer;
import com.jenkov.parsers.core.IndexBuffer;
import com.jenkov.parsers.json.ElementTypes;
import com.jenkov.parsers.json.JsonParser;
import org.boon.IO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.boon.Exceptions.die;

/**
 */
public class JsonParserBenchMark {

    public static void main(String[] args) throws IOException {

        String fileName = "data/webxml.json";


        String fileContents = IO.read ( fileName );


        JsonParser jsonParser   = new JsonParser();
        IndexBuffer jsonElements = new IndexBuffer(1024, true);

        int iterations = 10_000_000;
        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(new DataCharBuffer ( fileContents.toCharArray () ), jsonParser, jsonElements);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);




    }

    private static void parse(DataCharBuffer dataCharBuffer, JsonParser jsonParser, IndexBuffer jsonElements) {
        jsonParser.parse(dataCharBuffer, jsonElements);

    }

}
