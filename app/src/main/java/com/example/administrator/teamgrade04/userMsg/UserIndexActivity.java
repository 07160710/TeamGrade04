package com.example.administrator.teamgrade04.userMsg;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.teamgrade04.R;
import com.example.administrator.teamgrade04.searchscore.TermList;
import com.example.administrator.teamgrade04.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserIndexActivity extends AppCompatActivity {
    private TextView mShowPeople;
    private String xh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_index);
        setTitle("学生信息");
        mShowPeople = (TextView) findViewById(R.id.list);
        xh = NetworkUtils.user;
        GetPeople getPeople = new GetPeople(xh);
        getPeople.start();

    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    showPeple((String) msg.obj);
            }
        }
    };
    private void showPeple(String rs){
        try {
            JSONArray jsonArray = new JSONArray(rs);
            StringBuffer wbf = new StringBuffer();
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject job = jsonArray.getJSONObject(i);
                wbf.append("学号："+job.getString("xh")+"\n");
                wbf.append("姓名："+job.getString("xm")+"\n");
                wbf.append("性别："+job.getString("sex")+"\n");
                wbf.append("班级："+job.getString("class"));
            }
            mShowPeople.setText(wbf.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    class GetPeople extends Thread{
        private String xh ="";
        public GetPeople(String xh){
            this.xh = xh;
        }
        @Override
        public void run() {
            String url = NetworkUtils.apiUrl+"userstuinf.php?xh="+xh;
            String rs = NetworkUtils.netResponse(url);
            Message message = new Message();
            message.what = 1;
            message.obj = rs;
            handler.sendMessage(message);
        }
    }
}
