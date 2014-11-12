package com.appersonlabs.brightcove;

import java.util.ArrayList;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.annotations.Kroll.getProperty;

import com.brightcove.player.model.Playlist;
import com.brightcove.player.model.Video;

@Kroll.proxy(parentModule = BrightcoveModule.class)
public class PlaylistProxy extends KrollProxy {

    private Playlist playlist;

    public PlaylistProxy(Playlist playlist) {
        this.playlist = playlist;
    }
    
    @Override
    public String getApiName() {
        return BrightcoveModule.MODULE_ID + ".Playlist";
    }

    @getProperty(name="properties")
    public KrollDict getVideoProperties() {
        return BrightcoveModule.toKrollDict(playlist.getProperties());
    }
    
    @getProperty(name="videos")
    public VideoProxy[] getVideos() {
        List<VideoProxy> proxies = new ArrayList<VideoProxy>();
        for (Video video : playlist.getVideos()) {
            proxies.add(new VideoProxy(video));
        }
        return proxies.toArray(new VideoProxy[0]);
    }
}
