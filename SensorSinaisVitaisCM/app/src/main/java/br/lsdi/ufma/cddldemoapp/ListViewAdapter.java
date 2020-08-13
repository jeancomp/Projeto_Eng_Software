package br.lsdi.ufma.cddldemoapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    Activity activity;
    List<String> list;

    public ListViewAdapter(Activity activity, List<String> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (view == null) {
            view = inflater.inflate(R.layout.listview, null);
            viewHolder = new ViewHolder();
            viewHolder.messageId = view.findViewById(R.id.message_id);
            viewHolder.messageValue = view.findViewById(R.id.message_value);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.messageId.setText(String.valueOf(i));
        viewHolder.messageValue.setText(list.get(i));

        return view;
    }

    private class ViewHolder {
        TextView messageId;
        TextView messageValue;
    }
}
