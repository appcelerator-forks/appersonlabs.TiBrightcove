/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2014 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "ComAppersonlabsBrightcovePlayerViewProxy.h"
#import "ComAppersonlabsBrightcovePlayerView.h"
#import "PlaylistProxy.h"
#import "VideoProxy.h"
#import "TiUtils.h"

@interface ComAppersonlabsBrightcovePlayerViewProxy ()
@property (nonatomic, readonly) id<BCOVPlaybackController> playbackController;
@property (nonatomic, strong) id<BCOVPlaybackSession> currentPlaybackSession;
@end

@implementation ComAppersonlabsBrightcovePlayerViewProxy

USE_VIEW_FOR_CONTENT_WIDTH
USE_VIEW_FOR_CONTENT_HEIGHT

@synthesize playbackController=_playbackController;

- (id<BCOVPlaybackController>)playbackController {
    if (!_playbackController) {
        BCOVPlayerSDKManager *playbackManager = [BCOVPlayerSDKManager sharedManager];
        _playbackController = [[playbackManager createPlaybackControllerWithViewStrategy:nil] retain];
        _playbackController.delegate = self;
        [((ComAppersonlabsBrightcovePlayerView *)self.view) setPlaybackControllerView:_playbackController.view];
    }
    return _playbackController;
}

-(void)_initWithProperties:(NSDictionary *)properties {
    // check for any required parameters
    [super _initWithProperties:properties];
}

#pragma mark -
#pragma mark Public API

- (void)setPlaylist:(id)value {
    ENSURE_UI_THREAD_1_ARG(value)
    ENSURE_TYPE_OR_NIL(value, PlaylistProxy)
    if (value) {
        PlaylistProxy * proxy = (PlaylistProxy *)value;
        [self.playbackController setVideos:proxy.playlist.videos];
    }
    // TODO maybe nil should mean clear playlist?
}

- (void)setVideo:(id)value {
    ENSURE_UI_THREAD_1_ARG(value)
    ENSURE_TYPE_OR_NIL(value, VideoProxy)
    if (value) {
        VideoProxy * proxy = (VideoProxy *)value;
        [self.playbackController setVideos:@[proxy.video]];
    }
}

- (void)advanceToNext:(id)args {
    ENSURE_UI_THREAD_1_ARG(args)
    [self.playbackController advanceToNext];
}

- (void)pause:(id)args {
    ENSURE_UI_THREAD_1_ARG(args)
    [self.playbackController pause];
}

- (void)play:(id)args {
    ENSURE_UI_THREAD_1_ARG(args)
    [self.playbackController play];
}

#pragma mark -
#pragma mark BCOVPlaybackControllerDelegate

-(void)playbackController:(id<BCOVPlaybackController>)controller didAdvanceToPlaybackSession:(id<BCOVPlaybackSession>)session {
    self.currentPlaybackSession = session;
}

-(void)playbackController:(id<BCOVPlaybackController>)controller playbackSession:(id<BCOVPlaybackSession>)session didReceiveLifecycleEvent:(BCOVPlaybackSessionLifecycleEvent *)lifecycleEvent {

    // using if-else here in case we need to add event properties
    NSString * type = lifecycleEvent.eventType;
    if ([type isEqualToString:kBCOVPlaybackSessionLifecycleEventReady]) {
        [self fireEvent:@"ready" withObject:nil];
    }
    else if ([type isEqualToString:kBCOVPlaybackSessionLifecycleEventPlay]) {
        [self fireEvent:@"play" withObject:nil];
    }
    else if ([type isEqualToString:kBCOVPlaybackSessionLifecycleEventPause]) {
        [self fireEvent:@"pause" withObject:nil];
    }
    else if ([type isEqualToString:kBCOVPlaybackSessionLifecycleEventEnd]) {
        [self fireEvent:@"end" withObject:nil];
    }
    else if ([type isEqualToString:kBCOVPlaybackSessionLifecycleEventTerminate]) {
        [self fireEvent:@"terminate" withObject:nil];
    }
    else if ([type isEqualToString:kBCOVPlaybackSessionLifecycleEventFail]) {
        [self fireEvent:@"fail" withObject:nil];
    }
}


@end
