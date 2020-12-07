## Advent of Code answers written by John Gaughan

This project contains my answers to the Advent of Code that runs in December of each year using the Java language. My work is completely unaffiliated with the
Advent of Code project.

You can visit the Advent of Code web site here: [https://adventofcode.com/](https://adventofcode.com/).

This is a living project that I will update as new code challenges are released, or I go back to previous years and finish those problems.

## Why release this?

This is one way to show my coding abilities to the world both for professional and personal reasons. It makes it easy to collaborate with other developers by
being able to view my code: perhaps someone has an idea to help me improve.

## Technical details

This is an Eclipse project. I know people love to hate on it, but modern versions are fine even if it is not the latest flavor of the month.

I settled on Java 11. It is a recent-enough LTS release that is widely supported.

The programs are implemented as separate Java classes implementing a common interface. Each year has its own JUnit test harness, with a Test-annotated method
for each individual problem. To run the code, simply run the test harness as a JUnit test.

## Licenses

This project is licensed under the GNU GPL v3. See the LICENSE file for the complete text.

Licenses for included libraries are also included in LICENSE-junit and LICENSE-hamcrest.

Input data reproduced here is the IP of the Advent of Code team.
