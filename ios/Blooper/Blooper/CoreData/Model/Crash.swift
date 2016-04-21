//
//  Crash.swift
//  Blooper
//
//  Created by Ikjot Kaur on 14/01/16.
//  Copyright © 2016 Naukri. All rights reserved.
//

import Foundation
import CoreData
import UIKit

class Crash: NSManagedObject {
    
    static private var context: NSManagedObjectContext!
    static private var fetchRequest: NSFetchRequest!
    static private var entity1: NSEntityDescription!
    private var error:NSError!
    static private var debuggingEnabled:Bool!
    
    
    class func initCrash(debug:Bool)
    {
        
        debuggingEnabled = debug
     
        NSSetUncaughtExceptionHandler { (exception:NSException) -> Void in
            Crash.saveCrash(exception.description)
        }
        
        WebServiceManager.uploadCrashToServer(debug)
    }
    
    private class func saveCrash(crashDescription:String)
    {
        if(debuggingEnabled == true)
        {
            print("Crash occured -> \(crashDescription)")
        }
        context = CoreDataHelper.sharedInstance().writerMOC
        let obj = NSEntityDescription.insertNewObjectForEntityForName(CRASH_ENTITY_NAME, inManagedObjectContext: context) as! Crash
        obj.crashDescription = crashDescription
        do {
            try context.save()
            CoreDataHelper.saveDataContext()
            if(debuggingEnabled == true)
            {
                print("Crash saved")
            }
        }
        catch{
            if(debuggingEnabled == true)
            {
                print("Error occured while saving crash \(error)")
            }
            
        }
    }
    
        
     class func getCrash() -> NSArray
    {
        var fetchedObjects: [Crash]!
        context = CoreDataHelper.sharedInstance().mainMOC
        
        fetchRequest = NSFetchRequest()
        entity1 = NSEntityDescription.entityForName(CRASH_ENTITY_NAME, inManagedObjectContext: context)
        fetchRequest.entity = entity1
        do {
            try fetchedObjects = context.executeFetchRequest(fetchRequest) as! [Crash]
            if(debuggingEnabled == true)
            {
                print("No of entitites fetched -> \(fetchedObjects.count)")
            }
            return fetchedObjects;
        } catch let error{
            if(debuggingEnabled == true)
            {
                print("error occured while fetching entities -> \(error)")
            }
        }
        return NSArray()
    }
    
     class func deleteCrash()
    {
        var fetchedObjects: NSArray!
        context = CoreDataHelper.sharedInstance().managedObjectContext
        fetchRequest = NSFetchRequest()
        entity1 = NSEntityDescription.entityForName(CRASH_ENTITY_NAME, inManagedObjectContext: context)
        fetchRequest.entity = entity1
        do {
            try fetchedObjects = context.executeFetchRequest(fetchRequest)
            for obj in fetchedObjects
            {
                context.deleteObject(obj as! NSManagedObject)
            }
            
        } catch let error{
            if(debuggingEnabled == true)
            {
                print("Error occured while deleting the entities -> \(error)")
            }
        }
        
        do {
            try context.save()
            CoreDataHelper.saveDataContext()
            if(debuggingEnabled == true)
            {
                print(("Enitites successfully deleted"))
            }
        } catch let error{
            if(debuggingEnabled == true)
            {
                print("Error occured while deleting the enitties -> \(error)")
            }
        }

    }
    
    
}
