//
//  ViewController.m
//  BlooperSampleObjC
//
//  Created by Ikjot Kaur on 11/04/16.
//  Copyright © 2016 Naukri. All rights reserved.
//

#import "ViewController.h"
#import <Blooper/Blooper.h>


@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)crashBtnClicked:(id)sender {
    NSArray *array = [[NSArray alloc]init];
    NSString *elem = [array objectAtIndex:99];
    NSLog(@"%@",elem);
}

@end
