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

public class UserInsertActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
    private EditText edUsername;
    private EditText edPassword;
    private Spinner spPower;
    private Button btInsert;
    private User user;
    private DBHelper smb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);
        initView();
    }
    public void initView(){
        edUsername=findViewById(R.id.username_ed);
        edPassword=findViewById(R.id.password_ed);
        spPower=findViewById(R.id.power_sp);
        btInsert=findViewById(R.id.insert_bt);

        smb=new DBHelper(this);

        btInsert.setOnClickListener(this);
        spPower.setOnItemSelectedListener(this);
        user=new User();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.insert_bt:
                String username=edUsername.getText().toString().trim();
                String password=edPassword.getText().toString().trim();
                user.setUsername(username);
                user.setPassword(password);
                smb.insertUser(user);
                Toast.makeText(UserInsertActivity.this,"Successfully Added",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        user.setPower(position+1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
