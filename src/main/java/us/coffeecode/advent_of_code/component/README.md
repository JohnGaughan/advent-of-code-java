# Component Package

This package contains classes that are annotated as Spring components, or are core classes used by directly by those components
or are used to execute the tests in this project.

## AbstractSolverExecutable, SolverExecutable, VisualizerExecutable

Together, these classes bridge the gap between JUnit and solution classes. They encapsulate a single puzzle solution by containing
the implementation class, method to call, and the puzzle context which contains additional metadata. This is how unit tests can
display detailed information about the test being run.

The visualizer executable fits into the same framework but contains visualization results which can be displayed to the user.

## AocResources

Helper class that gets data from the resources directory. More abstractly, it gets data from configuration such that callers of
the class do not need to be concerned with _how_ the data is loaded or from _where_. It is the gatekeeper of dynamic data.

## AocTestExecutionListener

This class listens for JUnit results (pass, fail) and translates the result into a more descriptive result that contains stuff
like the year and day of the test. It then outputs this text to standard out. This is used when running tests from the
command-line, including Maven.

## DynamicTestFactory

I use annotations to mark up which classes contain puzzle solutions. This is the class that goes and finds those annotated classes
when running from the command-line.

## InjectionConfiguration

This class does not really "do" anything: it is simply a place to hang global Spring annotations to make the whole project work.

## InputLoader

Every test needs to load data: this is the class that actually does the heavy lifting. There are dozens of methods that return
puzzle input in various ways with the goal of stuffing as much code into this class and keeping the puzzle solution classes as
lean as possible. There are a few categories of methods, however:

* File as a list of strings, when the input is complex and generic methods simply will not do.
* File as a primitive or some other object. This is useful when the input is a single number or string and the desired result is
  that number, string, or some other object that can be constructed from that string.
* Lines of the file in a list. These could simply be strings, or converted to something else such as a primitive or object
  possibly with a functional object passed in to perform the conversion.
* Line groups. This is more complicated and need explanation. Sometimes the input is split up where there are a bunch of lines, a
  blank line, then a bunch more lines, possibly repeated. Sometimes each group has the same format, such as lists of data.
  Sometimes each group is different, where each group of lines represents different data. These methods can be a little confusing
  at first but they facilitate loading this data in different ways.

There are methods that allow loading the file, lines, or groups as various primitive types or by code point. The latter is useful
when a puzzle requires using string data character-by-character, such as navigating a 2D grid in string format. There are also
many method that accept functional interface objects for constructing puzzle-specific input objects.

## InputLocator

Input files are contained in a sister project. I have a private repository containing all of those files, and anyone else who
wants to download and run my code can make their own. This class does the work for finding where those files are located.

## SolverExecutable

This is the class that actually runs a test. Since tests are dynamic, we need a place to hang the code that invokes the test
method and checks the results. Usually this is hand-written code, but here, we use reflection to run a test and verify the
results.

## TestContext

The test context contains metadata about which tests to run when using the command-line. It is possible to run a specific year, or
all years. Maybe a specific year, day, and part. This class simply contains that information.