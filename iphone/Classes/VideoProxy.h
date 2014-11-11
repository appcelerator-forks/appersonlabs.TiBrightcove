/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2014 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
#import "TiProxy.h"
#import "BCOVPlayerSDK.h"
#import "ComAppersonlabsBrightcoveCatalogProxy.h"
#import "PlaylistProxy.h"

@interface VideoProxy : TiProxy
@property (nonatomic, strong) BCOVVideo * video;
+ (instancetype)proxyWithCatalog:(ComAppersonlabsBrightcoveCatalogProxy *)catalog video:(BCOVVideo *)video;
+ (instancetype)proxyWithPlaylist:(PlaylistProxy *)playlist video:(BCOVVideo *)video;
@end
