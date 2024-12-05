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
package us.coffeecode.advent_of_code.visualization;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.component.InjectionConfiguration;

/**
 * Program entry point for visualizing puzzle results.
 */
@Component
public class VisualizerApplication {

  public static ConfigurableApplicationContext context;

  /** Program entry point. Delegate to the GUI class so Spring can initialize first and work its magic. */
  public static void main(final String[] args) {
    context = new AnnotationConfigApplicationContext(InjectionConfiguration.class);
    context.getBean(VisualizerApplicationGui.class)
           .initAndRunGui();
  }

  public static void close() {
    // Normally this would be done with try-finally. Swing is heavily event-driven and the main thread will die too
    // early, breaking the whole application. Instead, allow the main frame handler to close the context.
    context.close();
  }

}
