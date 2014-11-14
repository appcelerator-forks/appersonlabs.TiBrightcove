package com.appersonlabs.brightcove;

import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.AsyncResult;
import org.appcelerator.kroll.common.TiMessenger;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

@Kroll.proxy(creatableInModule = BrightcoveModule.class)
public class PlayerViewProxy extends TiViewProxy implements Handler.Callback {

    private static final String LCAT                = "PlayerViewProxy";
    private static final int    MSG__FIRST_ID       = TiViewProxy.MSG_LAST_ID + 1;
    private static final int    MSG_ADVANCE_TO_NEXT = MSG__FIRST_ID + 1201;
    private static final int    MSG_PAUSE           = MSG__FIRST_ID + 1202;
    private static final int    MSG_PLAY            = MSG__FIRST_ID + 1203;
    private static final int    MSG_SET_PLAYLIST    = MSG__FIRST_ID + 1204;
    private static final int    MSG_SET_VIDEO       = MSG__FIRST_ID + 1205;

    @Kroll.method
    public void advanceToNext() {
        if (TiApplication.isUIThread()) {
            handleAdvanceToNext();
        }
        else {
            TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_ADVANCE_TO_NEXT));
        }
    }

    @Override
    public TiUIView createView(Activity activity) {
        PlayerView view = new PlayerView(this);
        // TODO set any default params
        return view;
    }

    private void handleAdvanceToNext() {
        getPlayerView().advanceToNext();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
        case MSG_SET_VIDEO: {
            AsyncResult result = (AsyncResult) msg.obj;
            handleSetVideo((VideoProxy) result.getArg());
            result.setResult(null);
            return true;
        }
        case MSG_SET_PLAYLIST: {
            AsyncResult result = (AsyncResult) msg.obj;
            handleSetPlaylist((PlaylistProxy) result.getArg());
            result.setResult(null);
            return true;
        }
        case MSG_PLAY: {
            handlePlay();
            return true;
        }
        case MSG_PAUSE: {
            handlePause();
            return true;
        }
        case MSG_ADVANCE_TO_NEXT: {
            handleAdvanceToNext();
            return true;
        }
        }

        return false;
    }

    private void handlePause() {
        getPlayerView().pause();
    }

    private void handlePlay() {
        getPlayerView().play();
    }

    private void handleSetPlaylist(PlaylistProxy proxy) {
        getPlayerView().setPlaylist(proxy);
    }

    private PlayerView getPlayerView() {
        return (PlayerView) getOrCreateView();
    }
    
    private void handleSetVideo(VideoProxy proxy) {
        getPlayerView().setVideo(proxy);
    }

    @Kroll.method
    public void pause() {
        if (TiApplication.isUIThread()) {
            handlePause();
        }
        else {
            TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_PAUSE));
        }
    }

    @Kroll.method
    public void play() {
        if (TiApplication.isUIThread()) {
            handlePlay();
        }
        else {
            TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_PLAY));
        }
    }

    @Kroll.setProperty(retain = false)
    public void setPlaylist(final PlaylistProxy proxy) {
        if (TiApplication.isUIThread()) {
            handleSetPlaylist(proxy);
        }
        else {
            TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_PLAYLIST), proxy);
        }
    }

    @Kroll.setProperty(retain = false)
    public void setVideo(final VideoProxy proxy) {
        if (TiApplication.isUIThread()) {
            handleSetVideo(proxy);
        }
        else {
            TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_VIDEO, proxy), proxy);
        }
    }
}
