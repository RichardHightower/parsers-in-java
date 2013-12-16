12/15/2013

This just in from the Gatling guy from France (a.k.a. Stephane). So not just the fastest, but independenty verified. :)
```
Benchmark                               Mode Thr     Count  Sec         Mean   Mean error    Units
1  BoonCharArrayBenchmark.roundRobin      thrpt  16        10    1   810895,127   102746,330    ops/s
2  JsonSmartBytesBenchmark.roundRobin     thrpt  16        10    1   582712,022    55424,984    ops/s
3  BoonDirectBytesBenchmark.roundRobin    thrpt  16        10    1   577522,193    38627,510    ops/s
4  JsonSmartStringBenchmark.roundRobin    thrpt  16        10    1   566789,030    42245,984    ops/s
5  JacksonObjectBenchmark.roundRobin      thrpt  16        10    1   554903,552   251024,921    ops/s
6  GSONStringBenchmark.roundRobin         thrpt  16        10    1   517546,880    68706,631    ops/s
7  JacksonASTBenchmark.roundRobin         thrpt  16        10    1   495469,035   299158,957    ops/s
8  GSONReaderBenchmark.roundRobin         thrpt  16        10    1   432468,228    51960,414    ops/s
9  JsonSmartStreamBenchmark.roundRobin    thrpt  16        10    1   284085,723   136069,599    ops/s
10 JsonSmartReaderBenchmark.roundRobin    thrpt  16        10    1    91780,987    24235,931    ops/s
```

This from FastJson (sleepless in Hayward).

```
Rick's "Boon" small test

Rick's "Boon" small test, slightly modified (deserializing x times the JSON contained in the boon-small.json.txt file = 79 bytes) - with POCO target (1 class):

10,000,000 iterations: in ~ 28.1 seconds
vs. Microsoft's JavaScriptSerializer in ~ 302.3 seconds (bad omen #1)
vs. JSON.NET in ~ 56.5 seconds
vs. ServiceStack in ~ 40.7 seconds
(Which yields System.Text.Json.JsonParser's throughput : 28,090,886 bytes / second)
the same Rick's "Boon" small test, this time with "loosely typed" deserialization (no POCO target, just dictionaries and lists - read above):

10,000,000 iterations: in ~ 34.7 seconds
vs. Microsoft's JavaScriptSerializer in ~ 232.2 seconds (bad omen #2)
vs. JSON.NET in ~ 72.4 seconds
vs. ServiceStack in... N / A
(Which yields System.Text.Json.JsonParser's throughput : 22,739,702 bytes / second)
Rick's original test can be found at:

http://rick-hightower.blogspot.com/2013/11/benchmark-for-json-parsing-boon-scores.html

Note Rick is one of our fellows from the Java realm - and from his own comparative figures that I eventually noticed, I take it Rick's "Boon" is pretty darn fast among them guys' Java toolboxes for JSON... That'd almost make a .NET / CLR dude like me jealous of Java... ;)
```
I never meant to make you jealous. :) What is this CLR thing that you speak of? :)


So it looks like boon comes in 1st and 3rd on this test. But look at first again. 
It is way ahead of the pack.
Also note that there is an Index Overlay option that is a bit faster. :)
Congrats to JsonSmart. Clearly, I need to tune the direct byte handling a bit.
I think Boon can get 1st (convert to char[] and then parse), 2nd (direct bytes) and 3rd (index overlay), but no rush.

If you want to turn a JSON file into a java.util.Map, it appears that Boon is the fastest option.
When I first read the article, this was not the case.
I had some ideas for speeding up the JSON parsing, but never seemed to need to (it seemed fast enough).
Then this article came out, http://www.infoq.com/articles/HIgh-Performance-Parsers-in-Java.


If you want independent confirmation of Boon's speed:
"Jackson and Boon are basically equivalent, Boon being slightly faster. JSON Parsing." Whoot!
https://github.com/gatling/json-parsers-benchmark

I forked the above and test Boon against this.
https://github.com/RichardHightower/json-parsers-benchmark

I downloaded the source for the benchmark, and ran the benchmarks.

Then I tweaked Boon JSON parser to be faster than GSON.

I also improved compliance testing of the Boon parser, 
and was able to tweak performance by 20x to 25x for Boon parser.

Then I added the performance enhancements that I dreamed about, but never implemented.

It appears it is now faster than Jackson and GSON for the use case of turning a JSON string into a java.util.Map.
(now with I/O, without I/O, and directo from byte [], faster, faster, and faster).

Boon allows turning a map into a Java object so it can do object serializaiton of JSON, 
but you have to first convert JSON into Map and then Map into Object.

I have not performance tuned full Object serialization this and I am sure GSON and Jackson must be faster.
Boon plans on supporting this so the plan is for it be very fast if not the fastest (eventually).

Update: I have performance tuned the hell out of Boon object serialization and it is quite a bit faster than GSON and Jackson! I put blood sweat, tears, profiling and code into it. 

For now, it is just the fastest at serialization to a map.

Update: more time was spent on Object serailization than to java.util.Map conversion. I think it can be even faster.

Boon does not plan on being a pull parser or a tree parser or a event parser... ever.
Boon does JSON but not all the ins and outs.
If you want a pull parser, use Jackson or GSON or json-smart.
If you want a tree view parser, use Jackson or GSON or json-smart.

Boon is optimized for REST calls and Websocket messages. It is not a generic JSON parser.
I don't want it to be. I don't care. 

Boon JSON parsing... would not be as good as it is without some help from my new brother from France.
He is my muse and has also improved the hell out of the benchmarking. 
Thank you sir!

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


Dec 3rd (near midnight)

Boon optimization once more....
Now Boon wins in every category and by a fairly wide margin.
Boon has four parser for different types of buffers and an index overlay parser.
It can parse direct from CharSequence, CharacterBuffer, StringBuilder, String, byte[] and char[].


Preferred buffer test:

```
Name                 Time                                 File           Iterations
jackson              1,247                    actionLabel.json              100,000
gson                 1,080                    actionLabel.json              100,000
boon char sequence   710                      actionLabel.json              100,000
boon original        586                      actionLabel.json              100,000
boon ascii           867                      actionLabel.json              100,000
Boon Index Overlay    497                      actionLabel.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  boon char sequence, boon ascii,     gson,           jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              1,238                   citm_catalog.json                  100
gson                 760                     citm_catalog.json                  100
boon char sequence   927                     citm_catalog.json                  100
boon original        698                     citm_catalog.json                  100
boon ascii           785                     citm_catalog.json                  100
Boon Index Overlay    568                     citm_catalog.json                  100
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  gson,           boon ascii,     boon char sequence, jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              1,279                         medium.json              100,000
gson                 1,225                         medium.json              100,000
boon char sequence   885                           medium.json              100,000
boon original        790                           medium.json              100,000
boon ascii           1,333                         medium.json              100,000
Boon Index Overlay    657                           medium.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  boon char sequence, gson,           jackson,        boon ascii
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              257                             menu.json              100,000
gson                 258                             menu.json              100,000
boon char sequence   176                             menu.json              100,000
boon original        158                             menu.json              100,000
boon ascii           223                             menu.json              100,000
Boon Index Overlay    129                             menu.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  boon char sequence, boon ascii,     jackson,        gson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              450                             sgml.json              100,000
gson                 373                             sgml.json              100,000
boon char sequence   300                             sgml.json              100,000
boon original        277                             sgml.json              100,000
boon ascii           427                             sgml.json              100,000
Boon Index Overlay    222                             sgml.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  boon char sequence, gson,           boon ascii,     jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              81                             small.json              100,000
gson                 83                             small.json              100,000
boon char sequence   150                            small.json              100,000
boon original        37                             small.json              100,000
boon ascii           39                             small.json              100,000
Boon Index Overlay    21                             small.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  boon ascii,     jackson,        gson,           boon char sequence
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              2,189                         webxml.json              100,000
gson                 2,401                         webxml.json              100,000
boon char sequence   1,843                         webxml.json              100,000
boon original        1,650                         webxml.json              100,000
boon ascii           2,742                         webxml.json              100,000
Boon Index Overlay    1,285                         webxml.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  boon char sequence, jackson,        gson,           boon ascii
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              471                           widget.json              100,000
gson                 468                           widget.json              100,000
boon char sequence   365                           widget.json              100,000
boon original        300                           widget.json              100,000
boon ascii           455                           widget.json              100,000
Boon Index Overlay    242                           widget.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  boon char sequence, boon ascii     gson,           jackson
```


From byte array!

```
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       1,300                    actionLabel.json              100,000
json-smart           941                      actionLabel.json              100,000
gson                 990                      actionLabel.json              100,000
boon original        617                      actionLabel.json              100,000
boon char sequence   679                      actionLabel.json              100,000
boon ascii           761                      actionLabel.json              100,000
Boon Index Overlay    521                      actionLabel.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  boon char sequence, boon ascii,     json-smart,     gson           jackson-object
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       1,087                   citm_catalog.json                  100
json-smart           1,082                   citm_catalog.json                  100
gson                 843                     citm_catalog.json                  100
boon original        1,191                   citm_catalog.json                  100
boon char sequence   1,384                   citm_catalog.json                  100
boon ascii           754                     citm_catalog.json                  100
Boon Index Overlay    1,079                   citm_catalog.json                  100
Winner: boon ascii
order:       boon ascii,     gson,           Boon Index Overlay,   json-smart,     jackson-object, boon original,  boon char sequence
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       1,054                         medium.json              100,000
json-smart           1,355                         medium.json              100,000
gson                 1,362                         medium.json              100,000
boon original        958                           medium.json              100,000
boon char sequence   1,029                         medium.json              100,000
boon ascii           1,356                         medium.json              100,000
Boon Index Overlay    820                           medium.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  boon char sequence, jackson-object, json-smart,     boon ascii     gson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       229                             menu.json              100,000
json-smart           250                             menu.json              100,000
gson                 338                             menu.json              100,000
boon original        210                             menu.json              100,000
boon char sequence   226                             menu.json              100,000
boon ascii           233                             menu.json              100,000
Boon Index Overlay    172                             menu.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  boon char sequence, jackson-object, boon ascii,     json-smart,     gson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       370                             sgml.json              100,000
json-smart           475                             sgml.json              100,000
gson                 530                             sgml.json              100,000
boon original        346                             sgml.json              100,000
boon char sequence   356                             sgml.json              100,000
boon ascii           400                             sgml.json              100,000
Boon Index Overlay    304                             sgml.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  boon char sequence, jackson-object, boon ascii,     json-smart,     gson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       63                             small.json              100,000
json-smart           68                             small.json              100,000
gson                 163                            small.json              100,000
boon original        65                             small.json              100,000
boon char sequence   66                             small.json              100,000
boon ascii           36                             small.json              100,000
Boon Index Overlay    49                             small.json              100,000
Winner: boon ascii
order:       boon ascii,     Boon Index Overlay,   jackson-object, boon original,  boon char sequence, json-smart     gson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       1,951                         webxml.json              100,000
json-smart           2,535                         webxml.json              100,000
gson                 2,699                         webxml.json              100,000
boon original        1,864                         webxml.json              100,000
boon char sequence   2,120                         webxml.json              100,000
boon ascii           2,735                         webxml.json              100,000
Boon Index Overlay    1,535                         webxml.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  jackson-objec,t boon char sequence, json-smart,     gson           boon ascii
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       414                           widget.json              100,000
json-smart           544                           widget.json              100,000
gson                 578                           widget.json              100,000
boon original        385                           widget.json              100,000
boon char sequence   403                           widget.json              100,000
boon ascii           447                           widget.json              100,000
Boon Index Overlay    304                           widget.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay,   boon original,  boon char sequence, jackson-object, boon ascii,     json-smart     gson
```


With I/O in the same call area.

```
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              3,006                    actionLabel.json              100,000
gson                 2,602                    actionLabel.json              100,000
boon original        2,869                    actionLabel.json              100,000
boon char sequence   2,277                    actionLabel.json              100,000
boon ascii           1,732                    actionLabel.json              100,000
Boon Index Overlay    2,026                    actionLabel.json              100,000
Winner: boon ascii
order:       boon ascii     Boon Index Overlay   boon char sequence  gson           boon original  jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              1,472                   citm_catalog.json                  100
gson                 928                     citm_catalog.json                  100
boon original        919                     citm_catalog.json                  100
boon char sequence   1,142                   citm_catalog.json                  100
boon ascii           1,024                   citm_catalog.json                  100
Boon Index Overlay    798                     citm_catalog.json                  100
Winner: Boon Index Overlay
order:       Boon Index Overlay   boon original  gson           boon ascii     boon char sequence  jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              3,484                         medium.json              100,000
gson                 3,035                         medium.json              100,000
boon original        2,596                         medium.json              100,000
boon char sequence   2,681                         medium.json              100,000
boon ascii           2,548                         medium.json              100,000
Boon Index Overlay    2,327                         medium.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay   boon ascii     boon original  boon char sequence  gson           jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              2,167                           menu.json              100,000
gson                 1,894                           menu.json              100,000
boon original        1,739                           menu.json              100,000
boon char sequence   1,722                           menu.json              100,000
boon ascii           1,122                           menu.json              100,000
Boon Index Overlay    1,582                           menu.json              100,000
Winner: boon ascii
order:       boon ascii     Boon Index Overlay   boon char sequence  boon original  gson           jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              2,422                           sgml.json              100,000
gson                 2,063                           sgml.json              100,000
boon original        1,793                           sgml.json              100,000
boon char sequence   1,838                           sgml.json              100,000
boon ascii           1,325                           sgml.json              100,000
Boon Index Overlay    1,720                           sgml.json              100,000
Winner: boon ascii
order:       boon ascii     Boon Index Overlay   boon original  boon char sequence  gson           jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              1,870                          small.json              100,000
gson                 1,706                          small.json              100,000
boon original        1,476                          small.json              100,000
boon char sequence   1,570                          small.json              100,000
boon ascii           926                            small.json              100,000
Boon Index Overlay    1,399                          small.json              100,000
Winner: boon ascii
order:       boon ascii     Boon Index Overlay   boon original  boon char sequence  gson           jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              4,639                         webxml.json              100,000
gson                 4,393                         webxml.json              100,000
boon original        3,489                         webxml.json              100,000
boon char sequence   3,869                         webxml.json              100,000
boon ascii           4,293                         webxml.json              100,000
Boon Index Overlay    3,160                         webxml.json              100,000
Winner: Boon Index Overlay
order:       Boon Index Overlay   boon original  boon char sequence  boon ascii     gson           jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              2,476                         widget.json              100,000
gson                 2,140                         widget.json              100,000
boon original        1,858                         widget.json              100,000
boon char sequence   1,918                         widget.json              100,000
boon ascii           1,336                         widget.json              100,000
Boon Index Overlay    1,742                         widget.json              100,000
Winner: boon ascii
order:       boon ascii     Boon Index Overlay   boon original  boon char sequence  gson           jackson
___________________________________________________________________________________
```


Not too bad.

Dec 3rd 2013
My new friend from France has been helping me out. Now there are more tests.
I got rid of a few parsers and wrote a few more.
The Boon original has been optimized quite a bit.

With I/O included

```
Name                 Time                                 File           Iterations
jackson              5,044                    actionLabel.json              100,000
gson                 3,904                    actionLabel.json              100,000
boon original        3,941                    actionLabel.json              100,000
boon char sequence   4,542                    actionLabel.json              100,000
boon ascii           2,258                    actionLabel.json              100,000
Winner: boon ascii
order:       boon ascii     gson           boon original  boon char sequence  jackson

Boon 1st, 3rd, 4th
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              1,577                   citm_catalog.json                  100
gson                 1,048                   citm_catalog.json                  100
boon original        1,156                   citm_catalog.json                  100
boon char sequence   3,769                   citm_catalog.json                  100
boon ascii           1,281                   citm_catalog.json                  100
Winner: gson
order:       gson           boon original  boon ascii     jackson        boon char sequence

Boon 2nd, 3rd, 5th
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              4,016                         medium.json              100,000
gson                 3,661                         medium.json              100,000
boon original        3,011                         medium.json              100,000
boon char sequence   5,709                         medium.json              100,000
boon ascii           2,949                         medium.json              100,000
Winner: boon ascii
order:       boon ascii     boon original  gson           jackson        boon char sequence

Boon 1st, 2nd, 5th (Boon Char Sequence is new and needs some tuning)
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              2,417                           menu.json              100,000
gson                 2,119                           menu.json              100,000
boon original        1,899                           menu.json              100,000
boon char sequence   2,184                           menu.json              100,000
boon ascii           1,218                           menu.json              100,000
Winner: boon ascii
order:       boon ascii     boon original  gson           boon char sequence  jackson

Boon 1st, 2nd, 4th

___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              2,751                           sgml.json              100,000
gson                 2,250                           sgml.json              100,000
boon original        1,988                           sgml.json              100,000
boon char sequence   2,717                           sgml.json              100,000
boon ascii           1,424                           sgml.json              100,000
Winner: boon ascii
order:       boon ascii     boon original  gson           boon char sequence  jackson

Boon 1st, 2nd, 4th

___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              1,988                          small.json              100,000
gson                 1,780                          small.json              100,000
boon original        1,668                          small.json              100,000
boon char sequence   1,569                          small.json              100,000
boon ascii           1,002                          small.json              100,000
Winner: boon ascii
order:       boon ascii     boon char sequence  boon original  gson           jackson


Boon 1st, 2nd, 3rd
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              4,860                         webxml.json              100,000
gson                 4,759                         webxml.json              100,000
boon original        4,452                         webxml.json              100,000
boon char sequence   8,738                         webxml.json              100,000
boon ascii           4,443                         webxml.json              100,000
Winner: boon ascii
order:       boon ascii     boon original  gson           jackson        boon char sequence


Boon 1st, 2nd, 5th
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              2,608                         widget.json              100,000
gson                 2,296                         widget.json              100,000
boon original        2,015                         widget.json              100,000
boon char sequence   2,734                         widget.json              100,000
boon ascii           1,483                         widget.json              100,000
Winner: boon ascii
order:       boon ascii     boon original  gson           jackson        boon char sequence

Boon 1st, 2nd, 5th
___________________________________________________________________________________
```

I am pretty sure I can make boon ascii and boon char sequence a lot faster when I get some time.
I think I can shave another 20% to 30% off of their times.
Boon original is fully optimized. All of the above times are full parse mode.

My new best friend from France sent me a patch so it already does better than the above.
So look at the above, and imagine boon winning in a few more cases.... :)

(There is a skip string encoding method of boon that is faster, but that is cheating).
All of my optimization and profiling went into object serialization so I have not spent any time
tuning these to a great amount. Boon original is faster because I copied the techniques
from the object serialization but did not re-tune it for this use case.
What I am saying is that I think they can be tuned to go much faster.


With that said... boon wins in every category below. :)

No I/O buffer of choice:


```
Name                 Time                                 File           Iterations
jackson              1,539                    actionLabel.json              100,000
gson                 1,167                    actionLabel.json              100,000
boon char sequence   1,080                    actionLabel.json              100,000
boon original        743                      actionLabel.json              100,000
boon ascii           979                      actionLabel.json              100,000
Winner: boon original
order:       boon original  boon ascii     boon char sequence gson           jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              1,358                   citm_catalog.json                  100
gson                 830                     citm_catalog.json                  100
boon char sequence   1,120                   citm_catalog.json                  100
boon original        823                     citm_catalog.json                  100
boon ascii           929                     citm_catalog.json                  100
Winner: boon original
order:       boon original  gson           boon ascii     boon char sequence jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              1,409                         medium.json              100,000
gson                 1,295                         medium.json              100,000
boon char sequence   1,386                         medium.json              100,000
boon original        985                           medium.json              100,000
boon ascii           1,509                         medium.json              100,000
Winner: boon original
order:       boon original  gson           boon char sequencejackson        boon ascii
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              309                             menu.json              100,000
gson                 282                             menu.json              100,000
boon char sequence   275                             menu.json              100,000
boon original        194                             menu.json              100,000
boon ascii           274                             menu.json              100,000
Winner: boon original
order:       boon original  boon ascii     boon char sequence gson           jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              480                             sgml.json              100,000
gson                 416                             sgml.json              100,000
boon char sequence   450                             sgml.json              100,000
boon original        357                             sgml.json              100,000
boon ascii           450                             sgml.json              100,000
Winner: boon original
order:       boon original  gson           boon char sequence boon ascii     jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              104                            small.json              100,000
gson                 86                             small.json              100,000
boon char sequence   55                             small.json              100,000
boon original        38                             small.json              100,000
boon ascii           51                             small.json              100,000
Winner: boon original
order:       boon original  boon ascii     boon char sequence gson           jackson
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              2,544                         webxml.json              100,000
gson                 2,683                         webxml.json              100,000
boon char sequence   2,750                         webxml.json              100,000
boon original        2,013                         webxml.json              100,000
boon ascii           3,151                         webxml.json              100,000
Winner: boon original
order:       boon original  jackson        gson           boon char sequence boon ascii
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              554                           widget.json              100,000
gson                 528                           widget.json              100,000
boon char sequence   550                           widget.json              100,000
boon original        387                           widget.json              100,000
boon ascii           542                           widget.json              100,000
Winner: boon original
order:       boon original  gson           boon ascii     boon char sequence jackson
___________________________________________________________________________________
```


Now what if you don't have a stream, but you just have a byte[]. How does boon do?
Quite well thanks! :)

Straight from byte[]
```
Name                 Time                                 File           Iterations
jackson-object       1,279                    actionLabel.json              100,000
json-smart           945                      actionLabel.json              100,000
gson                 1,146                    actionLabel.json              100,000
boon original        871                      actionLabel.json              100,000
boon char sequence   1,089                    actionLabel.json              100,000
boon ascii           906                      actionLabel.json              100,000
Winner: boon original
order:       boon original  boon ascii     json-smart     boon char sequence gson           jackson-object

1st, 2nd and 4th place!
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       1,014                   citm_catalog.json                  100
json-smart           1,015                   citm_catalog.json                  100
gson                 882                     citm_catalog.json                  100
boon original        963                     citm_catalog.json                  100
boon char sequence   1,606                   citm_catalog.json                  100
boon ascii           913                     citm_catalog.json                  100
Winner: gson
order:       gson           boon ascii     boon original  jackson-object json-smart     boon char sequence

Damn you GSON! 2nd, 3rd and 5th place
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       1,203                         medium.json              100,000
json-smart           1,171                         medium.json              100,000
gson                 1,429                         medium.json              100,000
boon original        1,184                         medium.json              100,000
boon char sequence   1,394                         medium.json              100,000
boon ascii           1,417                         medium.json              100,000
Winner: json-smart
order:       json-smart     boon original  jackson-object boon char sequence boon ascii     gson

Damn you json-smart! 2nd, 3rd and 4th place
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       246                             menu.json              100,000
json-smart           252                             menu.json              100,000
gson                 363                             menu.json              100,000
boon original        280                             menu.json              100,000
boon char sequence   285                             menu.json              100,000
boon ascii           232                             menu.json              100,000
Winner: boon ascii
order:       boon ascii     jackson-object json-smart     boon original  boon char sequence gson

1st, 3rd and 4th place

___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       412                             sgml.json              100,000
json-smart           421                             sgml.json              100,000
gson                 733                             sgml.json              100,000
boon original        620                             sgml.json              100,000
boon char sequence   538                             sgml.json              100,000
boon ascii           449                             sgml.json              100,000
Winner: jackson-object
order:       jackson-object json-smart     boon ascii     boon char sequence boon original  gson

Jackson! Damn you! 3rd, 4th, 5th place
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       64                             small.json              100,000
json-smart           71                             small.json              100,000
gson                 182                            small.json              100,000
boon original        132                            small.json              100,000
boon char sequence   59                             small.json              100,000
boon ascii           42                             small.json              100,000
Winner: boon ascii
order:       boon ascii     boon char sequence jackson-object json-smart     boon original  gson

Boon baby! Sonic boon! 1st, 2nd, 4th place
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       2,166                         webxml.json              100,000
json-smart           2,260                         webxml.json              100,000
gson                 2,763                         webxml.json              100,000
boon original        2,329                         webxml.json              100,000
boon char sequence   2,945                         webxml.json              100,000
boon ascii           2,904                         webxml.json              100,000
Winner: jackson-object
order:       jackson-object json-smart     boon original  gson           boon ascii     boon char sequence

Jackson and json-smart kicking some serious behind! 3rd, 4th and 5th

___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson-object       435                           widget.json              100,000
json-smart           558                           widget.json              100,000
gson                 593                           widget.json              100,000
boon original        525                           widget.json              100,000
boon char sequence   558                           widget.json              100,000
boon ascii           489                           widget.json              100,000
Winner: jackson-object
order:       jackson-object boon ascii     boon original  json-smart     boon char sequence gson
___________________________________________________________________________________


 Jackson! Grrr... 1st, 2nd, 4th place

```


So boon does well for not being profiled for these use cases.
I know boon ascii and boon char sequence can be optimized (there are a few buffer copies that I can get rid of).
I think with a bit of elbow grease Boon, although it does well now, can do much better.
I am sure it can win (for at least 15 minutes until Jackson, and Json-smart optimize theirs) in every category.

Boon can easily win in every category. It does so well. I don't even like showing those results.

Boon like the article implements an Index Overlay Parser Design that formats the data tokens to look like Map/List but does a lazy final realization of the final form into its final form to optimize the final encoding based on the known required type... In the Map list form, it gets realized when you ask for the Map item (map.get("foo")) or the list item (list.get(index)). It screams and does not do final parse until you are ready for it. This would be amazing to use inside of a Servlet request handler. When the request goes away the whole JSON map is gone anyway so if you don't use, it just goes away. It would not be good to use and then keep it aournd because it holds a reference to the orginal buffer, which is not good. It is only meant for transient objects. It is also really nice for Object Serialization because after you are done, the whole thing gets pitched and you can customize the parse based on the data type of the property of the object. It is also wicked fast and blows Jackson out of the water, which even if not useful (which it is)... it is sure fun!

Ok so for the case that I did optimize and profile boon for, how does it fair.
This is the one I put blood, sweat and tears into. This is the one that matters the most for boon.

Object Serialization...

```

_______________________________ Full object ____________________________________________
Name                 Time                                 File           Iterations
jackson              13,289                      AllTypes.json            1,000,000
gson                 11,092                      AllTypes.json            1,000,000
boon                 8,851                       AllTypes.json            1,000,000
boon full            8,926                       AllTypes.json            1,000,000
Winner: boon
order:       boon           boon full      gson           jackson
___________________________________________________________________________________
_______________________________ No sub item, no sum list ____________________________________________
Name                 Time                                 File           Iterations
jackson al3          11,230                      AllTypes.json            1,000,000
gson  al3            7,565                       AllTypes.json            1,000,000
boon al3             6,653                       AllTypes.json            1,000,000
boon al3 full        6,638                       AllTypes.json            1,000,000
Winner: boon al3 full
order:       boon al3 full  boon al3       gson  al3      jackson al3
___________________________________________________________________________________
_______________________________ No sub list ____________________________________________
Name                 Time                                 File           Iterations
jackson al2          12,035                      AllTypes.json            1,000,000
gson  al2            8,599                       AllTypes.json            1,000,000
boon al2 half        7,216                       AllTypes.json            1,000,000
boon al2 full        7,287                       AllTypes.json            1,000,000
Winner: boon al2 half
order:       boon al2 half  boon al2 full  gson  al2      jackson al2
___________________________________________________________________________________

```

The one I actually spent time on (a lot of time) wins in every category.
I was expecting to get more competition from Jackson and less from GSON.
GSON was a tougher nut to crack.
If it wasn't for GSON, I would live a few years longer.
Boon and Gson were neck and neck,
what put Boon over the top was looking at how Jackson handled numbers, and then duplicated it.

I guess what I am saying is the guys at GSON, Jackson and json-smart are good guys, and their software is good stuff.
I have used Jackson in anger and have very high respect for it.
Boon is not a JSON parsing project.
Boon is something else.
It includes JSON, but every time I mentioned JSON the response I always got was "why don't you just use Jackson".
My answer was because I want to use JSON in different ways, and want flexibility.
Then I would get : "but Jackson is so fast".
This is why I spent sometime tuning Boon's JSON support.
I figure if it is a little faster now, I can focus on other stuff, and let the JSON guys beat it.
It was fastest once.

That said... there is some low hanging fruit, and I might do a round of tuning in the JSON to java.util.Map arena.



Dec 1st 2013
I took a look at the way Jackson was doing int parsing. Nice. I borrowed this idea, and put it into Boon's String parsing lib,
and now I use it for byte, short, long and int. I also expanded on the idea and applied it to float and double after some
prototyping so earlier in the day... Boon was winning by a mere 100 ms due to the late bound magic of Value place holder.

Now, Boon is winning by 1 second and sometimes 2 seconds so... It is way ahead.
I have some elbow room to add feature w/o getting clobbered (for a bit) in performance.
```

_______________________________ Full object ____________________________________________
Name                 Time                                 File           Iterations
jackson              8,626                       AllTypes.json            1,000,000
gson                 9,460                       AllTypes.json            1,000,000
boon lazy c 1        7,455                       AllTypes.json            1,000,000
boon full c 1        7,455                       AllTypes.json            1,000,000
Winner: boon lazy c 1
___________________________________________________________________________________

```

Beats gson by a full two seconds!  And Jackson by a full 1.2 seconds.


```
_______________________________ No sub item, no sum list ____________________________________________
Name                 Time                                 File           Iterations
jackson al3          6,943                       AllTypes.json            1,000,000
gson  al3            6,445                       AllTypes.json            1,000,000
boon lazy c3         5,669                       AllTypes.json            1,000,000
boon full c3         5,680                       AllTypes.json            1,000,000
Winner: boon lazy c3
___________________________________________________________________________________
```

Here boon beats Jackson by 1.5 seconds and gson by 1.3.

```
_______________________________ No sub list ____________________________________________
Name                 Time                                 File           Iterations
jackson al2          7,583                       AllTypes.json            1,000,000
gson  al2            7,188                       AllTypes.json            1,000,000
boon lazy c 2        6,140                       AllTypes.json            1,000,000
boon full c 2        6,141                       AllTypes.json            1,000,000
Winner: boon lazy c 2
___________________________________________________________________________________

```


Here boon beats Jackson by 1.4 seconds and gson by 1 second.

Please note how fast GSON is. Everyone assumes Jackson is the fastest. I am not sure.

GSON is pretty darn fast too. Boon currently screams.

So now it is just clean up the API and make it more useable (it was always useable).



Boon is now dominating the JSON Object serialization benchmarks. :)
I needed a test to say "yeah boon is not slow". Now I have it. :)
Boon does not have all of the bells and whistles but it can do Object serialization.
(From JSON to Java at the moment, and from JSON to java.util.Map).


```
_______________________________ Full object ____________________________________________
Name                 Time                                 File           Iterations
jackson              8,589                       AllTypes.json            1,000,000
gson                 9,224                       AllTypes.json            1,000,000
boon lazy c 1        8,366                       AllTypes.json            1,000,000
boon full c 1        8,534                       AllTypes.json            1,000,000
Winner: boon lazy c 1
___________________________________________________________________________________
_______________________________ No sub item, no sum list ____________________________________________
Name                 Time                                 File           Iterations
jackson al3          6,724                       AllTypes.json            1,000,000
gson  al3            6,292                       AllTypes.json            1,000,000
boon lazy c3         5,894                       AllTypes.json            1,000,000
boon full c3         5,870                       AllTypes.json            1,000,000
Winner: boon full c3
___________________________________________________________________________________
_______________________________ No sub list ____________________________________________
Name                 Time                                 File           Iterations
jackson al2          7,532                       AllTypes.json            1,000,000
gson  al2            7,095                       AllTypes.json            1,000,000
boon lazy c 2        6,480                       AllTypes.json            1,000,000
boon full c 2        6,486                       AllTypes.json            1,000,000
Winner: boon lazy c 2
___________________________________________________________________________________

```

The object was fairly complex and has all Java primitive types.


Latest times after optimizing object serialization to be faster than GSON and Jackson:

```
Name                 Time                                 File           Iterations
jackson              6,530                    actionLabel.json            1,000,000
gson                 6,096                    actionLabel.json            1,000,000
boon 1               4,703                    actionLabel.json            1,000,000
boon full            4,911                    actionLabel.json            1,000,000
boon lazy a          3,516                    actionLabel.json            1,000,000
boon lazy c          3,103                    actionLabel.json            1,000,000
boon ascii           7,220                    actionLabel.json            1,000,000
boon full a          7,529                    actionLabel.json            1,000,000
boon lazy c values   4,073                    actionLabel.json            1,000,000
Winner: boon lazy c
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              8,575                         medium.json            1,000,000
gson                 9,638                         medium.json            1,000,000
boon 1               7,995                         medium.json            1,000,000
boon full            7,986                         medium.json            1,000,000
boon lazy a          5,379                         medium.json            1,000,000
boon lazy c          5,036                         medium.json            1,000,000
boon ascii           9,804                         medium.json            1,000,000
boon full a          10,511                        medium.json            1,000,000
boon lazy c values   5,030                         medium.json            1,000,000
Winner: boon lazy c values
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              1,952                           menu.json            1,000,000
gson                 2,056                           menu.json            1,000,000
boon 1               1,535                           menu.json            1,000,000
boon full            1,497                           menu.json            1,000,000
boon lazy a          1,005                           menu.json            1,000,000
boon lazy c          903                             menu.json            1,000,000
boon ascii           2,018                           menu.json            1,000,000
boon full a          2,167                           menu.json            1,000,000
boon lazy c values   975                             menu.json            1,000,000
Winner: boon lazy c
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              3,165                           sgml.json            1,000,000
gson                 3,232                           sgml.json            1,000,000
boon 1               2,657                           sgml.json            1,000,000
boon full            2,553                           sgml.json            1,000,000
boon lazy a          1,721                           sgml.json            1,000,000
boon lazy c          1,747                           sgml.json            1,000,000
boon ascii           3,278                           sgml.json            1,000,000
boon full a          3,495                           sgml.json            1,000,000
boon lazy c values   1,808                           sgml.json            1,000,000
Winner: boon lazy a
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              495                            small.json            1,000,000
gson                 660                            small.json            1,000,000
boon 1               284                            small.json            1,000,000
boon full            276                            small.json            1,000,000
boon lazy a          176                            small.json            1,000,000
boon lazy c          147                            small.json            1,000,000
boon ascii           379                            small.json            1,000,000
boon full a          447                            small.json            1,000,000
boon lazy c values   160                            small.json            1,000,000
Winner: boon lazy c
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              16,004                        webxml.json            1,000,000
gson                 20,982                        webxml.json            1,000,000
boon 1               16,580                        webxml.json            1,000,000
boon full            15,947                        webxml.json            1,000,000
boon lazy a          10,676                        webxml.json            1,000,000
boon lazy c          9,610                         webxml.json            1,000,000
boon ascii           19,862                        webxml.json            1,000,000
boon full a          21,652                        webxml.json            1,000,000
boon lazy c values   9,603                         webxml.json            1,000,000
Winner: boon lazy c values
___________________________________________________________________________________
Name                 Time                                 File           Iterations
jackson              3,435                         widget.json            1,000,000
gson                 3,872                         widget.json            1,000,000
boon 1               3,220                         widget.json            1,000,000
boon full            3,147                         widget.json            1,000,000
boon lazy a          1,931                         widget.json            1,000,000
boon lazy c          1,716                         widget.json            1,000,000
boon ascii           4,081                         widget.json            1,000,000
boon full a          4,374                         widget.json            1,000,000
boon lazy c values   1,764                         widget.json            1,000,000
Winner: boon lazy c
___________________________________________________________________________________
```

Notice that the scores for boon lazy a got better from the previous runs. Since this is the
one that was doing the best, this is the one I picked to do full object serialization.

BTW, Boon ASCII does so poorly due to a "bug" in java.lang.String whereby it copies the entire byte [] array not
just the portion you specify. There are some workarounds, but I might end up dropping a few of these.
I will probably keep 3. Boon original JSON parser. Lazy Encoder JSON parser (it is the really fast one), and will fix
the ASCII lazy encoder (for the use case of working directly with byte buffers).





Latest times after adding object serialization (Nov 28th 2013):

```
Name                 Time                                 File           Iterations 
jackson              6,466                    actionLabel.json            1,000,000 
gson                 6,229                    actionLabel.json            1,000,000 
boon 1               4,716                    actionLabel.json            1,000,000 
boon full            4,948                    actionLabel.json            1,000,000 
boon lazy a          3,603                    actionLabel.json            1,000,000 
boon lazy c          3,361                    actionLabel.json            1,000,000 
boon ascii           7,227                    actionLabel.json            1,000,000 
boon full a          7,516                    actionLabel.json            1,000,000 
Winner: boon lazy c 
___________________________________________________________________________________ 
Name                 Time                                 File           Iterations 
jackson              8,506                         medium.json            1,000,000 
gson                 9,742                         medium.json            1,000,000 
boon 1               7,878                         medium.json            1,000,000 
boon full            7,921                         medium.json            1,000,000 
boon lazy a          5,588                         medium.json            1,000,000 
boon lazy c          5,310                         medium.json            1,000,000 
boon ascii           9,817                         medium.json            1,000,000 
boon full a          10,649                        medium.json            1,000,000 
Winner: boon lazy c 
___________________________________________________________________________________ 
Name                 Time                                 File           Iterations 
jackson              1,913                           menu.json            1,000,000 
gson                 2,087                           menu.json            1,000,000 
boon 1               1,546                           menu.json            1,000,000 
boon full            1,510                           menu.json            1,000,000 
boon lazy a          1,031                           menu.json            1,000,000 
boon lazy c          964                             menu.json            1,000,000 
boon ascii           2,051                           menu.json            1,000,000 
boon full a          2,181                           menu.json            1,000,000 
Winner: boon lazy c 
___________________________________________________________________________________ 
Name                 Time                                 File           Iterations 
jackson              3,149                           sgml.json            1,000,000 
gson                 3,262                           sgml.json            1,000,000 
boon 1               2,601                           sgml.json            1,000,000 
boon full            2,552                           sgml.json            1,000,000 
boon lazy a          1,805                           sgml.json            1,000,000 
boon lazy c          1,697                           sgml.json            1,000,000 
boon ascii           3,303                           sgml.json            1,000,000 
boon full a          3,545                           sgml.json            1,000,000 
Winner: boon lazy c 
___________________________________________________________________________________ 
Name                 Time                                 File           Iterations 
jackson              494                            small.json            1,000,000 
gson                 668                            small.json            1,000,000 
boon 1               284                            small.json            1,000,000 
boon full            280                            small.json            1,000,000 
boon lazy a          184                            small.json            1,000,000 
boon lazy c          177                            small.json            1,000,000 
boon ascii           381                            small.json            1,000,000 
boon full a          445                            small.json            1,000,000 
Winner: boon lazy c 
___________________________________________________________________________________ 
Name                 Time                                 File           Iterations 
jackson              15,808                        webxml.json            1,000,000 
gson                 21,102                        webxml.json            1,000,000 
boon 1               16,118                        webxml.json            1,000,000 
boon full            15,731                        webxml.json            1,000,000 
boon lazy a          11,074                        webxml.json            1,000,000 
boon lazy c          10,607                        webxml.json            1,000,000 
boon ascii           19,918                        webxml.json            1,000,000 
boon full a          21,402                        webxml.json            1,000,000 
Winner: boon lazy c 
___________________________________________________________________________________ 
Name                 Time                                 File           Iterations 
jackson              3,378                         widget.json            1,000,000 
gson                 3,920                         widget.json            1,000,000 
boon 1               3,208                         widget.json            1,000,000 
boon full            3,170                         widget.json            1,000,000 
boon lazy a          2,015                         widget.json            1,000,000 
boon lazy c          1,877                         widget.json            1,000,000 
boon ascii           4,097                         widget.json            1,000,000 
boon full a          4,391                         widget.json            1,000,000 
Winner: boon lazy c 
___________________________________________________________________________________ 
```

Update for times:

Taking examples from json.org.

```
jackson              6446                     actionLabel.json              1000000 
gson                 6090                     actionLabel.json              1000000 
boon 1               5080                     actionLabel.json              1000000 
boon lazy a          3556                     actionLabel.json              1000000 
boon lazy c          3305                     actionLabel.json              1000000 
boon ascii           7115                     actionLabel.json              1000000 
```

Boon is faster and is up to twice as fast a the next fastest Jackson.


Here are the times for menu.json taken from json.org

```
jackson              1880                            menu.json              1000000 
gson                 2051                            menu.json              1000000 
boon 1               1483                            menu.json              1000000 
boon lazy a          1008                            menu.json              1000000 
boon lazy c          966                             menu.json              1000000 
boon ascii           2083                            menu.json              1000000 
```

Boon is faster. Up to twice as fast as Jackson. I'll describe the four different Boon parsers.

```
jackson              3052                            sgml.json              1000000 
gson                 3209                            sgml.json              1000000 
boon 1               2453                            sgml.json              1000000 
boon lazy a          1744                            sgml.json              1000000 
boon lazy c          1682                            sgml.json              1000000 
boon ascii           3270                            sgml.json              1000000 
```

Bit of a theme. :)

```
jackson              16202                         webxml.json              1000000 
gson                 20996                         webxml.json              1000000 
boon 1               14910                         webxml.json              1000000 
boon lazy a          10776                         webxml.json              1000000 
boon lazy c          10519                         webxml.json              1000000 
boon ascii           20122                         webxml.json              1000000 
```

Boon still faster by a large margin. Boon 1 is a full, eager parser. 

```
jackson              3413                          widget.json              1000000 
gson                 3842                          widget.json              1000000 
boon 1               2840                          widget.json              1000000 
boon lazy a          1995                          widget.json              1000000 
boon lazy c          1877                          widget.json              1000000 
boon ascii           4203                          widget.json              1000000 
```

Boon 1 is just a vanilla JSON parser albiet a fast one. 
Boon lazy C is a lazy encoder simmilar to what the article describes, but with the caveat that it actually works, and has a useable API.
Boon Ascii is a direct from byte[] parser. The overhead is in the character encoding which if you convert ahead of time, you don't have the cost of the encoding as part of the parse. So.... Boon Ascii might go away.

Boon Lazy A also works with Binary, but does a lazy encode so it is fast. 

I think Boon Lazy C is always going to win these. :)

The Object Serializaer (when I write it) will build on top of a derivative of Boon Lazy C.
It will be the fastest JSON to Java Object serializer for at least fifteen minutes. :)

Ok... that ends the update from the parser front. 

Boon Lazy A implements what he spoke about in the article with the caveat of it works. It does full JSON parsing not partial, and it is compliant. And it has a useable API. More later.... Back to work.

Parse times for small json 10,000,000 runs:

```
GSON:         8,334 mili second
JACKSON:      7,156
Boon 2:       2,645
Boon 1:       3,799
InfoQ :      11,431
```


Smaller is better.

The slowest parser was the one from the article on how to write fast parser.
I make mistakes too so no harm, no foul.
It is still an interesting article and has good ideas.
Boon 2 is 3x faster than Jackson for this use case.
Boon 1 is almost 2x faster then Jackson for this use case.


Parse times for large json file 1,000,000 runs:
```
Boon 2:     15,543 mili second
Boon 1:     19,967
JACKSON:    18,985
InfoQ:      ParserException
GSON:       25,870
```

Lower is better. 
It should be noted that Boon 1 was taking 200+ seconds when I first ran this test.
I had to make some changes to get Boon 1 to this level. It was actually one small change. ;)
I love profilers. 

Here is the JSON for the small json file (large JSON file is below):

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

I am not sure what a micro benchmark is, and this benchmark might not be completely fair. Please let me know how to improve it.

Thanks

--Rick Hightower
