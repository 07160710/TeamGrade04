package com.example.administrator.teamgrade04.searchscore;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.administrator.teamgrade04.R;
import com.example.administrator.teamgrade04.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XhScoreActivity extends AppCompatActivity {
    private Spinner mTermSP;
    private EditText mXhET;
    private Button mSearchBtn;
    private ListView mListScore;
    String termid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xh_score);
        mTermSP = (Spinner) findViewById(R.id.term_sp);
        mXhET = (EditText) findViewById(R.id.xhnumber);
        mSearchBtn = (Button) findViewById(R.id.btsearch);
        mListScore = (ListView) findViewById(R.id.lvscore);
        Getterms getTerm =new Getterms();
        getTerm.start();

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xh = mXhET.getText().toString();
                GetAdGrades getAdGrades = new GetAdGrades(xh);
                getAdGrades.start();
            }
        });
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    allTerm(msg.obj);
                    break;
                case 2:
                    searchAdGrade(msg.obj);
                    break;


            }
            super.handleMessage(msg);
        }
    };


    private void allTerm(Object obj){
        List<TermList> list = (List<TermList>)obj;
        ArrayAdapter<TermList> arrayAdapter = new ArrayAdapter<TermList>(XhScoreActivity.this,
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
    private void searchAdGrade(Object obj){
        ArrayList<Map<String,Object>> inf = (ArrayList<Map<String,Object>>)obj;
        SimpleAdapter simpleAdapter = new SimpleAdapter(XhScoreActivity.this,inf,R.layout.item_admin_grade,
                new String[]{"xm","km","cj"},new int[]{R.id.tv_xh,R.id.tv_xm,R.id.tv_cj});
        mListScore.setAdapter(simpleAdapter);
    }



    class GetAdGrades extends Thread{
        String xh = "";
        public GetAdGrades(String xh){
            this.xh = xh;
        }
        @Override
        public void run() {
            String url = NetworkUtils.apiUrl+"adminscore_byxh.php?tid="+termid+"&xh="+xh;
            String result = NetworkUtils.netResponse(url);
            try{
                JSONArray jsonArray = new JSONArray(result);
                StringBuffer sbf = new StringBuffer();
                ArrayList<Map<String,String>> inf =new ArrayList<Map<String, String>>();
                String xm,km,cj;
                for(int i=0;i<jsonArray.length();i++){
                    Map<String,String>item = new HashMap<String, String>();
                    xm = ((JSONObject)jsonArray.opt(i)).getString("xm");
                    km= ((JSONObject)jsonArray.opt(i)).getString("km");
                    cj = ((JSONObject)jsonArray.opt(i)).getString("cj");

                    item.put("xm",xm);
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


    class Getterms extends Thread{
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

}
