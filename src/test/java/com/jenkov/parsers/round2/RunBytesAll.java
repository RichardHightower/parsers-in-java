package com.jenkov.parsers.round2;

import static org.boon.Boon.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import org.apache.commons.io.FileUtils;
import org.boon.IO;
import org.boon.Lists;
import org.boon.Str;
import org.boon.criteria.Sort;
import org.boon.json.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class RunBytesAll {


    public static void main (String ... args) {

        List<String> list =  IO.listByExt ( "data", ".json" );


        for (String file : list) {

            String fileContents = IO.read ( file );

            List<BenchMarkIO> benchMarks =  buildBenchMarkList( fileContents, 100_000, file );


            puts ( Str.rpad ( "Name", 20, ' ' ),  Str.rpad("Time", 20, ' '),
                    Str.lpad ("File",  20, ' '),
                    Str.lpad("Iterations", 20, ' '));


            long min = Long.MAX_VALUE;
            String winner = "";

            for (BenchMarkIO benchMark : benchMarks) {
                benchMark.test ();
                System.gc ();
                if (benchMark.finalTime < min) {

                    min = benchMark.finalTime;
                    winner = benchMark.name;
                }
                puts (Str.rpad ( benchMark.name, 20, ' '),
                        Str.rpad(String.format("%,d", benchMark.finalTime), 20, ' '),
                        Str.lpad (""+ IO.path (file).getFileName (),  20, ' '),
                        Str.lpad( String.format ("%,d", benchMark.times), 20, ' '));
            }

            puts ("Winner:", winner);

            Sort.asc ("finalTime").sort ( benchMarks );

            System.out.print (Str.rpad("order:", 10, ' '));

            for (BenchMarkIO benchMarkIO: benchMarks) {
                System.out.print (Str.rpad(benchMarkIO.name, 15, ' '));
            }
            System.out.println();

            puts ( "___________________________________________________________________________________" );
        }

    }
    
    private static byte[] readFileAsBytes(String fileName) {
        try {
            return FileUtils.readFileToByteArray(new File(fileName));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
 
    private static List<BenchMarkIO> buildBenchMarkList(final String fileContents, int times, final String fileName) {

        if (fileContents.length () > 40_000) {
            times /= 1_000;
        }
        
        return Lists.list (
                new BenchMarkIO ( "jackson-object", times, fileName ) {
                    ObjectMapper mapper = new ObjectMapper ();
                    byte[] bytes = readFileAsBytes(fileName);

                    @Override
                    void run () {
                        try {
                            mapper.readValue ( bytes, Map.class );
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }
                    }
                },
                new BenchMarkIO ( "json-smart", times, fileName ) {
                    byte[] bytes = readFileAsBytes(fileName);

                    @Override
                    void run () {
                        try {
                            new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse ( bytes );
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new BenchMarkIO ( "gson   ", times, fileName ) {
                    Gson gson = new Gson ();
                    byte[] bytes = readFileAsBytes(fileName);

                    @Override
                    void run () {
                        // GSON can't handle byte arrays directly, have to use a reader
                        Reader reader = new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8);
                        Map<String, Object> map = ( Map<String, Object> ) gson.fromJson ( reader, Map.class );
                    }
                },
                new BenchMarkIO ( "boon original", times, fileName ) {

                    byte[] bytes = readFileAsBytes(fileName);
                    
                    @Override
                    void run () {
                        try (final InputStreamReader reader = new InputStreamReader (
                                new ByteArrayInputStream ( bytes ) )) {

                            char [] chars = new char[bytes.length];
                            reader.read ( chars );
                            JsonParser.fullParseMap ( chars );

                        }catch ( IOException ex ) {
                             ex.printStackTrace ();
                        }

                    }
                },
                new BenchMarkIO ( "boon char sequence ", times, fileName ) {

                    byte[] bytes = readFileAsBytes(fileName);

                    @Override
                    void run () {

                        JsonParserArrayCharSequence.fullParseMap ( new String ( bytes, StandardCharsets.UTF_8 ) );
                    }
                },
                new BenchMarkIO ( "boon ascii  ", times, fileName ) {

                    byte[] bytes = readFileAsBytes(fileName);

                    @Override
                    void run () {

                            JsonAsciiParser.fullParseMap ( bytes );


                    }
                }

        );
    }
}
