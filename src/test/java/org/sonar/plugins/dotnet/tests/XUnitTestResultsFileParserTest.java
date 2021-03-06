/*
 * SonarQube .NET Tests Library
 * Copyright (C) 2014-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plugins.dotnet.tests;

import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class XUnitTestResultsFileParserTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void no_counters() {
    thrown.expect(ParseErrorException.class);
    thrown.expectMessage("Missing attribute \"total\" in element <assembly> in ");
    thrown.expectMessage(new File("src/test/resources/xunit/no_counters.xml").getAbsolutePath());
    new XUnitTestResultsFileParser().parse(new File("src/test/resources/xunit/no_counters.xml"), mock(UnitTestResults.class));
  }

  @Test
  public void wrong_passed_number() {
    thrown.expect(ParseErrorException.class);
    thrown.expectMessage("Expected an integer instead of \"invalid\" for the attribute \"total\" in ");
    thrown.expectMessage(new File("src/test/resources/xunit/invalid_total.xml").getAbsolutePath());
    new XUnitTestResultsFileParser().parse(new File("src/test/resources/xunit/invalid_total.xml"), mock(UnitTestResults.class));
  }

  @Test
  public void invalid_root() {
    thrown.expect(ParseErrorException.class);
    thrown.expectMessage("Expected either an <assemblies> or an <assembly> root tag, but got <foo> instead.");
    thrown.expectMessage(new File("src/test/resources/xunit/invalid_root.xml").getAbsolutePath());
    new XUnitTestResultsFileParser().parse(new File("src/test/resources/xunit/invalid_root.xml"), mock(UnitTestResults.class));
  }

  @Test
  public void valid() throws Exception {
    UnitTestResults results = new UnitTestResults();
    new XUnitTestResultsFileParser().parse(new File("src/test/resources/xunit/valid.xml"), results);

    assertThat(results.tests()).isEqualTo(17);
    assertThat(results.passedPercentage()).isEqualTo(5 * 100.0 / 17);
    assertThat(results.skipped()).isEqualTo(4);
    assertThat(results.failures()).isEqualTo(3);
    assertThat(results.errors()).isEqualTo(5);
  }

  @Test
  public void valid_xunit_1_9_2() throws Exception {
    UnitTestResults results = new UnitTestResults();
    new XUnitTestResultsFileParser().parse(new File("src/test/resources/xunit/valid_xunit-1.9.2.xml"), results);

    assertThat(results.tests()).isEqualTo(6);
    assertThat(results.passedPercentage()).isEqualTo(3 * 100.0 / 6);
    assertThat(results.skipped()).isEqualTo(2);
    assertThat(results.failures()).isEqualTo(1);
    assertThat(results.errors()).isEqualTo(0);
  }

}
