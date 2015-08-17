package com.pyxx.chinesetourism.adapter;

import java.util.ArrayList;

import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.bean.InfoBean;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 旅游景点Adapter
 * 
 * @author wll
 */
public class AttractionAdapter extends BaseAdapter {

	private ArrayList<InfoBean> mArrayList;
	private InfoBean bean;
	private LayoutInflater inflater;
	private Context mContext;

	public AttractionAdapter(Context context, InfoBean bean,
			ArrayList<InfoBean> arrayList) {
		this.mContext = context;
		this.bean = bean;
		this.mArrayList = arrayList;
		inflater = LayoutInflater.from(context);
	}

	public void setList(ArrayList<InfoBean> arrayList) {
		this.mArrayList = arrayList;
	}

	@Override
	public int getCount() {
		if (mArrayList != null) {
			return mArrayList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mArrayList != null && position < mArrayList.size()) {
			return mArrayList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.adapter_attractions, null);
			holder.logo = (ImageView) convertView
					.findViewById(R.id.three_ico_img);
			holder.name = (TextView) convertView.findViewById(R.id.three_name);
			holder.detail = (TextView) convertView
					.findViewById(R.id.three_detail);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (position < mArrayList.size()) {
			InfoBean bean = mArrayList.get(position);
			holder.name.setText(bean.title);
			holder.detail.setText(bean.content);
			if (bean.logo != null) {
				Picasso.with(mContext).load(bean.logo).resize(110, 80)
						.placeholder(R.drawable.default_img).into(holder.logo);
			} else {
				holder.logo.setBackgroundResource(R.drawable.default_img);
			}
		}
		return convertView;
	}

	class Holder {
		ImageView logo;
		TextView name;
		TextView detail;
	}

}
