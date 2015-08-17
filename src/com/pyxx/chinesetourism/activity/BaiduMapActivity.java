package com.pyxx.chinesetourism.activity;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.bean.InfoBean;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * 调用百度地图
 * 
 * @author wll
 */
public class BaiduMapActivity extends Activity {

	MapView mapView = null;
	BaiduMap mBaiduMap;

	InfoBean infoBean;
	static Double latitude;
	static Double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 接收从上一个界面传过来的值
		if (getIntent() != null && getIntent().hasExtra("infoBean")) {
			infoBean = (InfoBean) getIntent().getSerializableExtra("infoBean");
		}

		/**
		 * 注意：在SDK各功能组件使用之前都需要调用
		 * SDKInitializer.initialize(getApplicationContext());
		 * 因此我们建议该方法放在Application的初始化方法中
		 */
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());

		setContentView(R.layout.detail_mymap_layout);
		// 获取地图控件引用
		mapView = (MapView) findViewById(R.id.bmapView);

		if (infoBean != null) {
			latitude = infoBean.lat;
			longitude = infoBean.lng;
		}

		initMap();

	}

	private void initMap() {
		mBaiduMap = mapView.getMap();
		// 设置地图的缩放比例
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
		mBaiduMap.setMapStatus(msu);
		final LatLng CENTER = new LatLng(latitude, longitude);
		// 设置显示当前经纬度位置为中心的地图
		MapStatusUpdate center = MapStatusUpdateFactory.newLatLng(CENTER);
		mBaiduMap.setMapStatus(center);
		// 添加当前位置点
		OverlayOptions ooDot = new DotOptions().center(CENTER).radius(15)
				.color(0xFF0000FF);
		mBaiduMap.addOverlay(ooDot);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView.onResume()，实现地图生命周期管理
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView.onPause()，实现地图生命周期管理
		mapView.onPause();
	}

}
