/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2019;

import static us.coffeecode.advent_of_code.y2019.ExecutionOption.BLOCK_UNTIL_INPUT_AVAILABLE;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Collections2;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.ArrayInfinitelyIterable;

@AdventOfCodeSolution(year = 2019, day = 7)
@Component
public final class Year2019Day07 {

  @Autowired
  private IntCodeFactory icf;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final IntCode original = icf.make(pc, BLOCK_UNTIL_INPUT_AVAILABLE);
    long answer = 0;
    for (final List<Integer> phaseSettings : Collections2.permutations(IntStream.range(0, 5)
                                                                                .mapToObj(Integer::valueOf)
                                                                                .toList())) {
      long signal = 0;
      for (Integer phaseSetting : phaseSettings) {
        final IntCode state = icf.make(original);
        state.getInput()
             .add(new long[] { phaseSetting.longValue(), signal });
        state.exec();
        signal = state.getOutput()
                      .remove();
      }
      answer = Math.max(signal, answer);
    }
    return answer;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final IntCode original = icf.make(pc, BLOCK_UNTIL_INPUT_AVAILABLE);
    long answer = 0;
    for (final List<Integer> phaseSettings : Collections2.permutations(IntStream.range(5, 10)
                                                                                .mapToObj(Integer::valueOf)
                                                                                .toList())) {

      // Create distinct amps and wire them together.
      // Output feeds into the next amp's output. We need to see amp 5's output though.
      // Input is the phase setting, then the previous amp's output. First time, amp 1 needs 0.
      final IntCode[] amps = new IntCode[5];
      amps[0] = icf.make(original);
      amps[1] = icf.make(original, new Pipe(amps[0], phaseSettings.get(1)
                                                                  .longValue()));
      amps[2] = icf.make(original, new Pipe(amps[1], phaseSettings.get(2)
                                                                  .longValue()));
      amps[3] = icf.make(original, new Pipe(amps[2], phaseSettings.get(3)
                                                                  .longValue()));
      amps[4] = icf.make(original, new Pipe(amps[3], phaseSettings.get(4)
                                                                  .longValue()));
      amps[0].setInput(new Pipe(amps[4], phaseSettings.getFirst()
                                                      .longValue(),
        0));

      // Loop over and over until there is a halt, as opposed to a pause.
      for (final IntCode amp : new ArrayInfinitelyIterable<>(amps)) {
        final ExecutionResult exec = amp.exec();
        if ((amp == amps[4]) && exec.isHalt()) {
          answer = Math.max(answer, amp.getOutput()
                                       .remove());
          break;
        }
      }
    }
    return answer;
  }

  /** Connects the output of one amplifier to the input of another. */
  private static final class Pipe
  implements IntCodeIoQueue {

    private final IntCodeIoQueue previous;

    private final long[] initial;

    private int i = 0;

    Pipe(final IntCode _previous, final long... _initial) {
      previous = _previous.getOutput();
      initial = _initial;
    }

    @Override
    public void add(final long value) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(final long[] values) {
      throw new UnsupportedOperationException();
    }

    @Override
    public long remove() {
      if (i < initial.length) {
        return initial[i++];
      }
      return previous.remove();
    }

    @Override
    public long[] remove(final int quantity) {
      throw new UnsupportedOperationException();
    }

    @Override
    public long[] removeAll() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
      throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
      return initial.length - i + previous.size();
    }

    @Override
    public boolean isEmpty() {
      return (i == initial.length) && previous.isEmpty();
    }

  }

}
