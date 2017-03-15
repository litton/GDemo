package com.fantasy.coolgif.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fantasy.coolgif.response.GifItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanlitao on 17/3/15.
 */

public class DBHelper {


    private static final String DATABASE_NAME = "cool_gif.db";// 数据库名
    SQLiteDatabase db;
    Context context;//应用环境上下文   Activity 是其子类

    public DBHelper(Context _context) {
        context = _context;
        //开启数据库
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        CreateTable();
    }


    public void CreateTable() {
        try {
            db.execSQL("CREATE TABLE gif_heart (" +
                    "_ID INTEGER PRIMARY KEY autoincrement,"
                    + "gif_title TEXT, gif_url varchar(255),like_info int"
                    + ");");

            db.execSQL("CREATE TABLE gif_like (" +
                    "_ID INTEGER PRIMARY KEY autoincrement,"
                    + "gif_title TEXT, gif_url varchar(255),like_info int"
                    + ");");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加数据
     *
     * @return
     */
    public boolean saveClickHeartGif(GifItem item) {
        String sql = "";
        try {
            sql = "insert into gif_heart values(null,'" + item.gif_title + "','" + item.gif_url + "','" + item.like_info + "')";
            db.execSQL(sql);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean saveLikeGif(GifItem item) {
        String sql = "";
        try {
            sql = "insert into gif_like values(null,'" + item.gif_title + "','" + item.gif_url + "','" + item.like_info + "')";
            db.execSQL(sql);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isHeartedGifItem(GifItem item) {
        Cursor cursor = db.query("gif_heart", null, "gif_url = ? ", new String[]{item.gif_url}, null, null, null);
        if (cursor != null) {
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    return true;
                }
            } catch (Exception ex) {

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return false;
    }

    public boolean isHLikeddGifItem(GifItem item) {
        Cursor cursor = db.query("gif_like", null, "gif_url = ? ", new String[]{item.gif_url}, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return true;
                }
            } catch (Exception ex) {

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return false;
    }

    public List<GifItem> getAllHeartedGifList() {
        Cursor cursor = db.query("gif_heart", new String[]{"gif_title", "gif_url", "like_info"}, null, null, null, null, null);
        List<GifItem> dataList = new ArrayList<GifItem>();
        if (cursor != null) {
            try {

                while (cursor.moveToNext()){
                    GifItem item = new GifItem();
                    item.gif_title = cursor.getString(0);
                    item.gif_url = cursor.getString(1);
                    item.like_info = cursor.getInt(2);
                    dataList.add(item);
                }
            } catch (Exception ex) {

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return dataList;
    }

    public List<GifItem> getAllLikedGIfList() {
        Cursor cursor = db.query("gif_like", new String[]{"gif_title", "gif_url", "like_info"}, null, null, null, null, null);
        List<GifItem> dataList = new ArrayList<GifItem>();
        if (cursor != null) {
            try {

                while (cursor.moveToNext()){
                    GifItem item = new GifItem();
                    item.gif_title = cursor.getString(0);
                    item.gif_url = cursor.getString(1);
                    item.like_info = cursor.getInt(2);
                    dataList.add(item);
                }
            } catch (Exception ex) {

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return dataList;
    }

}
