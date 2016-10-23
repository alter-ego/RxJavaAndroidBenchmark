#RXJavaAndroidBenchmark

This is a simple benchmarking Android app that uses StackOverflow to open fixed search terms pages, with detailed pages with comments. It's compiled against API 23 but supports all Android versions from API 19. It also uses v23 support libraries.

The purpose of the app is to check the differences in app size and speed between normal Android app, app using rxjava and rxjava2. **That is the only differences between the app modules.** They all use the same libraries otherwise. For data loading we have used Retrofit which supports reactive call adapters so they can be easily switched/removed.

All apps use normal Android activities and fragments. When opening the app the search button is shown that, when pressed, performs 4 parallel searches for tags (`android`, `rxjava`, `countdownlatch`, `multithreading`) performed on StackOverflow API, and then opens a fragment with results, shown in a `ListView`. Clicking on a particular search result, you can open the question with answers and comments.

This repo has 4 modules:

- `app-norx`: normal Android app
- `app`: app using rx-java 1.1.6
- `app-rx2`: app using rx-java 2.0.0-RC5
- `testapplication`: test that uses previously saved results file in JSON formats and offers it to networking calls through Retrofit's `MockWebServer`.

With these modules the idea was to be able to test the normal RAM and CPU consumption starting the three normal apps on a device, but also to see if the parallel searches are quicker using rx-java libraries; and on the other hand to see if the Retrofit library itself influences the results in some way (by excluding internet communication).
 
We are using a single Activity for the context and Fragments for particular views. There's also a SettingsManager that serves as a global context beyond Application context. We have also added the basic Navigation Drawer and image loading to emulate the normal app as much as possible.

## TESTING PROCEDURE USED

When testing the app modules, the testing procedure was the following:

### RAM, CPU usage testing

	1. install apk obtained through `assembleDebug` for {app, app-norx, app-rx2} modules     
	2. open the app
    3. press search button
    4. for n = 1, 10 {
    	open details for item `n`;
    	return to results
    }

During these operations, we would write down maximum CPU usage; RAM usage would be measured at the end, with the app still opened on the search results (after step 4).

### Retrofit library loading testing

We executed the `StackOverflowApiManagerAndroidTest` in `testapplication` module and checked for `Benchit` tag in android logs, that printed the call timings (tags `simple-search-result-call-normal`, `simple-search-result-call-rx`, `simple-search-result-call-rx2`). 

### Further app results

We have also uploaded the APKs to Nimble Droid to check the number of methods, APK size, page loading times and memory leaks. You can find them here:

- no-rx: [https://nimbledroid.com/my_apps/solutions.alterego.stackoverflow.norx.test](https://nimbledroid.com/my_apps/solutions.alterego.stackoverflow.norx.test)
- rx: [https://nimbledroid.com/my_apps/solutions.alterego.stackoverflow.test](https://nimbledroid.com/my_apps/solutions.alterego.stackoverflow.test)
- rx2: [https://nimbledroid.com/my_apps/solutions.alterego.stackoverflow.rx2.test](https://nimbledroid.com/my_apps/solutions.alterego.stackoverflow.rx2.test)

## DEPENDENCIES

- AdvancedAndroidLogger - for advanced logging to logcat
- Lombok - for generating getters, setters, tostring etc.
- Rx-Java (core, Android) - for reactive programming
- Retrofit with OkHttp - for REST calls with rx support
- Universal image loader - for cached image loading 
- Gson - for JSON <-> POJO conversions
- JodaTime - for Date management and conversions
- Butterknife - for view injecting

## LICENSE

This app cannot be used for any purposes except for testing rx-java libraries and StackOverflow API.