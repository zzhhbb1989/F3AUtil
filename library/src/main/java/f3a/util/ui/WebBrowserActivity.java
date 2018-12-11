package f3a.util.ui;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.f3a.f3autil.R;

/**
 * 网页浏览器
 *
 * @author Bob Ackles
 */
@SuppressLint("SetJavaScriptEnabled")
public class WebBrowserActivity extends BaseActivity {
	
	public static final String TITLE = "title";
	public static final String URL = "url";
	
	WebView webView;
	
	ProgressBar loading;

	@Override
	protected int getContentViewId() {
		return R.layout.activity_web_browser;
	}
    
    @Override
	protected void initData() {
	    webView = findViewById(R.id.webview);
	    loading = findViewById(R.id.loading);
		setTitle(getIntent().getStringExtra(TITLE));
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						try {
							webView.getSettings().setJavaScriptEnabled(true);
							webView.getSettings().setAllowFileAccess(true);
							webView.getSettings().setPluginState(PluginState.ON);
							webView.getSettings().setSupportZoom(true);
							webView.getSettings().setAppCacheEnabled(true);
							webView.setWebViewClient(new WebViewClient() {
								
								@Override
								public boolean shouldOverrideUrlLoading(WebView view, String url) {
									try {
										view.loadUrl(url);
									} catch (Exception e) {
										e.printStackTrace();
									}
									return true;
								}
							});
							webView.setWebChromeClient(new WebChromeClient() {
								
								@Override
								public void onProgressChanged(WebView view, int newProgress) {
									try {
										super.onProgressChanged(view, newProgress);
										if (newProgress == 100) {
											loading.setVisibility(View.GONE);
										} else {
											loading.setVisibility(View.VISIBLE);
											loading.setProgress(newProgress);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							webView.loadUrl(getIntent().getStringExtra(URL));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_1, menu);
        // 刷新
        MenuItem item = menu.findItem(R.id.item_1);
        item.setTitle(R.string.btn_refresh);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == R.id.item_1) {// 刷新
            reloadUrl();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	private void reloadUrl() {
		loading.setProgress(0);
		webView.reload();
	}
	
	@Override
	public void onBackPressedSupport() {
		if (webView.canGoBack()) {
			webView.goBack();
			return;
		}
		super.onBackPressedSupport();
	}
}
