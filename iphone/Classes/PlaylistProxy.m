/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2014 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "PlaylistProxy.h"

@implementation PlaylistProxy

+ (instancetype)proxyWithCatalog:(ComAppersonlabsBrightcoveCatalogProxy *)catalog {
    return [[[PlaylistProxy alloc] _initWithPageContext:catalog.pageContext] autorelease];
    
}

#pragma mark -
#pragma mark Public API

@end
