# Test Utilities

Classes in this package aid in testing classes in `src/main/java`.

## AbstractTests

Base class for test harnesses. This encapsulates common logic for managing the Spring application context.

## DynamicTestStream

This class contains utility methods for getting streams of dynamic tests for Advent of Code solution classes.

## TestExecutor

Abstract base class designed to be an intermediary for JUnit dynamic tests.

When a test harness for a particular year wants to test that year, it gets all classes properly annotated as a component and Advent of Code solution for that
year. It then finds the methods of that class properly annotated as a method that needs to be tested. The test harness then creates a concrete instance of a
subclass of this class that is appropriate for that method.

Test harnesses will then create a lambda and pass a concrete instance of this class to the dynamic test framework. The framework then executes this class
through a functional interface, which actually runs the test.

This is one piece of the puzzle that allows the author to add new Advent of Code solutions to the project without needing to write code for individual tests.
Once a test harness is set up for a particular year, new classes and methods can be added and wired up with annotations. There is no need to separate some of
the logic or even expected values into a test harness class.

## LongTestExecutor, StringTestExecutor

Concrete implementations of `TestExecutor` that operate on test cases for longs and strings.
