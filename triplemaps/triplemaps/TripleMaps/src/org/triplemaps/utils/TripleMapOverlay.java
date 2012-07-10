package org.triplemaps.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class TripleMapOverlay extends ItemizedOverlay<OverlayItem>{

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>(0);
	private Context mContext;
	private Drawable marker;

	
	public TripleMapOverlay(Drawable defaultMarker, Context context) {
		  super(defaultMarker);
		  mContext = context;
		  this.marker = defaultMarker;
	}
	public TripleMapOverlay(Drawable defaultMarker) {
		super(defaultMarker);
	}

	@Override
	protected OverlayItem createItem(int arg0) {
		return mOverlays.get(arg0);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public void addOverlay(OverlayItem overlayItem){
		mOverlays.add(overlayItem);
		populate();
	}
	
	public void addOverlay(List<OverlayItem> listOverlayItems){
		mOverlays.addAll(listOverlayItems);
		populate();
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		boundCenterBottom(marker);
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}
}
