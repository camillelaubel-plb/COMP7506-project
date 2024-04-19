package edu.hku.comp2506.warehouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.hku.comp2506.warehouse.R;
import edu.hku.comp2506.warehouse.database.DBHelper;
import edu.hku.comp2506.warehouse.entity.Goods;


public class GoodsOperatorActivity extends AppCompatActivity implements View.OnClickListener {
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
        intent = getIntent();
        Bundle bundle = intent.getBundleExtra("goods");
        goods = (Goods) bundle.getSerializable("goods");
        initView();
    }

    public void initView() {
        edId = findViewById(R.id.id_ed);
        edGoodsName = findViewById(R.id.goods_name_ed);
        edAmount = findViewById(R.id.amount_ed);
        btUpdate = findViewById(R.id.update_bt);
        btDelete = findViewById(R.id.delete_bt);

        smb = new DBHelper(this);

        btDelete.setOnClickListener(this);
        btUpdate.setOnClickListener(this);

        // ID cannot be changed
        edId.setText(String.valueOf(goods.getId()));
        edId.setEnabled(false);
        edId.setFocusable(false);
        edId.setFocusableInTouchMode(false);

        // Product Name cannot be changed
        // Fill with Product Name
        edGoodsName.setText(goods.getProductName());
        edGoodsName.setEnabled(false);
        edGoodsName.setFocusable(false);
        edGoodsName.setFocusableInTouchMode(false);

        // Fill with product Quantity
        edAmount.setText(String.valueOf(goods.getAmount()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_bt:
                int amount=Integer.parseInt(edAmount.getText().toString().trim());
                goods.setAmount(amount);
                smb.updateGoodsInfo(goods);
                Toast.makeText(GoodsOperatorActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.delete_bt:
                smb.deleteGoods(goods.getId());
                Toast.makeText(GoodsOperatorActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}