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
import njust.dzh.warehouse.entity.User;


/*对商品进行更新和删除*/
public class GoodsOperatorActivity extends AppCompatActivity implements View.OnClickListener {
    //声明变量
    private EditText edId;
    private EditText edGoodsName;
    private EditText edAmount;
    private Button btUpdate;
    private Button btDelete;
    private Intent intent;
    private Goods goods;
    private DBHelper smb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_operator);
        intent = getIntent();//获取跳转过来的意图
        Bundle bundle = intent.getBundleExtra("goods");
        goods = (Goods) bundle.getSerializable("goods");
        initView();
    }

    public void initView() {
        //初始化控件
        edId = findViewById(R.id.id_ed);
        edGoodsName = findViewById(R.id.goods_name_ed);
        edAmount = findViewById(R.id.amount_ed);
        btUpdate = findViewById(R.id.update_bt);
        btDelete = findViewById(R.id.delete_bt);
        smb = new DBHelper(this);
        btDelete.setOnClickListener(this);
        btUpdate.setOnClickListener(this);

        //设置控件参数
        edId.setText(String.valueOf(goods.getId()));
        edId.setEnabled(false);//去掉编号点击时编辑框下面横线:
        edId.setFocusable(false);//键盘无法操作
        edId.setFocusableInTouchMode(false);//无法触摸

        edGoodsName.setText(goods.getGoodsName());
        edGoodsName.setEnabled(false);
        edGoodsName.setFocusable(false);//键盘无法操作
        edGoodsName.setFocusableInTouchMode(false);//无法触摸

        edAmount.setText(String.valueOf(goods.getAmount()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_bt://更新商品
                int amount=Integer.parseInt(edAmount.getText().toString().trim());
                goods.setAmount(amount);
                smb.updateGoodsInfo(goods);
                Toast.makeText(GoodsOperatorActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                backActivity();
                break;
            case R.id.delete_bt:
                smb.deleteGoods(goods.getId());
                Toast.makeText(GoodsOperatorActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                backActivity();
                break;
        }
    }
    void backActivity(){
        User user=new User(1);
        Bundle bundle=new Bundle();
        bundle.putSerializable("user",user);
        Intent intent =new Intent(GoodsOperatorActivity.this,GoodsListActivity.class);
        intent.putExtra("user",bundle);
        startActivity(intent);
        finish();
    }

}