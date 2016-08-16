package com.wanbo.werb.util;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wanbo.werb.bean.Emotion;
import com.wanbo.werb.ui.activity.UrlActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Werb on 2016/7/11.
 * Email：1025004680@qq.com
 * 微博文本信息工具类
 */
public class StringUtil {

    //<a href="http://weibo.com" rel="nofollow">新浪微博</a>

    /**
     * 截取source中的来源
     *
     * @param html
     * @return
     */
    public static String getWeiboSource(String html) {
        int s0 = html.indexOf(">");
        if (s0 == -1) return null;
        int s1 = html.indexOf("</a>", s0);
        if (s1 == -1) return null;
        try {
            return html.substring(s0 + 1, s1);
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将微博正文中的 @ 和 # ，url标识出
     *
     * @param text
     * @return
     */
    public static SpannableString getWeiBoText(Context context, String text) {
        Resources res = context.getResources();
        //四种正则表达式
        Pattern AT_PATTERN = Pattern.compile("@[\\u4e00-\\u9fa5\\w\\-]+");
        Pattern TAG_PATTERN = Pattern.compile("#([^\\#|.]+)#");
        Pattern Url_PATTERN = Pattern.compile("((http|https|ftp|ftps):\\/\\/)?([a-zA-Z0-9-]+\\.){1,5}(com|cn|net|org|hk|tw)((\\/(\\w|-)+(\\.([a-zA-Z]+))?)+)?(\\/)?(\\??([\\.%:a-zA-Z0-9_-]+=[#\\.%:a-zA-Z0-9_-]+(&amp;)?)+)?");
        Pattern EMOJI_PATTER = Pattern.compile("\\[([\u4e00-\u9fa5\\w])+\\]");

        SpannableString spannable = new SpannableString(text);

        Matcher tag = TAG_PATTERN.matcher(spannable);
        while (tag.find()) {
            String tagNameMatch = tag.group();
            int start = tag.start();
            spannable.setSpan(new MyTagSpan(context, tagNameMatch), start, start + tagNameMatch.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        Matcher at = AT_PATTERN.matcher(spannable);
        while (at.find()) {
            String atUserName = at.group();
            int start = at.start();
            spannable.setSpan(new MyAtSpan(context, atUserName), start, start + atUserName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        Matcher url = Url_PATTERN.matcher(spannable);
        while (url.find()) {
            String urlString = url.group();
            int start = url.start();
            spannable.setSpan(new MyURLSpan(context, urlString), start, start + urlString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        Matcher emoji = EMOJI_PATTER.matcher(spannable);
        while (emoji.find()) {
            String key = emoji.group(); // 获取匹配到的具体字符
            int start = emoji.start(); // 匹配字符串的开始位置
            Integer imgRes = Emotion.getImgByName(key);
            System.out.println("@@@"+imgRes);
            if (imgRes != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(res, imgRes, options);

                int scale = (int) (options.outWidth / 32);
                options.inJustDecodeBounds = false;
                options.inSampleSize = scale;
                Bitmap bitmap = BitmapFactory.decodeResource(res, imgRes, options);

                ImageSpan span = new ImageSpan(context, bitmap);
                spannable.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spannable;
    }

    /**
     * 用于weibo text中的连接跳转
     */
    private static class MyURLSpan extends ClickableSpan {
        private String mUrl;
        private Context context;

        MyURLSpan(Context ctx, String url) {
            context = ctx;
            mUrl = url;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#f44336"));
        }

        @Override
        public void onClick(View widget) {
            Intent intent = UrlActivity.newIntent(context, mUrl);
            context.startActivity(intent);

        }
    }

    /**
     * 用于转发 weibo 中 @名字的点击跳转
     */
    private static class MyAtSpan extends ClickableSpan {
        private String mName;
        private Context context;

        MyAtSpan(Context ctx, String name) {
            context = ctx;
            mName = name;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#3F51B5"));
            ds.setUnderlineText(false); //去掉下划线
        }

        @Override
        public void onClick(View widget) {
            mName = mName.substring(1);
        }
    }

    /**
     * 用于转发 weibo 中 Tag 的点击跳转
     */
    private static class MyTagSpan extends ClickableSpan {
        private String mTag;
        private Context context;

        MyTagSpan(Context ctx, String tag) {
            context = ctx;
            mTag = tag;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#3F51B5"));
            ds.setUnderlineText(false); //去掉下划线
        }

        @Override
        public void onClick(View widget) {
//            mTag = mTag.substring(1,mTag.length()-1);
//            System.out.println("---tag--"+mTag);
//            Intent intent = TagListActivity.newIntent(context,mTag);
//            context.startActivity(intent);
        }
    }

    public static TextWatcher textNumberListener(EditText editText, TextView textView, Context context) {
        //输入字符监听
        TextWatcher mTextWatcher = new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                temp = s;
                if (140 - s.length() == 0) {
                    textView.setText("超出字数限制！");
                    textView.setTextColor(Color.RED);
                } else {
                    textView.setText(String.valueOf(140 - s.length()));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                editStart = editText.getSelectionStart();
                editEnd = editText.getSelectionEnd();
                if (temp.length() > 140) {
                    Toast.makeText(context,
                            "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
                            .show();
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    editText.setText(s);
                    editText.setSelection(tempSelection);
                }
            }
        };

        return mTextWatcher;
    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

}
