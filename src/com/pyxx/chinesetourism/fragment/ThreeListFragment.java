package com.pyxx.chinesetourism.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.activity.ThreeDetailActivity;
import com.pyxx.chinesetourism.adapter.AttractionAdapter;
import com.pyxx.chinesetourism.adapter.RecommendAdapter;
import com.pyxx.chinesetourism.bean.InfoBean;
import com.pyxx.chinesetourism.myhttp.AsyncHttpResponseHandler;
import com.pyxx.chinesetourism.myhttp.HttpClientUtils;
import com.pyxx.chinesetourism.myhttp.HttpParams;
import com.pyxx.chinesetourism.view.XListView;
import com.pyxx.chinesetourism.view.XListView.IXListViewListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 旅游景点 列表显示
 * 
 * @author wll
 */
@SuppressLint("ValidFragment")
public class ThreeListFragment extends Fragment implements IXListViewListener,
		OnItemClickListener {

	XListView xListView;
	private AttractionAdapter adapter;
	InfoBean infoBean;
	private ArrayList<InfoBean> infoList = new ArrayList<InfoBean>();
	private static final int RECEIVE_DATA = 0X123;

	int page = 0;
	private int pageCount = 0;
	private String refreshTag = "RefreshTag";

	private int channelId; // 最美地方 = 2，最热景点 = 3 ，最热城市 = 4

	public ThreeListFragment() {
	}

	public ThreeListFragment(int channelId) {
		this.channelId = channelId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.five_list_fragment, null);

		xListView = (XListView) view.findViewById(R.id.five_listview);
		xListView.setRefreshTag(refreshTag);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		adapter = new AttractionAdapter(getActivity(), infoBean, infoList);
		xListView.setAdapter(adapter);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(this);

		return view;
	}

	public void showData() {
		String path = "http://api.tcshenghuo.org:9999/";
		String url = "Zglygg/in/list!commodity?";
		HttpParams params = new HttpParams();
		params.put("commodity.menuId", 10);
		params.put("commodity.sort", channelId);
		HttpClientUtils.getInstance().get(path, url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						System.out.println("数据返回:" + jsonObject);
						if (jsonObject != null
								&& jsonObject.optInt("code", 0) == 1) {
							pageCount = jsonObject.optInt("pageCount", 0);
							JSONArray jsonArray = jsonObject
									.optJSONArray("lists");
							ArrayList<InfoBean> list = new ArrayList<InfoBean>();
							if (jsonArray != null && jsonArray.length() > 0) {
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject object = jsonArray
											.optJSONObject(i);
									InfoBean bean = new InfoBean();
									bean.address = object.optString("address",
											"");
									bean.logo = object.optString("logo", "");
									bean.content = object.optString("content",
											"");
									bean.tel = object.optString("tel", "");
									bean.price = object.optString("price", "");
									bean.lat = object.optDouble("lat", 0);
									bean.lng = object.optDouble("lng", 0);
									bean.source = object
											.optString("source", "");
									bean.time = object.optString("time", "");
									bean.title = object.optString("title", "");
									list.add(bean);
								}
							}
							Message message = new Message();
							message.what = RECEIVE_DATA;
							message.obj = list;
							handler.sendMessage(message);
						}
					}
				});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RECEIVE_DATA:
				xListView.stopLoadMore();
				xListView.stopRefresh();
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"MM-dd HH:mm:ss", Locale.getDefault());// 可以方便地修改日期格式
				String time = dateFormat.format(calendar.getTime());
				xListView.setRefreshTime(time);
				ArrayList<InfoBean> list = (ArrayList<InfoBean>) msg.obj;
				if (page == 0) {
					infoList.clear();
				}
				infoList.addAll(list);
				adapter.notifyDataSetChanged();
				break;
			}
		};
	};

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		Object object = adapterView.getAdapter().getItem(position);
		if (object instanceof InfoBean) {
			InfoBean bean = (InfoBean) object;
			Intent intent = new Intent(getActivity(), ThreeDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("infoBean", bean);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	@Override
	public void onRefresh() {
		page = 0;
		showData();
	}

	@Override
	public void onLoadMore() {
		if (page < pageCount - 1) {
			page++;
			showData();
		} else {
			xListView.stopLoadMore();
			xListView.setNoMoreData();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		showData();
	}

}
