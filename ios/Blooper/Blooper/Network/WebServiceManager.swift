//
//  WebServiceManager.swift
//  Blooper
//
//  Created by Ikjot Kaur on 09/04/16.
//  Copyright © 2016 Naukri. All rights reserved.
//

import UIKit

class WebServiceManager: NSObject {
    
    static var loggedInID:String?
    static var appId:String!
    static var versionNum:String!
    static var apiURL:String!
    static var debuggingEnabled:Bool!
    static var crashUploadInProgress:Bool = false
    
     class func uploadCrashToServer(debug:Bool)
    {
        if(crashUploadInProgress == true)
        {
            return
        }
        crashUploadInProgress = true
        debuggingEnabled = debug
        versionNum = NSBundle.mainBundle().infoDictionary?["CFBundleShortVersionString"] as! String
        
        let serverDictonary = NSMutableDictionary()
        serverDictonary.setObject(SOURCE_APP, forKey: SOURCE_KEY)
        serverDictonary.setObject(appId, forKey: APP_ID_KEY)
        if(loggedInID != nil)
        {
            serverDictonary.setObject(loggedInID!, forKey: USER_ID_KEY)
        }
        let enviromentDictonary = NSMutableDictionary()
        enviromentDictonary.setObject(NSDictionary(objects: [OS_IOS,UIDevice.currentDevice().systemVersion], forKeys: [NAME_KEY,VERSION_KEY]), forKey: OS_KEY)
        
        enviromentDictonary.setObject(NSDictionary(objects: [versionNum], forKeys: [VERSION_KEY]), forKey: APP_KEY)
        
        enviromentDictonary.setObject(NSDictionary(objects: [UIDevice.currentDevice().model], forKeys: [NAME_KEY]), forKey: DEVICE_KEY)
        serverDictonary.setObject(enviromentDictonary, forKey: ENVIRONMENT_KEY)
        
        let crashArray = NSMutableArray()
        let currentTimeStamp: Double! = NSDate().timeIntervalSince1970
        let currentTime: NSString! = NSString(format:"%f", currentTimeStamp)
        let fetchedObjects: [Crash]!  = Crash.getCrash() as! [Crash]
        if(fetchedObjects.count > 0)
        {
            for crashObj:Crash in fetchedObjects
            {
                let serverDict = NSMutableDictionary()
                serverDict.setObject(EXCEPTION_NAME, forKey: TAG_KEY)
                serverDict.setObject(currentTime, forKey: TIMESTAMP_KEY)
                serverDict.setObject(EXCEPTION_TYPE, forKey: EXCEPTION_TYPE_KEY)
                serverDict.setObject(EXCEPTION_MESSAGE, forKey: MESSAGE_KEY)
                serverDict.setObject(crashObj.crashDescription!, forKey: STACK_TRACE_KEY)
                crashArray.addObject(serverDict)
            }
            
            serverDictonary.setObject(crashArray, forKey: EXCETIONS_KEY)
            let serverData:NSData!
            do {
                try  serverData = NSJSONSerialization.dataWithJSONObject(serverDictonary, options: NSJSONWritingOptions.PrettyPrinted)
                let  mystring = NSString.init(data: serverData, encoding: NSUTF8StringEncoding)
                WebServiceManager.sendSeverCrash(mystring!)
            } catch let error{
                if(debuggingEnabled == true)
                {
                    print("Error ocurred -> \(error)")
                }
            }
        }
        else
        {
            if(debuggingEnabled == true)
            {
                print("No data in database")
            }
        }
    }
    
     class func sendSeverCrash(paramString:NSString!)
    {
        let request:NSMutableURLRequest! = NSMutableURLRequest(URL: NSURL(string: apiURL)!)
        request.HTTPMethod = HTTP_METHOD_POST
        request.HTTPBody = paramString.dataUsingEncoding(NSUTF8StringEncoding)
        NSURLConnection.sendAsynchronousRequest(request, queue: NSOperationQueue.mainQueue()) { (response:NSURLResponse?, data:NSData?, error:NSError?) -> Void in
            if let errorInfo = error
            {
                if(debuggingEnabled == true)
                {
                    print("Error occured in sending data to server -> \(errorInfo.description)")
                }
            }
            else
            {
                if(debuggingEnabled == true)
                {
                    print("Crash successfully uploaded")
                }
                Crash.deleteCrash()
                crashUploadInProgress = false
            }
        }
    }
}
