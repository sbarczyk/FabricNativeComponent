package com.webview;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.viewmanagers.CustomWebViewManagerInterface;
import com.facebook.react.viewmanagers.CustomWebViewManagerDelegate;

import java.util.HashMap;
import java.util.Map;

@ReactModule(name = ReactWebViewManager.REACT_CLASS)
public class ReactWebViewManager extends SimpleViewManager<ReactWebView> implements CustomWebViewManagerInterface<ReactWebView> {
    
    public static final String REACT_CLASS = "CustomWebView";

    private final ViewManagerDelegate<ReactWebView> delegate;

    public ReactWebViewManager(ReactApplicationContext context) {
        super();
        this.delegate = new CustomWebViewManagerDelegate<>(this);
    }

    @Override
    public ViewManagerDelegate<ReactWebView> getDelegate() {
        return delegate;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public ReactWebView createViewInstance(ThemedReactContext context) {
        return new ReactWebView(context);
    }

    @ReactProp(name = "sourceUrl")
    @Override
    public void setSourceURL(ReactWebView view, String sourceURL) {
        if (sourceURL == null) {
            view.emitOnScriptLoaded(ReactWebView.OnScriptLoadedEventResult.ERROR);
            return;
        }
        view.loadUrl(sourceURL);
    }

    @Override
    public Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> bubblingMap = new HashMap<>();
        bubblingMap.put("phasedRegistrationNames", new HashMap<String, String>() {{
            put("bubbled", "onScriptLoaded");
            put("captured", "onScriptLoadedCapture");
        }});
        map.put("onScriptLoaded", bubblingMap);
        return map;
    }
}