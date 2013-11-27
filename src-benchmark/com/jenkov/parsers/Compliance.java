package com.jenkov.parsers;

import com.google.gson.Gson;
import com.jenkov.parsers.core.DataCharBuffer;
import com.jenkov.parsers.core.IndexBuffer;
import org.boon.IO;
import org.boon.json.JsonParser;
import org.boon.json.JsonLazyEncodeParser;
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
                JsonParser.parseMap ( fileContents );
                puts ("BOON 1", "PASSED", fileName);
            } catch ( Exception ex ) {
                puts ("BOON 1", "FAILED", fileName);
                ex.printStackTrace ();
                System.err.flush ();
            }

            try {
                //Boon 2
                JsonLazyEncodeParser.parseMap ( fileContents );
                puts ("BOON 2", "PASSED", fileName);
            } catch ( Exception ex ) {
                puts ("BOON 2", "FAILED", fileName);
                ex.printStackTrace ();
                System.err.flush ();
            }


            try {
                //GSON
                Gson gson = new Gson();

                gson.fromJson ( fileContents, Map.class );
                puts ("GSON #", "PASSED", fileName);
            } catch ( Exception ex ) {
                puts ("GSON #", "FAILED", fileName);
                ex.printStackTrace ();
                System.err.flush ();
            }





            try {

                com.jenkov.parsers.json.JsonParser jsonParser   = new com.jenkov.parsers.json.JsonParser();
                IndexBuffer jsonElements = new IndexBuffer(1024, true);
                jsonParser.parse(new DataCharBuffer (fileContents.toCharArray ()), jsonElements);

                puts ("INFO Q", "PASSED", fileName);
            } catch ( Exception ex ) {
                puts ("INFO Q", "FAILED", fileName);
                //ex.printStackTrace ();
                //System.err.flush ();
            }



            try {

                ObjectMapper mapper = new ObjectMapper();
                mapper.readValue ( fileContents, Map.class );
                puts ("JACK 1", "PASSED", fileName);
            } catch ( Exception ex ) {
                puts ("JACK 1", "FAILED", fileName);
                ex.printStackTrace ();
                System.err.flush ();
            }


        }


    }
}
