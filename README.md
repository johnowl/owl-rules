# Owl Rules

[![CircleCI](https://circleci.com/gh/johnowl/owl-rules.svg?style=svg)](https://circleci.com/gh/johnowl/owl-rules)
[![codecov](https://codecov.io/gh/johnowl/owl-rules/branch/master/graph/badge.svg)](https://codecov.io/gh/johnowl/owl-rules)
[![Download](https://api.bintray.com/packages/johnowl/maven/owl-rules/images/download.svg?version=1.2.031) ](https://bintray.com/johnowl/maven/owl-rules/1.2.031/link)

A rule engine processor with an expression boolean language to define the rules written in Kotlin. Powered by [better-parse](https://github.com/h0tk3y/better-parse).

See the Go version at [gobule](https://github.com/udhos/gobule)

## How to use

    val result = RulesEngine().check("platform = 'android'", mapOf("platform" to "android"))
    
The first parameter is the rule and the second is a Map<String, Any> that contains de variables used to evaluate the rule.


## Bule Language

![Blue teapot](docs/assets/bule.jpg "Blue teapot1")

In Brazilian Portuguese bule means teapot. Bule is the name of the programming language used to write the rules, it's just boolean expressions. The result of a Bule expression is always `true` or `false`. See some examples of valid Bule expressions:

| Expression | Result | Description |
| --- | --- | --- |
| true | true | Literal true |
| false | false | Literal false |
| 1 = 1 | true | Number equality |
| 1 = 2 | false | Number equality |
| 1 > 3 | false | Number is greater than other number |
| 1 >= 3 | false | Number is greater than or equals other number |
| 1 < 3 | true | Number is less than other number |
| 1 <= 3 | false | Number is less than or equals other number |
| 'john' = 'owl' | false | Text equality |
| [1 2 3 4] CONTAINS 4 | true | Check if a number list contains a number |
| ['blue' 'yellow' 'green'] CONTAINS 'pink' | false | Check if a text list contains a text |
| [1 2 3 4] NOT CONTAINS 4 | false | Check if a number list does not contain a number |
| Number(version) >= 12] | false | Check if a variable named version converted to Number is greater than or equals the number 12 |
| (1 > 2) OR (1 = 1)| true | Return true if one of the expressions is true |
| (3 > 2) AND (1 = 1)| true | Return true if all of the expressions are true |
| NOT (1 = 1)| false | Return true if the result is false and false if the result is true |


### Data Types

- **Number**: it's an integer positive number like `12`, `56`, `0`, `99`.
- **Text**: it's something inside `'` and `'`, like `'John'`, `'12'`, `'Lorem ipsum'`.
- **List**: it's a group of Number or Text values, some valid lists are: `[1 2 3 4]`, `['blue' 'green' 'yellow']`

### Variables

**Every variable in Bule is text**, if you want to use a variable as a Number or a List you must convert it 
using `Number(variableName)` for a number or `List(variableName)` for a list.

### Complex expressions

**You always have to use parentheses in complex expressions** with AND, OR, NOT to avoid inconsistencies. Examples:

- `(NOT (1 > 3)) AND (2 > 3) OR (3 > 2)`
- `((3 > 2) AND (2 > 4) AND (4 > 3)) OR (1 = 1)`


### CurrentTime()

Since version 1.2.0 you can use the function `CurrentTime()` in your rules, 
this function returns the current time in an integer format, for example,
`06:00:27` turns into `060027`. This behaviour allow us to use the integer 
operators to compare time. You need to use the time in 24 hour format.

See some examples:

```
    @Test
    fun `should return true when time is greater than current time`() {
        val currentMinusOneHour = LocalDateTime.now().minusHours(1)
        val formatter = DateTimeFormatter.ofPattern("HHmmss")
        val currentMinusOneHourFormatted = currentMinusOneHour.format(formatter).toInt()

        assert(engine.check("(CurrentTime() > Number(myTime))", mapOf("myTime" to currentMinusOneHourFormatted)))
    }

    @Test
    fun `should return false when time is not greater than current time`() {
        val currentPlusOneHour = LocalDateTime.now().plusHours(1)
        val formatter = DateTimeFormatter.ofPattern("HHmmss")
        val currentPlusOneHourFormatted = currentPlusOneHour.format(formatter).toInt()

        assertFalse(engine.check("(CurrentTime() > Number(myTime))", mapOf("myTime" to currentPlusOneHourFormatted)))
    }

    @Test
    fun `should return true when time is in interval`() {
        assert(engine.check("(Number(myTime) > 220000) OR (Number(myTime) < 060000)", mapOf("myTime" to "235600")))
        assert(engine.check("(Number(myTime) > 220000) OR (Number(myTime) < 060000)", mapOf("myTime" to "220001")))
        assert(engine.check("(Number(myTime) > 220000) OR (Number(myTime) < 060000)", mapOf("myTime" to "055959")))
        assert(engine.check("(Number(myTime) > 220000) OR (Number(myTime) < 060000)", mapOf("myTime" to "032700")))
        assert(engine.check("(Number(myTime) > 070000) AND (Number(myTime) < 080000)", mapOf("myTime" to "070100")))
        assert(engine.check("(Number(myTime) > 070000) AND (Number(myTime) < 080000)", mapOf("myTime" to "075959")))
    }

    @Test
    fun `should return false when time is not in interval`() {
        assertFalse(engine.check("(Number(myTime) > 220000) OR (Number(myTime) < 060000)", mapOf("myTime" to "070000")))
        assertFalse(engine.check("(Number(myTime) > 220000) OR (Number(myTime) < 060000)", mapOf("myTime" to "060100")))
        assertFalse(engine.check("(Number(myTime) > 220000) OR (Number(myTime) < 060000)", mapOf("myTime" to "120000")))
        assertFalse(engine.check("(Number(myTime) > 220000) OR (Number(myTime) < 060000)", mapOf("myTime" to "215959")))
        assertFalse(engine.check("(Number(myTime) > 070000) AND (Number(myTime) < 080000)", mapOf("myTime" to "080001")))
        assertFalse(engine.check("(Number(myTime) > 070000) AND (Number(myTime) < 080000)", mapOf("myTime" to "065959")))
    }
```
