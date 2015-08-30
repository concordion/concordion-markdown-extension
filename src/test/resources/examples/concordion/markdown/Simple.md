# Simple

We start with value {#x="one"}.

We expect to receive value {#x=="one"}.

## Assertions on method calls
{#x="1"} + {#y="2"} = {add(#x,#y)=="3"}

## Execute

{setup()}

When we initialise, we expect x to have the value of {x=="setup"}.

{run()}

After we run, we expect x to have the value of {x=="run"}.

