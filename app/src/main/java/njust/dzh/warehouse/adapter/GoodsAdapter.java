package njust.dzh.warehouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import njust.dzh.warehouse.R;
import njust.dzh.warehouse.entity.Goods;

public class GoodsAdapter extends BaseAdapter {
    List<Goods> goods;//列表
    Context context;//上下文

    public GoodsAdapter(Context context, List<Goods> goods) {
        this.context = context;
        this.goods = goods;
    }

    @Override
    public int getCount() {
        return goods.size();
    }

    @Override
    public Object getItem(int position) {
        return goods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.goods_item,parent,false);
        }
        //绑定控件
        TextView tvId= convertView.findViewById(R.id.id_tv);
        TextView tvGoodsName =  convertView.findViewById(R.id.goods_name_tv);
        TextView tvAmount= convertView.findViewById(R.id.amount_tv);
        //设置信息
        tvId.setText(String.valueOf(goods.get(position).getId()));
        tvGoodsName.setText(goods.get(position).getGoodsName());
        tvAmount.setText(String.valueOf(goods.get(position).getAmount()));

        return convertView;
    }
}
