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

/**
 * Result of executing an instruction.
 */
enum ExecutionResult {

  COMPLETE(false, false),
  HALT(true, true),
  BLOCK(true, false);

  private final boolean block;

  private final boolean halt;

  private ExecutionResult(final boolean _block, final boolean _halt) {
    block = _block;
    halt = _halt;
  }

  public boolean isBlock() {
    return block;
  }

  public boolean isHalt() {
    return halt;
  }

}
