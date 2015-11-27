Concatenating [a](- "#c1"), [&](- "#c2") and [b](- "#c3") results in [a&b](- "?=concat(#c1, #c2, #c3)")


My browser is [Firefox 3.6][browser]

[browser]: - "?=getBrowserDetails()"

# Concordion Command Examples

| [_add_](- "#z=add(#x,#y)")[Number 1](- "#x") | [Number 2](- "#y") | [Result](- "?=#z") |
| --------------: | --------------: | -------------: |
|               1 |               0 |              1 |
|               1 |              -3 |             -2 |

| [add][] [Number 1](- "#x") | [Number 2](- "#y") | [Result](- "?=#z") |
| ---------------------------: | --------------: | -------------: |
|                            1 |               0 |              1 |
|                            1 |              -3 |             -2 |

[add]: - "#z=add(#x,#y)"

### Verify Rows

Checks a collection of results returned from the fixture.
The collection must be sorted and implement `java.lang.Iterable`.

It may be necessary to sort the collection in the fixture if it is not already sorted.

#### [Verify Rows Example](- "verifyRows")
| [check GST](- "c:verifyRows=#detail:getInvoiceDetails()") [Sub Total](- "?=#detail.subTotal") | [GST](- "?=#detail.gst") |
| ----------------------------------: | -----------------------: |
|                                 100 |                       15 |
|                                 500 |                       75 |
|                                  20 |                        3 |

#### ~~Verify Rows Example~~

### Echo

Normally used for adding information about a test run:

Tests executed using [_browser_](- "c:echo=getBrowserDetails()").


### Run

Runs another test from this test. See [Run command](http://concordion.org/dist/1.3.1/test-output/concordion/spec/concordion/command/run/Run.html).

[Calc](Calc.html "c:run")

### Accessing contents of an HREF

Allows you to put test data in a separate file and access it from a specification.
This may indicate a "test smell".
See [Access to a Link's HREF](http://concordion.org/dist/1.3.1/test-output/concordion/spec/concordion/command/execute/AccessToLinkHref.html).

### Examples

#### [Example 1](- "calculator c:status=ExpectedToFail")

#### ~~Example 1~~


