package cn.edu.nju.software.gof.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.edu.nju.software.gof.activity.R;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class DynamicItemizedOverlay<T extends OverlayItem> extends
		ItemizedOverlay<T> {

	private ArrayList<T> items = null;
	private MapView mapView = null;
	private View root = null;
	private Drawable defaultMarker;

	public DynamicItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker));
		this.defaultMarker = defaultMarker;
		this.mapView = mapView;
		items = new ArrayList<T>();
		populate();
	}

	public void updateMap() {
		populate();
	}

	public void clearMap() {
		items.clear();
	}

	public static Drawable adjustBound(Drawable marker) {
		return boundCenterBottom(marker);
	}

	public void addNewItem(T item) {
		items.add(item);
		populate();
	}

	public void removeItem(int index) {
		items.remove(index);
		populate();
	}

	@Override
	protected T createItem(int index) {
		return items.get(index);
	}

	@Override
	public int size() {
		return items.size();
	}

	@Override
	protected boolean onTap(int index) {
		mapView.removeAllViews();
		OverlayItem item = getItem(index);
		Context context = mapView.getContext();
		if (root == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			root = inflater.inflate(R.layout.map_item, null);
		}
		TextView text = (TextView) root.findViewById(R.id.title);
		text.setText(item.getTitle());
		MapView.LayoutParams screenLP;
		Drawable drawable = item.getMarker(OverlayItem.ITEM_STATE_FOCUSED_MASK);
		if (drawable == null) {
			drawable = defaultMarker;
		}
		Projection projection = mapView.getProjection();
		Point bottomPoint = projection.toPixels(item.getPoint(), null);
		bottomPoint.offset(0, -drawable.getMinimumHeight());
		GeoPoint geoPoint = projection.fromPixels(bottomPoint.x, bottomPoint.y);
		screenLP = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
				MapView.LayoutParams.WRAP_CONTENT, geoPoint,
				MapView.LayoutParams.BOTTOM_CENTER);
		mapView.addView(root, screenLP);
		return true;
	}
	// @Override
	// public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	// super.draw(canvas, mapView, shadow);
	// if (shadow) {
	// mapView.removeAllViews();
	// for (int index = 0; index < size(); index++) {
	// OverlayItem item = getItem(index);
	// TextView text = new TextView(mapView.getContext());
	// text.setText(item.getTitle());
	// MapView.LayoutParams screenLP;
	// GeoPoint geoPoint = item.getPoint();
	// screenLP = new MapView.LayoutParams(
	// MapView.LayoutParams.WRAP_CONTENT,
	// MapView.LayoutParams.WRAP_CONTENT, geoPoint,
	// MapView.LayoutParams.CENTER_HORIZONTAL);
	// mapView.addView(text, screenLP);
	// }
	//
	// }
	// }
}
