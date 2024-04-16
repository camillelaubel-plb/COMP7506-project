package njust.dzh.warehouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

import njust.dzh.warehouse.R;
import njust.dzh.warehouse.adapter.GoodsAdapter;
import njust.dzh.warehouse.adapter.UserAdapter;
import njust.dzh.warehouse.database.DBHelper;
import njust.dzh.warehouse.entity.Goods;
import njust.dzh.warehouse.entity.User;

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
            case R.id.import_bt://入库按钮
                //加载自定义窗口布局文件
                final AlertDialog dialog2 = new AlertDialog.Builder(this).create();
                LinearLayout line2 = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_goods_import, null);
                dialog2.setView(line2);
                dialog2.show();//显示对话框
                //绑定对话框中的控件
                Button btn2 = line2.findViewById(R.id.import_bt_dialog);
                final EditText edImport = line2.findViewById(R.id.goods_name_ed_dialog);
                final EditText edAmount = line2.findViewById(R.id.import_num_ed_dialog);
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //获取商品名称
                        String goodsName2 = edImport.getText().toString().trim();
                        //获取商品对象
                        Goods tempGoods = smb.searchGoods(goodsName2);
                        //判断商品是否存在
                        if (tempGoods.getProductName() != null) {
                            String amounts2 = edAmount.getText().toString().trim();
                            if(amounts2.isEmpty()){//如果数量为空
                                Toast.makeText(GoodsListActivity.this,"请输入数量",Toast.LENGTH_SHORT).show();
                            }else{//数量不为空
                                int amount2 = Integer.valueOf(amounts2);//字符串转整型
                                amount2+=tempGoods.getAmount();//数量相加
                                tempGoods.setAmount(amount2);//重新设置数量
                                smb.updateGoodsInfo(tempGoods);//更新商品
                                Toast.makeText(GoodsListActivity.this, "入库成功", Toast.LENGTH_SHORT).show();
                                updateList();
                                dialog2.dismiss();
                            }
                        } else {
                            Toast.makeText(GoodsListActivity.this, "该商品不存在", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                break;
            case R.id.export_bt:
                //加载自定义窗口布局文件
                final AlertDialog dialog3 = new AlertDialog.Builder(this).create();
                LinearLayout line3 = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_goods_export, null);
                dialog3.setView(line3);
                dialog3.show();
                //初始化控件
                Button btn3 = line3.findViewById(R.id.export_bt_dialog);
                final EditText edExport = line3.findViewById(R.id.goods_name_ed_dialog);
                final EditText edExAmount = line3.findViewById(R.id.export_num_ed_dialog);
                btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //获取控件参数，设置Goods值
                        String goodsName3 = edExport.getText().toString().trim();
                        String amounts3 = edExAmount.getText().toString().trim();
                        if(amounts3.isEmpty()){
                            Toast.makeText(GoodsListActivity.this,"数量不能为空",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int amount3 = Integer.valueOf(amounts3);
                        //判断产品是否存在
                        Goods tempGoods3 = smb.searchGoods(goodsName3);
                        if (tempGoods3.getProductName() != null) {//如果商品存在
                            if(amount3<=tempGoods3.getAmount()) {//如果数量足够
                                int newAmount3 = tempGoods3.getAmount() - amount3;
                                tempGoods3.setAmount(newAmount3);
                                smb.updateGoodsInfo(tempGoods3);
                                Toast.makeText(GoodsListActivity.this, "出库成功", Toast.LENGTH_SHORT).show();
                                updateList();
                                dialog3.dismiss();
                            }else {
                                Toast.makeText(GoodsListActivity.this, "库存不够，请重新输入数量", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(GoodsListActivity.this, "该商品不存在", Toast.LENGTH_SHORT).show();
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

        if(power==2){
            Toast.makeText(this,"您可以进行出入库操作",Toast.LENGTH_SHORT).show();
        }else{
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
