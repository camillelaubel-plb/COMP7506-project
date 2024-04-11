package njust.dzh.warehouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import njust.dzh.warehouse.R;
import njust.dzh.warehouse.database.DBHelper;
import njust.dzh.warehouse.entity.User;

/*添加用户的活动*/
public class UserInsertActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
    //声明变量
    private EditText edUsername;
    private EditText edPassword;
    private Spinner spPower;
    private Button btInsert;
    private Button btBack;
    private User user;
    private DBHelper smb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);
        initView();
    }
    //初始化控件和设置监听器
    public void initView(){
        edUsername=findViewById(R.id.username_ed);
        edPassword=findViewById(R.id.password_ed);
        spPower=findViewById(R.id.power_sp);
        btInsert=findViewById(R.id.insert_bt);
        btBack=findViewById(R.id.exit_bt);
        user=new User();//先声明一个空对象
        smb=new DBHelper(this);
        btInsert.setOnClickListener(this);
        btBack.setOnClickListener(this);
        //获取修改后的下拉列表参数
        spPower.setOnItemSelectedListener(this);
        user=new User();
    }
    @Override//按钮点击事件
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.insert_bt:
                String username=edUsername.getText().toString().trim();
                String password=edPassword.getText().toString().trim();
                user.setUsername(username);
                user.setPassword(password);
                smb.insertUser(user);
                Toast.makeText(UserInsertActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                //添加完后回到刚刚的活动
                backActivity();
                break;
            case R.id.exit_bt:
                //直接退出
                backActivity();
                break;
        }
    }
    //跳转，并销毁
    public void backActivity(){
        Intent intent=new Intent(UserInsertActivity.this, UserListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        user.setPower(position+1);//下标从0开始，所以加1
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
