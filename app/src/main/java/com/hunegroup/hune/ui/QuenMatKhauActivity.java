package com.hunegroup.hune.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hunegroup.hune.R;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 02/07/2017.
 */

public class QuenMatKhauActivity extends AppCompatActivity {
    TextView txtSlogan,txtCancel,txtTitle;
    EditText edtPhoneNumber;
    Button btnGetCode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        setContentView(R.layout.activity_quenmatkhau);
        init();
    }
    private void init(){
        txtSlogan = (TextView) findViewById(R.id.txtSlogan);
        txtCancel = (TextView) findViewById(R.id.txtCancel);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        btnGetCode = (Button) findViewById(R.id.btnGetCode);

        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuenMatKhauActivity.this,NhapMaXacNhanActivity.class);
                intent.putExtra("phone",edtPhoneNumber.getText().toString());
                startActivity(intent);
            }
        });
    }
}
