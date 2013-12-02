package com.jenkov.parsers.round2;

import java.nio.charset.StandardCharsets;

public abstract class BenchMark {

    String name;
    int times;
    String contents;
    char [] chars;
    byte [] bytes;
    long finalTime;

    public BenchMark( String name, int times, String contents ) {
        this.name = name;
        this.times =times;
        this.contents=contents;
        this.chars = contents.toCharArray ();
        this.bytes = contents.getBytes ( StandardCharsets.UTF_8);
    }




    void test () {
        long startTime = System.nanoTime ();

        for(int i=0; i<times; i++) {
            run();
        }
        long endTime = System.nanoTime ();

        finalTime = (endTime - startTime) / 1_000_000;

    }

    abstract void run () ;



}
