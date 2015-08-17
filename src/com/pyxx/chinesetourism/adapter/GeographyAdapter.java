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
 * 国家地理Adapter
 * 
 * @author wll
 */
public class GeographyAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<InfoBean> infoList;
	private Context mContext;

	public GeographyAdapter(Context context, ArrayList<InfoBean> infoList) {
		this.mContext = context;
		this.infoList = infoList;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (infoList != null) {
			return infoList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (infoList != null && position < infoList.size()) {
			return infoList.get(position);
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
			convertView = inflater.inflate(R.layout.adapter_geography, null);
			holder.logo = (ImageView) convertView
					.findViewById(R.id.five_ico_img);
			holder.name = (TextView) convertView.findViewById(R.id.five_name);
			holder.detail = (TextView) convertView
					.findViewById(R.id.five_detail);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (position < infoList.size()) {
			InfoBean bean = infoList.get(position);
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
