# icebook

icebook is a simple simulator for SETS iceberg order book in Java.

The application will read new orders from *stdin*, apply them to the order book
and output to *stdout* any generated trades in the format defined by *icebook
.OutputFormatter#append(icebook.Trade)*, followed by the current state of the
 book in the format defined by *icebook.OutputFormatter#append(icebook
 .OrderBook)*. Any errors will be printed to *stderr*.

The parsers assume well formed input.

### licencing ###
See [LICENCE](LICENCE).

### requirements ###
* Apache Maven
* Java JDK 1.7
