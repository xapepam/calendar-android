package ru.xapepam.calendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Тонкая WebView-обёртка вокруг веб-клиента календаря.
 * Всё приложение целиком живёт на сервере по адресу SERVER_URL.
 */
public class MainActivity extends AppCompatActivity {

    // Адрес сервера календаря в локальной сети.
    private static final String SERVER_URL = "http://192.168.10.204:8081/";

    private WebView webView;
    private ProgressBar progress;
    private LinearLayout errorPanel;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        progress = findViewById(R.id.progress);
        errorPanel = findViewById(R.id.errorPanel);
        Button retryButton = findViewById(R.id.retryButton);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);   // localStorage на всякий случай
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                // Показываем панель ошибки только если не загрузилась сама страница.
                if (request.isForMainFrame()) {
                    progress.setVisibility(View.GONE);
                    errorPanel.setVisibility(View.VISIBLE);
                }
            }
        });

        retryButton.setOnClickListener(v -> loadCalendar());

        loadCalendar();
    }

    private void loadCalendar() {
        errorPanel.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        webView.loadUrl(SERVER_URL);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // При возврате на экран перечитываем состояние: цвета могли поменять с другого устройства.
        webView.evaluateJavascript("typeof loadState === 'function' && loadState();", null);
    }
}
