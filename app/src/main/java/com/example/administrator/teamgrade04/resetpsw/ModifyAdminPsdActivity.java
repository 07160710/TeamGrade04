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

import com.example.administrator.teamgrade04.R;
import com.example.administrator.teamgrade04.utils.NetworkUtils;

import org.json.JSONObject;

public class ModifyAdminPsdActivity extends AppCompatActivity {
    private EditText mOldPass;
    private EditText mNewPass;
    private EditText mRePass;
    private Button mXiugaiBtn;
    String username,oldpass,newpass,repass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_admin_psd);
        mOldPass = (EditText) findViewById(R.id.oldadpassword);
        mNewPass = (EditText) findViewById(R.id.newadpassword);
        mRePass = (EditText) findViewById(R.id.pswad_again);
        mXiugaiBtn = (Button) findViewById(R.id.xiugaiad_btn);
        mXiugaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = NetworkUtils.user;
                oldpass = mOldPass.getText().toString();
                newpass = mNewPass.getText().toString();
                repass = mRePass.getText().toString();
                final boolean flag = NetworkUtils.checkpwd(oldpass,newpass,repass,ModifyAdminPsdActivity.this);
                if(flag == true){
                    Adpsd adpsd = new Adpsd(username,oldpass,newpass,repass);
                    adpsd.start();
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
        Toast.makeText(ModifyAdminPsdActivity.this,"修改密码成功！",Toast.LENGTH_LONG).show();
        NetworkUtils.reset(this);
    }


    private class Adpsd extends Thread{
        private String username = "";
        private String oldpass = "";
        private String newpass = "";
        private String repass = "";
        public Adpsd(String username,String oldpass,String newpass,String repass){
            this.username = username;
            this.oldpass = oldpass;
            this.newpass = newpass;
            this.repass = repass;
        }
        @Override
        public void run() {
            String url = NetworkUtils.apiUrl+"modifyadminpsd.php?user="+username+"&oldpassword="+oldpass+"&newpassword="+newpass+"&repassword="+repass;
            String rs = NetworkUtils.netResponse(url);
            try{
                JSONObject jsonObject = new JSONObject(rs);
                String lg = jsonObject.getString("modifypsd");
                if(lg.equalsIgnoreCase("false")){
                    Looper.prepare();
                    Toast.makeText(ModifyAdminPsdActivity.this,"修改密码失败，服务器错误",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }else{
                    Message msg = new Message();
                    msg.what =1;
                    handler.sendMessage(msg);
                }
            }catch (Exception ee){
                ee.printStackTrace();
                Looper.prepare();
                Toast.makeText(ModifyAdminPsdActivity.this,rs,Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
    }
    
}
