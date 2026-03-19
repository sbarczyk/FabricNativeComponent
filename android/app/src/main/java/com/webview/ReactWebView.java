package com.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.ViewGroup.LayoutParams;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcher;

public class ReactWebView extends WebView {
    public ReactWebView(Context context) {
        super(context);
        configureComponent();
    }

    public ReactWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        configureComponent();
    }

    public ReactWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configureComponent();
    }

    private void configureComponent() {
    this.setLayoutParams(new android.view.ViewGroup.LayoutParams(
        android.view.ViewGroup.LayoutParams.MATCH_PARENT, 
        android.view.ViewGroup.LayoutParams.MATCH_PARENT
    ));
    
    this.setWebViewClient(new WebViewClient() {
        @Override
        public void onPageFinished(android.webkit.WebView view, String url) {
            emitOnScriptLoaded(OnScriptLoadedEventResult.SUCCESS);
        }
    });
}

    public void emitOnScriptLoaded(OnScriptLoadedEventResult result) {
        ReactContext reactContext = (ReactContext) getContext();
        
        int surfaceId = UIManagerHelper.getSurfaceId(reactContext);
        EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, getId());
        
        WritableMap payload = Arguments.createMap();
        payload.putString("result", result.name().toLowerCase());

        if (eventDispatcher != null) {
            eventDispatcher.dispatchEvent(new OnScriptLoadedEvent(surfaceId, getId(), payload));
        }
    }

    public enum OnScriptLoadedEventResult {
        SUCCESS,
        ERROR,
    }

    private static class OnScriptLoadedEvent extends Event<OnScriptLoadedEvent> {
        private final WritableMap payload;

        OnScriptLoadedEvent(int surfaceId, int viewId, WritableMap payload) {
            super(surfaceId, viewId);
            this.payload = payload;
        }

        @Override
        public String getEventName() {
            return "onScriptLoaded";
        }

        @Override
        public WritableMap getEventData() {
            return payload;
        }
    }
}