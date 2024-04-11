package njust.dzh.warehouse.activity;

import android.content.DialogInterface;
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


import java.util.List;

import njust.dzh.warehouse.R;
import njust.dzh.warehouse.adapter.UserAdapter;
import njust.dzh.warehouse.database.DBHelper;
import njust.dzh.warehouse.entity.User;

/*管理员的主活动，显示用户列表*/
public class UserListActivity extends AppCompatActivity implements View.OnClickListener{
    //声明变量
    private Button btInsert;
    private Button btSelect;
    private Button btExit;
    private ListView lvUser;
    private UserAdapter userAdapter;
    private DBHelper smb;
    private AlertDialog alertDialog=null;
    private AlertDialog.Builder builder=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        initView();
    }

    //绑定控件和设置监听器
    public void initView(){
        btInsert=findViewById(R.id.insert_bt);
        btSelect=findViewById(R.id.select_bt);
        btExit=findViewById(R.id.exit_bt);
        lvUser=findViewById(R.id.user_lv);
        smb=new DBHelper(this);
        btSelect.setOnClickListener(this);
        btInsert.setOnClickListener(this);
        btExit.setOnClickListener(this);
    }

    @Override//按钮点击事件
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.insert_bt://插入用户
                Intent intent=new Intent(UserListActivity.this,UserInsertActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.select_bt://查询用户
                //自定义对话框
                final AlertDialog dialog=new AlertDialog.Builder(this).create();
                LinearLayout line=(LinearLayout) getLayoutInflater().inflate(R.layout.dialog_user_search,null);
                //设置视图
                dialog.setView(line);
                dialog.show();
                //初始化控件
                Button btn=line.findViewById(R.id.search_bt_dialog);
                final EditText edUsername=line.findViewById(R.id.username_ed_dialog);
                //点击查询
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username=edUsername.getText().toString().trim();
                        if(username.isEmpty()){
                            Toast.makeText(UserListActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                        }else{
                            //判断用户是否存在
                            if(smb.searchUser(username).getUsername()==null){
                                Toast.makeText(UserListActivity.this,"该用户不存在",Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent1=new Intent(UserListActivity.this,UserInfoActivity.class);
                                intent1.putExtra("username",username);
                                startActivity(intent1);
                                finish();//销毁活动
                                dialog.dismiss();//销毁对话框
                            }
                        }
                    }
                });
                break;
            case R.id.exit_bt://退出
                builder =new AlertDialog.Builder(this);
                alertDialog=builder.setIcon(R.drawable.alert)
                        .setTitle("退出提示")
                        .setMessage("您确定要退出系统吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        }).create();
                alertDialog.show();
                break;
        }
    }

    @Override
    protected void onResume() {
        //重新运行时调用
        super.onResume();
        //从数据库中查询所有用户信息
        final List<User> users=smb.getAllUsers();

        //加载适配器
        userAdapter=new UserAdapter(this,users);
        lvUser.setAdapter(userAdapter);

        //点击事件
        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u=users.get(position);//获取该用户
                if(u.getPower()==0){//判断权限
                    Toast.makeText(UserListActivity.this,"超级管理员不允许修改",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(UserListActivity.this,UserOperatorActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("user",u);//bundle存储序列化信息
                    intent.putExtra("user",bundle);//intent再发送bundle信息
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}
