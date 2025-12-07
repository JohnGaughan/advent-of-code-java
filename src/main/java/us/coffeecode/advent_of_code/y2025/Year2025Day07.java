/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2025  John Gaughan
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
package us.coffeecode.advent_of_code.y2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MyLongMath;

@AdventOfCodeSolution(year = 2025, day = 7)
@Component
public class Year2025Day07 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc).partOne;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc).partTwo;
  }

  /** Perform the program calculation which is the same between parts. */
  private Output calculate(final PuzzleContext pc) {
    final Input input = getInput(pc);
    long splits = 0;
    // Mapping of locations to how many beams are at that location.
    Map<Integer, Long> beams = new HashMap<>();
    beams.put(input.start, MyLongMath.ONE);

    // Loop over each non-empty line in the input.
    for (final int[] splitters : input.splitters) {
      final Map<Integer, Long> nextBeams = new HashMap<>();

      // Loop over each beam from the previous round.
      for (final var beam : beams.entrySet()) {
        final int beamInt = beam.getKey()
                                .intValue();
        if (Arrays.binarySearch(splitters, beamInt) >= 0) {
          /*
           * Beam hit a splitter, so split the beam left and right. Take into account that the beam may already be
           * present at the new location.
           */
          nextBeams.merge(Integer.valueOf(beamInt - 1), beam.getValue(), LONG_MERGE);
          nextBeams.merge(Integer.valueOf(beamInt + 1), beam.getValue(), LONG_MERGE);
          ++splits;
        }
        else {
          /*
           * Beam continues downward. However, we have to merge here because there could have been a splitter next to
           * this location which already populated this entry during a previous inner loop iteration.
           */
          nextBeams.merge(beam.getKey(), beam.getValue(), LONG_MERGE);
        }
      }
      beams = nextBeams;
    }

    // Return results for both parts one and two.
    return new Output(splits, beams.values()
                                   .stream()
                                   .mapToLong(Long::longValue)
                                   .sum());
  }

  /** Parse the program input and return it in a useful form. */
  private Input getInput(final PuzzleContext pc) {
    final List<String> lines = il.lines(pc);
    final List<int[]> splitters = new ArrayList<>();

    for (final String line : lines) {
      var splitterLocations = IntStream.builder();
      // This is implicitly sorted, which is important.
      for (int i = 0; i < line.length(); ++i) {
        if (line.codePointAt(i) == SPLITTER) {
          splitterLocations.accept(i);
        }
      }
      final int[] splitterIndices = splitterLocations.build()
                                                     .toArray();
      // Ignore empty lines.
      if (splitterIndices.length > 0) {
        splitters.add(splitterIndices);
      }
    }

    return new Input(Integer.valueOf(lines.getFirst()
                                          .indexOf(START)),
      splitters);
  }

  /** Character designating the beam's starting location. */
  private static final int START = 'S';

  /** Character designating a beam splitter. */
  private static final int SPLITTER = '^';

  /** Output from the calculation algorithm. */
  private record Output(long partOne, long partTwo) {}

  /** Parsed program input. */
  private record Input(Integer start, List<int[]> splitters) {}

  /** Function that adds two boxed longs. This makes code in the main algorithm more concise and readable. */
  private static final BiFunction<? super Long, ? super Long, ? extends Long> LONG_MERGE =
    (a, b) -> Long.valueOf(a.longValue() + b.longValue());
}
