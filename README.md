If you want to turn a JSON file into a java.util.Map, it appears that Boon is the fastest option.
When I first read the article, this was not the case.
I had some ideas for speeding up the JSON parsing, but never seemed to need to (it seemed fast enough).
Then this article came out, http://www.infoq.com/articles/HIgh-Performance-Parsers-in-Java.

I downloaded the source for the benchmark, and ran the benchmarks.

Then I tweaked Boon JSON parser to be faster than GSON.

I also improved compliance testing of the Boon parser, 
and was able to tweak performance by 20x to 25x for Boon parser.

Then I added the performance enhancements that I dreamed about, but never implemented.

It appears it is now faster the Jackson for the use case of turning a JSON string into a java.util.Map.

Boon allows turning a map into a Java object so it can do object serializaiton of JSON, 
but you have to first convert JSON into Map and then Map into Object.

I have not performance tuned this and I am sure GSON and Jackson must be faster.

But tune in later for more benchmarks.


parsers-in-java
===============

A set of JSON parser benchmarks.


JSON Compliance

Using json.org/examples (http://json.org/example) as a guide:

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

For background, read this article on how to write fast parsers.

http://www.infoq.com/articles/HIgh-Performance-Parsers-in-Java

Parse times for small json 10,000,000 runs:

```
GSON:         8,334
JACKSON:      7,156
Boon 2:       2,645
Boon 1:       3,799
InfoQ :      11,431
```

Smaller is better.

Parse times for large json file 1,000,000 runs:
```
Boon 2:     15,543
Boon 1:     19,967
JACKSON:    18,985
InfoQ:      ParserException
GSON:       25,870
```

Lower is better. 


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

Source code for large test:

InfoQ
```java
package com.jenkov.parsers.round2;

import com.jenkov.parsers.FileUtil;
import com.jenkov.parsers.core.DataCharBuffer;
import com.jenkov.parsers.core.IndexBuffer;
import com.jenkov.parsers.json.ElementTypes;
import com.jenkov.parsers.json.JsonParser;
import org.boon.IO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.boon.Exceptions.die;

/**
 */
public class JsonParserBenchMark {

    public static void main(String[] args) throws IOException {

        String fileName = "data/webxml.json";


        String fileContents = IO.read ( fileName );


        JsonParser jsonParser   = new JsonParser();
        IndexBuffer jsonElements = new IndexBuffer(1024, true);

        int iterations = 10_000_000;
        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(new DataCharBuffer ( fileContents.toCharArray () ), jsonParser, jsonElements);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);




    }

    private static void parse(DataCharBuffer dataCharBuffer, JsonParser jsonParser, IndexBuffer jsonElements) {
        jsonParser.parse(dataCharBuffer, jsonElements);

    }

}
```

Jackson
```java
package com.jenkov.parsers.round2;

import org.boon.IO;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class JacksonBenchmark {



    public static void main(String[] args) throws IOException {
        String fileName = "data/webxml.json";


        String fileContents = IO.read ( fileName );

        ObjectMapper mapper = new ObjectMapper();

        int iterations = 1_000_000; //10.000.000 iterations to warm up JIT and minimize one-off overheads etc.
        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(fileContents, mapper);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(String fileContents, ObjectMapper mapper) {
        try {
            Map<String, Object> map = (Map<String, Object>) mapper.readValue ( fileContents, Map.class);
        } catch ( IOException e ) {
            e.printStackTrace ();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

}

```

GSON
```java
package com.jenkov.parsers.round2;

import com.google.gson.Gson;
import org.boon.IO;

import java.io.IOException;
import java.util.Map;

public class GsonBenchMark {


    public static void main(String[] args) throws IOException {
        String fileName = "data/webxml.json";


        String fileContents = IO.read ( fileName );
        Gson gson = new Gson();

        int iterations = 1_000_000; //10.000.000 iterations to warm up JIT and minimize one-off overheads etc.
        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(fileContents, gson);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(String fileContents, Gson gson) {
        Map<String, Object> map = (Map<String, Object>) gson.fromJson (fileContents, Map.class );



    }

}
```

BOON 2
```java
package com.jenkov.parsers.round2;

import org.boon.IO;
import org.boon.json.JSONParser2;

import java.io.IOException;
import java.util.Map;


/**
 */
public class BoonBenchV2Mark {

    public static void main(String[] args) throws IOException {
        String fileName = "data/webxml.json";


        String fileContents = IO.read ( fileName );


        int iterations = 1_000_000; //1.000.000 iterations to warm up JIT and minimize one-off overheads etc.
        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(fileContents);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(String fileContents) {
        Map<String, Object> map =  JSONParser2.parseMap ( fileContents );


    }


}

```

Boon 1
```java
package com.jenkov.parsers.round2;


import org.boon.IO;
import org.boon.json.JSONParser;

import java.io.IOException;
import java.util.Map;


public class BoonV1BenchMark {


    public static void main(String[] args) throws IOException {
        String fileName = "data/webxml.json";


        String fileContents = IO.read ( fileName );


        int iterations = 1_000_000; //1.000.000 iterations to warm up JIT and minimize one-off overheads etc.

        long startTime = System.currentTimeMillis();
        for(int i=0; i<iterations; i++) {
            parse(fileContents);
        }
        long endTime = System.currentTimeMillis();

        long finalTime = endTime - startTime;

        System.out.println("final time: " + finalTime);
    }

    private static void parse(String fileContents) {
        Map<String, Object> map =  JSONParser.parseMap ( fileContents );


    }

}

```


Large json file from json.org examples
```json
{"web-app": {
    "servlet": [
        {
            "servlet-name": "cofaxCDS",
            "servlet-class": "org.cofax.cds.CDSServlet",
            "init-param": {
                "configGlossary:installationAt": "Philadelphia, PA",
                "configGlossary:adminEmail": "ksm@pobox.com",
                "configGlossary:poweredBy": "Cofax",
                "configGlossary:poweredByIcon": "/images/cofax.gif",
                "configGlossary:staticPath": "/content/static",
                "templateProcessorClass": "org.cofax.WysiwygTemplate",
                "templateLoaderClass": "org.cofax.FilesTemplateLoader",
                "templatePath": "templates",
                "templateOverridePath": "",
                "defaultListTemplate": "listTemplate.htm",
                "defaultFileTemplate": "articleTemplate.htm",
                "useJSP": false,
                "jspListTemplate": "listTemplate.jsp",
                "jspFileTemplate": "articleTemplate.jsp",
                "cachePackageTagsTrack": 200,
                "cachePackageTagsStore": 200,
                "cachePackageTagsRefresh": 60,
                "cacheTemplatesTrack": 100,
                "cacheTemplatesStore": 50,
                "cacheTemplatesRefresh": 15,
                "cachePagesTrack": 200,
                "cachePagesStore": 100,
                "cachePagesRefresh": 10,
                "cachePagesDirtyRead": 10,
                "searchEngineListTemplate": "forSearchEnginesList.htm",
                "searchEngineFileTemplate": "forSearchEngines.htm",
                "searchEngineRobotsDb": "WEB-INF/robots.db",
                "useDataStore": true,
                "dataStoreClass": "org.cofax.SqlDataStore",
                "redirectionClass": "org.cofax.SqlRedirection",
                "dataStoreName": "cofax",
                "dataStoreDriver": "com.microsoft.jdbc.sqlserver.SQLServerDriver",
                "dataStoreUrl": "jdbc:microsoft:sqlserver://LOCALHOST:1433;DatabaseName=goon",
                "dataStoreUser": "sa",
                "dataStorePassword": "dataStoreTestQuery",
                "dataStoreTestQuery": "SET NOCOUNT ON;select test='test';",
                "dataStoreLogFile": "/usr/local/tomcat/logs/datastore.log",
                "dataStoreInitConns": 10,
                "dataStoreMaxConns": 100,
                "dataStoreConnUsageLimit": 100,
                "dataStoreLogLevel": "debug",
                "maxUrlLength": 500}},
        {
            "servlet-name": "cofaxEmail",
            "servlet-class": "org.cofax.cds.EmailServlet",
            "init-param": {
                "mailHost": "mail1",
                "mailHostOverride": "mail2"}},
        {
            "servlet-name": "cofaxAdmin",
            "servlet-class": "org.cofax.cds.AdminServlet"},

        {
            "servlet-name": "fileServlet",
            "servlet-class": "org.cofax.cds.FileServlet"},
        {
            "servlet-name": "cofaxTools",
            "servlet-class": "org.cofax.cms.CofaxToolsServlet",
            "init-param": {
                "templatePath": "toolstemplates/",
                "log": 1,
                "logLocation": "/usr/local/tomcat/logs/CofaxTools.log",
                "logMaxSize": "",
                "dataLog": 1,
                "dataLogLocation": "/usr/local/tomcat/logs/dataLog.log",
                "dataLogMaxSize": "",
                "removePageCache": "/content/admin/remove?cache=pages&id=",
                "removeTemplateCache": "/content/admin/remove?cache=templates&id=",
                "fileTransferFolder": "/usr/local/tomcat/webapps/content/fileTransferFolder",
                "lookInContext": 1,
                "adminGroupID": 4,
                "betaServer": true}}],
    "servlet-mapping": {
        "cofaxCDS": "/",
        "cofaxEmail": "/cofaxutil/aemail/*",
        "cofaxAdmin": "/admin/*",
        "fileServlet": "/static/*",
        "cofaxTools": "/tools/*"},

    "taglib": {
        "taglib-uri": "cofax.tld",
        "taglib-location": "/WEB-INF/tlds/cofax.tld"}}}
```


