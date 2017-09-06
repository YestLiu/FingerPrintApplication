package com.yestliu.fingerprint;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CancellationSignal.OnCancelListener {
    CancellationSignal cancellationSignal;
    FingerprintManagerCompat fingerprintManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fingerprintManagerCompat = FingerprintManagerCompat.from(this);
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(this);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!fingerprintManagerCompat.isHardwareDetected()) {
                    Toast.makeText(v.getContext(), "设备不支持指纹识别", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {//判断设备是否已经注册过指纹


                    Toast.makeText(v.getContext(), "设备尚未注册指纹", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                    return;

                }

                fingerprintManagerCompat.authenticate(null, 0, cancellationSignal, callback, handler);
            }
        });


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private FingerprintManagerCompat.AuthenticationCallback callback =
            new FingerprintManagerCompat.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                    super.onAuthenticationError(errMsgId, errString);

                    Toast.makeText(MainActivity.this, errString.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                    super.onAuthenticationHelp(helpMsgId, helpString);
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(MainActivity.this, "指纹识别成功", Toast.LENGTH_SHORT).show();
                    //成功后 释放识别事件
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(MainActivity.this, "指纹识别失败", Toast.LENGTH_SHORT).show();
                    //成功后 可继续验证指纹
                }
            };

    @Override
    public void onCancel() {
        Toast.makeText(MainActivity.this, "取消指纹识别", Toast.LENGTH_SHORT).show();
    }
}
