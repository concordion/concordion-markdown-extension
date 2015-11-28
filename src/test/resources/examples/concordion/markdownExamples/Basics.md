# Basic Commands

These examples are based on the [Concordion Tutorial](http://concordion.org/Tutorial.html), which contains additional commentary.

## Assert Equals

The world likes to be greeted with [Hello World!](- "?=getGreeting()").

## Set

The greeting for user [Bob](- "#firstName") will be: [Hello Bob!](- "?=greetingFor(#firstName)").

## Execute

### Execute with a void result
 
If the time is [09:00AM](- "setCurrentTime(#TEXT)"), then the greeting will say: [Good Morning World!](- "?=getDailyGreeting()").

### Execute with an object result

See [Splitting Names](SplittingNames.html "c:run")
