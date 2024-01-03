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
 * Interface for an object that provides input or output for an IntCode program.
 */
interface IntCodeIoQueue {

  /**
   * Add a single value to the queue.
   */
  void add(final long value);

  /**
   * Add multiple values to the queue.
   */
  void add(final long[] values);

  /**
   * Remove the head of the queue, or throw an exception if the queue is empty.
   */
  long remove();

  /**
   * Remove a specified number of elements from the head of the queue, or throw an exception if there are not at least
   * that many elements in the queue.
   */
  long[] remove(final int quantity);

  /**
   * Remove all elements from the queue, or throw an exception if the queue is empty.
   */
  long[] removeAll();

  /**
   * Remove all elements from the queue but do not return them.
   */
  void clear();

  /**
   * Get the number of elements in the queue.
   */
  int size();

  /**
   * Get whether the queue is empty.
   */
  boolean isEmpty();
}
