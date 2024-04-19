package edu.hku.comp2506.warehouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.hku.comp2506.warehouse.R;
import edu.hku.comp2506.warehouse.entity.User;

public class UserAdapter extends BaseAdapter {
    List<User> user;
    Context context;

    public UserAdapter(Context context, List<User> user) {
        this.context = context;
        this.user = user;
    }

    @Override
    public int getCount() {
        return user.size();
    }

    @Override
    public Object getItem(int position) {
        return user.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        }
        TextView tvUsername = convertView.findViewById(R.id.username_tv);
        TextView tvPassword = convertView.findViewById(R.id.password_tv);
        TextView tvPower = convertView.findViewById(R.id.power_tv);

        tvUsername.setText(user.get(position).getUsername());
        tvPassword.setText(user.get(position).getPassword());

        if(user.get(position).getPower()==0){
            tvPower.setText("Admin");
        }else if(user.get(position).getPower()==1){
            tvPower.setText("Product Manager");
        }else if(user.get(position).getPower()==2){
            tvPower.setText("User");
        }

        return convertView;
    }
}