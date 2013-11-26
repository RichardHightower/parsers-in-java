parsers-in-java
===============

A set of JSON parser benchmarks.


JSON Compliance

Using json.org/examples as a guide:

```
testing actionLabel.json 
BOON 1 PASSED actionLabel.json 
BOON 2 PASSED actionLabel.json 
GSON # PASSED actionLabel.json 
INFO Q FAILED actionLabel.json 
JACK 1 PASSED actionLabel.json 
testing menu.json 
BOON 1 PASSED menu.json 
BOON 2 PASSED menu.json 
GSON # PASSED menu.json 
INFO Q FAILED menu.json 
JACK 1 PASSED menu.json 
testing sgml.json 
BOON 1 PASSED sgml.json 
BOON 2 PASSED sgml.json 
GSON # PASSED sgml.json 
INFO Q FAILED sgml.json 
JACK 1 PASSED sgml.json 
testing webxml.json 
BOON 1 PASSED webxml.json 
BOON 2 PASSED webxml.json 
GSON # PASSED webxml.json 
INFO Q FAILED webxml.json 
JACK 1 PASSED webxml.json 
testing widget.json 
BOON 1 PASSED widget.json 
BOON 2 PASSED widget.json 
GSON # PASSED widget.json 
INFO Q FAILED widget.json 
JACK 1 PASSED widget.json 

```

Parse times for small json 10,000,000 runs:

```
GSON:         8,334
JACKSON:      7,156
Boon 2:       2,645
Boon 1:       3,799
InfoQ :      11,431
```

Here is the JSON for the small json file:

```json
{
    "debug": "on\toff",
    "num" : 1

}
```


Source code for benchmark tests:

```java
...
public class BoonBench2Mark {

    public static void main(String[] args) throws IOException {
        String fileName = "data/small.json.txt";
        if(args.length > 0) {
            fileName = args[0];
        }
        System.out.println("parsing: " + fileName);

        DataCharBuffer dataCharBuffer = FileUtil.readFile(fileName);


        int iterations = 10_000_000; //10.000.000 iterations to warm up JIT and minimize one-off overheads etc.
        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(dataCharBuffer);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(DataCharBuffer dataCharBuffer) {
        Map<String, Object> map =  JSONParser2.parseMap ( dataCharBuffer.data );


    }

}
...
public class BoonBenchMark {

    public static void main(String[] args) throws IOException {
        String fileName = "data/small.json.txt";
        if(args.length > 0) {
            fileName = args[0];
        }
        System.out.println("parsing: " + fileName);

        DataCharBuffer dataCharBuffer = FileUtil.readFile(fileName);


        int iterations = 10_000_000; //10.000.000 iterations to warm up JIT and minimize one-off overheads etc.
        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(dataCharBuffer);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(DataCharBuffer dataCharBuffer) {
        Map<String, Object> map =  JSONParser.parseMap ( dataCharBuffer.data );
    }
}
...
public class JacksonBenchmark {


    public static void main(String[] args) throws IOException {
        String fileName = "data/small.json.txt";
        if(args.length > 0) {
            fileName = args[0];
        }
        System.out.println("parsing: " + fileName);

        DataCharBuffer dataCharBuffer = FileUtil.readFile(fileName);
        ObjectMapper objectMapper = new ObjectMapper();

        int iterations = 10_000_000; //10.000.000 iterations to warm up JIT and minimize one-off overheads etc.
        long startTime = System.currentTimeMillis();

        String str = new String (dataCharBuffer.data);
        for(int i=0; i<iterations; i++) {
            parse(str, objectMapper);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(String str, ObjectMapper mapper) {
        try {
            Map<String, Object> map = (Map<String, Object>) mapper.readValue ( str, Map.class );
        } catch ( IOException e ) {
            e.printStackTrace ();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
...
public class GsonBenchmark {

    public static void main(String[] args) throws IOException {
        String fileName = "data/small.json.txt";
        if(args.length > 0) {
            fileName = args[0];
        }
        System.out.println("parsing: " + fileName);

        DataCharBuffer dataCharBuffer = FileUtil.readFile(fileName);
        Gson gson = new Gson();

        int iterations = 10_000_000; //10.000.000 iterations to warm up JIT and minimize one-off overheads etc.
        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(dataCharBuffer, gson);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(DataCharBuffer dataCharBuffer, Gson gson) {
        Map<String, Object> map = (Map<String, Object>) gson.fromJson (
                new CharArrayReader ( dataCharBuffer.data, 0, dataCharBuffer.length ), Map.class );
   }
}
```

Here are the results for the large json file from json.org/examples




