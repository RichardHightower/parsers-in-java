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
import java.util.Map;

import static org.boon.Boon.puts;

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

            puts("___________________________________________________________________________________");
        }

    }

    private static List<BenchMark> buildBenchMarkList(final String fileContents, int times) {

        if (fileContents.length () > 40_000) {
            times /= 1_000;
        }
        return Lists.list (
                new BenchMark("jackson", times, fileContents) {
                    ObjectMapper mapper = new ObjectMapper();

                    @Override
                    void run() {
                        try {
                            Map<String, Object> map = (Map<String, Object>) mapper.readValue ( fileContents, Map.class);
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }
                    }
                },
                new BenchMark("gson   ", times, fileContents) {
                    Gson gson = new Gson();

                    @Override
                    void run() {
                        Map<String, Object> map = (Map<String, Object>) gson.fromJson (fileContents, Map.class );

                    }
                },
                new BenchMark("boon 1   ", times, fileContents) {

                    @Override
                    void run() {
                        Map<String, Object> map =  JsonParser.parseMap ( chars );

                    }
                },
                new BenchMark("boon full  ", times, fileContents) {

                    @Override
                    void run() {
                        Map<String, Object> map =  JsonParser.fullParseMap ( chars );

                    }
                },

                new BenchMark("boon lazy a", times, fileContents) {

                    @Override
                    void run() {
                        Map<String, Object> map =  JsonLazyAsciiEncodeParser.parseMap ( bytes );

                    }
                },
                new BenchMark("boon lazy c", times, fileContents) {

                    @Override
                    void run() {
                        Map<String, Object> map =  JsonLazyEncodeParser.parseMap ( chars );

                    }
                },
                new BenchMark("boon ascii", times, fileContents) {

                    @Override
                    void run() {
                        Map<String, Object> map =  JsonAsciiParser.parseMap ( bytes );

                    }
                },
                new BenchMark("boon full a", times, fileContents) {

                    @Override
                    void run() {
                        Map<String, Object> map =  JsonAsciiParser.fullParseMap ( bytes );

                    }
                }, new BenchMark("boon lazy c values", times, fileContents) {

                    @Override
                    void run() {
                         JsonLazyEncodeParser.fullParseMapUseValue ( chars );

                    }
                }







        );
    }
}
