package njust.dzh.warehouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

import njust.dzh.warehouse.R;
import njust.dzh.warehouse.adapter.UserAdapter;
import njust.dzh.warehouse.database.DBHelper;
import njust.dzh.warehouse.entity.User;

/*用户查询的活动*/
public class UserInfoActivity extends AppCompatActivity {
    //声明变量
    private Button btExit;
    private ListView lvUser;
    private UserAdapter userAdapter;
    private Intent intent;
    private DBHelper smb;
    private String username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        intent=getIntent();
        username=intent.getStringExtra("username");
        initView();
    }
    //绑定控件和设置监听器
    public void initView(){
        lvUser=findViewById(R.id.user_lv);
        btExit=findViewById(R.id.exit_bt);
        smb=new DBHelper(this);
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserInfoActivity.this, UserListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user=smb.searchUser(username);
        final List<User> users=new ArrayList<>();
        users.add(user);
        //绑定适配器
        userAdapter=new UserAdapter(this,users);
        lvUser.setAdapter(userAdapter);
    }
}
