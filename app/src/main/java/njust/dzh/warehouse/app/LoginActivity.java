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

        // Set Event Listeners
        btLogin.setOnClickListener(this);
        btRegister.setOnClickListener(this);
    }
    // Event Binding
    public void initView() {
        edUsername = findViewById(R.id.username_ed);
        edPassword = findViewById(R.id.password_ed);
        btRegister = findViewById(R.id.register_bt);
        btLogin = findViewById(R.id.login_bt);
        smb = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Login button click event
            case R.id.login_bt:
                String username = edUsername.getText().toString().trim();
                String password = edPassword.getText().toString().trim();

                // Username and Password cannot be empty
                // Show a toast if empty
                if (username.isEmpty()||password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter Username and Password", Toast.LENGTH_SHORT).show();
                    break;
                }

                // Check DB for User info
                User u = smb.searchUser(username);
                // Match Password
                if (u.getUsername()!=null&&password.equals(u.getPassword())) {
                    // If user is admin, show UserListActivity
                    if (u.getPower()==0) {
                        Intent intent = new Intent(LoginActivity.this, UserListActivity.class);
                        startActivity(intent);
                        // Welcome toast for admin user
                        Toast.makeText(LoginActivity.this, String.format("Welcome admin user: %s", username), Toast.LENGTH_SHORT).show();
                    }else {
                        // If you are a Product Manager or Warehouse Manager, show GoodsListActivity
                        Intent intent = new Intent(LoginActivity.this, GoodsListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", u);
                        intent.putExtra("user",bundle);
                        startActivity(intent);
                        if(u.getPower()==1){
                            // Welcome toast for product manager user
                            Toast.makeText(LoginActivity.this, String.format("Welcome Product Manager: %s", username), Toast.LENGTH_SHORT).show();
                        }else{
                            // Welcome toast for user
                            Toast.makeText(LoginActivity.this, String.format("Welcome user: %s", username), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Otherwise, the account does not exist or password does not match
                    Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                }
                break;

            // Register button click event
            case R.id.register_bt:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}

