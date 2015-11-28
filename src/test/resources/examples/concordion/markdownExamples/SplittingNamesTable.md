#Splitting Names

To help personalise our mailshots we want to have the first name and last name of the customer. Unfortunately the customer data that we are supplied only contains full names.

The system therefore attempts to break a supplied full name into its constituents by splitting around whitespace.

### [Examples](- "basic")

|[_split_][]  [Full Name][]|[First Name][]|[Last Name][]|
| :----------------------: | :----------: |  :--------: |
|                John Smith|          John|        Smith|
|            David Peterson|         David|     Peterson|

[Full Name]:  - "#fullName"
[_split_]:    - "#result = split(#fullName)"
[First Name]: - "?=#result.firstName"
[Last Name]:  - "?=#result.lastName"
