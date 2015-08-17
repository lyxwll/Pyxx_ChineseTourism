package com.pyxx.chinesetourism.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.activity.TwoDetailActivity;
import com.pyxx.chinesetourism.activity.more.MainTopMoreActivity;
import com.pyxx.chinesetourism.adapter.HotelBookAdapter;
import com.pyxx.chinesetourism.bean.BookingBean;
import com.pyxx.chinesetourism.myhttp.AsyncHttpResponseHandler;
import com.pyxx.chinesetourism.myhttp.HttpClientUtils;
import com.pyxx.chinesetourism.myhttp.HttpParams;
import com.pyxx.chinesetourism.view.HeaderClickListener;
import com.pyxx.chinesetourism.view.HeaderView;
import com.pyxx.chinesetourism.view.LMListView;
import com.pyxx.chinesetourism.view.LMListView.LMListViewListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 酒店预定 列表
 * 
 * @author wll
 */
public class TwoFragment extends Fragment implements OnItemClickListener,
		HeaderClickListener, LMListViewListener {

	private HeaderView headerView;
	private HotelBookAdapter adapter;
	BookingBean bookBean;
	private ArrayList<BookingBean> infoList = new ArrayList<BookingBean>();
	private static final int RECEIVE_DATA = 0X123;

	// 集合中最大可包含的页码数量
	// 集合中最大可包含的数据量为 MAX_CONTAINS_PAGE * pageSize
	private LMListView lmListView;
	private static final int MAX_CONTAINS_PAGE = 10;
	private boolean isNew = true;
	int page = 0;
	private int pageCount = 0;
	private int containgPage = 0;
	private int action = -1;
	private int pageSize = 5;
	private String refreshTag = "RefreshTag";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_one_layout, null);

		headerView = (HeaderView) view.findViewById(R.id.one_header);
		headerView.setTitle("酒店预定");
		headerView.setHeaderClickListener(this);
		headerView.hiddenBack();
		headerView.showOpt();

		lmListView = (LMListView) view.findViewById(R.id.one_listview);
		lmListView.setRefreshTag(refreshTag);
		lmListView.setPullLoadEnable(true);
		lmListView.setPullRefreshEnable(true);
		if (getActivity() != null) {
			adapter = new HotelBookAdapter(getActivity(), bookBean, infoList);
		}
		lmListView.setAdapter(adapter);
		lmListView.setLMListViewListener(this);
		lmListView.setOnItemClickListener(this);
		adapter.notifyDataSetChanged();

		return view;
	}

	@Override
	public void onHeaderClick(View v, int which) {
		switch (v.getId()) {
		case R.id.header_back_btn:
			TwoFragment.this.getActivity().finish();
			break;
		case R.id.header_more_img:
			Intent intent = new Intent(getActivity(), MainTopMoreActivity.class);
			startActivity(intent);
			break;
		}
	}

	/**
	 * 获取数据
	 */
	public void showData() {
		String path = "http://api.tcshenghuo.org:9999/";
		String url = "Zglygg/in/list!seller?";
		HttpParams params = new HttpParams();
		params.put("seller.menuId", 11);
		HttpClientUtils.getInstance().get(path, url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						// System.out.println("酒店预定返回:" + jsonObject);
						if (jsonObject != null
								&& jsonObject.optInt("code", 0) == 1) {
							pageCount = jsonObject.optInt("pageCount", 0);
							JSONArray jsonArray = jsonObject
									.optJSONArray("lists");
							ArrayList<BookingBean> list = new ArrayList<BookingBean>();
							if (jsonArray != null && jsonArray.length() > 0) {
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject object = jsonArray
											.optJSONObject(i);
									BookingBean bean = new BookingBean();
									bean.addTime = object.optString("addTime",
											"");
									bean.address = object.optString("address",
											"");
									bean.sellerBrief = object.optString(
											"sellerBrief", "");
									bean.logo = object.optString("logo", "");
									bean.lat = object.optDouble("lat", 0);
									bean.lng = object.optDouble("lng", 0);
									bean.name = object.optString("name", "");
									bean.productPrice = object.optString(
											"productPrice", "");
									bean.productBrief = object.optString(
											"productBrief", "");
									bean.tel = object.optString("tel", "");
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

	Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case RECEIVE_DATA:
				lmListView.stopRefresh();
				lmListView.stopLoadMore();
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"MM-dd HH:mm:ss", Locale.getDefault());// 可以方便地修改日期格式
				String time = dateFormat.format(calendar.getTime());
				lmListView.setRefreshTime(time);
				if (TwoFragment.this.getActivity() != null
						&& !TwoFragment.this.getActivity().isFinishing()
						&& lmListView != null) {
					lmListView.stopLoadMore();
					lmListView.stopRefresh();
					@SuppressWarnings("unchecked")
					ArrayList<BookingBean> list = (ArrayList<BookingBean>) msg.obj;
					if (isNew) { // 重新加载
						infoList.clear();
						infoList.addAll(list);
						adapter.notifyDataSetChanged();
						containgPage = 1;
					} else {
						int count = 0;
						if (action == 0) { // 加载上一页
							if (containgPage >= MAX_CONTAINS_PAGE) {
								// 移走最后一页
								int size = infoList.size();
								if (infoList.size() > pageSize) {
									for (int i = 0; i < pageSize; i++) {
										infoList.remove(size - 1);
										size--;
										count++;
									}
								}
							} else {
								containgPage++;
							}
							infoList.addAll(0, list); // 将前一页加载到最前面
							adapter.notifyDataSetChanged();
							if (count > 0) {
								lmListView.setSelectionFromTop(count, 0);
							}
						} else if (action == 1) { // 加载下一页
							if (containgPage >= MAX_CONTAINS_PAGE) {
								// 移走最前一页
								if (infoList.size() > pageSize) {
									for (int i = 0; i < pageSize; i++) {
										infoList.remove(0);
										count++;
									}
								}
							} else {
								containgPage++; // 未超过最大页码，所包含的页数加1
							}
							infoList.addAll(list);
							adapter.notifyDataSetChanged();
							if (count > 0) {
								lmListView.setSelectionFromTop(infoList.size()
										- list.size() - count, 0);
							}
						}
					}
				}
				break;
			}
			return true;
		}
	});

	/**
	 * 列表点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> adpterView, View view, int position,
			long id) {
		Object object = adpterView.getAdapter().getItem(position);
		if (object instanceof BookingBean) {
			BookingBean bean = (BookingBean) object;
			Intent intent = new Intent(getActivity(), TwoDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("bookBean", bean);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	// 下拉刷新
	@Override
	public void onLoadLast() {
		action = 0;
		if (page > 0) {// 加载前一页
			if (containgPage >= MAX_CONTAINS_PAGE) {
				isNew = false;
				page--;
				showData();
			} else {
				lmListView.setIsFirstPage();
				lmListView.stopRefresh();
			}
		} else if (page == 0) {
			lmListView.setIsFirstPage();
			lmListView.stopRefresh();
		}
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		action = 1;
		isNew = false; // 并不是重新加载
		if (page < pageCount - 1) {
			page++;
			showData();
		} else {
			lmListView.stopLoadMore();
			lmListView.setNoMoreData();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		showData();
	}

}
