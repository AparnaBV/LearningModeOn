** Testing 

All the test suites reside under TakeAway/src/it/java/comLearningModeOn/.

Corresponding TestNG xml's reside in TakeAway/testngSuites.

For debugging tests, run /testngSuites/smoke.xml in debug mode with breakpoints in tests of interest.

Regression Suite
Regression suite uses smoke suite and run against the provided Movie List Database. 

For running whole suite / single test modify is in TakeAway/testngSuites/smoke.xml.

For running tests using 'smoke' profile, run below command from TakeAway 

mvn clean install -P smoke

Alternatively , mvn clean install will trigger all the tests.

Environment
https://api.themoviedb.org/

Note : One of the assertions under ListTest fails due to a potential bug. To reproduce the bug - uncomment the data entry in the dataprovider
 (under base/SmokeBaseTest/getInvalidMovieListData)

Test Credentials
The tests leverage an apiKey, and 2 sets of access tokens (one for read access and one for write access).
Individual credentials can be created by following these steps for  [apiKey](https://www.themoviedb.org/settings/api) and [Write access Token](https://developers.themoviedb.org/4/getting-started/authorization)
Once key and access tokens are procured, update the details in the passkeys.json file (in Takeaway/src/it/resources/)

Test Data
The different test data files are configured under Takeaway/src/it/resources/test-data/
Note: There seems to be a rule placed on the number of same lists that can be created using a given key. In case issue of such kind arises, just update the 'description' of the particular list test-file 
 and re-run the same test. The test would complete without any issues.


