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

        // Set Event Listeners
        btExit.setOnClickListener(this);
        btRegister.setOnClickListener(this);
    }
    // Event Binding
    public void initView(){
        edUsername=findViewById(R.id.username_ed);
        edPassword=findViewById(R.id.password_ed);
        btRegister=findViewById(R.id.register_bt);
        btExit=findViewById(R.id.exit_bt);
        smb=new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            // Register button click event
            case R.id.register_bt:
                String username=edUsername.getText().toString().trim();
                String password=edPassword.getText().toString().trim();

                // Username and Password cannot be empty
                // Show a toast if empty
                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Please enter Username and Password",Toast.LENGTH_SHORT).show();
                }else if(smb.searchUser(username).getUsername()!=null){
                    Toast.makeText(RegisterActivity.this,"Account {username} already exists",Toast.LENGTH_SHORT).show();
                }else{
                    User s=new User(username,password,0);
                    // Add user to DB
                    smb.insertUser(s);
                    Intent intent=new Intent(RegisterActivity.this, UserListActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;

            // Return to login
            case R.id.exit_bt:
                finish();
                break;
        }
    }
}
