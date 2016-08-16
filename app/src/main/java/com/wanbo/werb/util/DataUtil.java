package com.wanbo.werb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Werb on 2016/7/27.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * format Date for WeiBo create_time
 */
public class DataUtil {

    /**
     * 格式化新浪返回的日期数据
     * @return
     */
    private static String getDataFormat(String sinaData) {

        SimpleDateFormat format1 = new SimpleDateFormat(
                "EEE MMM d HH:mm:ss Z yyyy", Locale.US);

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy MM dd hh:mm:ss");
        try {
            return formatDate.format(format1.parse(sinaData));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 显示时间，如果与当前时间差别小于一天，则自动用**秒(分，小时)前，如果大于一天则用format规定的格式显示
     *
     * @param date 时间
     *            格式 格式描述:例如:yyyy-MM-dd yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String showTime(String date) {

        String formatDate = getDataFormat(date);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd hh:mm:ss");

        Date ctime = null;
        try {
            ctime = sdf.parse(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String r = "";
        if(ctime==null)return r;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,-12);

        if(calendar.get(Calendar.HOUR_OF_DAY)>=12&&calendar.get(Calendar.AM_PM)==1){
            calendar.add(Calendar.HOUR_OF_DAY,-12);
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }

        long nowtimelong = calendar.getTimeInMillis();
        long ctimelong = ctime.getTime();
        long result = Math.abs(nowtimelong - ctimelong);

        if(result < 60000){// 一分钟内
            long seconds = result / 1000;
            if(seconds == 0){
                r = "刚刚";
            }else{
                r = seconds + "秒";
            }
        }else if (result >= 60000 && result < 3600000){// 一小时内
            long seconds = result / 60000;
            r = seconds + "分";
        }else if (result >= 3600000 && result < 86400000){// 一天内
            long seconds = result / 3600000;
            r = seconds + "时";
        }else if (result >= 86400000 && result < 1702967296){// 三十天内
            long seconds = result / 86400000;
            r = seconds + "天";
        }else{// 日期格式
            String format="MM-dd HH:mm";
            SimpleDateFormat df = new SimpleDateFormat(format);
            r = df.format(ctime).toString();
        }
        return r;
    }

}
