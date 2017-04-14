package cn.cerc.summer.android.Activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.huagu.ehealth.R;

/**
 * Created by admin on 2017/4/14.
 */

public class CallphoneActivity extends AppCompatActivity {

    private EditText etPhone;
    private Button btnPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callphone);
        etPhone = (EditText) findViewById(R.id.et_phone_num);
        btnPhone = (Button) findViewById(R.id.btn_call_phone);

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhone.getText().toString().trim() == null || etPhone.getText().toString().trim().equals("")) {
                    Toast.makeText(CallphoneActivity.this, "对不起，电话不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etPhone.getText().toString().trim() != null && !(etPhone.getText().toString().trim().equals(""))) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                            + etPhone.getText().toString().trim()));
                    if (ActivityCompat.checkSelfPermission(CallphoneActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);

                }
            }
        });


    }

}
