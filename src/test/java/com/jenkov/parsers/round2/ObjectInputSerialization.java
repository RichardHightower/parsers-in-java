package com.jenkov.parsers.round2;


import static org.boon.Boon.*;

import java.io.IOException;
import java.util.List;

import org.boon.IO;
import org.boon.Lists;
import org.boon.Str;
import org.boon.criteria.Sort;
import org.boon.json.JsonLazyEncodeParser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class ObjectInputSerialization {

    public static void main ( String... args ) {

        String file = "object-serialization/AllTypes.json";
        String fileContents = IO.read ( file  );

        List<BenchMark> benchMarks =  buildBenchMarkList ( fileContents, 100_000 );



        puts ("Warming up the monkeys!");




        for (BenchMark benchMark : benchMarks) {
            benchMark.test ();
            puts(".");
        }

        processRun ( file, buildBenchMarkListFullObject ( fileContents, 1_000_000 ),  "Full object" );


        processRun ( file, buildBenchMarkListNoSubListNoSubItem ( fileContents, 1_000_000 ),  "No sub item, no sum list" );


        processRun ( file, buildBenchMarkListNoSublist ( fileContents, 1_000_000 ),  "No sub list" );

    }

    private static void processRun( String file, List<BenchMark> benchMarks, String name ) {

        puts("_______________________________", name ,"____________________________________________");

        puts ( Str.rpad ( "Name", 20, ' ' ),  Str.rpad("Time", 20, ' '),
                Str.lpad ("File",  20, ' '),
                Str.lpad("Iterations", 20, ' '));


        long min = Long.MAX_VALUE;
        String winner = "";

        for (BenchMark benchMark : benchMarks) {
            benchMark.test ();
            if (benchMark.finalTime < min) {

                min = benchMark.finalTime;
                winner = benchMark.name;
            }
            puts ( Str.rpad ( benchMark.name, 20, ' ' ),
                    Str.rpad(String.format("%,d", benchMark.finalTime), 20, ' '),
                    Str.lpad (""+ IO.path ( file ).getFileName (),  20, ' '),
                    Str.lpad( String.format ("%,d", benchMark.times), 20, ' '));
        }

        puts ("Winner:", winner);

        Sort.asc ( "finalTime" ).sort ( benchMarks );

        System.out.print (Str.rpad("order:", 13, ' '));

        for (BenchMark benchMark: benchMarks) {
            System.out.print (Str.rpad(benchMark.name, 15, ' '));
        }
        System.out.println();



        puts("___________________________________________________________________________________");

    }


    private static List<BenchMark> buildBenchMarkList(final String fileContents, int times) {
        return Lists.list (
                new BenchMark ( "jackson", times, fileContents ) {
                    ObjectMapper mapper = new ObjectMapper ();

                    {
                        mapper.configure ( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
                    }

                    @Override
                    void run() {
                        try {
                            mapper.readValue ( fileContents, AllTypes.class );
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }
                    }
                },
                new BenchMark ( "gson   ", times, fileContents ) {
                    Gson gson = new Gson ();

                    @Override
                    void run() {
                        gson.fromJson ( fileContents, AllTypes.class );

                    }
                },
                new BenchMark ( "jackson al2 ", times, fileContents ) {
                    ObjectMapper mapper = new ObjectMapper ();


                    {
                        mapper.configure ( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
                    }


                    @Override
                    void run() {
                        try {
                            mapper.readValue ( fileContents, AllTypes2.class );
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }
                    }
                },
                new BenchMark ( "gson  al2 ", times, fileContents ) {
                    Gson gson = new Gson ();

                    @Override
                    void run() {
                        gson.fromJson ( fileContents, AllTypes2.class );

                    }
                },
                new BenchMark ( "jackson al3 ", times, fileContents ) {
                    ObjectMapper mapper = new ObjectMapper ();

                    {
                        mapper.configure ( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
                    }

                    @Override
                    void run() {
                        try {
                            mapper.readValue ( fileContents, AllTypes3.class );
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }
                    }
                },
                new BenchMark ( "gson  al3 ", times, fileContents ) {
                    Gson gson = new Gson ();

                    @Override
                    void run() {
                        gson.fromJson ( fileContents, AllTypes3.class );

                    }
                },


                new BenchMark ( "boon ", times, fileContents ) {

                    @Override
                    void run() {

                        JsonLazyEncodeParser.parseInto ( AllTypes.class, chars );

                    }
                },
                new BenchMark ( "boon ", times, fileContents ) {

                    @Override
                    void run() {

                        JsonLazyEncodeParser.parseInto ( AllTypes2.class, chars );

                    }
                },
                new BenchMark ( "boon ", times, fileContents ) {

                    @Override
                    void run() {

                        JsonLazyEncodeParser.parseInto ( AllTypes3.class, chars );

                    }
                },

                new BenchMark ( "boon at", times, fileContents ) {

                    @Override
                    void run() {

                        JsonLazyEncodeParser.fullParseInto ( AllTypes.class, chars );

                    }
                },
                new BenchMark ( "boon at2", times, fileContents ) {

                    @Override
                    void run() {

                        JsonLazyEncodeParser.fullParseInto ( AllTypes2.class, chars );

                    }
                },
                new BenchMark ( "boon at3", times, fileContents ) {

                    @Override
                    void run() {

                        JsonLazyEncodeParser.fullParseInto ( AllTypes3.class, chars );

                    }
                }

        );
    }




    private static List<BenchMark> buildBenchMarkListNoSublist(final String fileContents, int times) {
        return Lists.list (
                new BenchMark ( "jackson al2 ", times, fileContents ) {
                    ObjectMapper mapper = new ObjectMapper ();


                    {
                        mapper.configure ( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
                    }


                    @Override
                    void run() {
                        try {
                            mapper.readValue ( fileContents, AllTypes2.class );
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }
                    }
                },
                new BenchMark ( "gson  al2 ", times, fileContents ) {
                    Gson gson = new Gson ();

                    @Override
                    void run() {
                        gson.fromJson ( fileContents, AllTypes2.class );

                    }
                },
                new BenchMark ( "boon al2 half", times, fileContents ) {

                    @Override
                    void run() {

                        JsonLazyEncodeParser.parseInto ( AllTypes2.class, chars );

                    }
                },
                new BenchMark ( "boon al2 full", times, fileContents ) {

                    @Override
                    void run() {

                        JsonLazyEncodeParser.fullParseInto ( AllTypes2.class, chars );

                    }
                }
        );
    }

    private static List<BenchMark> buildBenchMarkListNoSubListNoSubItem(final String fileContents, int times) {
        return Lists.list (
                new BenchMark ( "jackson al3 ", times, fileContents ) {
                    ObjectMapper mapper = new ObjectMapper ();


                    {
                        mapper.configure ( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
                    }


                    @Override
                    void run() {
                        try {
                            mapper.readValue ( fileContents, AllTypes3.class );
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }
                    }
                },
                new BenchMark ( "gson  al3 ", times, fileContents ) {
                    Gson gson = new Gson ();

                    @Override
                    void run() {
                        gson.fromJson ( fileContents, AllTypes3.class );

                    }
                },
                new BenchMark ( "boon al3", times, fileContents ) {

                    @Override
                    void run() {

                        JsonLazyEncodeParser.parseInto ( AllTypes3.class, chars );

                    }
                },
                new BenchMark ( "boon al3 full", times, fileContents ) {

                    @Override
                    void run() {

                        JsonLazyEncodeParser.fullParseInto ( AllTypes3.class, chars );

                    }
                }
        );
    }

    private static List<BenchMark> buildBenchMarkListFullObject(final String fileContents, int times) {
        return Lists.list (
                new BenchMark ( "jackson", times, fileContents ) {
                    ObjectMapper mapper = new ObjectMapper ();

                    {
                        mapper.configure ( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
                    }

                    @Override
                    void run() {
                        try {
                            mapper.readValue ( fileContents, AllTypes.class );
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }
                    }
                },
                new BenchMark ( "gson   ", times, fileContents ) {
                    Gson gson = new Gson ();

                    @Override
                    void run() {
                        gson.fromJson ( fileContents, AllTypes.class );

                    }
                },
                new BenchMark ( "boon ", times, fileContents ) {

                    @Override
                    void run() {

                        JsonLazyEncodeParser.parseInto ( AllTypes.class, chars );

                    }
                },
                new BenchMark ( "boon full", times, fileContents ) {

                    @Override
                    void run() {

                        JsonLazyEncodeParser.fullParseInto ( AllTypes.class, chars );

                    }
                }

        );
    }
}
