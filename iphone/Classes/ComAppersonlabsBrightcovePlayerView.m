/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2014 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "ComAppersonlabsBrightcovePlayerView.h"

@interface ComAppersonlabsBrightcovePlayerView () {
    UIView * playbackControllerView;
}
@end

@implementation ComAppersonlabsBrightcovePlayerView

- (void)dealloc {
    RELEASE_TO_NIL(playbackControllerView);
    [super dealloc];
}

- (void)setPlaybackControllerView:(UIView *)view {
    playbackControllerView = [view retain];
    playbackControllerView.bounds = self.bounds;
    [self addSubview:playbackControllerView];
}

- (void)frameSizeChanged:(CGRect)frame bounds:(CGRect)bounds {
    if (playbackControllerView) {
        [TiUtils setView:playbackControllerView positionRect:bounds];
    }
    [super frameSizeChanged:frame bounds:bounds];
}

@end
