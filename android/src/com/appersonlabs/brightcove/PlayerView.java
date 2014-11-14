package com.appersonlabs.brightcove;

import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

import com.brightcove.player.event.EventEmitterImpl;
import com.brightcove.player.view.BrightcoveVideoView;

public class PlayerView extends TiUIView {

    private static final String LCAT = "PlayerView";

    private BrightcoveVideoView videoView;

    public PlayerView(final TiViewProxy proxy) {
        super(proxy);
        proxy.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                videoView = new BrightcoveVideoView(proxy.getActivity().getApplicationContext());
                videoView.setEventEmitter(new EventEmitterImpl());
                setNativeView(videoView);
            }

        });
    }

    protected void setVideo(VideoProxy proxy) {
        videoView.clear();
        videoView.add(proxy.getVideo());
    }

    protected void setPlaylist(PlaylistProxy proxy) {
        videoView.clear();
        videoView.addAll(proxy.getPlaylist().getVideos());
    }

    protected void play() {
        videoView.start();
    }

    protected void pause() {
        videoView.pause();
    }

    protected void advanceToNext() {
        // TODO
    }
}
