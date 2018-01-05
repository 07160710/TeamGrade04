package com.example.administrator.teamgrade04;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.teamgrade04.resetpsw.ResetPasswordActivity;
import com.example.administrator.teamgrade04.userMsg.UserIndexActivity;
import com.example.administrator.teamgrade04.userMsg.UserScoreActivity;
import com.example.administrator.teamgrade04.utils.NetworkUtils;

public class UserActivity extends AppCompatActivity {
    private TextView mShowNumber;
    private Button mModifyBtn;
    private Button mShowPeople;
    private Button mSearchScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle("普通用户");
        mModifyBtn = (Button) findViewById(R.id.modifyPsd);
        mShowPeople = (Button) findViewById(R.id.showPeople);
        mSearchScore = (Button) findViewById(R.id.searchGrade);
        mShowNumber = (TextView) findViewById(R.id.tv_name);
        mShowNumber.setText("欢迎你："+NetworkUtils.user);
        mSearchScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UserScoreActivity.class);
                startActivity(intent);
            }
        });
        mModifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        mShowPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this,UserIndexActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("注销");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getOrder()){
            case 0:
                NetworkUtils.reset(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
