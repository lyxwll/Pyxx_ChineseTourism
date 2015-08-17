package com.pyxx.chinesetourism.fragment;

import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.activity.more.MainTopMoreActivity;
import com.pyxx.chinesetourism.custom.PagerSlidingTabStrip;
import com.pyxx.chinesetourism.custom.ZoomOutPageTransformer;
import com.pyxx.chinesetourism.view.HeaderClickListener;
import com.pyxx.chinesetourism.view.HeaderView;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 旅游景点 列表
 * 
 * @author wll
 */
public class ThreeFragment extends Fragment implements HeaderClickListener {

	HeaderView headerView;

	private ViewPager contentPager;// 滑动页面
	private mPagerAdapter adapter;
	private PagerSlidingTabStrip tabs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_five_layout, null);

		headerView = (HeaderView) view.findViewById(R.id.five_header);
		headerView.setTitle("国家地理");
		headerView.setHeaderClickListener(this);
		headerView.hiddenBack();
		headerView.showOpt();

		setPager(view);

		return view;
	}

	public void setPager(View view) {
		contentPager = (ViewPager) view.findViewById(R.id.content_pager);
		adapter = new mPagerAdapter(getActivity().getSupportFragmentManager());
		contentPager.setAdapter(adapter);
		contentPager.setOffscreenPageLimit(2);
		contentPager.setPageTransformer(true, new ZoomOutPageTransformer());
		tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		tabs.setTextColorResource(R.color.light_gray_text);
		tabs.setDividerColorResource(R.color.common_list_divider);
		tabs.setIndicatorColorResource(R.color.orange);
		tabs.setSelectedTextColorResource(R.color.orange);
		tabs.setViewPager(contentPager);
	}

	/**
	 * 滑动页面的Adapter
	 * 
	 * @author wll
	 */
	private class mPagerAdapter extends FragmentStatePagerAdapter {

		private String Title[] = { "最美地方", "最热景点", "最热城市" };

		public mPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			int[] id = { 2, 3, 4 }; // 最美地方 = 2，最热景点 = 3 ，最热城市 = 4
			// 返回要显示的列表
			return new ThreeListFragment(id[arg0]);
		}

		@Override
		public int getCount() {
			return Title.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return Title[position];
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ThreeFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ThreeFragment");
	}

	@Override
	public void onHeaderClick(View v, int which) {
		switch (which) {
		case HeaderView.BACK_POSITION:
			this.getActivity().finish();
			break;
		case HeaderView.OPT_POSITION:
			Intent intent = new Intent(getActivity(), MainTopMoreActivity.class);
			startActivity(intent);
			break;
		}
	}

}
