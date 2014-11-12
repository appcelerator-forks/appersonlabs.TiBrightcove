package com.appersonlabs.brightcove;

import java.util.HashMap;
import java.util.Map;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollObject;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;

import android.util.Log;

import com.brightcove.player.media.Catalog;
import com.brightcove.player.media.PlaylistListener;
import com.brightcove.player.media.VideoListener;
import com.brightcove.player.model.Playlist;
import com.brightcove.player.model.Video;

@Kroll.proxy(parentModule = BrightcoveModule.class, creatableInModule = BrightcoveModule.class)
public class CatalogProxy extends KrollProxy {

    private static final String LCAT = "CatalogProxy";

    private Catalog             catalog;

    private Map<String, String> convertToOptions(KrollDict dict) {
        if (dict == null) {
            return null;
        }
        Map<String, String> result = new HashMap<String, String>();
        for (String key : dict.keySet()) {
            dict.put(key, dict.getString(key));
        }
        return result;
    }

    private PlaylistListener createPlaylistListener(final KrollObject krollObject, final KrollFunction cb) {
        return new PlaylistListener() {

            @Override
            public void onError(String error) {
                KrollDict cbparams = new KrollDict();
                cbparams.put("type", "error");
                cbparams.put("code", 0);
                cbparams.put("error", error);
                cb.call(krollObject, new Object[] { cbparams });
            }

            @Override
            public void onPlaylist(Playlist playlist) {
                KrollDict cbparams = new KrollDict();
                cbparams.put("type", "success");
                cbparams.put("result", new PlaylistProxy(playlist));
                cb.call(krollObject, new Object[] { cbparams });
            }
        };
    }

    private VideoListener createVideoListener(final KrollObject krollObject, final KrollFunction cb) {
        return new VideoListener() {

            @Override
            public void onError(String error) {
                KrollDict cbparams = new KrollDict();
                cbparams.put("type", "error");
                cbparams.put("code", 0);
                cbparams.put("error", error);
                cb.call(krollObject, new Object[] { cbparams });
            }

            @Override
            public void onVideo(Video video) {
                KrollDict cbparams = new KrollDict();
                cbparams.put("type", "success");
                cbparams.put("result", new VideoProxy(video));
                cb.call(krollObject, new Object[] { cbparams });
            }

        };
    }

    @Kroll.method
    public void findPlaylistWithPlaylistID(String playlistID, final KrollFunction cb, @Kroll.argument(optional = true) KrollDict params) {
        catalog.findPlaylistByID(playlistID, convertToOptions(params), createPlaylistListener(getKrollObject(), cb));
    }

    @Kroll.method
    public void findPlaylistWithReferenceID(String referenceID, final KrollFunction cb, @Kroll.argument(optional = true) KrollDict params) {
        catalog.findPlaylistByReferenceID(referenceID, convertToOptions(params), createPlaylistListener(getKrollObject(), cb));
    }

    @Kroll.method
    public void findVideoWithReferenceID(String referenceID, final KrollFunction cb, @Kroll.argument(optional = true) KrollDict params) {
        catalog.findVideoByReferenceID(referenceID, convertToOptions(params), createVideoListener(getKrollObject(), cb));
    }

    @Kroll.method
    public void findVideoWithVideoID(String videoID, final KrollFunction cb, @Kroll.argument(optional = true) KrollDict params) {
        catalog.findVideoByID(videoID, convertToOptions(params), createVideoListener(getKrollObject(), cb));

    }

    @Override
    public String getApiName() {
        return BrightcoveModule.MODULE_ID + ".Catalog";
    }
    
    

    @Override
    public void handleCreationDict(KrollDict dict) {
        super.handleCreationDict(dict);

        String token = dict.getString("token");
        if (token != null) {
            catalog = new Catalog(token);
        } else {
            Log.e(LCAT, "BrightCove catalog missing required parameter 'token'");
        }
    }

}
