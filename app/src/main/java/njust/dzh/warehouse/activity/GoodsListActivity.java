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


import java.util.List;

import njust.dzh.warehouse.R;
import njust.dzh.warehouse.adapter.GoodsAdapter;
import njust.dzh.warehouse.database.DBHelper;
import njust.dzh.warehouse.entity.Goods;
import njust.dzh.warehouse.entity.User;

/*商品列表的活动*/
public class GoodsListActivity extends AppCompatActivity implements View.OnClickListener {
    //声明变量
    private Button btInsert;
    private Button btSelect;
    private Button btExit;
    private Button btImport;
    private Button btExport;
    private ListView lvGoods;
    private GoodsAdapter goodsAdapter;
    private DBHelper smb;
    private Intent intent;
    private int power;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取权限
        intent = getIntent();
        Bundle bundle = intent.getBundleExtra("user");
        if(bundle!=null){
            user=(User) bundle.getSerializable("user");
            power = user.getPower();
        }
        if (power==2) {//2是出入库人员界面
            setContentView(R.layout.activity_goods_list_port);
        } else {//1是商品管理员界面
            setContentView(R.layout.activity_goods_list);
        }
        initView();
    }
    //初始化控件
    public void initView() {
        //查询按钮、退出按钮、商品列表
        btSelect = findViewById(R.id.select_bt);
        btExit = findViewById(R.id.exit_bt);
        lvGoods = findViewById(R.id.goods_lv);
        //插入按钮
        btInsert = findViewById(R.id.insert_bt);
        //入库按钮、入库按钮
        btImport = findViewById(R.id.import_bt);
        btExport = findViewById(R.id.export_bt);
        //初始化数据库对象
        smb = new DBHelper(this);
        //如果按钮不为空则设置监听器
        if (btInsert != null) {
            btInsert.setOnClickListener(this);
        }
        if(btImport!=null&&btExport!=null){
            btImport.setOnClickListener(this);
            btExport.setOnClickListener(this);
        }
        if(btSelect!=null){
            btSelect.setOnClickListener(this);
        }
        btExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert_bt://添加按钮
                Intent intent = new Intent(GoodsListActivity.this, GoodsInsertActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.select_bt://查询按钮
                //加载自定义窗口布局文件
                final AlertDialog dialog = new AlertDialog.Builder(this).create();
                LinearLayout line = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_goods_search, null);
                dialog.setView(line);
                dialog.show();
                //初始化控件
                Button btn = line.findViewById(R.id.search_bt_dialog);
                final EditText edGoodsName = line.findViewById(R.id.goods_name_ed_dialog);
                final TextView tvAmount = line.findViewById(R.id.amount_tv_dialog);
                final TextView tvId = line.findViewById(R.id.id_tv_dialog);
                tvAmount.setVisibility(View.INVISIBLE);
                tvId.setVisibility(View.INVISIBLE);
                //点击查询
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String goodsName = edGoodsName.getText().toString().trim();
                        if (goodsName.isEmpty()) {
                            Toast.makeText(GoodsListActivity.this, "请输入商品名称", Toast.LENGTH_SHORT).show();
                        } else {
                            Goods g = smb.searchGoods(goodsName);
                            //判断用户是否存在
                            if (goodsName.equals(g.getGoodsName())) {
                                tvAmount.setText("数量：" + g.getAmount());
                                tvId.setText("编号：" + g.getId());
                                tvAmount.setVisibility(View.VISIBLE);
                                tvId.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(GoodsListActivity.this, "该商品不存在", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
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
                        if (tempGoods.getGoodsName() != null) {
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
                        if (tempGoods3.getGoodsName() != null) {//如果商品存在
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
            case R.id.exit_bt://退出按钮
                finish();
                break;
        }
    }
    public void updateList(){
        final List<Goods> goodsList=smb.getAllGoods();
        goodsAdapter=new GoodsAdapter(this,goodsList);
        lvGoods.setAdapter(goodsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //从数据库中查询所有商品信息
        final List<Goods> goodsList = smb.getAllGoods();

        //绑定适配器
        goodsAdapter = new GoodsAdapter(this, goodsList);
        lvGoods.setAdapter(goodsAdapter);
        //商品管理员才有修改和删除权限
        if(power==2){
            Toast.makeText(this,"您可以进行出入库操作",Toast.LENGTH_SHORT).show();
        }else{
            //列表子项的点击事件
            lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(GoodsListActivity.this, GoodsOperatorActivity.class);
                    Goods g = goodsList.get(position);//获取商品对象
                    Bundle bundle = new Bundle();//创建bundle
                    bundle.putSerializable("goods", g);//放入键值对
                    intent.putExtra("goods", bundle);//再放入intent
                    startActivity(intent);//跳转活动
                    finish();
                }
            });
        }
    }

}
