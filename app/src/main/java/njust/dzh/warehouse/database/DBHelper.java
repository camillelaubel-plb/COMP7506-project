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
    //数据库名称和版本号
    private static final String DATABASE_NAME = "warehouse.db";
    private static final int VERSION = 1;

    //用户表的属性
    private static final String KY_USERNAME = "username";//用户名
    private static final String KY_PASSWORD = "password";//用户密码
    private static final String KY_POWER = "power";//权限：商品管理员/出入库员
    //商品表的属性
    private static final String KY_ID = "id";//商品编号
    private static final String KY_GOODSNAME = "goods_name";//商品名称
    private static final String KY_AMOUNT = "amount";//商品数量

    //数据库的两张表
    private static final String TABLE_USER = "user";//用户表
    private static final String TABLE_GOODS = "goods";//商品表
    //数据库对象
    SQLiteDatabase db;

    //重写父类构造方法
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建用户表
        final String createUser = "create table " + TABLE_USER + "(" + KY_USERNAME + " text primary key,"
                + KY_PASSWORD + " text not null,"
                + KY_POWER + " text not null);";
        db.execSQL(createUser);
        //创建商品表
        final String createGoods = "create table " + TABLE_GOODS + "(" + KY_ID + " integer primary key autoincrement,"
                + KY_GOODSNAME + " text,"
                + KY_AMOUNT + " integer);";
        db.execSQL(createGoods);
    }

    //获取所有用户
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = db.query(TABLE_USER, new String[]{KY_USERNAME, KY_PASSWORD, KY_POWER}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            User u=new User();//创建对象
            u.setUsername(cursor.getString(0));
            u.setPassword(cursor.getString(1));
            u.setPower(cursor.getInt(2));
            users.add(u);
        }
        return users;
    }

    //获取所有商品
    public List<Goods> getAllGoods() {
        List<Goods> goods = new ArrayList<>();
        Cursor cursor = db.query(TABLE_GOODS, new String[]{KY_ID, KY_GOODSNAME, KY_AMOUNT}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Goods g = new Goods();//创建对象
            g.setId(cursor.getInt(0));
            g.setGoodsName(cursor.getString(1));
            g.setAmount(cursor.getInt(2));
            goods.add(g);
        }
        return goods;
    }

    //添加商品
    public void insertGoods(Goods goods) {
        ContentValues values = new ContentValues();
        values.put(KY_GOODSNAME, goods.getGoodsName());
        values.put(KY_AMOUNT, goods.getAmount());
        db.insert(TABLE_GOODS, null, values);
    }

    //删除商品
    public void deleteGoods(int id) {
        db.delete(TABLE_GOODS, KY_ID + "=?", new String[]{String.valueOf(id)});
    }

    //更新商品
    public void updateGoodsInfo(Goods goods) {
        ContentValues values = new ContentValues();
        values.put(KY_GOODSNAME, goods.getGoodsName());
        values.put(KY_AMOUNT, goods.getAmount());
        db.update(TABLE_GOODS, values, KY_ID + "=?", new String[]{String.valueOf(goods.getId())});
    }

    //查询商品信息
    public Goods searchGoods(String goodsName) {
        Cursor cursor = db.query(TABLE_GOODS, new String[]{KY_ID, KY_GOODSNAME, KY_AMOUNT}, KY_GOODSNAME + "=?", new String[]{goodsName}, null, null, null);
        Goods g = new Goods();
        if (cursor.moveToFirst()) {
            g.setId(cursor.getInt(0));
            g.setGoodsName(cursor.getString(1));
            g.setAmount(cursor.getInt(2));
        }
        return g;
    }

    //添加用户
    public void insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put(KY_USERNAME,user.getUsername());
        values.put(KY_PASSWORD,user.getPassword());
        values.put(KY_POWER,user.getPower());
        db.insert(TABLE_USER, null, values);
    }

    //删除用户
    public void deleteUser(String username) {
        db.delete(TABLE_USER, KY_USERNAME + "=?", new String[]{username});
    }

    //更新用户
    public void updateUserInfo(User user) {
        ContentValues values = new ContentValues();
        values.put(KY_PASSWORD, user.getPassword());
        values.put(KY_POWER, user.getPower());
        db.update(TABLE_USER, values, KY_USERNAME + "=?", new String[]{user.getUsername()});
    }

    //查询用户信息
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

    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOODS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER + ";");
        onCreate(db);
    }
}
