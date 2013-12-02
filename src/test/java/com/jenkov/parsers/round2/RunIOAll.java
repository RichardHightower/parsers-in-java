package com.jenkov.parsers.round2;

import static org.boon.Boon.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.boon.IO;
import org.boon.Lists;
import org.boon.Str;
import org.boon.json.JsonLazyEncodeParser;
import org.boon.json.JsonParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class RunIOAll {


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

            puts("___________________________________________________________________________________");
        }

    }

    private static List<BenchMarkIO> buildBenchMarkList(final String fileContents, int times, final String fileName) {

        if (fileContents.length () > 40_000) {
            times /= 1_000;
        }
        return Lists.list (
                new BenchMarkIO ( "jackson", times, fileName ) {
                    ObjectMapper mapper = new ObjectMapper ();

                    @Override
                    void run () {
                        try( BufferedReader fis= new BufferedReader ( new FileReader ( fileName )) ) {
                            Map<String, Object> map = ( Map<String, Object> ) mapper.readValue ( fis, Map.class );
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }
                    }
                },
                new BenchMarkIO ( "gson   ", times, fileName ) {
                    Gson gson = new Gson ();

                    @Override
                    void run () {

                        ;
                        try( FileReader fis= new FileReader ( fileName ) ) {
                            Map<String, Object> map = ( Map<String, Object> ) gson.fromJson ( fis, Map.class );


                        } catch ( IOException e ) {
                            e.getStackTrace ();
                             //die("unable to run gson");
                        }


                    }
                },
                new BenchMarkIO ( "boon 1   ", times, fileName ) {

                    @Override
                    void run () {
                        final char[] chars = IO.readCharBuffer ( IO.path ( fileName ) );

                        Map<String, Object> map = JsonParser.parseMap ( chars );

                    }
                },
                new BenchMarkIO ( "boon full  ", times, fileName ) {

                    @Override
                    void run () {
                        final char[] chars = IO.readCharBuffer ( IO.path ( fileName ) );

                        Map<String, Object> map = JsonParser.fullParseMap ( chars );

                    }
                }
//                ,
//                new BenchMarkIO ( "boon lazy  ", times, fileName ) {
//
//                    @Override
//                    void run () {
//                        final char[] chars = IO.readCharBuffer ( IO.path ( fileName ) );
//                        JsonLazyEncodeParser.fullParseMap ( chars );
//
//                    }
//                }

        );
    }

}
