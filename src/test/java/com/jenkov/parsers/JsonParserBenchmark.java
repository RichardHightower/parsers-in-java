package com.jenkov.parsers;

import com.jenkov.parsers.core.DataCharBuffer;
import com.jenkov.parsers.core.IndexBuffer;
import com.jenkov.parsers.json.ElementTypes;
import com.jenkov.parsers.json.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.boon.Exceptions.die;

/**
 */
public class JsonParserBenchmark {

    public static void main(String[] args) throws IOException {
        String fileName = "data/small.json";
        if(args.length > 0) {
            fileName = args[0];
        }
        System.out.println("parsing: " + fileName);

        DataCharBuffer dataCharBuffer = FileUtil.readFile(fileName);
        JsonParser jsonParser   = new JsonParser();
        IndexBuffer jsonElements = new IndexBuffer(1024, true);

        int iterations = 10_000_000;
        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(dataCharBuffer, jsonParser, jsonElements);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);




    }

    private static void parse(DataCharBuffer dataCharBuffer, JsonParser jsonParser, IndexBuffer jsonElements) {
        jsonParser.parse(dataCharBuffer, jsonElements);


//        Map<String, Object> map = new HashMap<> (  );
//
//        int num=0;
//
//        for (int index = 0; index < jsonElements.type.length; index++) {
//
//            if (jsonElements.type[index] == ElementTypes.JSON_PROPERTY_NAME) {
//                int start = jsonElements.position[index];
//                int len = jsonElements.length[index];
//                String name = new String(dataCharBuffer.data, start, len);
//                name = name.substring ( 1, name.length () -1 );
//                index ++; //pray it is in bounds
//                if (name.equals ( "num" )) {
//                    if (jsonElements.type[index] == ElementTypes.JSON_PROPERTY_VALUE_NUMBER) {
//                        int snum = jsonElements.position[index];
//                        int slen = jsonElements.length[index];
//                        String value = new String(dataCharBuffer.data, snum, slen);
//                        num = Integer.parseInt ( value );
//                        map.put ( name, num );
//
//                    }
//
//                }else if (name.equals ( "debug" )) {
//                    if (jsonElements.type[index] == ElementTypes.JSON_PROPERTY_VALUE_STRING) {
//                        int snum = jsonElements.position[index];
//                        int slen = jsonElements.length[index];
//                        String value = new String(dataCharBuffer.data, snum+1, slen-1);
//
//                        map.put ( name, value );
//
//                    }
//
//
//                }
//
//            }
//        }
//
//        if (num!=1) {
//            die("die" + num);
//        }
//
//
////        if (!map.get ( "debug" ).equals ( "on\toff" )) {
////            die( "$$" + map.get("debug") + "$$");
////        }


    }

}
