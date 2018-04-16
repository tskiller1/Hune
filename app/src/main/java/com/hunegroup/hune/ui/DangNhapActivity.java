package com.hunegroup.hune.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.util.JSONParser;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

public class DangNhapActivity extends AppCompatActivity {

    /* todo : layout*/
    private TextView txtName;
    private TextView txtPhoneNumber;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView txtForgotPassword;
    private Button btnChangeNumber;
    private Button btnFacebook;

    JSONParser jsonParser = new JSONParser();
    private UserDTO userDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,false);
        setContentView(R.layout.activity_dang_nhap);

        findViews();
    }
    private void findViews() {
        txtName = (TextView)findViewById( R.id.txtName );
        txtPhoneNumber = (TextView)findViewById( R.id.txtPhoneNumber );
        edtPassword = (EditText)findViewById( R.id.edtPassword );
        btnLogin = (Button)findViewById( R.id.btnLogin );
        txtForgotPassword = (TextView)findViewById( R.id.txtForgotPassword );
        btnChangeNumber = (Button)findViewById( R.id.btnChangeNumber );
        btnFacebook = (Button)findViewById( R.id.btnFacebook );

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
