package com.example.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyProvider extends ContentProvider {
    // 存放合法的Uri容器
    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    // 保存一些合法的uri
    // content://com.example.contentprovider.myprovider/person/id
    static {
        matcher.addURI("com.example.contentprovider.myprovider", "/person", 1);
        matcher.addURI("com.example.contentprovider.myprovider", "person/#", 2);
    }

    private DBHelper dbHelper;
    private static final String TAG = "MyProvider";

    public MyProvider() {
        Log.d(TAG, "MyProvider");
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // 得到连接对象
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // 1 匹配uri, 返回code
        int code = matcher.match(uri);
        Log.d(TAG, "query" +
                ", uri = " + uri +
                ", code = " + code);
        // 如果合法，进行查询操作
        if (code == 1) {
            // 不根据id查询
            Cursor cursor = database.query("person", projection, selection, selectionArgs,
                    null, null, null);
            return cursor;
        } else if (code == 2) {
            // 根据id查询
            // 得到id
            long id = ContentUris.parseId(uri);
            Log.d(TAG, "query" +
                    ", uri = " + uri +
                    ", id = " + id);
            // 查询
            Cursor cursor = database.query("person", projection, "_id=?", new String[]{id + ""},
                    null, null, null);
            return cursor;
        } else {
            // 如果不合法，抛出异常
            throw new RuntimeException("查询的uri不合法");
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    // content://com.example.contentprovider.myprovider/person 不根据id插入
    // content://com.example.contentprovider.myprovider/person/id 根据id插入（没有）
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "insert" +
                ", uri = " + uri +
                ", values = " + values);
        // 得到连接对象
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        // 匹配uri，返回code
        int code = matcher.match(uri);
        // 如果合法，插入
        if (code == 1) {
            long id = database.insert("person", null, values);
            // 将id添加到uri中
            uri = ContentUris.withAppendedId(uri, id);
            // 关闭连接
            database.close();
            return uri;
        } else {
            // 如果不合法，抛出异常
            database.close();
            throw new RuntimeException("插入的uri不合法");
        }
    }

    @Override
    // content://com.example.contentprovider.myprovider/person 不根据id删除
    // content://com.example.contentprovider.myprovider/person/id 根据id删除
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "delete" +
                ", uri = " + uri);
        // 得到连接对象
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        int code = matcher.match(uri);
        int deleteCount = -1;
        if (code == 1) {
            deleteCount = database.delete("person", selection, selectionArgs);
        } else if (code == 2) {
            long id = ContentUris.parseId(uri);
            deleteCount = database.delete("person", "_id="+id, null);
        } else {
            database.close();
            throw new RuntimeException("删除的uri不合法");
        }
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update" +
                "");
        // 得到连接对象
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        int code = matcher.match(uri);
        int updateCount = -1;
        if (code == 1) {
            database.update("person", values, selection, selectionArgs);
        } else if (code == 2) {
            long id = ContentUris.parseId(uri);
            updateCount = database.update("person", values, "_id="+id, null);
        } else {
            database.close();
            throw new RuntimeException("更新的uri不合法");
        }
        database.close();
        return updateCount;
    }
}
