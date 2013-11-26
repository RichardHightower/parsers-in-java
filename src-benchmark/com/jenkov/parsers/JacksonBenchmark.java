package com.jenkov.parsers;

import com.jenkov.parsers.core.DataCharBuffer;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class JacksonBenchmark {


    public static void main(String[] args) throws IOException {
        String fileName = "data/small.json.txt";
        if(args.length > 0) {
            fileName = args[0];
        }
        System.out.println("parsing: " + fileName);

        DataCharBuffer dataCharBuffer = FileUtil.readFile(fileName);
        ObjectMapper objectMapper = new ObjectMapper();

        int iterations = 10_000_000; //10.000.000 iterations to warm up JIT and minimize one-off overheads etc.
        long startTime = System.currentTimeMillis();

        String str = new String (dataCharBuffer.data);
        for(int i=0; i<iterations; i++) {
            parse(str, objectMapper);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(String str, ObjectMapper mapper) {
        try {
            Map<String, Object> map = (Map<String, Object>) mapper.readValue ( str, Map.class );
        } catch ( IOException e ) {
            e.printStackTrace ();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}