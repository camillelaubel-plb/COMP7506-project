package njust.dzh.warehouse.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import njust.dzh.warehouse.R;
import njust.dzh.warehouse.activity.UserListActivity;
import njust.dzh.warehouse.database.DBHelper;
import njust.dzh.warehouse.entity.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    //声明布局中用到的控件变量
    private EditText edUsername;
    private EditText edPassword;
    private Button btRegister;
    private Button btExit;
    private DBHelper smb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        //设置监听器
        btExit.setOnClickListener(this);
        btRegister.setOnClickListener(this);
    }
    //初始化绑定控件
    public void initView(){
        edUsername=findViewById(R.id.username_ed);
        edPassword=findViewById(R.id.password_ed);
        btRegister=findViewById(R.id.register_bt);
        btExit=findViewById(R.id.exit_bt);
        smb=new DBHelper(this);
    }
    //注册按钮和退出按钮
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register_bt:
                String username=edUsername.getText().toString().trim();
                String password=edPassword.getText().toString().trim();
                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"账号和密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(smb.searchUser(username).getUsername()!=null){
                    Toast.makeText(RegisterActivity.this,"该账号已存在",Toast.LENGTH_SHORT).show();
                }else{
                    User s=new User(username,password,0);
                    smb.insertUser(s);//将该用户信息插入数据库
                    Intent intent=new Intent(RegisterActivity.this, UserListActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.exit_bt:
                //返回到登录界面
                finish();
                break;
        }
    }
}
