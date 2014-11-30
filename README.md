#StackOverflowApiTest

This is a simple test program for StackOverflow Api, written for Android. It's compiled against API 21 (5.0) but supports all Android versions from API 14 (4.0) onwards. It also uses v21 support libraries. It is made to compile against Android Studio RC2 with gradle plugin 0.14. 

The program uses reactive programming techniques that enable event-driven programming, and rx-java with rx-android library enables us to select particular threads we want it to run on (for example, subscribing to DAO results is on Schedulers.io() thread, while observing and UI updating is on AndroidSchedulers.mainThread()) so as to avoid UI blocking.

From the Android UI perspective, it uses Navigation Drawer to easily navigate through the app, single Activity for the context and Fragments for particular views. There's also a SettingsManager that serves as a global context beyond Application context. The app supports both smartphones and tablets, portrait and landscape.

## DEPENDENCIES

- AdvancedAndroidLogger - for advanced logging to logcat
- Lombok - for generating getters, setters, tostring etc.
- Rx-Java (core, Android) - for event-driven programming
- Retrofit with OkHttp - for REST calls with rx support
- Universal image loader - for cached image loading 
- Gson - for JSON <-> POJO conversions
- JodaTime - for Date management and conversions
- Butterknife - for view injecting

## LICENSE

This app cannot be used for any purposes except for learning how to use RX Java and StackOverflow REST API.