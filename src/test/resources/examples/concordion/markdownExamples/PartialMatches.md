# Partial Matches

Username searches return partial matches, i.e. all usernames containing the search string are returned.

### [Example](- "matches")

Given these users:

|[_setup_](- "setUpUser(#username)") [Username](- "#username")|
| ---------------------------------------------------------: |
| john.lennon     |
| ringo.starr     |
| george.harrison |
| paul.mccartney  |

Searching for [arr](- "#searchString") will return:

|[_search_][] [Matching Usernames][]|
| --------------------------------: |
| george.harrison |
| ringo.starr |

[_search_]:           - "c:verifyRows=#username : getSearchResultsFor(#searchString)"
[Matching Usernames]: - "?=#username"