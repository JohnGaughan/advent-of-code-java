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
package us.coffeecode.advent_of_code.exec;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.component.InjectionConfiguration;
import us.coffeecode.advent_of_code.component.TestContext;

/**
 * Program entry point for running JUnit tests from the command line as opposed to the Eclipse JUnit plugin. Maven can
 * also run tests, but both Maven and the JUnit Eclipse plugin both don't have flexibility to run specific dynamic tests
 * without running others. This main entry point is useful for running a specific test or test part over and over while
 * trying to get it working quickly during December.
 */
@Component
public class Main {

  public static ConfigurableApplicationContext context;

  public static final void main(final String[] args) {
    context = new AnnotationConfigApplicationContext(InjectionConfiguration.class);
    try {
      final CommandLineTestRunner runner = context.getBean(CommandLineTestRunner.class);
      runner.exec(new TestContext(args));
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      context.close();
    }
  }
}
