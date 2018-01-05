package com.example.administrator.teamgrade04.searchscore;


public class TermList {
    public String id;
    public String xh;
    public String xm;
    public String sex;
    public String classname;
    public String term;
    public String toString(){
        return term;//为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()
    }
}
