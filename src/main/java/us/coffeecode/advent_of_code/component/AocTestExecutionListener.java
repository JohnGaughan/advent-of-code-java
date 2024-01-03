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

import org.junit.platform.engine.TestDescriptor.Type;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

/**
 * Test listener customized for AOC. This listens for events from JUnit indicating that a test finished running. It then
 * outputs to standard output the way I want it to look. This filters on type of TEST because container events also come
 * through here. Tests are encapsulated in containers representing enclosing classes or collections of dynamic tests.
 * Ignore those container level events.
 */
public class AocTestExecutionListener
implements TestExecutionListener {

  @Override
  public void executionFinished(final TestIdentifier testIdentifier, final TestExecutionResult testExecutionResult) {
    if (testIdentifier.getType() == Type.TEST) {
      final StringBuilder result = new StringBuilder(256);
      result.append(testIdentifier.getDisplayName()).append(" ");
      result.append(testExecutionResult.getStatus());
      if (testExecutionResult.getStatus() != Status.SUCCESSFUL) {
        result.append(" ").append(testExecutionResult.getThrowable());
      }
      System.out.println(result);
    }
  }

}
