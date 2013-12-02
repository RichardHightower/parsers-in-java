package com.jenkov.parsers.round2;

import org.boon.IO;
import org.boon.json.JsonAsciiParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BoonBenchMarkAsciiJson {

        public static void main(String[] args) throws IOException {
            String fileName = "data/webxml.json";


            String fileContents = IO.read ( fileName );


            int iterations = 1_000_000; //1.000.000 iterations to warm up JIT and minimize one-off overheads etc.
            long startTime = System.currentTimeMillis();

            final byte[] chars = fileContents.getBytes ( StandardCharsets.UTF_8 );

            for(int i=0; i<iterations; i++) {
                parse(chars);
            }
            long endTime = System.currentTimeMillis();

            long finalTime = endTime - startTime;

            System.out.println("final time: " + finalTime);
        }

        private static void parse(byte [] fileContents) {
            Map<String, Object> map =  JsonAsciiParser.parseMap ( fileContents );


        }


    }
