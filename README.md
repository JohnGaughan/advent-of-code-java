## Advent of Code answers written by John Gaughan

This project contains my answers to the Advent of Code that runs in December of each year using the Java language. My work is
completely unaffiliated with the Advent of Code project.

You can visit the Advent of Code web site here: [https://adventofcode.com/](https://adventofcode.com/).

This is a living project that I will update as new code challenges are released or I find new and better solutions to puzzles I
already solved.

One of my life goals is to give back to the community. Releasing this will not solve any of the world's problems, but maybe
someone will learn something new from reading my code or will suggest an improvement that I can learn from.

## Technical details

This is an Eclipse project. I know people love to hate on it, but modern versions are fine even if it is not the latest flavor of
the month IDE. It integrates well with Maven and JUnit, making it simple to acquire dependencies and run tests that validate all
of the puzzle solutions as well as verifying utility classes work correctly.

The project uses Java 21 and moderately recently versions of several other libraries such as Spring, JUnit, and Guava.

Each Advent of Code problem has a solution class in `src/main/java` that solves both parts. Each class that solves a specific
year and day uses custom annotations that mark it as such. Other classes assemble dynamic tests for the various solutions and
present them to JUnit along with metadata about each test. This allows me to drop new solutions into the project without needing
to wire up individual tests. This includes the ability to have multiple tests per solution: for example, having tests that cover
the example inputs as well as the real input for each day.

I try to keep as much code relevant to a puzzle in its solution class. However, there are several utility classes such as a
`Point2D` leveraged by multiple solutions. Externalizing these utility classes means I do not have to reinvent them each time,
and more importantly, these classes can have their own JUnit tests which allows catching bugs if I need to add functionality.

## Running this Project

You will need a basic Java development environment:

* Java JDK 21. Technically a JRE is fine, but being able to view source and step into core Java code in the debugger is useful.
* Recent version of Eclipse.
* Maven and git plugins for Eclipse. These should come with Eclipse as long as you download the default version with Java tools
  included.

Once you have the basics set up, follow these steps:

* Download this project from GitHub either by exporting or cloning.
* Import the project into Eclipse, which will detect that it is a Maven project and perform its voodoo magic that tends to
  confuse people who are not familiar with Maven.
* You need a second project `advent-of-code-input` which contains the input data for each puzzle. More on this below.

### Input Project

The AOC authors request that people do not copy data from their site and make it available elsewhere. This includes problem text
as well as input data and answers. As such, this project does not include their intellectual property. However, I do have a
skeleton project under this one that shows the structure. This is located in the `advent-of-code-input` folder and contains
empty files for a single day. If all you want to do is run the code you can create folders in that project for whichever days you
want to run and populate its data.

Spring loads `src/main/resources/application.properties` as its global configuration file. Inside that file is a property named
`inputRoot` which contains the path to that project, relative to the root of this project. Or specify the absolute path if you
want: I am not your boss.

### Configuring Inputs

You have the project downloaded. It runs, but does nothing. This is because the dynamic test logic not only looks at annotations
on test classes, but it also dynamically detects inputs. It might need to run a specific test once, twice, or many times: once
for each configured input file. If there are zero configured input files, the test does not run. Let us look at how to configure
tests to run, then.

Inside the directory of each day, e.g. `advent-of-code-input/year2021/day12`, there are a few files. Technically, all of them are
optional, but realistically you will need answers and inputs to do anything useful.

* `partX.answers.properties` where `X` is `1` or `2`: this is a standard Java properties file where the key is a filename that
  must exist in the same directory. This file contains whatever data is provided from the Advent of Code web site, either example
  or real input data. I follow the naming convention `exampleXX.txt` for example input and `input01.txt` for real input which
  works well but those names are not enforced. You should be able to call them anything you want, but see the notes on properties
  later.
* `input01.txt`: this is where I put the real data. Generally this is a copy and paste from the web site.
* `exampleXX.txt`: as I mentioned above, I use this naming convention for example inputs. When populating these files please be
  careful because on some days the formatting of example data does not match the real data in the text file linked by the
  problem. You may need to adjust the formatting because while the solutions I wrote may handle things such as tabs being
  converted to spaces, it generally cannot handle issues such as a comma-delimited list of integers being spread out one per
  line. Sometimes the example data shows a grid, but the web site shows the grid and a state transition next to it which
  solutions will not be able to handle.
* `parameters.properties`: this is where any variables go. Sometimes a problem will say to run a simulation for a thousand
  iterations, but the example input only shows four. Maybe part one runs a thousand iterations, but part two runs ten thousand.
  In order to keep logic streamlined and enable code reuse, I often move these variables to this properties file. See below for
  more information on how to specify a variable should have different values at different times.

### parameters.properties

Some puzzles also need parameters, which are specified in this file. This is used because sometimes parts one and two have
differences such as changing the number of times an algorithm needs to run, such as Conway's Game of Life iterating a different
number of rounds. Sometimes, the differences are between inputs: perhaps the example shows an algorithm running for a certain
number of rounds, but the real input needs to run for ten times as many rounds. Using this properties file, we can move those
variables to the input and make the code cleaner by avoiding the whole question of "how can we tell if an input is an example
or a 'real' input?"

Parameter loading is intelligent and will look for specific keys over more general keys. This makes it possible to specify a
value for all example inputs (if they are named consistently, such as `example??.txt`) without repeating if there are many
inputs.

Example: let us assume there is a parameter named `widget` and the input file is `example01.txt`. Keys will be searched in the
following order, assuming part one:

1. `widget.example01.part1`
1. `widget.example.part1`
1. `widget.example01`
1. `widget.example`
1. `widget.part1`
1. `widget`

The first match found is the value used. This means it is possible to specify a global, default value under `widget` but
override it for a specific input or part.

## Who am I?

My name is John Gaughan, and I am a professional software developer living in Ohio, USA. My email address is
<john@coffeecode.us>.

## Licenses

This project is licensed under the GNU GPL v3. See the LICENSE file for the complete text.

External dependencies have their own licenses which are not reproduced here because I am not redistributing them.

Input data is the IP of the Advent of Code team and is not reproduced as part of this project.
