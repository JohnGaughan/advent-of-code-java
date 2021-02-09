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

The programs are implemented as separate Java classes implementing a common interface. There is a common JUnit test harness, with a Test-annotated method for
each individual problem. To run the code, simply run the test harness as a JUnit test.

Each day of two problems is in a self-contained Java class, possibly with nested classes. This does clutter the code a little, but I think the tradeoff of
having the entire program just a scroll wheel away actually makes it _more_ readable.

Programs only rely on core Java, with a small number relying on one utility method included in this project related to input parsing. My goal is to avoid having
any of the solution logic outside of the program itself beyond the bare minimum: things like Java containers and streams, or JUnit which manages running the
various programs and validating their answers.

## Who am I?

My name is John Gaughan, and I am a professional software developer living in Ohio, USA. My email address is <john@coffeecode.us> and my personal blog is at
<https://coffeecode.us/>.

## Licenses

This project is licensed under the GNU GPL v3. See the LICENSE file for the complete text.

Input data reproduced here is the IP of the Advent of Code team.
