package com.jenkov.parsers.round2;


public abstract class BenchMarkIO {

    String name;
    String fileName;

    int times;
    long finalTime;

    public BenchMarkIO( String name, int times, String fileName ) {
        this.name = name;
        this.times =times;
        this.fileName = fileName;
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
