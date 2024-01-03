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
package us.coffeecode.advent_of_code;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import us.coffeecode.advent_of_code.component.InjectionConfiguration;

/**
 * Base for a class that runs tests. This contains common logic for setting up the Spring application context.
 */
public abstract class AbstractTests {

  protected static ConfigurableApplicationContext context;

  @BeforeAll
  public static void initialize() {
    context = new AnnotationConfigApplicationContext(InjectionConfiguration.class);
  }

  @AfterAll
  public static void teardown() {
    context.close();
  }

}
