#Splitting Names

To help personalise our mailshots we want to have the first name and last name of the customer. Unfortunately the customer data that we are supplied only contains full names.

The system therefore attempts to break a supplied full name into its constituents by splitting around whitespace.

### [Basic Example](- "basic")

The full name [John Smith](- "#result = split(#TEXT)") 
will be broken into first name
[John](- "?=#result.firstName")
and last name
[Smith](- "?=#result.lastName")
