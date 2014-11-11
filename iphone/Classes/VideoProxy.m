/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2014 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "VideoProxy.h"

@interface VideoSource : TiProxy
@property (nonatomic, retain) BCOVSource * source;
+ (instancetype)proxyWithVideo:(VideoProxy *)video source:(BCOVSource *)source;
@end

@implementation VideoSource

+ (instancetype)proxyWithVideo:(VideoProxy *)video source:(BCOVSource *)source {
    return [[[VideoSource alloc] _initWithPageContext:video.pageContext source:source] autorelease];
}

- (id)_initWithPageContext:(id<TiEvaluator>)context source:(BCOVSource *)source {
    if (self = [super _initWithPageContext:context]) {
        self.source = source;
    }
    return self;
}

- (id)url {
    return self.source.url;
}

- (id)deliveryMethod {
    return self.source.deliveryMethod;
}

- (id)properties {
    return self.source.properties;
}

@end

@interface CuePoint : TiProxy
@property (nonatomic, retain) BCOVCuePoint * cuePoint;
+ (instancetype)proxyWithVideo:(VideoProxy *)video cuePoint:(BCOVCuePoint *)cuePoint;
@end

@implementation CuePoint

+ (instancetype)proxyWithVideo:(VideoProxy *)video cuePoint:(BCOVCuePoint *)cuePoint {
    return [[[CuePoint alloc] _initWithPageContext:video.pageContext cuePoint:cuePoint] autorelease];
}

- (id)_initWithPageContext:(id<TiEvaluator>)context cuePoint:(BCOVCuePoint *)cuePoint {
    if (self = [super _initWithPageContext:context]) {
        self.cuePoint = cuePoint;
    }
    return self;
}

- (id)position {
    // return seconds, currently ignores epoch and flags
    return NUMDOUBLE(self.cuePoint.position.value / self.cuePoint.position.timescale);
}

- (id)type {
    return self.cuePoint.type;
}

- (id)properties {
    return self.cuePoint.properties;
}

@end


@implementation VideoProxy

+ (instancetype)proxyWithCatalog:(ComAppersonlabsBrightcoveCatalogProxy *)catalog video:(BCOVVideo *)video {
    return [[[VideoProxy alloc] _initWithPageContext:catalog.pageContext video:video] autorelease];
}

+ (instancetype)proxyWithPlaylist:(PlaylistProxy *)playlist video:(BCOVVideo *)video {
    return [[[VideoProxy alloc] _initWithPageContext:playlist.pageContext video:video] autorelease];
}

- (id)_initWithPageContext:(id<TiEvaluator>)context video:(BCOVVideo *)video {
    if (self = [super _initWithPageContext:context]) {
        self.video = video;
    }
    return self;
}

#pragma mark -
#pragma mark Public API

- (id)properties {
    return self.video.properties;
}

- (id)cuePoints {
    NSMutableArray * result = [NSMutableArray arrayWithCapacity:self.video.cuePoints.count];
    for (BCOVCuePoint * cuePoint in self.video.cuePoints) {
        [result addObject:[CuePoint proxyWithVideo:self cuePoint:cuePoint]];
    }
    return result;
}

- (id)sources {
    // array of BCOVSource objects
    NSMutableArray * result = [NSMutableArray arrayWithCapacity:self.video.sources.count];
    for (BCOVSource * source in self.video.sources) {
        [result addObject:[VideoSource proxyWithVideo:self source:source]];
    }
    return result;
}

// TODO updatable video?

@end
