/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
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

import java.util.Arrays;

/**
 * Represents the address modes of an IntCode computer.
 */
enum Mode {

  POSITION(0) {

    @Override
    public long getValue(final IntCode state, final long position) {
      return state.memory.get(state.memory.get(position));
    }

    @Override
    public void setValue(final IntCode state, final long position, final long value) {
      state.memory.set(state.memory.get(position), value);
    }
  },

  IMMEDIATE(1) {

    @Override
    public long getValue(final IntCode state, final long position) {
      return state.memory.get(position);
    }

    @Override
    public void setValue(final IntCode state, final long position, final long value) {
      throw new UnsupportedOperationException("Cannot write immediate");
    }
  },

  RELATIVE(2) {

    @Override
    public long getValue(final IntCode state, final long position) {
      return state.memory.get(state.relativeBase + state.memory.get(position));
    }

    @Override
    public void setValue(final IntCode state, final long position, final long value) {
      state.memory.set(state.relativeBase + state.memory.get(position), value);
    }
  };

  public static final Mode valueOf(final long _id) {
    return Arrays.stream(values())
                 .filter(m -> m.id == _id)
                 .findFirst()
                 .get();
  }

  public static final Mode[] valuesOf(final long intcode) {
    Mode[] modes = new Mode[3];
    long code = intcode / 100;
    modes[0] = valueOf(code % 10);
    code /= 10;
    modes[1] = valueOf(code % 10);
    code /= 10;
    modes[2] = valueOf(code % 10);
    return modes;
  }

  private final int id;

  private Mode(final int _id) {
    id = _id;
  }

  /** Get a value from memory using this mode's technique to resolve the target's address. */
  public abstract long getValue(final IntCode state, final long position);

  /** Set a value at a memory address, using this mode's technique to resolve the target address. */
  public abstract void setValue(final IntCode state, final long position, final long value);

}
