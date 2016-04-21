//
//  Crash+CoreDataProperties.swift
//  Blooper
//
//  Created by Ikjot Kaur on 14/01/16.
//  Copyright © 2016 Naukri. All rights reserved.
//
//  Choose "Create NSManagedObject Subclass…" from the Core Data editor menu
//  to delete and recreate this implementation file for your updated model.
//

import Foundation
import CoreData

extension Crash {

    @NSManaged var crashCallStack: String?
    @NSManaged var crashDescription: String?

}
