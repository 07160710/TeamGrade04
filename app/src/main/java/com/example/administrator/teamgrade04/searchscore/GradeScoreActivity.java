package com.example.administrator.teamgrade04.searchscore;

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

import com.example.administrator.teamgrade04.R;
import com.example.administrator.teamgrade04.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeScoreActivity extends AppCompatActivity {
    Spinner sp_course,sp_term,sp_class;
    String termid,classid,courseid="";
    Button bt_sh;
    ListView lv_grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_score);
        setTitle("按班级查询成绩");
        sp_term = (Spinner)findViewById(R.id.sp_term);
        sp_class = (Spinner)findViewById(R.id.sp_class);
        sp_course = (Spinner)findViewById(R.id.sp_course);
        bt_sh = (Button)findViewById(R.id.bt_sh);
        lv_grade = (ListView)findViewById(R.id.lv_grade);
        GetTerm getTerm =new GetTerm();
        getTerm.start();
        GetClass getClass = new GetClass();
        getClass.start();
        GetCourse getCourse = new GetCourse();
        getCourse.start();
        bt_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetGrade getGrade = new GetGrade();
                getGrade.start();
            }
        });
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    fillTerm(msg.obj);
                    break;
                case 2:
                    fillClass(msg.obj);
                    break;
                case 3:
                    fillCourse(msg.obj);
                    break;
                case 4:
                    fillGrade(msg.obj);
                    break;
            }
        }
    };

    private void fillTerm(Object obj){
        List<TermList> list = (List<TermList>)obj;
        ArrayAdapter<TermList> arrayAdapter = new ArrayAdapter<TermList>(GradeScoreActivity.this,
                android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_term.setAdapter(arrayAdapter);
        sp_term.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
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
    private void fillClass(Object obj){
        List<TermList>list = (List<TermList>)obj;
        ArrayAdapter<TermList>arrayAdapter = new ArrayAdapter<TermList>(GradeScoreActivity.this,
                android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_class.setAdapter(arrayAdapter);
        sp_class.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classid = ((TermList)parent.getSelectedItem()).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                classid ="";
            }
        });
    }
    private void fillCourse(Object obj){
        List<TermList>list = (List<TermList>)obj;
        ArrayAdapter<TermList>arrayAdapter = new ArrayAdapter<TermList>(GradeScoreActivity.this,
                android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_course.setAdapter(arrayAdapter);
        sp_course.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                courseid = ((TermList)parent.getSelectedItem()).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                courseid="";
            }
        });
    }
    private void fillGrade(Object obj){
        ArrayList<Map<String,Object>> inf = (ArrayList<Map<String,Object>>)obj;
        SimpleAdapter simpleAdapter = new SimpleAdapter(GradeScoreActivity.this,inf,R.layout.item_admin_grade,
                new String[]{"xh","xm","cj"},new int[]{R.id.tv_xh,R.id.tv_xm,R.id.tv_cj});
        lv_grade.setAdapter(simpleAdapter);
    }
    class GetTerm extends Thread{
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
    class GetClass extends Thread{
        @Override
        public void run() {
            String url = NetworkUtils.apiUrl+"getclass.php";
            String result = NetworkUtils.netResponse(url);
            try{
                JSONArray jsonArray = new JSONArray(result);
                List<TermList>list = new ArrayList<TermList>();
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject per = (JSONObject)jsonArray.opt(i);
                    TermList t = new TermList();
                    t.id = per.getString("id");
                    t.term = per.getString("class_name");
                    list.add(t);
                }
                Message msg = new Message();
                msg.what =2;
                msg.obj = list;
                handler.sendMessage(msg);

            }catch (Exception ee){
                ee.printStackTrace();
            }
        }
    }
    class GetCourse extends Thread{
        @Override
        public void run() {
            String url = NetworkUtils.apiUrl+"getcourse.php";
            String result = NetworkUtils.netResponse(url);
            try{
                JSONArray jsonArray = new JSONArray(result);
                List<TermList>list = new ArrayList<TermList>();
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject per = (JSONObject)jsonArray.opt(i);
                    TermList t = new TermList();
                    t.id = per.getString("id");
                    t.term = per.getString("km_name");
                    list.add(t);
                }
                Message msg = new Message();
                msg.what =3;
                msg.obj = list;
                handler.sendMessage(msg);

            }catch (Exception ee){
                ee.printStackTrace();
            }
        }
    }
    class GetGrade extends Thread{
        @Override
        public void run() {
            String url = NetworkUtils.apiUrl+"getgarade_byclass.php?tid="+termid+"&cid="+courseid+"&bid="+classid;
            String result = NetworkUtils.netResponse(url);
            try{
                JSONArray jsonArray = new JSONArray(result);
                StringBuffer sbf = new StringBuffer();
                ArrayList<Map<String,String>> inf =new ArrayList<Map<String, String>>();
                String xh,xm,cj;
                for(int i=0;i<jsonArray.length();i++){
                    Map<String,String>item = new HashMap<String, String>();
                    xh= ((JSONObject)jsonArray.opt(i)).getString("xh");
                    xm= ((JSONObject)jsonArray.opt(i)).getString("xm");

                    cj = ((JSONObject)jsonArray.opt(i)).getString("cj");

                    item.put("xh",xh);
                    item.put("xm",xm);
                    item.put("cj",cj);
                    inf.add(item);
                }
                Message msg = new Message();
                msg.what = 4;
                msg.obj = inf;
                handler.sendMessage(msg);

            }catch (Exception ee){
                ee.printStackTrace();
            }
        }
    }
}
