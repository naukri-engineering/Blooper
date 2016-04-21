We have been using [NewMonk](https://github.com/naukri-engineering/NewMonk) for Android Crash reporting. It helps us quickly identify and pin point crashes and diagnose them by device, app version and OS. Thus it helps fix issues before they reach end users and ship higher quality apps. It also keeps us informed about crashes through alerts so that we never miss a problem. 

**Blooper** is a library that enables an Android application to automatically post detailed crash reports to NewMonk.

**Features**

1. Quick and easy set up with just a few lines of code.
2. Ability to configure the endpoint, appid, interval, request headers and debug mode easily. More variables to be made configurable soon.
3. Detailed crash reports along with OS, device and application details.
4. Compatible with all Android versions from 2.3 onwards.
5. Send reports for caught and custom exceptions also.
6. Automatically handles device restart.
7. Batched transfers to conserve battery and bandwidth. Limits the number of network calls.
8. If there is no network coverage, reports are stored and sent later on next run. To limit the storage size, not more than 20 exceptions will be stored at any time.
9. Find out who your app crashes affected. Can be used to trace specific customer reported issues.

**Integration**

To integrate the library in your application, follow the steps below,

1. Download and include this library in your project
2. Configure mandatory parameters. An exception is thrown if these are not provided.
   * uri: The newmonk endpoint to send the crashes/exceptions to. It should return a response code 202/204 without a response body. Response body, if any, would be ignored.
   * appId: Your unique application id. This is the id that NewMonk server has created for you so that it can differentiate between multiple apps.
3. Configure optional parameters.
   * debug: Used to enable or disable the logcat entries from this library. If your app is not in debug mode then logs will not be shown even if you set it to true. Default value is false.
   * interval: The frequency at which crash logs should be sent to server. Default value is 1 hour.
   * headers: If you want to send own headers, define them in this field.
   
4. Configure using either of the methods below
    * Using Annotation.
    
    ```Java
    @@BlooperCrashReport(
        uri = "http://www.example.com/newmonk/log.php",
        appId = "13",
        interval = AlarmManager.INTERVAL_HOUR,
        debug = true
    )
    public class AppController extends Application {
	    @Override
	    public void onCreate() {
   		    super.onCreate();
	    }
    }
    ```
    * Using BlooperConfiguration class
    
    ```Java
    public class AppController extends Application {
	    @Override
	    public void onCreate() {
    		super.onCreate();
	        try {
      	      		BlooperConfiguration blooperConfiguration = new BlooperConfiguration();
	      	        blooperConfiguration.uri = "http://www.example.com/newmonk/log.php";
        	      	blooperConfiguration.appId = "13";
	            	blooperConfiguration.debug = true;
   	                blooperConfiguration.interval = AlarmManager.INTERVAL_HOUR;
      		        blooperConfiguration.httpHeaders = new HashMap<>();
            		Blooper.initBlooper(this, blooperConfiguration);
		    } catch (BlooperException e) {
				// handle exception
            }
	    }
    }
    ```
5. Initialize - Just call initBlooper in your application class.
    ```Java
    Blooper.initBlooper(Context context)
    ```
    If you choose to configure using  BlooperConfiguration then call 
    ```Java
    Blooper.initBlooper(Context context, BlooperConfiguration blooperConfiguration)
    ```
6. Define user identifier – In order to link crashes with users, you have to define a user id. This is needed only once, when a user logs in to your app. Remember to set it to blank on logout.
   ```Java
    Blooper.getBlooperConfiguration().setUserId("1234567");
    ```
7. Log caught exceptions – The library takes care of all the uncaught exceptions for you. But sometimes you may want to report caught exceptions without throwing them because you know that your application has reached an unexpected state. This can be done as,
   ```Java 
    Blooper.logException(String classname, Exception exception)
    ```


**Contributors**

* Akash Singla

* Minni Arora

* Sudeep SR

**Contact Us**

Get in touch with us with your suggestions, thoughts and queries at engineering@naukri.com

**License**

Please see [LICENSE.md](LICENSE.md) for details
