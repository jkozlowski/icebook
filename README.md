# icebook

icebook is a simple simulator for SETS iceberg order book in Java.

The application will read new orders from *stdin*, apply them to the order book and output to *stdout* any generated
trades in the format defined by *icebook.output.Appenders#append(Appendable, Collection)*,
followed by the current state of the book in the format defined by
*icebook.output.Appenders#append(Appendable, SortedSet, SortedSet)*. Any errors will be printed to *stderr*.

Should the input not conform to the grammar defined in *icebook.input.Parsers*, the system will output an error
message and exit with an error code.

To simplify the design, the simulator does not explicitly deal with particular instruments (i.e. it does not have
a notion of an *FB.O* stock), but rather all orders are implicitly assumed to concern a single, but
unspecified stock instrument.

Similarly, prices are assumed to be quoted in pennies and sub-penny prices are prohibited; hence,
prices are simply assumed to be *long* values.

The default implementation uses a late addition to the system: *icebook.book.OptimisedOrderBook*,
which was implemented to a fixed deadline, just to challenge myself. If this implementation is doing something wrong,
you can revert to using the default implementation by changing the implementation in *icebook.Main*. Both
implementations however, pass the same battery of tests, therefore if something is not behaving how it should,
it is most likely a bug in *icebook.exec.ExecutionEngine*; Please create an issue if you find any bugs.

### requirements ###
* Apache Maven
* Java JDK 1.6

### usage ###
Create a runnable jar and run:
```
$ java -jar icebook-<version>-SNAPSHOT-jar-with-dependencies.jar
```

### clean, compile, run tests and install to local repository ###
```
$ mvn clean install
```

### test ###
```
$ mvn clean test
```

### coverage ###
```
$ mvn clean cobertura:cobertura
```

Open the *target/site/cobertura/index.html* file to view the report.

### create runnable jar ###
```
$ mvn clean install assembly:single
```

This will create *icebook-&lt;version&gt;-SNAPSHOT-jar-with-dependencies.jar* in *target/* directory.
