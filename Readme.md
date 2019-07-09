## Main logic

- Main logic for matching is in TopNMatches
- Number of matches to be returned is configurable in `application.properties`
- Ordering of jobs are done after excluding jobs that are not within 
location or doesn't fullfill 
required skills or doesn't satisfy driver's license condition.
- ordering is done for number of workers required in decending order.
- for earliest start date.
- and ranking of match calculated for certification criteria.
- after that only top 3 [or according to configuration] ranks are further processed.
- which later is flatten to single stream and only top 3 out of all is selected. 
