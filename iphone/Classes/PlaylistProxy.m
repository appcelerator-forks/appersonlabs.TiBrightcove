/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2014 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "PlaylistProxy.h"
#import "VideoProxy.h"

@implementation PlaylistProxy

+ (instancetype)proxyWithCatalog:(ComAppersonlabsBrightcoveCatalogProxy *)catalog playlist:(BCOVPlaylist *)playlist {
    return [[[PlaylistProxy alloc] _initWithPageContext:catalog.pageContext playlist:playlist] autorelease];
}

- (id)_initWithPageContext:(id<TiEvaluator>)context playlist:(BCOVPlaylist *)playlist {
    if (self = [super _initWithPageContext:context]) {
        self.playlist = playlist;
    }
    return self;
}

#pragma mark -
#pragma mark Public API

- (id)videos {
    NSMutableArray * result = [NSMutableArray arrayWithCapacity:self.playlist.videos.count];
    for (BCOVVideo * video in self.playlist.videos) {
        [result addObject:[VideoProxy proxyWithPlaylist:self video:video]];
    }
    return result;
}

- (id)properties {
    return self.playlist.properties;
}

// TODO updatable?

@end
