/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2014 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "ComAppersonlabsBrightcoveCatalogProxy.h"
#import "BCOVPlayerSDK.h"
#import "PlaylistProxy.h"
#import "VideoProxy.h"

@interface ComAppersonlabsBrightcoveCatalogProxy ()
@property (nonatomic, strong) BCOVCatalogService * catalogService;
@end

@implementation ComAppersonlabsBrightcoveCatalogProxy

-(void)_initWithProperties:(NSDictionary *)properties {
    // ensure that a token was passed on creation
    id token = [properties valueForKey:@"token"];
    if (token) {
        // catalog service must be created before setting playlist or video IDs
        ENSURE_TYPE(token, NSString);
        self.catalogService = [[BCOVCatalogService alloc] initWithToken:token];
    }
    else {
        NSLog(@"[ERROR] BrightCove catalog missing required parameter 'token'");
    }
    
    [super _initWithProperties:properties];
}

#pragma mark -
#pragma mark Helper Methods

- (NSDictionary *)_constructCallbackErrorResponse:(NSError *)error {
    if (error) {
        return @{
                 @"code": NUMLONG(error.code),
                 @"error": error.localizedDescription,
                 @"result": [NSNull null],
                 @"success": NUMBOOL(NO)
                 };
    }
    else {
        return @{
                 @"code": NUMLONG(-1),
                 @"error": @"unknown error fetching playlist",
                 @"result": [NSNull null],
                 @"success": NUMBOOL(NO)
                 };
    }
    
}

- (NSDictionary *)_constructCallbackSuccessResponse:(TiProxy *)proxy {
    if (proxy) {
        return @{
                 @"code": [NSNull null],
                 @"error": [NSNull null],
                 @"result": proxy,
                 @"success": NUMBOOL(YES)
                 };
    }
    else {
        return @{
                 @"code": NUMLONG(-1),
                 @"error": @"unknown error",
                 @"result": [NSNull null],
                 @"success": NUMBOOL(NO)
                 };
    }
    
}

#pragma mark -
#pragma mark Public API

- (void)findPlaylist:(id)args {
    NSString * playlistID = nil;
    KrollCallback * cb = nil;
    NSDictionary * params = nil;
    ENSURE_ARG_AT_INDEX(playlistID, args, 0, NSString);
    ENSURE_ARG_AT_INDEX(cb, args, 1, KrollCallback);
    ENSURE_ARG_OR_NIL_AT_INDEX(params, args, 2, NSDictionary);
    
    __block PlaylistProxy * proxy = [PlaylistProxy proxyWithCatalog:self];
    [self.catalogService findPlaylistWithPlaylistID:playlistID parameters:params completion:^(BCOVPlaylist *playlist, NSDictionary *jsonResponse, NSError *error) {
        NSDictionary * cbresp;
        if (playlist) {
            proxy.playlist = playlist;
            cbresp = [self _constructCallbackSuccessResponse:proxy];
        }
        else {
            cbresp = [self _constructCallbackErrorResponse:error];
        }
        [cb call:@[cbresp] thisObject:nil];
    }];
}

- (void)findVideo:(id)args {
    NSString * videoID = nil;
    KrollCallback * cb = nil;
    NSDictionary * params = nil;
    ENSURE_ARG_AT_INDEX(videoID, args, 0, NSString);
    ENSURE_ARG_AT_INDEX(cb, args, 1, KrollCallback);
    ENSURE_ARG_OR_NIL_AT_INDEX(params, args, 2, NSDictionary);
    
    __block VideoProxy * proxy = [VideoProxy proxyWithCatalog:self];
    [self.catalogService findVideoWithVideoID:videoID parameters:params completion:^(BCOVVideo *video, NSDictionary *jsonResponse, NSError *error) {
        NSDictionary * cbresp;
        if (video) {
            proxy.video = video;
            cbresp = [self _constructCallbackSuccessResponse:proxy];
        }
        else {
            cbresp = [self _constructCallbackErrorResponse:error];
        }
        [cb call:@[cbresp] thisObject:nil];
    }];
}

@end
