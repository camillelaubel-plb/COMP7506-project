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


/*对用户信息进行修改和删除的活动*/
public class UserOperatorActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
    //声明变量
    private EditText edUsername;
    private EditText edPassword;
    private Spinner spPower;
    private Button btUpdate;
    private Button btDelete;
    private Intent intent;
    private User user;
    private DBHelper smb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_operator);
        intent=getIntent();//先获得intent
        Bundle bundle=intent.getBundleExtra("user");//再取出bundle
        user= (User) bundle.getSerializable("user");//最后取出键值对
        initView();
    }
    public void initView(){
        //初始化控件
        edUsername=findViewById(R.id.username_ed);
        edPassword=findViewById(R.id.password_ed);
        spPower=findViewById(R.id.power_sp);
        btUpdate=findViewById(R.id.update_bt);
        btDelete=findViewById(R.id.delete_bt);
        smb=new DBHelper(this);
        btDelete.setOnClickListener(this);
        btUpdate.setOnClickListener(this);
        //设置控件参数
        edUsername.setText(user.getUsername());
        edUsername.setEnabled(false);//去掉点击时编辑框下面横线:
        edUsername.setFocusable(false);//键盘点击无效
        edUsername.setFocusableInTouchMode(false);//触摸无效
        edPassword.setText(user.getPassword());
        //根据用户权限设置下拉框索引
        spPower.setSelection(user.getPower()-1);
        //设置子项监听器
        spPower.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_bt://更新操作
                user.setPassword(edPassword.getText().toString().trim());
                smb.updateUserInfo(user);
                Toast.makeText(UserOperatorActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                backActivity();
                break;
            case R.id.delete_bt://删除操作
                smb.deleteUser(user.getUsername());
                Toast.makeText(UserOperatorActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                backActivity();
                break;
        }
    }

    //跳转，并销毁
    public void backActivity(){
        Intent intent=new Intent(UserOperatorActivity.this, UserListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        user.setPower(position+1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
