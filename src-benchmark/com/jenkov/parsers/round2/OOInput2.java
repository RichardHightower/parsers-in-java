package com.jenkov.parsers.round2;

import com.google.gson.Gson;
import org.boon.IO;
import org.boon.Lists;
import org.boon.Str;
import org.boon.json.JsonAsciiParser;
import org.boon.json.JsonLazyAsciiEncodeParser;
import org.boon.json.JsonLazyEncodeParser;
import org.boon.json.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static org.boon.Boon.puts;


public class OOInput2 {

    public static void main( String... args ) {

        String file = "object-serialization/AllTypes.json";
        String fileContents = IO.read ( file );

        List<BenchMark> benchMarks = buildBenchMarkList ( fileContents, 1_000_000 );


        puts ( Str.rpad ( "Name", 20, ' ' ), Str.rpad ( "Time", 20, ' ' ),
                Str.lpad ( "File", 20, ' ' ),
                Str.lpad ( "Iterations", 20, ' ' ) );


        long min = Long.MAX_VALUE;
        String winner = "";

        for (int index = 0; index < 100000; index++)
        for ( BenchMark benchMark : benchMarks ) {
            benchMark.test ();
            if ( benchMark.finalTime < min ) {

                min = benchMark.finalTime;
                winner = benchMark.name;
            }
            puts ( Str.rpad ( benchMark.name, 20, ' ' ),
                    Str.rpad ( String.format ( "%,d", benchMark.finalTime ), 20, ' ' ),
                    Str.lpad ( "" + IO.path ( file ).getFileName (), 20, ' ' ),
                    Str.lpad ( String.format ( "%,d", benchMark.times ), 20, ' ' ) );
        }

        puts ( "Winner:", winner );

        puts ( "___________________________________________________________________________________" );
    }


    private static List<BenchMark> buildBenchMarkList( final String fileContents, int times ) {
        return Lists.list (
                new BenchMark ( "boon lazy c 2", times, fileContents ) {

                    @Override
                    void run() {
                        JsonLazyEncodeParser.parseInto ( AllTypes.class, chars );


                    }
                },
                new BenchMark ( "boon lazy  c 3", times, fileContents ) {

                    @Override
                    void run() {
                        JsonLazyEncodeParser.parseInto ( AllTypes.class, chars );


                    }
                }
        );
    }
}
