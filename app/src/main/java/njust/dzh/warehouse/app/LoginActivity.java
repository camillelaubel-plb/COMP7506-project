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
import njust.dzh.warehouse.activity.GoodsListActivity;
import njust.dzh.warehouse.activity.UserListActivity;
import njust.dzh.warehouse.database.DBHelper;
import njust.dzh.warehouse.entity.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //声明布局中用到的控件变量
    private EditText edUsername;
    private EditText edPassword;
    private Button btRegister;
    private Button btLogin;
    private DBHelper smb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        //按钮监听
        btLogin.setOnClickListener(this);
        btRegister.setOnClickListener(this);
    }
    //绑定控件
    public void initView() {
        edUsername = findViewById(R.id.username_ed);
        edPassword = findViewById(R.id.password_ed);
        btRegister = findViewById(R.id.register_bt);
        btLogin = findViewById(R.id.login_bt);
        smb = new DBHelper(this);
    }

    //监听注册和登录按钮
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bt://登录按钮的点击事件
                String username = edUsername.getText().toString().trim();//获得输入框中的账号
                String password = edPassword.getText().toString().trim();//获得密码
                if (username.isEmpty()||password.isEmpty()) {//如果账号或密码为空
                    Toast.makeText(LoginActivity.this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
                    break;
                }
                User u = smb.searchUser(username);//初始化对象
                if (u.getUsername()!=null&&password.equals(u.getPassword())) {//密码正确
                    if (u.getPower()==0) {//如果权限显示是超级管理员，跳到用户列表的活动
                        Intent intent = new Intent(LoginActivity.this, UserListActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "欢迎超级管理员", Toast.LENGTH_SHORT).show();
                    }else {//如果是商品管理员或出入库管理员，跳到对应的活动
                        Intent intent = new Intent(LoginActivity.this, GoodsListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", u);
                        intent.putExtra("user",bundle);
                        startActivity(intent);
                        if(u.getPower()==1){
                            Toast.makeText(LoginActivity.this, "欢迎商品管理员", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "欢迎出入库人员", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {//对象不存在即账号错误，密码不匹配则密码错误
                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_bt://注册按钮的点击事件
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}

