package com.example.administrator.teamgrade04.userMsg;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.administrator.teamgrade04.R;
import com.example.administrator.teamgrade04.searchscore.TermList;
import com.example.administrator.teamgrade04.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserScoreActivity extends AppCompatActivity {
    private Spinner mTermSP;
    private ListView mShowScore;
    String termid="";
    private Button mSearchSX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_score);
        mTermSP = (Spinner) findViewById(R.id.sp_terms);
        mShowScore = (ListView) findViewById(R.id.xs_grade);
        mSearchSX = (Button) findViewById(R.id.bt_searchsx);
        GetTerms getTerm =new GetTerms();
        getTerm.start();
        mSearchSX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetGrades getGrade = new GetGrades();
                getGrade.start();
            }
        });
    }
    class GetTerms extends Thread{
        @Override
        public void run() {
            String url = NetworkUtils.apiUrl+"getterm.php";
            String result = NetworkUtils.netResponse(url);
            try{
                JSONArray jsonArray = new JSONArray(result);
                List<TermList>list = new ArrayList<TermList>();
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject per = (JSONObject)jsonArray.opt(i);
                    TermList t = new TermList();
                    t.id = per.getString("id");
                    t.term = per.getString("term")+per.getString("termno");
                    list.add(t);
                }
                Message msg = new Message();
                msg.what =1;
                msg.obj = list;
                handler.sendMessage(msg);

            }catch (Exception ee){
                ee.printStackTrace();
            }

        }
    }
    private void allTerm(Object obj){
        List<TermList> list = (List<TermList>)obj;
        ArrayAdapter<TermList> arrayAdapter = new ArrayAdapter<TermList>(UserScoreActivity.this,
                android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTermSP.setAdapter(arrayAdapter);
        mTermSP.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                termid = ((TermList)parent.getSelectedItem()).id;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                termid="";
            }
        });
    }


    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    allTerm(msg.obj);
                    break;
                case 2:
                    searchGrade(msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void searchGrade(Object obj){
        ArrayList<Map<String,Object>> inf = (ArrayList<Map<String,Object>>)obj;
        SimpleAdapter simpleAdapter = new SimpleAdapter(UserScoreActivity.this,inf,R.layout.item_user_score,
                new String[]{"km","cj"},new int[]{R.id.xs_km,R.id.xs_cj});
        mShowScore.setAdapter(simpleAdapter);
    }



    class GetGrades extends Thread{
        @Override
        public void run() {
            String url = NetworkUtils.apiUrl+"userscores.php?tid="+termid+"&xh="+NetworkUtils.user;
            String result = NetworkUtils.netResponse(url);
            try{
                JSONArray jsonArray = new JSONArray(result);
                StringBuffer sbf = new StringBuffer();
                ArrayList<Map<String,String>> inf =new ArrayList<Map<String, String>>();
                String km,cj;
                for(int i=0;i<jsonArray.length();i++){
                    Map<String,String>item = new HashMap<String, String>();
                    km= ((JSONObject)jsonArray.opt(i)).getString("km");
                    cj = ((JSONObject)jsonArray.opt(i)).getString("cj");

                    item.put("km",km);
                    item.put("cj",cj);
                    inf.add(item);
                }
                Message msg = new Message();
                msg.what = 2;
                msg.obj = inf;
                handler.sendMessage(msg);

            }catch (Exception ee){
                ee.printStackTrace();
            }
        }
    }



}
