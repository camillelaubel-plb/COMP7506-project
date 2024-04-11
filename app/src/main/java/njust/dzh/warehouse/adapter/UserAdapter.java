package njust.dzh.warehouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import njust.dzh.warehouse.R;
import njust.dzh.warehouse.entity.User;

public class UserAdapter extends BaseAdapter {
    List<User> user;//列表
    Context context;//上下文

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
        //绑定控件
        TextView tvUsername = convertView.findViewById(R.id.username_tv);
        TextView tvPassword = convertView.findViewById(R.id.password_tv);
        TextView tvPower = convertView.findViewById(R.id.power_tv);
        //设置信息
        tvUsername.setText(user.get(position).getUsername());
        tvPassword.setText(user.get(position).getPassword());
        //权限设置
        if(user.get(position).getPower()==0){
            tvPower.setText("超级管理员");
        }else if(user.get(position).getPower()==1){
            tvPower.setText("商品管理员");
        }else if(user.get(position).getPower()==2){
            tvPower.setText("出入库人员");
        }

        return convertView;
    }
}