package com.example.switch2launcher;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/index.html");

        webView.postDelayed(() -> {
            try {
                JSONArray appsArray = getInstalledApps();
                webView.evaluateJavascript("displayApps(" + appsArray.toString() + ")", null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 2000);
    }

    private JSONArray getInstalledApps() {
        JSONArray apps = new JSONArray();
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo appInfo : packages) {
            try {
                JSONObject app = new JSONObject();
                app.put("name", appInfo.loadLabel(pm).toString());
                Drawable icon = appInfo.loadIcon(pm);
                String iconBase64 = drawableToBase64(icon);
                app.put("icon", iconBase64);
                apps.put(app);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return apps;
    }

    private String drawableToBase64(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return bitmapToBase64(((BitmapDrawable) drawable).getBitmap());
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmapToBase64(bitmap);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return "data:image/png;base64," + Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
    }
}
