# Annotation Package

This package contains custom annotations used by this project. Currently, these are only used to mark classes and methods that
implement a solution to an Advent of Code puzzle.

A class will be annotated with `@AdventOfCodeSolution` that records the year, day, and title of the puzzle. Then one or two
methods need to be annotated with `@Solver` which designates which part of the puzzle they solve.
