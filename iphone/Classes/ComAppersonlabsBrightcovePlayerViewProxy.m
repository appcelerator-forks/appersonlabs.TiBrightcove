/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2014 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "ComAppersonlabsBrightcovePlayerViewProxy.h"
#import "ComAppersonlabsBrightcovePlayerView.h"
#import "TiUtils.h"

@interface ComAppersonlabsBrightcovePlayerViewProxy ()
@property (nonatomic, readonly) id<BCOVPlaybackController> playbackController;
@property (nonatomic, strong) id<BCOVPlaybackSession> currentPlaybackSession;
@property (nonatomic, strong) BCOVCatalogService * catalogService;
@end

@implementation ComAppersonlabsBrightcovePlayerViewProxy

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

#pragma mark -
#pragma mark Public API

- (void)setToken:(id)value {
    self.catalogService = [[BCOVCatalogService alloc] initWithToken:value];
}

- (void)setPlaylistID:(id)value {
    // @"3868842075001"
    [self.catalogService findPlaylistWithPlaylistID:value parameters:nil completion:^(BCOVPlaylist *playlist, NSDictionary *jsonResponse, NSError *error) {
        if (error) {
            NSLog(@"[ERROR] error setting playlist ID: %@", error);
            return;
        }
        
        if (playlist) {
            [self.playbackController setVideos:playlist.videos];
        }
    }];
}

- (void)advanceToNext:(id)args {
    [self.playbackController advanceToNext];
}

- (void)pause:(id)args {
    [self.playbackController pause];
}

- (void)play:(id)args {
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
