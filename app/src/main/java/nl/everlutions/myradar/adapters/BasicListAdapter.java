package nl.everlutions.myradar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nl.everlutions.myradar.R;
import nl.everlutions.myradar.activities.BaseActivity;


public class BasicListAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final BaseActivity mCtx;
    private final ArrayList<String> mSampleList;

    public BasicListAdapter(BaseActivity context, ArrayList<String> sampleList) {
        mCtx = context;
        mInflater = LayoutInflater.from(context);
        mSampleList = sampleList;
    }

    @Override
    public int getCount() {
        return mSampleList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_sample_item, parent,
                    false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.sample_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(mSampleList.get(position));
        return convertView;
    }

    private class ViewHolder {
        public TextView tvName;
    }

}
