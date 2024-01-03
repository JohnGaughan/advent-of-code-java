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
package us.coffeecode.advent_of_code.component;

import java.util.Objects;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;

/**
 * Contains information about the scope of a test run. What years should be tested? What days? This class is used early
 * in the test run, before the dynamic test loading is performed. This class encapsulates the metadata needed to know
 * which tests to feed to JUnit through the dynamic test load logic.
 */
public class TestContext {

  private final Integer year;

  private final Integer day;

  private final Integer part;

  private final String inputId;

  private final boolean visual;

  public TestContext() {
    this(null, null, null, null, false);
  }

  public TestContext(final boolean _visual) {
    this(null, null, null, null, _visual);
  }

  public TestContext(final Integer _year) {
    this(_year, null, null, null, false);
  }

  public TestContext(final Integer _year, final Integer _day, final Integer _part, final String _inputId, final boolean _visual) {
    year = _year;
    day = _day;
    part = _part;
    inputId = _inputId;
    visual = _visual;
  }

  public TestContext(final String[] args) {
    year = (args.length > 0) ? Integer.valueOf(args[0]) : null;
    day = (args.length > 1) ? Integer.valueOf(args[1]) : null;
    part = (args.length > 2) ? Integer.valueOf(args[2]) : null;
    inputId = (args.length > 3) ? args[3] : null;
    visual = (args.length > 4) ? Boolean.parseBoolean(args[4]) : false;
  }

  public int getPart() {
    return part == null ? Integer.MIN_VALUE : part.intValue();
  }

  public boolean matches(final AdventOfCodeSolution aoc) {
    if (aoc == null) {
      return false;
    }
    if ((year != null) && (year.intValue() != aoc.year())) {
      return false;
    }
    return (day == null) || (day.intValue() == aoc.day());
  }

  public boolean matches(final Solver solver) {
    if (solver == null) {
      return false;
    }
    return ((part == null) || (part.intValue() == solver.part())) && (visual == solver.visual());
  }

  public boolean matches(final String _inputId) {
    if (_inputId == null) {
      return false;
    }
    return (inputId == null) || Objects.equals(inputId, _inputId);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof TestContext) {
      final TestContext o = (TestContext) obj;
      return Objects.equals(year, o.year) && Objects.equals(day, o.day) && Objects.equals(part, o.part)
        && Objects.equals(inputId, o.inputId);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(part, year, day, inputId);
  }

  @Override
  public String toString() {
    return "(" + year + "," + day + "," + part + "," + inputId + ")";
  }
}
