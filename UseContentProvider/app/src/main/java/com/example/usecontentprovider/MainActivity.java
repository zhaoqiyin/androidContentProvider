package com.example.usecontentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity---";
    private String queryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = findViewById(R.id.edit_query);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                queryId = s.toString();
            }
        });
    }

    // 通过ContentResolver调用ContentProvider插入一条记录
    public void insert(View view) {
        // 得到ContentResolver对象
        ContentResolver resolver = getContentResolver();
        // 调用insert
        Uri uri = Uri.parse("content://com.example.contentprovider.myprovider/person/");
        ContentValues values = new ContentValues();
        values.put("name", "Jack");
        uri = resolver.insert(uri, values);
        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
    }

    public void delete(View view) {
        Log.d(TAG, "delete" +
                "");
        //1 得到ContentResolver对象
        ContentResolver resolver = getContentResolver();
        // 2 delete
        Uri uri = Uri.parse("content://com.example.contentprovider.myprovider/person/3");
        int deleteCount = resolver.delete(uri, null, null);
        Toast.makeText(this, "deleteCount=" + deleteCount, Toast.LENGTH_SHORT).show();
    }

    public void update(View view) {
        // 1 得到ContentResolver对象
        ContentResolver resolver = getContentResolver();
        // 执行update
        Uri uri = Uri.parse("content://com.example.contentprovider.myprovider/person/3");
        ContentValues values = new ContentValues();
        values.put("name", "Jack2");
        int updateCount = resolver.update(uri, values, null, null);
        Toast.makeText(this, "updateCount="+ updateCount, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断字符串是否为数字
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }



    public void query(View view) {
        Log.d(TAG, "query" +
                ", queryId = " + queryId);
        if (!isNumeric(queryId)) {
            Toast.makeText(this, "请输入整数", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1 得到ContentResolver对象
        ContentResolver resolver = getContentResolver();
        // 2 调用query，得到cursor
        Uri uri = Uri.parse("content://com.example.contentprovider.myprovider/person/" + queryId);
        Cursor cursor = resolver.query(uri, null, null, null, null);

        Log.d(TAG, "query");

        // 3 取出cursor中的数据
        if (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Log.d(TAG, "query" +
                    ", id = " + id +
                    ", name = " + name);
            Toast.makeText(this, id + ":" + name, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "该id不存在，请重新输入" , Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }
}
