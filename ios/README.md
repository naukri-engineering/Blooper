*Blooper* is an open source Universal framework, which automatically posts crash reports to [New Monk](https://github.com/naukri-engineering/NewMonk) (or lets say, any server side script you may have)


### Features
***
1. Quick and easy setup with just a few lines of code.

2. Detailed crash reports along with OS, device and application details.

3. Compatible with all iOS 8.0 and above (Dynamic frameworks are supported from ios 8.0 and above)

4. If there is no network coverage, reports are stored locally and sent later on the next run.

5. Find out who your app crashes affected. Can be used to trace specific customer reported issues.

6. Flexible enough to be integrated in both Swift and Objective-C projects

### Integration
***
To integrate the library in your application, follow the below mentioned steps:
`
1. Download the library in your project.

2. Configure mandatory parameters: 

    **apiURL**: The newmonk endpoint to send the crashes/exceptions to. It should return a http response code 202/204 (without a response body). Response body, if any, would be ignored.
    
    **appID**:  Your unique application id. This id can be server generated or by your own logic so that it can differentiate between multiple apps.
    
3. Configure optional properties:

    **debug**: Used to enable or disable the log/print entries from this library. This is an optional variable. Use “true” for enabling log and “false” for disabling it. Default value is disabled “true”. 

4. Set Logged in email Id: To track the crash of a particular user, user ID is passed in `setUserID(loggedInID:String)` function.

5. Initialize the library with the following code:
`

    #### Swift:
    ***
    ```swift
        
        import Blooper
        
        /**
        * Some code
        * goes here
        */
        
        let loggedInID = "test@crash.com"
        
        let appId = APP_ID
        
        let url = "www.example.com" 
        
        Blooper.sharedManager.debug = true
        
        Blooper.sharedManager.setUserId(loggedInID)
        
        Blooper.sharedManager.addCrash(appId, apiURL: url)
    ```

    #### Objective-C:
    ***
    ```objc
        #import <Blooper/Blooper.h>
        
        /**
        * Some code
        * goes here
        */
        
        NSString *loggedInID = @"test1@crash.com";
        
        NSString *appId = APP_ID;
        
        NSString *url =  @"www.example.com";    
        
        [Blooper sharedManager].debug = true;
        
        [[Blooper sharedManager] setUserId:loggedInID];
        
        [[Blooper sharedManager]addCrash:appId apiURL:url];
        
    ```

###  Troubleshooting:

#### The framework is created in Swift 3.0 and xcode 7.3 ####
***
If you encounter below errors:
#### Error:
```swift
Xcode: Module file was created by an older version of the compiler
or
Xcode: Module file was created by a newer version of the compiler
or
dyld: Library not loaded: @rpath/libswiftCore.dylib
Reason: Incompatible library version: Blooper requires version 1.0.0 or later, but libswiftCore.dylib provides version 0.0.0
```
***
#### Reason  : The reason this error is occurring is because there might be a version difference between the xcode/swift version in which this framework is created and version you are using and which apparently is not backward/forward compatible with other versions . 
As mentioned in [Apple docs](https://developer.apple.com/swift/blog/?id=2) : 
To be safe, all components of your app should be built with the same version of Xcode and the Swift compiler to ensure that they work together.

#### Solution  : Rebuild the universal framework by :
* Select Blooper-Universal scheme 
* Select build only device -> Generic ios Device 
* Run 
* Replace the current framework in your project with the newly generated one 

***
#### Error:
```swift
dyld: Library not loaded: @rpath/Blooper.framework/Blooper
Referenced from: /Users/syshen/Library/Developer/CoreSimulator/Devices/..../Blooper.app/Blooper
Reason: image not found
```

#### Solution: 
* Ensure you have added Blooper.framework in `General > Embedded libraries` and `General > Linked Frameworks and Libraries`
* In Build Settings > Build options` : Set `“Embedded Content Contains Swift Code” to  Yes.
* And make sure the `“Runpath Search Paths” is set as “@executable_path/Frameworks”`.


### Development Troubleshooting :
***
```php
Symbol not found 
```
In order to build the Universal Framework, always set “Build Active Architecture Only” to “No” in both debug and release 
Always build the Blooper-Universal scheme to get the Universal Framework. 


### Warning:
***
If you redeclare **NSSetUncaughtExceptionHandler** function then this framework will not work.

### Contributors
***
* Ikjot Kaur

* Ayush Goel

### Contact Us
***
Get in touch with us with your suggestions, thoughts and queries at engineering@naukri.com


### License
***
Please see [LICENSE.md](LICENSE.md) for details
