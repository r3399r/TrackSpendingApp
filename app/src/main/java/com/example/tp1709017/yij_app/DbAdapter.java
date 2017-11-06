package com.example.tp1709017.yij_app;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TP1709017 on 2017/10/27.
 */

public class DbAdapter {

    private static final String DB_NAME = "yij.db";
    private static final int DB_VERSION = 1;

    private static final String DB_TABLE = "data";
    private Context ctx = null;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqlDb;

    public DbAdapter(Context context) {
        ctx = context;
    }

    public DbAdapter open() throws SQLException {

        dbHelper = new DatabaseHelper(ctx);
        sqlDb = dbHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertNewData(long epoch_time, int money, String ps) {
        String strSQL = new String("INSERT INTO " + DB_TABLE + " "
                + "(epoch_time, money, ps) VALUES ("
                + "'" + epoch_time + "', "
                + "'" + money + "', "
                + "'" + ps + "')");

        sqlDb.execSQL(strSQL);
        return true;
    }

    public Cursor getHistoryData() {

        String strSQL = new String("SELECT * FROM " + DB_TABLE
                + " ORDER BY epoch_time DESC"
        );

        return sqlDb.rawQuery(strSQL, null);
    }

    public boolean deleteData(int rowId) {

        String strSQL = new String("DELETE FROM " + DB_TABLE
                + " WHERE _id = " + rowId);

        sqlDb.execSQL(strSQL);
        return true;
    }

    public boolean updateData(int rowId, int money, String ps) {

        String strSQLUpdate = new String("UPDATE " + DB_TABLE + " SET"
                + " money = " + money + ","
                + " ps = '" + ps + "'"
                + " WHERE _id = " + rowId);
        sqlDb.execSQL(strSQLUpdate);

        return true;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        private void createTable(SQLiteDatabase db) {
            String strSQLCreate = "CREATE TABLE " + DB_TABLE + " ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "epoch_time BIGINT, "
                    + "money INTEGER, "
                    + "ps TEXT)";

            db.execSQL(strSQLCreate);
        }
    }
}
