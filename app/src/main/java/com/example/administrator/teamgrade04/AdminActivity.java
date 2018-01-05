package com.example.administrator.teamgrade04;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.teamgrade04.resetpsw.ModifyAdminPsdActivity;
import com.example.administrator.teamgrade04.searchscore.ClassScoreActivity;
import com.example.administrator.teamgrade04.utils.NetworkUtils;

public class AdminActivity extends AppCompatActivity {
    private TextView mShowAdTV;
    private Button mModifyAdBtn;
    private Button mSearchScoreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mShowAdTV = (TextView) findViewById(R.id.adtv_name);
        mShowAdTV.setText("欢迎你："+NetworkUtils.user);
        mSearchScoreBtn = (Button) findViewById(R.id.searchAllGrade);
        mSearchScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ClassScoreActivity.class);
                startActivity(intent);
            }
        });
        mModifyAdBtn = (Button) findViewById(R.id.modifyAdminPsd);
        mModifyAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this,ModifyAdminPsdActivity.class);
                startActivity(intent);
            }
        });
        setTitle("管理员用户");
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
