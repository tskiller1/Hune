package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hunegroup.hune.R;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;

/**
 * Created by apple on 1/16/18.
 */

public class NapCashServerActivity extends AppCompatActivity {
    private WebView wvServer;

    Dialog dialogProgress;
    //TODO: Declaring
    private JSONParser jsonParser = new JSONParser();
    String url;
    int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_napcashserver);
        findViews();
        initData();
    }

    private void findViews() {
        dialogProgress = DialogUtils.dialogProgress(this);
        wvServer = (WebView) findViewById(R.id.wv_server);
    }

    private void initData() {
        wvServer.getSettings().setJavaScriptEnabled(true);
        if (getIntent().getExtras() != null) {
            url = getIntent().getStringExtra("url");
            type = getIntent().getIntExtra("type", 0);
            if (!TextUtils.isEmpty(url)) {
                wvServer.setWebViewClient(new WebViewController());
                wvServer.loadUrl(url);

            }
        }
    }

    @Override
    public void onBackPressed() {
        if (type == 1) {
            Intent intent = new Intent(NapCashServerActivity.this, HunePayTuyenDungActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(NapCashServerActivity.this, HunePayTimViecActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


    }
}
