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
package us.coffeecode.advent_of_code;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.TestFactory;

import us.coffeecode.advent_of_code.component.DynamicTestFactory;
import us.coffeecode.advent_of_code.component.TestContext;

/**
 * This class dynamically loads all tests that cover puzzle solutions. Each public method returns a DynamicContainer
 * that contains tests for a specific year. Tests are annotated with the year and day number, and the DynamicTestFactory
 * searches the project using Spring to locate all matching test cases. Eclipse run configurations have references to
 * this class and each method name, which allows a developer to run JUnit tests for a particular year through this
 * class.
 */
public class TestSolutions
extends AbstractTests {

  private final DynamicTestFactory dtf;

  public TestSolutions() {
    dtf = context.getBean(DynamicTestFactory.class);
  }

  @TestFactory
  public DynamicContainer year2015() {
    return tests(Integer.valueOf(2015));
  }

  @TestFactory
  public DynamicContainer year2016() {
    return tests(Integer.valueOf(2016));
  }

  @TestFactory
  public DynamicContainer year2017() {
    return tests(Integer.valueOf(2017));
  }

  @TestFactory
  public DynamicContainer year2018() {
    return tests(Integer.valueOf(2018));
  }

  @TestFactory
  public DynamicContainer year2019() {
    return tests(Integer.valueOf(2019));
  }

  @TestFactory
  public DynamicContainer year2020() {
    return tests(Integer.valueOf(2020));
  }

  @TestFactory
  public DynamicContainer year2021() {
    return tests(Integer.valueOf(2021));
  }

  @TestFactory
  public DynamicContainer year2022() {
    return tests(Integer.valueOf(2022));
  }

  @TestFactory
  public DynamicContainer year2023() {
    return tests(Integer.valueOf(2023));
  }

  @TestFactory
  public DynamicContainer year2024() {
    return tests(Integer.valueOf(2024));
  }

  private DynamicContainer tests(final Integer year) {
    return DynamicContainer.dynamicContainer("Advent of Code " + year + ": " + TITLES.get(year),
      dtf.getTests(new TestContext(year)));
  }

  private static final Map<Integer, String> TITLES = new HashMap<>();

  static {
    TITLES.put(Integer.valueOf(2015), "Make it Snow");
    TITLES.put(Integer.valueOf(2016), "Santa v. Easter Bunny");
    TITLES.put(Integer.valueOf(2017), "Deus ex Machina");
    TITLES.put(Integer.valueOf(2018), "Backwards Through Time");
    TITLES.put(Integer.valueOf(2019), "Journey Through the Solar System with IntCode");
    TITLES.put(Integer.valueOf(2020), "Tropical Vacation");
    TITLES.put(Integer.valueOf(2021), "Ocean Exploration");
    TITLES.put(Integer.valueOf(2022), "Jungle Expedition");
    TITLES.put(Integer.valueOf(2023), "Global Snow Production");
    TITLES.put(Integer.valueOf(2024), "Finding the Chief Historian");
  }

}
