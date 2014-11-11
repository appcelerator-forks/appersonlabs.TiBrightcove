/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2014 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
#import "TiProxy.h"
#import "BCOVPlayerSDK.h"
#import "ComAppersonlabsBrightcoveCatalogProxy.h"

@interface PlaylistProxy : TiProxy
@property (nonatomic, strong) BCOVPlaylist * playlist;
+ (instancetype)proxyWithCatalog:(ComAppersonlabsBrightcoveCatalogProxy *)catalog playlist:(BCOVPlaylist *)playlist;
@end
