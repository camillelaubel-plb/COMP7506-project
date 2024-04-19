package edu.hku.comp2506.warehouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

import edu.hku.comp2506.warehouse.R;
import edu.hku.comp2506.warehouse.adapter.GoodsAdapter;
import edu.hku.comp2506.warehouse.database.DBHelper;
import edu.hku.comp2506.warehouse.entity.Goods;
import edu.hku.comp2506.warehouse.entity.User;

public class GoodsListActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btInsert;
    private EditText edProductName;
    private Button btImport;
    private Button btExport;
    private Button btSearch;

    private ListView lvGoods;
    private GoodsAdapter goodsAdapter;
    private DBHelper smb;
    private Intent intent;
    private int power;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        Bundle bundle = intent.getBundleExtra("user");
        if(bundle!=null){
            user=(User) bundle.getSerializable("user");
            // Get Permission
            power = user.getPower();
        }
        // If user is a normal user
        if (power==2) {
            setContentView(R.layout.activity_goods_list_port);
        } else {
            // If user is admin or Product Manager
            setContentView(R.layout.activity_goods_list);
        }
        initView();
    }

    public void initView() {
        edProductName=findViewById(R.id.productName_ed);
        lvGoods = findViewById(R.id.goods_lv);
        btInsert = findViewById(R.id.insert_bt);
        btSearch=findViewById(R.id.search_bt);
        btImport = findViewById(R.id.import_bt);
        btExport = findViewById(R.id.export_bt);

        smb = new DBHelper(this);

        // For different users we use different xml files
        // These do not have the same buttons
        if (btInsert != null) {
            btInsert.setOnClickListener(this);
        }

        if(btImport!=null&&btExport!=null){
            btImport.setOnClickListener(this);
            btExport.setOnClickListener(this);
        }

        if (btSearch != null) {
            btSearch.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Add Product
            case R.id.insert_bt:
                Intent intent = new Intent(GoodsListActivity.this, GoodsInsertActivity.class);
                startActivity(intent);
                break;
            case R.id.search_bt:
                // On search, filter out the list of products
                List<Goods> products=smb.getAllProducts();
                String username=edProductName.getText().toString().trim();

                if(username.isEmpty()){
                    goodsAdapter=new GoodsAdapter(this,products);
                    lvGoods.setAdapter(goodsAdapter);
                }else{
                    List<Goods> filteredProducts = getFilteredList(products, username);

                    goodsAdapter=new GoodsAdapter(this,filteredProducts);
                    lvGoods.setAdapter(goodsAdapter);
                }
                break;
            // Inbound
            case R.id.import_bt:
                // Show dialog
                final AlertDialog dialog2 = new AlertDialog.Builder(this).create();
                LinearLayout line2 = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_goods_import, null);
                dialog2.setView(line2);
                dialog2.show();

                Button btn2 = line2.findViewById(R.id.import_bt_dialog);
                final EditText edImport = line2.findViewById(R.id.goods_name_ed_dialog);
                final EditText edAmount = line2.findViewById(R.id.import_num_ed_dialog);

                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String goodsName2 = edImport.getText().toString().trim();
                        Goods tempGoods = smb.searchGoods(goodsName2);
                        // Check if product already exists
                        if (tempGoods.getProductName() != null) {
                            String amounts2 = edAmount.getText().toString().trim();
                            // If product has no quantity, add some
                            if(amounts2.isEmpty()){
                                Toast.makeText(GoodsListActivity.this,"Provide an Inbound Quantity",Toast.LENGTH_SHORT).show();
                            }else{
                                // Otherwise, add the quantities and update product
                                int amount2 = Integer.valueOf(amounts2);
                                amount2+=tempGoods.getAmount();
                                tempGoods.setAmount(amount2);

                                smb.updateGoodsInfo(tempGoods);
                                Toast.makeText(GoodsListActivity.this, "Accepted, Stock Updated", Toast.LENGTH_SHORT).show();
                                updateList();
                                dialog2.dismiss();
                            }
                        } else {
                            Toast.makeText(GoodsListActivity.this, "Product does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                break;
            // Outbound
            case R.id.export_bt:
                // Show dialog
                final AlertDialog dialog3 = new AlertDialog.Builder(this).create();
                LinearLayout line3 = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_goods_export, null);
                dialog3.setView(line3);
                dialog3.show();

                Button btn3 = line3.findViewById(R.id.export_bt_dialog);
                final EditText edExport = line3.findViewById(R.id.goods_name_ed_dialog);
                final EditText edExAmount = line3.findViewById(R.id.export_num_ed_dialog);
                btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String goodsName3 = edExport.getText().toString().trim();
                        String amounts3 = edExAmount.getText().toString().trim();
                        if(amounts3.isEmpty()){
                            Toast.makeText(GoodsListActivity.this,"Provide an Outbound Quantity",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int amount3 = Integer.valueOf(amounts3);
                        Goods tempGoods3 = smb.searchGoods(goodsName3);
                        // Check if product already exists
                        if (tempGoods3.getProductName() != null) {
                            // If there is enough product quantity, allow
                            if(amount3<=tempGoods3.getAmount()) {
                                int newAmount3 = tempGoods3.getAmount() - amount3;
                                tempGoods3.setAmount(newAmount3);
                                smb.updateGoodsInfo(tempGoods3);
                                Toast.makeText(GoodsListActivity.this, "Shipped, Stock Updated", Toast.LENGTH_SHORT).show();
                                updateList();
                                dialog3.dismiss();
                            }else {
                                Toast.makeText(GoodsListActivity.this, "Insufficient Stock", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(GoodsListActivity.this, "Product does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }
    public void updateList(){
        final List<Goods> goodsList=smb.getAllProducts();
        goodsAdapter=new GoodsAdapter(this,goodsList);
        lvGoods.setAdapter(goodsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Show all products from DB
        final List<Goods> goodsList = smb.getAllProducts();

        goodsAdapter = new GoodsAdapter(this, goodsList);
        lvGoods.setAdapter(goodsAdapter);

        if(power!=2){
            lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Goods g = goodsList.get(position);

                    Intent intent = new Intent(GoodsListActivity.this, GoodsOperatorActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", g);
                    intent.putExtra("goods", bundle);
                    startActivity(intent);
                }
            });
        }
    }

    private List<Goods> getFilteredList(List<Goods> products, String productToCheck) {
        List<Goods> productList = new ArrayList<Goods>();
        for(Goods product : products) {
            if (product.getProductName().contains(productToCheck)) {
                productList.add(product);
            }
        }
        return productList;
    }

}
