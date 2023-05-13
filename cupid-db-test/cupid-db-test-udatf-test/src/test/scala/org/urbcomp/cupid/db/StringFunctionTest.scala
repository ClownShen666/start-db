/* 
 * Copyright (C) 2022  ST-Lab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.urbcomp.cupid.db

/**
  * String Function test
  * @author zaiyuan, XiangHe
  */
class StringFunctionTest extends AbstractCalciteSparkFunctionTest {
  test("upper") {
    executeQueryCheck("select upper('abcde')", List("ABCDE"))
  }

  test("lower") {
    executeQueryCheck("select lower('ABCDE')", List("abcde"))
  }

  test("substring1") {
    executeQueryCheck("select substring('abcde', 2, 3)", List("bcd"))
  }

  test("substring2") {
    executeQueryCheck("select substring('abcde', 2)", List("bcde"))
  }

  test("trim") {
    executeQueryCheck("select trim('  abcde ')", List("abcde"))
  }

  test("concat") {
    executeQueryCheck("select concat('1', '2'),concat(null, '2')", List("12", null))
  }

  test("reverse") {
    executeQueryCheck("select reverse('abcde'),reverse(null)", List("edcba", null))
  }

  test("ltrim") {
    executeQueryCheck("select ltrim('  abcde '),ltrim(null)", List("abcde ", null))
  }

  test("rtrim") {
    executeQueryCheck("select rtrim('  abcde '),rtrim(null)", List("  abcde", null))
  }

  test("lpad1") {
    executeQueryCheck("select lpad('abcde', 2), lpad(null, 2)", List("  abcde", null))
  }

  test("lpad2") {
    executeQueryCheck(
      "select lpad('abcde', 2, 'a'), lpad(null, 5, 'a'), lpad('abcde', 6, 'a')",
      List("ab", null, "aabcde")
    )
  }

  test("rpad1") {
    executeQueryCheck("select rpad('abcde', 1), rpad(null, 1)", List("abcde ", null))
  }

  test("rpad2") {
    executeQueryCheck(
      "select rpad('abcde', 1, 'e'), rpad('abcde', 6, 'e'), " +
        "rpad('abcde', 6, null)",
      List("a", "abcdee", null)
    )
  }

  //TODO 多字节字符测试
  test("length") {
    executeQueryCheck("select length('abc')", List(3))
  }

  //TODO 多字节字符测试
  test("charLength") {
    executeQueryCheck("select charLength('abc')", List(3))
  }

  test("locate1") {
    executeQueryCheck("select locate('bc', 'abcabc')", List(2))
  }

  test("locate2") {
    executeQueryCheck("select locate('bc', 'abcabc', 2)", List(5))
  }

  test("md5") {
    executeQueryCheck("select md5('abcde')", List("AB56B4D92B40713ACC5AF89985D4B786"))
  }
}
