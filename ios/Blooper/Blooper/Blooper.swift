//
//  Blooper.swift
//  Blooper
//
//  Created by Ikjot Kaur on 03/03/16.
//  Copyright © 2016 Naukri. All rights reserved.
//

import UIKit

public class Blooper: NSObject {
    
    public var debug:Bool = true
    public static let sharedManager = Blooper()
    
    private override init()
    {
        super.init()
    }
    
    public func setUserId(loggedInID:String)
    {
        WebServiceManager.loggedInID = loggedInID
    }
    
    public func addCrash(appID:String,apiURL:String)
    {
        WebServiceManager.appId = appID
        WebServiceManager.apiURL = apiURL
        Crash.initCrash(debug)

    }
}
