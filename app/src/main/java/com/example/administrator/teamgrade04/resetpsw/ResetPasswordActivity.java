package com.example.administrator.teamgrade04.resetpsw;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.teamgrade04.LoginActivity;
import com.example.administrator.teamgrade04.R;
import com.example.administrator.teamgrade04.utils.NetworkUtils;

import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText mOldPass;
    private EditText mNewPass;
    private EditText mRePass;
    private String oldpass,newpass,repass;
    private Button mModifyBTN;
    private String xh = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mOldPass = (EditText) findViewById(R.id.oldpassword);
        mNewPass = (EditText) findViewById(R.id.newpassword);
        mRePass = (EditText) findViewById(R.id.psw_again);
        mModifyBTN = (Button) findViewById(R.id.xiugai_btn);

        mModifyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    xh = NetworkUtils.user;
                    oldpass = mOldPass.getText().toString();
                    newpass = mNewPass.getText().toString();
                    repass = mRePass.getText().toString();
                    final boolean flag = NetworkUtils.checkpwd(oldpass,newpass,repass,ResetPasswordActivity.this);
                    if(flag == true){
                       Xiugai xiugai = new Xiugai(xh,oldpass,newpass,repass);
                       xiugai.start();
                    }
            }
        });
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Tologin();
                    break;
            }
        }
    };
    private void Tologin() {
        Toast.makeText(ResetPasswordActivity.this,"修改密码成功！",Toast.LENGTH_LONG).show();
        NetworkUtils.reset(this);
    }


    private class Xiugai extends Thread{
        private String xh = "";
        private String oldpass = "";
        private String newpass = "";
        private String repass = "";
        public Xiugai(String xh,String oldpass,String newpass,String repass){
            this.xh = xh;
            this.oldpass = oldpass;
            this.newpass = newpass;
            this.repass = repass;
        }
        @Override
        public void run() {
            String url = NetworkUtils.apiUrl+"modifypassword.php?xh="+xh+"&oldpassword="+oldpass+"&newpassword="+newpass+"&repassword="+repass;
            String rs = NetworkUtils.netResponse(url);
            try{
                JSONObject jsonObject = new JSONObject(rs);
                String lg = jsonObject.getString("upda");
                if(lg.equalsIgnoreCase("false")){
                    Looper.prepare();
                    Toast.makeText(ResetPasswordActivity.this,"修改密码失败，服务器错误",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }else{
                    Message msg = new Message();
                    msg.what =1;
                    handler.sendMessage(msg);
                }
            }catch (Exception ee){
                ee.printStackTrace();
                Looper.prepare();
                Toast.makeText(ResetPasswordActivity.this,rs,Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
    }
}


