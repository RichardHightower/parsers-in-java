package com.jenkov.parsers;

import com.jenkov.parsers.core.DataCharBuffer;
import org.boon.json.JSONParser2;

import java.io.IOException;
import java.util.Map;

import static org.boon.Exceptions.die;

public class BoonBench2Mark {

    public static void main(String[] args) throws IOException {
        String fileName = "data/small.json.txt";
        if(args.length > 0) {
            fileName = args[0];
        }
        System.out.println("parsing: " + fileName);

        DataCharBuffer dataCharBuffer = FileUtil.readFile(fileName);


        int iterations = 10_000_000; //10.000.000 iterations to warm up JIT and minimize one-off overheads etc.
        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(dataCharBuffer);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(DataCharBuffer dataCharBuffer) {
        Map<String, Object> map =  JSONParser2.parseMap ( dataCharBuffer.data );

//        Integer i  = (Integer) map.get ( "num" );
//
//        if (i!=1) {
//            die();
//        }
//
//
//        if (!map.get ( "debug" ).equals ( "on\toff" )) {
//            die( "$$" + map.get("debug") + "$$");
//        }

    }

}
