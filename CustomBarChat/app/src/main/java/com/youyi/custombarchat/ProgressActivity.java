package com.youyi.custombarchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Created by Roy on 16/10/14.
 */

public class ProgressActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = ProgressActivity.class.getSimpleName();

    private LinearLayout llGroup;
    private RelativeLayout rlHeader;

    private TextView tvTitle;
    private View leftBack;
    private RadioGroup rgStatistic;
    private RelativeLayout rlProgressBar;
    private SectionProgressBar xProgressBar;
    private TextView tvLearn;


    private String type = "day";

    private long startTime = 0;
    private long endTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_progress);
        initView();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        type = "day";
        //默认显示日
        tvLearn.setText("今天" + getTrackLearnStr("day") + "销售成绩");
        rlHeader.setBackgroundDrawable(getResources().getDrawable(R.drawable.learn_track_bg_top));
        tvTitle.setText("花样进度条");
        endTime = System.currentTimeMillis() / 1000;
        initClick();
        xProgressBar.setCurrent(80);

    }

    private void initView() {
        llGroup = (LinearLayout) this.findViewById(R.id.ll_group);
        rlHeader = (RelativeLayout) this.findViewById(R.id.rl_header);
        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        leftBack = this.findViewById(R.id.title_back);
        rgStatistic = (RadioGroup) this.findViewById(R.id.radio_group_list);
        rlProgressBar = (RelativeLayout) this.findViewById(R.id.rl_progress_bar);
        xProgressBar = (SectionProgressBar) this.findViewById(R.id.yy_progress_bar);
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
            type = "day";
            tvLearn.setText("今天" + getTrackLearnStr(type) + "销售成绩");
            startTime = DateUtils.getDayTimeInMillis() / 1000;
        } else if (checkedId == R.id.rb_week) {
            type = "week";
            tvLearn.setText("本周" + getTrackLearnStr(type) + "销售成绩");
            //本周一
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            String curWeekOne = formatter.format(DateUtils.getNowWeekMonday(DateUtils.getDayDate()));
            startTime = DateUtils.getSomeDayTimeInMillis(curWeekOne) / 1000;
        } else if (checkedId == R.id.rb_month) {
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


}
