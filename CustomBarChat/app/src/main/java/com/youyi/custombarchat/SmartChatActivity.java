package com.youyi.custombarchat;

import android.os.Bundle;
import android.os.Handler;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;


import java.util.Locale;


/**
 * Created by Roy on 16/10/14.
 */

public class SmartChatActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = SmartChatActivity.class.getSimpleName();

    private LinearLayout llGroup;
    private RelativeLayout rlHeader;

    private TextView tvTitle;
    private View leftBack;
    private RadioGroup rgStatistic;
    private ScrollBarPic scrollBarPic;
    private RelativeLayout rlSmartChat;

    private TextView tvLearn;


    /**
     * 以下才是跟柱形图有关
     */

    private ScrollBarBean scrollBarBean;
    private ArrayList<ScrollPerBarBean> lists;


    private String type = "day";

    private long startTime = 0;
    private long endTime = 0;


    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_chart);
        initView();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        type = "day";
        //默认显示日
        tvLearn.setText("今天" + getTrackLearnStr("day") + "销售成绩");
        rlHeader.setBackgroundDrawable(getResources().getDrawable(R.drawable.learn_track_bg_top));
        endTime = System.currentTimeMillis() / 1000;
        scrollBarBean = new ScrollBarBean();
        initClick();
        initEvent();

        setScrollBarData();
    }

    private void initView() {
        llGroup = (LinearLayout) this.findViewById(R.id.ll_group);
        rlHeader = (RelativeLayout) this.findViewById(R.id.rl_header);
        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        leftBack = this.findViewById(R.id.title_back);
        rgStatistic = (RadioGroup) this.findViewById(R.id.radio_group_list);
        scrollBarPic = (ScrollBarPic) this.findViewById(R.id.sbp_bar);
        rlSmartChat = (RelativeLayout) this.findViewById(R.id.rl_smart_chat);
        tvLearn = (TextView) this.findViewById(R.id.tv_learn);

    }

    private void initClick() {
        leftBack.setOnClickListener(this);
        rgStatistic.setOnCheckedChangeListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            default:
                break;


        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        endTime = System.currentTimeMillis() / 1000;
        if (checkedId == R.id.rb_day) {
            rlSmartChat.setVisibility(View.VISIBLE);
            type = "day";
            tvLearn.setText("今天" + getTrackLearnStr(type) + "销售成绩");
            startTime = DateUtils.getDayTimeInMillis() / 1000;
        } else if (checkedId == R.id.rb_week) {
            rlSmartChat.setVisibility(View.VISIBLE);
            type = "week";
            tvLearn.setText("本周" + getTrackLearnStr(type) + "销售成绩");
            //本周一
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            String curWeekOne = formatter.format(DateUtils.getNowWeekMonday(DateUtils.getDayDate()));
            startTime = DateUtils.getSomeDayTimeInMillis(curWeekOne) / 1000;
        } else if (checkedId == R.id.rb_month) {
            rlSmartChat.setVisibility(View.VISIBLE);
            type = "month";
            tvLearn.setText("本月" + getTrackLearnStr(type) + "销售成绩");
            String curMonthOne = DateUtils.getYear() + "-" + (DateUtils.getMonth() + 1) + "-" + 01;
            startTime = DateUtils.getSomeDayTimeInMillis(curMonthOne) / 1000;
        } else { //替换下面的布局

            type = "total";
            tvLearn.setText("累计销售成绩");
            startTime = 0;
        }

    }

    private String getTrackLearnStr(String type) {
        String str = "";
        if (type.equals("day")) {
            str = "(" + (DateUtils.getMonth() + 1) + "月" + DateUtils.getDay() + "日" + ")";
        } else if (type.equals("week")) {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            String temp = formatter.format(DateUtils.getNowWeekMonday(DateUtils.getDayDate()));
            String desDay = DateUtils.getDate(temp, 6);
            str = "(" + DateUtils.getMonthByStrNorm(temp) + "." + DateUtils.getDayByStrNorm(temp) + "-" + DateUtils.getMonthByStrNorm(desDay) + "." + DateUtils.getDayByStrNorm(desDay) + ")";

        } else {
            str = "(" + (DateUtils.getMonth() + 1) + ".1-" + (DateUtils.getMonth() + 1) + "." + DateUtils.getDayNumOfMonth(DateUtils.getYear(), (DateUtils.getMonth() + 1)) + ")";
        }
        return str;
    }


    private void setScrollBarData() {
        scrollBarBean = new ScrollBarBean();
        scrollBarBean.setTotal(7);
        lists = new ArrayList<ScrollPerBarBean>();
        lists.add(new ScrollPerBarBean("8.19", 10.f, false));
        lists.add(new ScrollPerBarBean("8.20", 20.f, false));
        lists.add(new ScrollPerBarBean("8.21", 30f, false));
        lists.add(new ScrollPerBarBean("8.22", 40f, false));
        lists.add(new ScrollPerBarBean("8.23", 50f, false));
        lists.add(new ScrollPerBarBean("8.24", 60f, false));
        lists.add(new ScrollPerBarBean("今日", 70f, true));
        scrollBarBean.setLists(lists);

    }

    private void initEvent() {

        scrollBarPic.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (scrollBarBean != null) {
                            if (scrollBarBean.getLists() != null && scrollBarBean.getLists().size() > 0) {
                                scrollBarPic.setDatas(scrollBarBean);
                            }

                        }

                    }
                });

        scrollBarPic.setOnClickListener(new ScrollBarPic.OnClickListener() {

            @Override
            public void onclick(int i) {
                if (lists != null && lists.size() > 0) {
                    String indexStr = "";
                    Toast.makeText(SmartChatActivity.this, "" + lists.get(i).getxIndexStr(), Toast.LENGTH_SHORT);

                    if (type.equals("day")) {

                        //要单独处理今日
                        if (lists.get(i).getxIndexStr().equals("今日")) {
                            startTime = DateUtils.getDayTimeInMillis() / 1000;
                            String strStar = DateUtils.getYear() + "-" + (DateUtils.getMonth() + 1) + "-" + DateUtils.getDay();
                            String strEnd = DateUtils.getDate(strStar, 1);
                            endTime = (DateUtils.getSomeDayTimeInMillis(strEnd) / 1000) - 1;
                            indexStr = (DateUtils.getMonth() + 1) + "月" + DateUtils.getDay() + "日";

                        } else {
                            String[] strings = lists.get(i).getxIndexStr().split("[.]");
                            String strStar = DateUtils.getYear() + "-" + strings[0] + "-" + strings[1];
                            startTime = DateUtils.getSomeDayTimeInMillis(strStar) / 1000;
                            String strEnd = DateUtils.getDate(strStar, 1);
                            //+ 1天
                            endTime = (DateUtils.getSomeDayTimeInMillis(strEnd) / 1000) - 1;
                            indexStr = strings[0] + "月" + strings[1] + "日";
                        }

                        tvLearn.setText("今天" + "(" + indexStr + ")" + "销售成绩");
                    } else if (type.equals("week")) {

                        //要单独处理本周
                        if (lists.get(i).getxIndexStr().equals("本周")) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                            String curWeekOne = formatter.format(DateUtils.getNowWeekMonday(DateUtils.getDayDate()));

                            startTime = DateUtils.getSomeDayTimeInMillis(curWeekOne) / 1000;
                            //拿下个周一得到截止时间戳
                            String strEnd = DateUtils.getDate(curWeekOne, 6);
                            endTime = (DateUtils.getSomeDayTimeInMillis(strEnd) / 1000) - 1;

                            indexStr = "(" + DateUtils.getMonthByStrNorm(curWeekOne) + "." + DateUtils.getDayByStrNorm(curWeekOne) + "-" + DateUtils.getMonthByStrNorm(strEnd) + "." + DateUtils.getDayByStrNorm(strEnd) + ")";

                        } else {
                            String[] strTemp = lists.get(i).getxIndexStr().split("[-]");
                            String str = strTemp[0];
                            String[] strings = str.split("[.]");
                            String strStar = DateUtils.getYear() + "-" + strings[0] + "-" + strings[1];
                            startTime = DateUtils.getSomeDayTimeInMillis(strStar) / 1000;
                            String strEnd = DateUtils.getDate(strStar, 7);
                            //+ 7天
                            endTime = (DateUtils.getSomeDayTimeInMillis(strEnd) / 1000) - 1;

                            indexStr = lists.get(i).getxIndexStr();
                        }

                        tvLearn.setText("本周" + "(" + indexStr + ")" + "销售成绩");


                    } else if (type.equals("month")) {
                        String monTemp = lists.get(i).getxIndexStr();
                        String mon = monTemp.substring(0, monTemp.length() - 1);
                        String strStar = DateUtils.getYear() + "-" + mon + "-" + "01";
                        startTime = DateUtils.getSomeDayTimeInMillis(strStar) / 1000;
                        String endStr = DateUtils.getYear() + "-" + mon + "-" + DateUtils.getDayNumOfMonth(DateUtils.getYear(), Integer.parseInt(mon));
                        endTime = (DateUtils.getSomeDayTimeInMillis(endStr) / 1000) - 1;

                        indexStr = "(" + mon + "." + "1" + "-" + mon + "." + DateUtils.getDayNumOfMonth(DateUtils.getYear(), Integer.parseInt(mon)) + ")";
                        tvLearn.setText("本月" + indexStr + "销售成绩");

                    }


                }


            }
        });
    }


}
