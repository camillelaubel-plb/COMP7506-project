package njust.dzh.warehouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

import njust.dzh.warehouse.R;
import njust.dzh.warehouse.adapter.UserAdapter;
import njust.dzh.warehouse.database.DBHelper;
import njust.dzh.warehouse.entity.User;


public class UserListActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btInsert;
    private EditText edUsername;
    private Button btSearch;
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

    public void initView(){
        edUsername=findViewById(R.id.username_ed);
        btInsert=findViewById(R.id.insert_bt);
        btSearch=findViewById(R.id.search_bt);
        lvUser=findViewById(R.id.user_lv);

        smb=new DBHelper(this);

        btSearch.setOnClickListener(this);
        btInsert.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // Add new user
            case R.id.insert_bt:
                Intent intentInsert=new Intent(UserListActivity.this,UserInsertActivity.class);
                startActivity(intentInsert);
                break;
            case R.id.search_bt:
                // On search, filter out the list of users
                List<User> users=smb.getAllUsers();
                String username=edUsername.getText().toString().trim();

                if(username.isEmpty()){
                    userAdapter=new UserAdapter(this,users);
                    lvUser.setAdapter(userAdapter);
                }else{
                    List<User> filteredUsers = getFilteredList(users, username);

                    userAdapter=new UserAdapter(this,filteredUsers);
                    lvUser.setAdapter(userAdapter);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Show all users from DB
        final List<User> users=smb.getAllUsers();

        userAdapter=new UserAdapter(this,users);
        lvUser.setAdapter(userAdapter);

        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Fetch user
                User u=users.get(position);
                if(u.getPower()==0){
                    Toast.makeText(UserListActivity.this,"Cannot update Admin users",Toast.LENGTH_SHORT).show();
                }else{
                    final AlertDialog dialog=new AlertDialog.Builder(UserListActivity.this).create();
                    LinearLayout line=(LinearLayout) getLayoutInflater().inflate(R.layout.dialog_user_edit,null);

                    dialog.setView(line);
                    dialog.show();

                    Button btUpdate=findViewById(R.id.update_bt);
                    Button btDelete=findViewById(R.id.delete_bt);

                    final EditText edPassword=findViewById(R.id.password_ed);
                    final EditText edUsername=line.findViewById(R.id.username_ed);
                    final Spinner spPower=findViewById(R.id.power_sp);

//                    btUpdate.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            u.setPassword(edPassword.getText().toString().trim());
//                            smb.updateUserInfo(u);
//                            Toast.makeText(UserListActivity.this,"Successfully Modified",Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                    });
//
//                    btDelete.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            smb.deleteUser(u.getUsername());
//                            Toast.makeText(UserListActivity.this,"Successfully Deleted",Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                    });
                }
            }
        });
    }

    private List<User> getFilteredList(List<User> users, String usernameToCheck) {
        List<User> userList = new ArrayList<User>();
        for(User user : users) {
            if (user.getUsername().contains(usernameToCheck)) {
                userList.add(user);
            }
        }
        return userList;
    }
}
