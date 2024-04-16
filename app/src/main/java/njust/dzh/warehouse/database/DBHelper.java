package njust.dzh.warehouse.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import njust.dzh.warehouse.entity.Goods;
import njust.dzh.warehouse.entity.User;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "warehouse.db";
    private static final int VERSION = 1;

    // User Table Attributes
    private static final String KY_USERNAME = "username";
    private static final String KY_PASSWORD = "password";
    private static final String KY_POWER = "power";

    // Goods Table Attributes
    private static final String KY_ID = "id";
    private static final String KY_PRODUCTNAME = "goods_name";
    private static final String KY_AMOUNT = "amount";


    private static final String TABLE_USER = "user";
    private static final String TABLE_PRODUCTS = "goods";

    SQLiteDatabase db;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create User Table
        final String createUser = "create table " + TABLE_USER + "(" + KY_USERNAME + " text primary key,"
                + KY_PASSWORD + " text not null,"
                + KY_POWER + " text not null);";
        db.execSQL(createUser);

        // Create Products Table
        final String createProducts = "create table " + TABLE_PRODUCTS + "(" + KY_ID + " integer primary key autoincrement,"
                + KY_PRODUCTNAME + " text,"
                + KY_AMOUNT + " integer);";
        db.execSQL(createProducts);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = db.query(TABLE_USER, new String[]{KY_USERNAME, KY_PASSWORD, KY_POWER}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            User u=new User();
            u.setUsername(cursor.getString(0));
            u.setPassword(cursor.getString(1));
            u.setPower(cursor.getInt(2));
            users.add(u);
        }
        return users;
    }


    public List<Goods> getAllProducts() {
        List<Goods> products = new ArrayList<>();
        Cursor cursor = db.query(TABLE_PRODUCTS, new String[]{KY_ID, KY_PRODUCTNAME, KY_AMOUNT}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Goods g = new Goods();
            g.setId(cursor.getInt(0));
            g.setProductName(cursor.getString(1));
            g.setAmount(cursor.getInt(2));
            products.add(g);
        }
        return products;
    }

    public void insertGoods(Goods goods) {
        ContentValues values = new ContentValues();
        values.put(KY_PRODUCTNAME, goods.getProductName());
        values.put(KY_AMOUNT, goods.getAmount());
        db.insert(TABLE_PRODUCTS, null, values);
    }

    public void deleteGoods(int id) {
        db.delete(TABLE_PRODUCTS, KY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updateGoodsInfo(Goods goods) {
        ContentValues values = new ContentValues();
        values.put(KY_PRODUCTNAME, goods.getProductName());
        values.put(KY_AMOUNT, goods.getAmount());
        db.update(TABLE_PRODUCTS, values, KY_ID + "=?", new String[]{String.valueOf(goods.getId())});
    }

    public Goods searchGoods(String goodsName) {
        Cursor cursor = db.query(TABLE_PRODUCTS, new String[]{KY_ID, KY_PRODUCTNAME, KY_AMOUNT}, KY_PRODUCTNAME + "=?", new String[]{goodsName}, null, null, null);
        Goods g = new Goods();
        if (cursor.moveToFirst()) {
            g.setId(cursor.getInt(0));
            g.setProductName(cursor.getString(1));
            g.setAmount(cursor.getInt(2));
        }
        return g;
    }

    public void insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put(KY_USERNAME,user.getUsername());
        values.put(KY_PASSWORD,user.getPassword());
        values.put(KY_POWER,user.getPower());
        db.insert(TABLE_USER, null, values);
    }

    public void deleteUser(String username) {
        db.delete(TABLE_USER, KY_USERNAME + "=?", new String[]{username});
    }

    public void updateUserInfo(User user) {
        ContentValues values = new ContentValues();
        values.put(KY_PASSWORD, user.getPassword());
        values.put(KY_POWER, user.getPower());
        db.update(TABLE_USER, values, KY_USERNAME + "=?", new String[]{user.getUsername()});
    }

    // Find user login information
    public User searchUser(String username) {
        Cursor cursor = db.query(TABLE_USER, new String[]{KY_USERNAME, KY_PASSWORD, KY_POWER}, KY_USERNAME + "=?", new String[]{username}, null, null, null);
        User u = new User();
        if (cursor.moveToFirst()) {
            u.setUsername(cursor.getString(0));
            u.setPassword(cursor.getString(1));
            u.setPower(cursor.getInt(2));
        }
        return u;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER + ";");
        onCreate(db);
    }
}
