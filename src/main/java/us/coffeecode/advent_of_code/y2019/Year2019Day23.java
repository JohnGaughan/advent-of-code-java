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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2019, day = 23)
@Component
public final class Year2019Day23 {

  @Autowired
  private IntCodeFactory icf;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  private long calculate(final PuzzleContext pc) {
    final boolean returnFirstYValue = pc.getBoolean("ReturnFirstYValue");
    final IntCode original = icf.make(pc, ExecutionOption.ONE_INSTRUCTION_PER_EXEC);
    final IntCode[] computers = new IntCode[50];
    for (int i = 0; i < computers.length; ++i) {
      computers[i] = icf.make(original, new Day23InputQueue());
      computers[i].getInput()
                  .add(i);
    }

    long previousY = Long.MIN_VALUE;
    long natPacket[] = null;

    // Don't let this run forever.
    int idleTicks = 0;
    for (int iterations = 0; iterations < (1 << 20); ++iterations) {
      boolean idle = true;
      for (int i = 0; i < computers.length; ++i) {
        if (!computers[i].getInput()
                         .isEmpty()) {
          idle = false;
        }
        computers[i].exec();
        if (!computers[i].getOutput()
                         .isEmpty()) {
          idle = false;
        }
        if (computers[i].getOutput()
                        .size() >= 3) {
          final long[] output = computers[i].getOutput()
                                            .remove(3);
          final long[] packet = new long[] { output[1], output[2] };
          if (output[0] == 255) {
            if (returnFirstYValue) {
              return output[2];
            }
            natPacket = packet;
          }
          else {
            computers[(int) output[0]].getInput()
                                      .add(packet);
          }
        }
      }
      if (idle) {
        ++idleTicks;
      }
      else {
        idleTicks = 0;
      }
      if ((idleTicks > 650) && (natPacket != null)) {
        if (natPacket[1] == previousY) {
          return previousY;
        }
        previousY = natPacket[1];
        computers[0].getInput()
                    .add(natPacket);
        natPacket = null;
      }
    }
    return 0;
  }

  /**
   * I/O queue implementation that delegates to another queue, except that it will produce a default -1 value if the
   * other queue is empty. It always reports its size using the delegate even though it can still produce values.
   */
  private static final class Day23InputQueue
  implements IntCodeIoQueue {

    final IntCodeIoQueue delegate = new LongQueue();

    @Override
    public void add(final long value) {
      delegate.add(value);
    }

    @Override
    public void add(final long[] values) {
      delegate.add(values);
    }

    @Override
    public long remove() {
      return delegate.isEmpty() ? -1 : delegate.remove();
    }

    @Override
    public long[] remove(final int quantity) {
      if (delegate.isEmpty() && (quantity == 1)) {
        return new long[] { -1 };
      }
      return delegate.remove(quantity);
    }

    @Override
    public long[] removeAll() {
      if (delegate.isEmpty()) {
        return new long[] { -1 };
      }
      return delegate.removeAll();
    }

    @Override
    public void clear() {
      delegate.clear();
    }

    @Override
    public int size() {
      return delegate.size();
    }

    @Override
    public boolean isEmpty() {
      return delegate.isEmpty();
    }

  }

}
