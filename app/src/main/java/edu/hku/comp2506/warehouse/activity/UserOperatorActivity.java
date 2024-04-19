package edu.hku.comp2506.warehouse.activity;

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

import edu.hku.comp2506.warehouse.R;
import edu.hku.comp2506.warehouse.database.DBHelper;
import edu.hku.comp2506.warehouse.entity.User;


public class UserOperatorActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
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
        intent=getIntent();
        Bundle bundle=intent.getBundleExtra("user");
        user= (User) bundle.getSerializable("user");
        initView();
    }
    public void initView(){
        edUsername=findViewById(R.id.username_ed);
        edPassword=findViewById(R.id.password_ed);
        spPower=findViewById(R.id.power_sp);
        btUpdate=findViewById(R.id.update_bt);
        btDelete=findViewById(R.id.delete_bt);

        smb=new DBHelper(this);

        btDelete.setOnClickListener(this);
        btUpdate.setOnClickListener(this);

        // Username cannot be changed
        edUsername.setText(user.getUsername());
        edUsername.setEnabled(false);
        edUsername.setFocusable(false);
        edUsername.setFocusableInTouchMode(false);

        // Fill with user password
        edPassword.setText(user.getPassword());

        // Set Power based on user permission
        spPower.setSelection(user.getPower()-1);
        spPower.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_bt:
                user.setPassword(edPassword.getText().toString().trim());
                smb.updateUserInfo(user);
                Toast.makeText(UserOperatorActivity.this,"Successfully Updated",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.delete_bt:
                smb.deleteUser(user.getUsername());
                Toast.makeText(UserOperatorActivity.this,"Successfully Deleted",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    // Must include to satisfy error
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        user.setPower(position+1);
    }

    // Must include to satisfy error
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}