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
    checkCalciteSparkFlink("select upper('abcde')", List("ABCDE"))
  }

  test("lower") {
    checkCalciteSparkFlink("select lower('ABCDE')", List("abcde"))
  }

  test("substring1") {
    checkCalciteSparkFlink("select substring('abcde', 2, 3)", List("bcd"))
  }

  test("substring2") {
    checkCalciteSparkFlink("select substring('abcde', 2)", List("bcde"))
  }

  test("trim") {
    checkCalciteSparkFlink("select trim('  abcde ')", List("abcde"))
  }

  test("concat") {
    checkCalciteSparkFlink("select concat('1', '2')", List("12"))
    checkCalciteSpark("select concat(null, '2')", List(null))
  }

  test("reverse") {
    checkCalciteSparkFlink("select reverse('abcde')", List("edcba"))
    checkCalciteSpark("select reverse(null)", List(null))
  }

  test("ltrim") {
    checkCalciteSparkFlink("select ltrim('  abcde ')", List("abcde "))
    checkCalciteSpark("select ltrim(null)", List(null))
  }

  test("rtrim") {
    checkCalciteSparkFlink("select rtrim('  abcde ')", List("  abcde"))
    checkCalciteSpark("select rtrim(null)", List(null))
  }

  test("pad") {
    checkCalciteSparkFlink("select pad('abcde', 2, 'a'), pad('abcde', 7, 'a')", List("ab", "aa"))
    checkCalciteSpark("select pad(null, 5, 'a')", List(null))
  }

  //FIXME flink Invalid number of arguments to function 'LPAD'. Was expecting 3 arguments
  test("lpad1") {
    checkCalciteSpark("select lpad('abcde', 2)", List("  abcde"))
    checkCalciteSpark("select lpad(null, 2)", List(null))
  }

  test("lpad2") {
    checkCalciteSparkFlink(
      "select lpad('abcde', 2, 'a'), lpad('abcde', 6, 'a')",
      List("ab", "aabcde")
    )
    checkCalciteSpark("select lpad(null, 5, 'a')", List(null))
  }

  //FIXME flink Invalid number of arguments to function 'LPAD'. Was expecting 3 arguments
  test("rpad1") {
    checkCalciteSpark("select rpad('abcde', 1)", List("abcde "))
    checkCalciteSpark("select rpad(null, 1)", List(null))
  }

  test("rpad2") {
    checkCalciteSparkFlink(
      "select rpad('abcde', 1, 'e'), rpad('abcde', 6, 'e')",
      List("a", "abcdee")
    )
    checkCalciteSpark("select rpad('abcde', 6, null)", List(null))
  }

  //TODO 多字节字符测试
  test("length") {
    checkCalciteSparkFlink("select length('abc')", List(3))
  }

  //TODO 多字节字符测试
  // FIXME flink
  test("charLength") {
    checkCalciteSpark("select charLength('abc')", List(3))
  }

  test("locate1") {
    checkCalciteSparkFlink("select locate('bc', 'abcabc')", List(2))
  }

  //FIXME flink output: 2
  test("locate2") {
    checkCalciteSpark("select locate('bc', 'abcabc', 2)", List(5))
    checkCalciteSpark("select locate(null, 'abcabc', 2)", List(null))
  }

  test("md5") {
    checkCalciteSpark("select md5('abcde')", List("AB56B4D92B40713ACC5AF89985D4B786"))
  }
}
