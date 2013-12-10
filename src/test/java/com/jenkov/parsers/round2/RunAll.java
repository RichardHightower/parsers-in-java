package com.jenkov.parsers.round2;

import static org.boon.Boon.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.boon.IO;
import org.boon.Lists;
import org.boon.Str;
import org.boon.criteria.Sort;
import org.boon.json.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class RunAll {

    public static void main (String ... args) {

        List<String> list =  IO.listByExt ( "data", ".json" );


        for (String file : list) {

            String fileContents = IO.read ( file );

            List<BenchMark> benchMarks =  buildBenchMarkList( fileContents, 100_000 );


            puts (Str.rpad ( "Name", 20, ' '),  Str.rpad("Time", 20, ' '),
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
                puts (Str.rpad ( benchMark.name, 20, ' '),
                        Str.rpad(String.format("%,d", benchMark.finalTime), 20, ' '),
                        Str.lpad (""+ IO.path (file).getFileName (),  20, ' '),
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

    }

    private static List<BenchMark> buildBenchMarkList(final String fileContents, int times) {

        if (fileContents.length () > 40_000) {
            times /= 100;
        }
        return Lists.list (
                new BenchMark("jackson", times, fileContents) {
                    ObjectMapper mapper = new ObjectMapper();

                    @Override
                    void run() {
                        try {
                             mapper.readValue ( fileContents, Map.class);
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }
                    }
                },
                new BenchMark("gson   ", times, fileContents) {
                    Gson gson = new Gson();

                    @Override
                    void run() {
                         gson.fromJson ( fileContents, Map.class );

                    }
                },
                new BenchMark("boon char sequence", times, fileContents) {

                    @Override
                    void run() {
                        new JsonParserFactory ().preferCharSequence ().create ().parse ( Map.class, contents );

                    }
                },
                new BenchMark("boon original ", times, fileContents) {

                    @Override
                    void run() {
                        new JsonParserFactory ().create ().parse ( Map.class, chars );

                    }
                },
                new BenchMark("boon utf8", times, fileContents) {

                    @Override
                    void run() {
                        new JsonParserFactory ().useDirectBytes ().create ().parse ( Map.class, chars );

                    }
                },
                new BenchMark ( "Boon Index Overlay   ", times, fileContents ) {


                    @Override
                    void run () {

                        new JsonParserFactory ().useOverlay ().create ().parse ( Map.class, chars );


                    }
                }





        );
    }
}
