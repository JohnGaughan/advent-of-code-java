/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.y2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SequencedCollection;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.LongRange;

@AdventOfCodeSolution(year = 2023, day = 5, title = "If You Give A Seed A Fertilizer")
@Component
public class Year2023Day05 {

  /*
   * Implementation note: this code must use LongRange instead of Range because quite a few values in the input data
   * will not fit into a Java integer primitive due to many, many numbers being between 2^31 and 2^32. It should be
   * possible to use an unsigned integer in another language such as C++. My data has one value that is exactly
   * (2^32)-1, or UINT_MAX, but nothing greater.
   */

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(getInput(pc));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(getInput(pc));
  }

  /** Calculate the answer for the given input data. This operates on the entirety of the input data. */
  private long calculate(final Input in) {
    long answer = Long.MAX_VALUE;
    for (final LongRange seeds : in.seeds) {
      answer = Math.min(answer, calculate(seeds, in.mappings));
    }
    return answer;
  }

  /**
   * Calculate the answer for the given seed range and mappings. This operates on one input range and pushes it through
   * each of the mappings to get the answer for that specific input range.
   */
  private long calculate(final LongRange seeds, final Iterable<? extends Iterable<RangeMapping>> mappings) {
    SequencedCollection<LongRange> inputs = List.of(seeds);
    for (final Iterable<RangeMapping> mappingGroup : mappings) {
      inputs = process(inputs, mappingGroup);
    }
    return inputs.getFirst()
                 .getX1();
  }

  /**
   * Given ranges of inputs, process them through the mapping group and return the unique ranges to which they map. This
   * is one round of taking an input range or ranges, and pushing it through all of the range mappings for that specific
   * round.
   */
  private SequencedCollection<LongRange> process(final SequencedCollection<LongRange> inputs, final Iterable<RangeMapping> mappingGroup) {
    final Collection<LongRange> result = new ArrayList<>(Math.max(inputs.size(), 32));
    Collection<LongRange> unmapped = new ArrayList<>(inputs);
    for (final RangeMapping mapping : mappingGroup) {
      final Collection<LongRange> nextUnmapped = new ArrayList<>(unmapped.size());
      for (final LongRange input : unmapped) {
        // Ranges here are closed intervals.
        if (input.overlaps(mapping.src)) {
          // Map the part that overlaps.
          result.add(input.intersection(mapping.src)
                          .shift(mapping.shift));
          // Copy the part below the range, if anything.
          if (input.getX1() < mapping.src.getX1()) {
            nextUnmapped.add(new LongRange(input.getX1(), mapping.src.getX1() - 1));
          }
          // Copy the part above the range, if anything.
          if (input.getX2() > mapping.src.getX2()) {
            nextUnmapped.add(new LongRange(mapping.src.getX2() + 1, input.getX2()));
          }
        }
        // No overlap: don't map anything.
        else {
          nextUnmapped.add(input);
        }
      }
      unmapped = nextUnmapped;
    }
    // Anything not mapped follows through with its current value.
    result.addAll(unmapped);
    return LongRange.merge(result);
  }

  /** Splits an input line that contains space-separated numbers. */
  private static final Pattern SPLIT = Pattern.compile("\\s+");

  /** Used to strip the prefix from the seeds line, leaving only the numeric data and separators. */
  private static final String SEEDS = "seeds: ";

  /** Parse the entire input, represented as groups of lines. */
  private Input getInput(final PuzzleContext pc) {
    final List<List<String>> groups = il.groups(pc);

    // Convert the initial seeds into ranges.
    final List<LongRange> seedRanges;
    {
      final long[] seedInputs = Arrays.stream(SPLIT.split(groups.getFirst()
                                                                .getFirst()
                                                                .substring(SEEDS.length())))
                                      .mapToLong(Long::parseLong)
                                      .toArray();
      // Part two: treat pairs of seeds as ranges. Map each pair of longs to a range.
      if (pc.getBoolean("InputIsRange")) {
        seedRanges = new ArrayList<>(seedInputs.length >> 1);
        for (int i = 0; i < seedInputs.length; i += 2) {
          seedRanges.add(new LongRange(seedInputs[i], seedInputs[i] + seedInputs[i + 1] - 1));
        }
      }
      // Part one: treat each seed as a distinct value. Map each long to a range of size one.
      else {
        seedRanges = Arrays.stream(seedInputs)
                           .mapToObj(n -> new LongRange(n, n))
                           .toList();
      }
    }

    // Convert each block of mappings to objects.
    final List<List<RangeMapping>> rangeMaps = new ArrayList<>(groups.size() - 1);
    for (int i = 1; i < groups.size(); ++i) {
      final List<String> group = groups.get(i);
      final List<RangeMapping> rangeMap = new ArrayList<>(group.size() - 1);
      for (int j = 1; j < group.size(); ++j) {
        final long[] values = Arrays.stream(SPLIT.split(group.get(j)))
                                    .mapToLong(Long::parseLong)
                                    .toArray();
        // Store ranges as a closed interval. It makes the math easier later.
        final LongRange src = new LongRange(values[1], values[1] + values[2] - 1);
        final LongRange dest = new LongRange(values[0], values[0] + values[2] - 1);
        rangeMap.add(new RangeMapping(src, dest, values[0] - values[1]));
      }
      rangeMaps.add(rangeMap);
    }
    return new Input(seedRanges, rangeMaps);
  }

  private record Input(List<LongRange> seeds, List<List<RangeMapping>> mappings) {}

  private record RangeMapping(LongRange src, LongRange dest, long shift) {}

}
