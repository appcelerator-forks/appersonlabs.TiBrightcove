package com.appersonlabs.brightcove;

import java.util.Map;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

@Kroll.module(name = "Brightcove", id = "com.appersonlabs.brightcove")
public class BrightcoveModule extends KrollModule {

    // Standard Debugging variables
    private static final String   LCAT      = "BrightcoveModule";

    protected static final String MODULE_ID = "com.appersonlabs.brightcove";

    @Kroll.onAppCreate
    public static void onAppCreate(TiApplication app) {
        Log.d(LCAT, "inside onAppCreate");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static KrollDict toKrollDict(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        KrollDict result = new KrollDict();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value instanceof Boolean || value instanceof Double || value instanceof Integer || value instanceof String || value instanceof String[]) {
                result.put(key, value);
            }
            else if (value instanceof Map) {
                result.put(key, toKrollDict((Map) value));
            }
            else {
                result.put(key, value.toString());
            }
        }
        return result;
    }

    public BrightcoveModule() {
        super();
    }

    @Override
    public String getApiName() {
        return BrightcoveModule.MODULE_ID + ".Catalog";
    }
}
