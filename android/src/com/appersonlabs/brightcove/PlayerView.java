package com.appersonlabs.brightcove;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.brightcove.player.event.Event;
import com.brightcove.player.event.EventEmitter;
import com.brightcove.player.event.EventEmitterImpl;
import com.brightcove.player.event.EventListener;
import com.brightcove.player.event.EventType;
import com.brightcove.player.view.BrightcoveVideoView;

public class PlayerView extends TiUIView implements Handler.Callback {

    private static final int    ADVANCE_TO_NEXT = 50001;

    private static final String LCAT            = "PlayerView";

    private static final int    PAUSE           = 50002;

    private static final int    PLAY            = 50003;

    private static final int    SET_PLAYLIST    = 50004;

    private static final int    SET_VIDEO       = 50005;

    private Activity            activity;

    private Handler             mainHandler     = new Handler(Looper.getMainLooper(), this);

    private BrightcoveVideoView videoView;

    public PlayerView(final TiViewProxy proxy) {
        super(proxy);

        final EventEmitter emitter = createEventEmitter();
        this.activity = proxy.getActivity();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                videoView = new BrightcoveVideoView(proxy.getActivity().getApplicationContext());
                videoView.setEventEmitter(emitter);
                setNativeView(videoView);
            }

        });
    }

    protected void advanceToNext() {
        if (!TiApplication.isUIThread()) {
            Message message = mainHandler.obtainMessage(ADVANCE_TO_NEXT);
            message.sendToTarget();
        } else {
            handleAdvanceToNext();
        }
    }

    private EventEmitter createEventEmitter() {
        EventEmitter emitter = new EventEmitterImpl();
        emitter.on(EventType.DID_PAUSE, new EventListener() {
            @Override
            public void processEvent(Event e) {
                Log.i(LCAT, "did pause");
            }
        });
        emitter.on(EventType.DID_PLAY, new EventListener() {
            @Override
            public void processEvent(Event e) {
                Log.i(LCAT, "did play");
            }
        });
        emitter.on(EventType.READY_TO_PLAY, new EventListener() {
            @Override
            public void processEvent(Event e) {
                Log.i(LCAT, "ready to play");
            }
        });
        emitter.on(EventType.COMPLETED, new EventListener() {
            @Override
            public void processEvent(Event e) {
                Log.i(LCAT, "completed");
            }
        });
        // TODO terminate and fail?

        return emitter;
    }

    private void handleAdvanceToNext() {
        // TODO
    }

    @Override
    public boolean handleMessage(Message msg) {
        boolean handled = false;
        switch (msg.what) {
        case PLAY:
            handlePlay();
            handled = true;
            break;
        case PAUSE:
            handlePause();
            handled = true;
            break;
        case ADVANCE_TO_NEXT:
            handleAdvanceToNext();
            handled = true;
            break;
        case SET_VIDEO:
            handleSetVideo((VideoProxy) msg.obj);
            handled = true;
            break;
        case SET_PLAYLIST:
            handleSetPlaylist((PlaylistProxy) msg.obj);
            handled = true;
            break;
        }
        return handled;
    }

    private void handlePause() {
        videoView.pause();
    }

    private void handlePlay() {
        videoView.start();
    }

    private void handleSetPlaylist(PlaylistProxy proxy) {
        videoView.stopPlayback();
        videoView.clear();
        videoView.addAll(proxy.getPlaylist().getVideos());
    }

    private void handleSetVideo(VideoProxy proxy) {
        videoView.stopPlayback();
        videoView.clear();
        videoView.add(proxy.getVideo());
    }

    protected void pause() {
        if (!TiApplication.isUIThread()) {
            Message message = mainHandler.obtainMessage(PAUSE);
            message.sendToTarget();
        } else {
            handlePause();
        }
    }

    protected void play() {
        if (!TiApplication.isUIThread()) {
            Message message = mainHandler.obtainMessage(PLAY);
            message.sendToTarget();
        } else {
            handlePlay();
        }
    }

    protected void setPlaylist(PlaylistProxy proxy) {
        if (!TiApplication.isUIThread()) {
            Message message = mainHandler.obtainMessage(SET_PLAYLIST, proxy);
            message.sendToTarget();
        } else {
            handleSetPlaylist(proxy);
        }
    }

    protected void setVideo(VideoProxy proxy) {
        if (!TiApplication.isUIThread()) {
            Message message = mainHandler.obtainMessage(SET_VIDEO, proxy);
            message.sendToTarget();
        } else {
            handleSetVideo(proxy);
        }
    }
}
