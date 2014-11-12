package com.appersonlabs.brightcove;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;

import com.brightcove.player.media.DeliveryType;
import com.brightcove.player.model.CuePoint;
import com.brightcove.player.model.Source;
import com.brightcove.player.model.SourceCollection;
import com.brightcove.player.model.Video;

@Kroll.proxy(parentModule = BrightcoveModule.class)
public class VideoProxy extends KrollProxy {

    private Video video;

    public VideoProxy(Video video) {
        this.video = video;
    }

    @Override
    public String getApiName() {
        return BrightcoveModule.MODULE_ID + ".Video";
    }

    @Kroll.getProperty(name = "cuepoints")
    public CuePointProxy[] getCuePoints() {
        if (video.getCuePoints() == null) {
            return null;
        }

        List<CuePointProxy> result = new ArrayList<CuePointProxy>();
        for (CuePoint cuePoint : video.getCuePoints()) {
            result.add(new CuePointProxy(cuePoint));
        }
        return result.toArray(new CuePointProxy[0]);
    }

    @Kroll.getProperty(name = "sources")
    public VideoSourceProxy[] getSources() {
        // flatten the source object tree to match iOS
        List<VideoSourceProxy> result = new ArrayList<VideoSourceProxy>();
        Map<DeliveryType, SourceCollection> sources = video.getSourceCollections();
        for (DeliveryType deliveryType : sources.keySet()) {
            for (Source source : sources.get(deliveryType).getSources()) {
                result.add(new VideoSourceProxy(source, deliveryType));
            }
        }
        return result.toArray(new VideoSourceProxy[0]);
    }

    @Kroll.getProperty(name = "properties")
    public KrollDict getVideoProperties() {
        return BrightcoveModule.toKrollDict(video.getProperties());
    }
}
