package com.example.administrator.teamgrade04;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.teamgrade04.utils.NetworkUtils;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginBTN;
    private Button mReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mLoginBTN = (Button) findViewById(R.id.login_btn);
        mUsername.setText("07110801");
        //mPassword.setText("123456");
        mReset = (Button) findViewById(R.id.reset);
        mLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                yzLogin yz = new yzLogin(username,password);
                yz.start();

            }
        });
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername.setText("");
                mPassword.setText("");
            }
        });

    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    islogin();
                    break;
            }
        }
    };

    private void islogin(){
        String user = mUsername.getText().toString();
        if(user.equalsIgnoreCase("admin")){
            NetworkUtils.user=mUsername.getText().toString();
            NetworkUtils.pwd = mPassword.getText().toString();

            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,AdminActivity.class);
            startActivity(intent);

        }else{
            NetworkUtils.user=mUsername.getText().toString();
            NetworkUtils.pwd = mPassword.getText().toString();
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,UserActivity.class);
            startActivity(intent);

        }
        this.finish();
    }

    private class yzLogin extends Thread{
        private String user = "";
        private String pwd = "";
        public yzLogin(String user,String pwd){
            this.user = user;
            this.pwd = pwd;

        }
        @Override

        public void run() {
            String url = NetworkUtils.apiUrl+"login.php?user="+user+"&pwd="+pwd;
            String rs = NetworkUtils.netResponse(url);
            try{
                JSONObject jsonObject = new JSONObject(rs);
                String lg = jsonObject.getString("login");
                if(lg.equalsIgnoreCase("false")){
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this,"登录失败，用户名或密码错误",Toast.LENGTH_LONG).show();
                    Looper.loop();

                }else{
                    Message msg = new Message();
                    msg.what =1;
                    handler.sendMessage(msg);
                }
            }catch (Exception ee){
                ee.printStackTrace();
                Looper.prepare();
                Toast.makeText(LoginActivity.this,rs,Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
    }


}
