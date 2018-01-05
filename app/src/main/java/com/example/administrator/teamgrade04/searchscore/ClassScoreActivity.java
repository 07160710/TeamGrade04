package com.example.administrator.teamgrade04.searchscore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.teamgrade04.R;

public class ClassScoreActivity extends AppCompatActivity {
    private Button mSearchBJ;
    private Button mSarchXH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_score);
        mSearchBJ = (Button) findViewById(R.id.search_bj);
        mSarchXH = (Button) findViewById(R.id.search_xh);
        mSearchBJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ClassScoreActivity.this,GradeScoreActivity.class);
                startActivity(intent);
            }
        });
        mSarchXH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassScoreActivity.this,XhScoreActivity.class);
                startActivity(intent);
            }
        });
    }
}
