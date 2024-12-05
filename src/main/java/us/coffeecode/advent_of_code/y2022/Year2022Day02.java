/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package us.coffeecode.advent_of_code.y2022;

import java.util.Arrays;
import java.util.function.ToLongFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2022, day = 2, title = "Rock Paper Scissors")
@Component
public class Year2022Day02 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, s -> {
      Play opponent = Play.valueOf(s.codePointAt(0));
      Play me = Play.valueOf(s.codePointAt(2));
      Outcome out = Outcome.valueOf(opponent, me);
      return me.getScore() + out.getScore();
    });
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, s -> {
      Play opponent = Play.valueOf(s.codePointAt(0));
      Outcome out = Outcome.valueOf(s.codePointAt(2));
      Play me = Play.valueOf(opponent, out);
      return me.getScore() + out.getScore();
    });
  }

  private long calculate(final PuzzleContext pc, final ToLongFunction<String> parser) {
    return Arrays.stream(il.linesAsLongs(pc, parser))
                 .sum();
  }

  /** The outcome of a match from my perspective. */
  private static enum Outcome {

    LOSS('X', 0),
    DRAW('Y', 3),
    WIN('Z', 6);

    static Outcome valueOf(final int codePoint) {
      for (Outcome out : values()) {
        if (out.codePoint == codePoint) {
          return out;
        }
      }
      throw new IllegalArgumentException(Character.toString(codePoint));
    }

    static Outcome valueOf(final Play opponent, final Play me) {
      if (opponent == me) {
        return DRAW;
      }
      else if (opponent == Play.ROCK) {
        return (me == Play.PAPER) ? WIN : LOSS;
      }
      else if (opponent == Play.PAPER) {
        return (me == Play.SCISSORS) ? WIN : LOSS;
      }
      else {
        return (me == Play.ROCK) ? WIN : LOSS;
      }
    }

    private final int codePoint;

    private final long score;

    private Outcome(final int _codePoint, final long _score) {
      codePoint = _codePoint;
      score = _score;
    }

    long getScore() {
      return score;
    }
  }

  /** A single play in a match from either participant's perspective. */
  private static enum Play {

    ROCK('A', 'X', 1),
    PAPER('B', 'Y', 2),
    SCISSORS('C', 'Z', 3);

    static Play valueOf(final int codePoint) {
      for (Play play : values()) {
        if ((play.codePoint1 == codePoint) || (play.codePoint2 == codePoint)) {
          return play;
        }
      }
      return Arrays.stream(values())
                   .filter(p -> ((p.codePoint1 == codePoint) || (p.codePoint2 == codePoint)))
                   .findFirst()
                   .get();
    }

    static Play valueOf(final Play opponent, final Outcome out) {
      if (out == Outcome.DRAW) {
        return opponent;
      }
      else if (opponent == ROCK) {
        return (out == Outcome.WIN) ? PAPER : SCISSORS;
      }
      else if (opponent == PAPER) {
        return (out == Outcome.WIN) ? SCISSORS : ROCK;
      }
      else {
        return (out == Outcome.WIN) ? ROCK : PAPER;
      }
    }

    private final int codePoint1;

    private final int codePoint2;

    private final long score;

    private Play(final int _codePoint1, final int _codePoint2, final long _score) {
      codePoint1 = _codePoint1;
      codePoint2 = _codePoint2;
      score = _score;
    }

    long getScore() {
      return score;
    }

  }

}
