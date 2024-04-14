package njust.dzh.warehouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import njust.dzh.warehouse.R;
import njust.dzh.warehouse.database.DBHelper;
import njust.dzh.warehouse.entity.Goods;


/*添加商品的活动*/
public class GoodsInsertActivity extends AppCompatActivity implements View.OnClickListener {
    //声明变量
    private EditText edGoodsName;
    private EditText edAmount;
    private Button btInsert;
    private Button btBack;
    private Goods goods;
    private DBHelper smb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_add);
        initView();
    }
    public void initView(){
        //初始化控件
        edGoodsName=findViewById(R.id.goods_name_ed);
        edAmount=findViewById(R.id.amount_ed);
        btInsert=findViewById(R.id.insert_bt);
        btBack=findViewById(R.id.exit_bt);
        smb=new DBHelper(this);
        btInsert.setOnClickListener(this);
        btBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.insert_bt:
                String goodsName=edGoodsName.getText().toString().trim();
                String amounts=edAmount.getText().toString().trim();
                if(smb.searchGoods(goodsName).getProductName()!=null){
                    Toast.makeText(GoodsInsertActivity.this,"该商品已存在，无法添加",Toast.LENGTH_SHORT).show();
                }else{
                    goods=new Goods(goodsName,Integer.parseInt(amounts));
                    smb.insertGoods(goods);
                    Toast.makeText(GoodsInsertActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                }
                backActivity();
                break;
            case R.id.exit_bt:
                backActivity();
                break;
        }
    }
    //跳转，并销毁
    public void backActivity(){
        Intent intent=new Intent(GoodsInsertActivity.this,GoodsListActivity.class);
        startActivity(intent);
        finish();
    }
}
