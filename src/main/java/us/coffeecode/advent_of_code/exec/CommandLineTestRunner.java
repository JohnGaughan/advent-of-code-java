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

import java.util.Collection;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.component.AocTestExecutionListener;
import us.coffeecode.advent_of_code.component.DynamicTestFactory;
import us.coffeecode.advent_of_code.component.TestContext;

/**
 * This class is used by Main to run tests from the command line. Main handles the program logic such as initializing
 * state and managing the program life cycle, while this class handles the test logic.
 */
@Component
public class CommandLineTestRunner {

  @Autowired
  private DynamicTestFactory dst;

  private static TestContext TC;

  public void exec(final TestContext tc) throws Exception {
    TC = tc;
    final MethodSelector ms =
      DiscoverySelectors.selectMethod(CommandLineTestRunner.class, "getTests", TestContext.class.getName());
    final LauncherDiscoveryRequest ldr = LauncherDiscoveryRequestBuilder.request().selectors(ms).build();
    final Launcher launcher = LauncherFactory.create();
    final TestExecutionListener sgl = new AocTestExecutionListener();
    launcher.registerTestExecutionListeners(sgl);
    launcher.discover(ldr);
    launcher.execute(ldr);
  }

  @TestFactory
  @ExtendWith(TestContextParameterResolver.class)
  public Collection<DynamicTest> getTests(final TestContext tc) {
    // Clunky workaround because somehow injections work when running in Eclipse, but not from the command line.
    if (dst == null) {
      dst = Main.context.getBean(DynamicTestFactory.class);
    }
    Collection<DynamicTest> tests = dst.getTests(tc);
    return tests;
  }

  public static class TestContextParameterResolver
  implements ParameterResolver {

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
      return parameterContext.getParameter().getType() == TestContext.class;
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
      return TC;
    }
  }

}
