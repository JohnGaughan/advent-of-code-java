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

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.TestFactory;

import us.coffeecode.advent_of_code.component.AocResources;
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

  private final DynamicTestFactory testFactory;

  private final AocResources resources;

  public TestSolutions() {
    testFactory = context.getBean(DynamicTestFactory.class);
    resources = context.getBean(AocResources.class);
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

  @TestFactory
  public DynamicContainer year2025() {
    return tests(Integer.valueOf(2025));
  }

  private DynamicContainer tests(final Integer year) {
    final String title = "Advent of Code " + year + ": " + resources.getYearTitle(year);
    return DynamicContainer.dynamicContainer(title, testFactory.getTests(new TestContext(year)));
  }

}
