package com.appersonlabs.brightcove;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;

import com.brightcove.player.model.CuePoint;

@Kroll.proxy(parentModule = BrightcoveModule.class)
public class CuePointProxy extends KrollProxy {

    private CuePoint cuePoint;

    public CuePointProxy(CuePoint cuePoint) {
        this.cuePoint = cuePoint;
    }

    @Override
    public String getApiName() {
        return BrightcoveModule.MODULE_ID + ".CuePoint";
    }

    @Kroll.getProperty(name = "properties")
    public KrollDict getCuePointProperties() {
        return BrightcoveModule.toKrollDict(cuePoint.getProperties());
    }

    @Kroll.getProperty(name = "position")
    public double getPosition() {
        return cuePoint.getPosition();
    }

    @Kroll.getProperty(name = "type")
    public String getType() {
        return cuePoint.getType();
    }

}
