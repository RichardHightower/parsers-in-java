package com.jenkov.parsers;

import com.google.gson.Gson;
import com.jenkov.parsers.core.DataCharBuffer;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.Map;

import static org.boon.Exceptions.die;

/**
 */
public class GsonBenchmark {

    public static void main(String[] args) throws IOException {
        String fileName = "data/small.json";
        if(args.length > 0) {
            fileName = args[0];
        }
        System.out.println("parsing: " + fileName);

        DataCharBuffer dataCharBuffer = FileUtil.readFile(fileName);
        Gson gson = new Gson();

        int iterations = 10_000_000; //10.000.000 iterations to warm up JIT and minimize one-off overheads etc.
        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(dataCharBuffer, gson);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(DataCharBuffer dataCharBuffer, Gson gson) {
        Map<String, Object> map = (Map<String, Object>) gson.fromJson (
                new CharArrayReader ( dataCharBuffer.data, 0, dataCharBuffer.length ), Map.class );

//        Double d  = (Double) map.get ( "num" );
//
//        if (d != 1.0) {
//            die();
//        }
//
//
//        if (!map.get ( "debug" ).equals ( "on\toff" )) {
//            die( "$$" + map.get("debug") + "$$");
//        }



    }


}
