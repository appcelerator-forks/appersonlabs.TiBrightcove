package com.appersonlabs.brightcove;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;

import com.brightcove.player.media.DeliveryType;
import com.brightcove.player.model.Source;

@Kroll.proxy(parentModule = BrightcoveModule.class)
public class VideoSourceProxy extends KrollProxy {

    private DeliveryType deliveryType;
    
    private Source       source;

    public VideoSourceProxy(Source source, DeliveryType deliveryType) {
        this.source = source;
        this.deliveryType = deliveryType;
    }

    @Override
    public String getApiName() {
        return BrightcoveModule.MODULE_ID + ".VideoSource";
    }

    @Kroll.getProperty(name = "deliveryMethod")
    public String getDeliveryMethod() {
        return deliveryType.toString();
    }

    @Kroll.getProperty(name = "properties")
    public KrollDict getSourceProperties() {
        return BrightcoveModule.toKrollDict(source.getProperties());
    }

    @Kroll.getProperty(name = "url")
    public String getURL() {
        return source.getUrl();
    }

}
