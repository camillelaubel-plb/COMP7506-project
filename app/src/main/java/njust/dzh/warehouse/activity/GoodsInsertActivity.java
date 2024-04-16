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

public class GoodsInsertActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edGoodsName;
    private EditText edAmount;
    private Button btInsert;
    private Goods goods;
    private DBHelper smb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_add);
        initView();
    }
    public void initView(){
        edGoodsName=findViewById(R.id.goods_name_ed);
        edAmount=findViewById(R.id.amount_ed);
        btInsert=findViewById(R.id.insert_bt);

        smb=new DBHelper(this);

        btInsert.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.insert_bt:
                String goodsName=edGoodsName.getText().toString().trim();
                String amounts=edAmount.getText().toString().trim();

                if(smb.searchGoods(goodsName).getProductName()!=null){
                    Toast.makeText(GoodsInsertActivity.this,"Product already exists",Toast.LENGTH_SHORT).show();
                }else{
                    goods=new Goods(goodsName,Integer.parseInt(amounts));
                    smb.insertGoods(goods);
                    Toast.makeText(GoodsInsertActivity.this,"Successfully Added",Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
        }
    }
}
