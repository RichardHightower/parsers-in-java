package com.jenkov.parsers;

import com.google.gson.Gson;
import com.jenkov.parsers.core.DataCharBuffer;
import com.jenkov.parsers.core.IndexBuffer;
import com.jenkov.parsers.json.JsonParser;
import org.boon.IO;
import org.boon.json.JSONParser;
import org.boon.json.JSONParser2;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;
import java.util.Map;

import static org.boon.Boon.puts;

public class Compliance {


    public static void main (String... args) {



        final List<String> list = IO.listByExt ( "data", ".json" );

        for ( String file : list ) {

            String fileContents = IO.read ( file );
            String fileName = IO.path ( file ).getFileName ().toString ();

            puts ( "testing", fileName );

            try {
                //Boon 1
                JSONParser.parseMap ( fileContents );
                puts ("BOON 1", "PASSED", fileName);
            } catch ( Exception ex ) {
                puts ("BOON 1", "FAILED", fileName);
            }

            try {
                //Boon 2
                JSONParser2.parseMap ( fileContents );
                puts ("BOON 2", "PASSED", fileName);
            } catch ( Exception ex ) {
                puts ("BOON 2", "FAILED", fileName);
            }


            try {
                //GSON
                Gson gson = new Gson();

                gson.fromJson ( fileContents, Map.class );
                puts ("GSON #", "PASSED", fileName);
            } catch ( Exception ex ) {
                puts ("GSON #", "FAILED", fileName);
            }





            try {

                JsonParser jsonParser   = new JsonParser();
                IndexBuffer jsonElements = new IndexBuffer(1024, true);
                jsonParser.parse(new DataCharBuffer (fileContents.toCharArray ()), jsonElements);

                puts ("INFO Q", "PASSED", fileName);
            } catch ( Exception ex ) {
                puts ("INFO Q", "FAILED", fileName);
            }



            try {

                ObjectMapper mapper = new ObjectMapper();
                mapper.readValue ( fileContents, Map.class );
                puts ("JACK 1", "PASSED", fileName);
            } catch ( Exception ex ) {
                puts ("JACK 1", "FAILED", fileName);
            }


        }


    }
}
