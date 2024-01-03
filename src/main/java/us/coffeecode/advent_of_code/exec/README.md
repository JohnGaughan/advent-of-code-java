# Exec Package

This package contains classes used for manually running tests from the command line. Generally when validating the solutions for
a year, it is better to run tests through Maven or the JUnit Eclipse plugin. However, it can be more time-efficient to run a test
from the command line when rapidly developing a solution when a new puzzle releases during the challenge in December.

The `Main` class accepts optional parameters that filter the tests to run:

    Main <year> <day> <part> <input ID>

Omitting a parameter treats it like a wildcard, that is, perform no filtering.
