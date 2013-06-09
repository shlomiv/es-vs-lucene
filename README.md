es-vs-lucene
============

This is follow up repository for testing elasticsearch's lucene db size vs a plain java lucene db size [linked here](https://groups.google.com/d/msg/elasticsearch/6j0E-2pTbWg/bAorYsCcQjsJ).

there are two folders:
 * **es** - a clojure impl for loading up the test file to an elasticsearch server (version 0.20.6). it's really straight forward clojure, that gets resolved to REST calls, so nothing fancy here. i wrote it in clojure becase i didnt feel like writing it in bash, and it would output the same exact rest calls. This is very different (and inefficient) from the code I actually use (I use BulkProcessor etc), but the outcome is the same.
 * **lucene** - this one is written in java, to keep it looking like the code we actually use.
 
# Instructions on how to run both tests
Both projects are using lein as their buildtool, so get it from [here](https://github.com/technomancy/leiningen#installation)

## ES
after cloning this repo:<br>
the default configuration is that ES is running on 127.0.0.1. if its not, edit the file [src/es/es.clj](https://github.com/vadali/es-vs-lucene/blob/master/es/src/es/es.clj#L9). 

```bash
cd es
lein run -m es.es/-main
```

this will upload the data to the lucene server, under the index "test".

its really inefficient, and wait a silly 10 seconds between batchs, so it wont hit connection errors. if you get connection errors, you can increase the wait. this takes about 5 minutes to complete.

## Lucene
this one is much simpler and way faster, it actually looks alot like what i have in productions, so any comments on it will be good too.
the code sits in [lucene/java/test/Indexer.java](https://github.com/vadali/es-vs-lucene/blob/master/lucene/java/test/Indexer.java). 

```bash
cd lucene
lein javac
lein run -m test.Indexer
```
Once done, you will have a new folder lucene/luc-index, which contains the optimized lucene index.

you can see the lucene dependency in [project.clj](https://github.com/vadali/es-vs-lucene/blob/master/lucene/project.clj), this is maven competible and actually grabs that version of lucene from the public maven repository.
