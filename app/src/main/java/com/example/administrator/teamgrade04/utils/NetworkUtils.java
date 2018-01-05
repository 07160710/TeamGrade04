package com.example.administrator.teamgrade04.utils;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.administrator.teamgrade04.LoginActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2018/1/2.
 */

public class NetworkUtils {
    public static final String apiUrl="http://10.0.2.2/phptest/AudroidStuGrade/";//接口网址
    public static String user="";
    public static String pwd="";
    public static String netResponse(String url){  //输入网址，返回json字符串
        String rs="获取数据失败";
        try {
            URL apiurl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)apiurl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            int code = httpURLConnection.getResponseCode();
            if(code==200){
                InputStream is = httpURLConnection.getInputStream();
                InputStreamReader ris = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(ris);
                StringBuffer sbf = new StringBuffer();
                String line=null;
                while ((line=br.readLine())!=null){
                    sbf.append(line);
                }
                rs = new String(sbf.toString().getBytes(),"UTF-8");

            }else{
                rs="网址无法访问";
            }
        }catch (Exception ee){
            ee.printStackTrace();
        }

        return rs;
    };
    public static void reset(Activity ac){
        Intent intent = new Intent(ac,LoginActivity.class);
        NetworkUtils.user="";
        NetworkUtils.pwd="";
        ac.startActivity(intent);
        ac.finish();
    }

    public static boolean checkpwd(String oldpwd, String pwd, String pwdcfg, Activity ac){
        boolean check=false;

        if(!oldpwd.equals(NetworkUtils.pwd)){
            check = false;
            Toast.makeText(ac,"旧密码不正确",Toast.LENGTH_SHORT).show();
            return check;
        }else if(!pwd.equals(pwdcfg)){
            check = false;
            Toast.makeText(ac,"两次密码不一致",Toast.LENGTH_SHORT).show();
            return check;

        }else if(pwd.length()<6){
            check = false;
            Toast.makeText(ac,"密码要求不少于6位",Toast.LENGTH_SHORT).show();
            return check;
        }else{
            check=true;
            return check;
        }
    }

}
