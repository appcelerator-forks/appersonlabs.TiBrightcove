package com.appersonlabs.brightcove;

import java.util.List;

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
import com.brightcove.player.model.Video;
import com.brightcove.player.view.BrightcoveVideoView;

public class PlayerView extends TiUIView implements Handler.Callback {

    private static final String LCAT            = "PlayerView";

    private static final int    MSG_ADVANCE_TO_NEXT = 50001;

    private static final int    MSG_PAUSE           = 50002;

    private static final int    MSG_PLAY            = 50003;

    private static final int    MSG_SET_PLAYLIST    = 50004;

    private static final int    MSG_SET_VIDEO       = 50005;

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
            Message message = mainHandler.obtainMessage(MSG_ADVANCE_TO_NEXT);
            message.sendToTarget();
        }
        else {
            handleAdvanceToNext();
        }
    }

    private void clearVideosAfter(int index) {
        List<Video> list = videoView.getList();
        int size = list != null ? list.size() : 0;
        for (int i = size - 1; i > index; i--) {
            videoView.remove(i);
        }
    }

    private EventEmitter createEventEmitter() {
        EventEmitter emitter = new EventEmitterImpl();
        emitter.on(EventType.DID_PAUSE, new EventListener() {
            @Override
            public void processEvent(Event e) {
                fireEvent("pause", null);
            }
        });
        emitter.on(EventType.DID_PLAY, new EventListener() {
            @Override
            public void processEvent(Event e) {
                fireEvent("play", null);
            }
        });
        emitter.on(EventType.READY_TO_PLAY, new EventListener() {
            @Override
            public void processEvent(Event e) {
                fireEvent("ready", null);
            }
        });
        emitter.on(EventType.COMPLETED, new EventListener() {
            @Override
            public void processEvent(Event e) {
                fireEvent("end", null);
            }
        });
        // TODO terminate and fail events?

        return emitter;
    }

    private void handleAdvanceToNext() {
        boolean playing = videoView.isPlaying();
        List<Video> list = videoView.getList();
        int size = list != null ? list.size() : 0;
        if (size > 0) {
            int current = videoView.getCurrentIndex();
            if (current < size - 1) {
                videoView.setCurrentIndex(current + 1);
                if (playing) {
                    videoView.start();
                }
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        boolean handled = false;
        switch (msg.what) {
        case MSG_PLAY:
            handlePlay();
            handled = true;
            break;
        case MSG_PAUSE:
            handlePause();
            handled = true;
            break;
        case MSG_ADVANCE_TO_NEXT:
            handleAdvanceToNext();
            handled = true;
            break;
        case MSG_SET_VIDEO:
            handleSetVideo((VideoProxy) msg.obj);
            handled = true;
            break;
        case MSG_SET_PLAYLIST:
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
        List<Video> videos = proxy.getPlaylist().getVideos();

        videoView.stopPlayback();
        videoView.addAll(0, videos);
        clearVideosAfter(videos.size());
    }

    private void handleSetVideo(VideoProxy proxy) {
        videoView.stopPlayback();
        videoView.add(0, proxy.getVideo());
        clearVideosAfter(0);
    }

    protected void pause() {
        if (!TiApplication.isUIThread()) {
            Message message = mainHandler.obtainMessage(MSG_PAUSE);
            message.sendToTarget();
        }
        else {
            handlePause();
        }
    }

    protected void play() {
        if (!TiApplication.isUIThread()) {
            Message message = mainHandler.obtainMessage(MSG_PLAY);
            message.sendToTarget();
        }
        else {
            handlePlay();
        }
    }

    protected void setPlaylist(PlaylistProxy proxy) {
        if (!TiApplication.isUIThread()) {
            Message message = mainHandler.obtainMessage(MSG_SET_PLAYLIST, proxy);
            message.sendToTarget();
        }
        else {
            handleSetPlaylist(proxy);
        }
    }

    protected void setVideo(VideoProxy proxy) {
        if (!TiApplication.isUIThread()) {
            Message message = mainHandler.obtainMessage(MSG_SET_VIDEO, proxy);
            message.sendToTarget();
        }
        else {
            handleSetVideo(proxy);
        }
    }
}
