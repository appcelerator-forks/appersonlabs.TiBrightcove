package com.appersonlabs.brightcove;

import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

import android.app.Activity;

@Kroll.proxy(creatableInModule = BrightcoveModule.class)
public class PlayerViewProxy extends TiViewProxy {

    private static final String LCAT = "PlayerViewProxy";

    @Kroll.method
    public void advanceToNext() {
        getPlayerView().advanceToNext();
    }

    @Override
    public TiUIView createView(Activity activity) {
        PlayerView view = new PlayerView(this);
        // TODO set any default params
        return view;
    }

    private PlayerView getPlayerView() {
        return (PlayerView) getOrCreateView();
    }

    @Kroll.method
    public void pause() {
        getPlayerView().pause();
    }

    @Kroll.method
    public void play() {
        getPlayerView().play();
    }

    @Kroll.setProperty(retain = false)
    public void setPlaylist(final PlaylistProxy proxy) {
        getPlayerView().setPlaylist(proxy);
    }

    @Kroll.setProperty(retain = false)
    public void setVideo(final VideoProxy proxy) {
        getPlayerView().setVideo(proxy);
    }
}
