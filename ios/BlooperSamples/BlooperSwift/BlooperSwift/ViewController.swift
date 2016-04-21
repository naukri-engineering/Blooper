//
//  ViewController.swift
//  BlooperSwift
//
//  Created by Ikjot Kaur on 11/04/16.
//  Copyright © 2016 Naukri. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
   
    @IBAction func crashBtnClicked(sender: AnyObject) {
        let array = NSArray()
        let elem = array.objectAtIndex(99)
        print(elem)

    }


}

