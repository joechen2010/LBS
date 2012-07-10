package cn.edu.nju.software.gof.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import cn.edu.nju.software.gof.beans.PersonInformationBean;
import cn.edu.nju.software.gof.beans.PlaceCreationBean;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.requests.InformationUtilities;
import cn.edu.nju.software.gof.requests.PlaceUtilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreatNewPlaceActivity extends Activity {
	private static final int IMAGE_PICKER_DIALOG = 0;
	private final static int DATE_DIALOG_ID = 1;
	private final static int WAITTING_DIALOG_ID = 2;
	private final static int UPDATE_SUCCESS = 3;
	private final static int UPDATE_FAILED = 4;
	private static final int FROM_IMAGE = 0;
	private static final int FROM_CAMERA = 1;

	private static final int CROP_IMAGE = 2;

	private Uri uri;

	private ImageView place_image;
	private EditText place_name;
	private EditText place_comment;
	private Button submit;

	private String parentID = null;
	private Double latitude, longitude;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_place);
		//
		getViews();
		registerEventHandler();
		//
		Bundle bundle = getIntent().getExtras();
		parentID = bundle.getString("placeID");
		latitude = bundle.getDouble("latitude");
		longitude = bundle.getDouble("longitude");
	}

	private void getViews() {
		place_name = (EditText) findViewById(R.id.create_place_name);
		place_comment = (EditText) findViewById(R.id.create_place_comment);

		place_image = (ImageView) findViewById(R.id.place_avatar_edit);

		submit = (Button) findViewById(R.id.submit_place);
	}

	private void registerEventHandler() {
		place_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (place_name.getText().toString().equals("(点击添加地点名称)")) {
					place_name.setText("");
				}
			}

		});
		place_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (place_comment.getText().toString().equals("(点击添加你的评论)")) {
					place_comment.setText("");
				}
			}

		});

		place_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(IMAGE_PICKER_DIALOG);
			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(WAITTING_DIALOG_ID);
				//
				MyApplication application = (MyApplication) CreatNewPlaceActivity.this
						.getApplication();
				final String sessionID = (String) application
						.getData("sessionID");

				final String placeName = place_name.getEditableText()
						.toString();

				final String placeDescription = place_comment.getEditableText()
						.toString();

				Drawable avatar = place_image.getDrawable();
				if (avatar != null) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					((BitmapDrawable) avatar).getBitmap().compress(
							Bitmap.CompressFormat.JPEG, 100, out);
					final byte[] image = out.toByteArray();
					(new AsyncTask<Void, Void, Void>() {

						private boolean success = false;

						@Override
						protected Void doInBackground(Void... params) {
							PlaceCreationBean bean = null;
							if (parentID == null) {
								bean = new PlaceCreationBean(placeName,
										latitude, longitude);
							} else {
								bean = new PlaceCreationBean(placeName,
										parentID);
							}
							bean.setImage(image);
							bean.setPlaceDescription(placeDescription);
							success = PlaceUtilities.createNewPlace(sessionID,
									bean);
							return null;

						}

						@Override
						protected void onPostExecute(Void result) {
							super.onPostExecute(result);
							dismissDialog(WAITTING_DIALOG_ID);
							if (success) {
								Toast toast = NotifyUtilities
										.createMessageToast(
												CreatNewPlaceActivity.this,
												"更新成功");
								toast.show();
								setResult(Activity.RESULT_OK);
								CreatNewPlaceActivity.this.finish();
							} else {
								showDialog(UPDATE_FAILED);
							}
						}

					}).execute();
				}
				//

			}
		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case IMAGE_PICKER_DIALOG:
			AlertDialog dlg = new AlertDialog.Builder(
					CreatNewPlaceActivity.this)
					.setTitle(getResources().getString(R.string.choose_image))
					.setItems(
							getResources().getStringArray(R.array.choose_list),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									if (item == FROM_CAMERA) {
										//
										ContentValues content = new ContentValues();
										content.put(Media.TITLE, "Crop-Temp");
										content.put(
												Media.DATE_ADDED,
												System.currentTimeMillis() / 1000);
										content.put(Media.MIME_TYPE, "image/*");
										ContentResolver resolver = getContentResolver();
										uri = resolver
												.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
														content);
										//
										Intent getImageByCamera = new Intent(
												MediaStore.ACTION_IMAGE_CAPTURE);

										getImageByCamera.putExtra(
												MediaStore.EXTRA_OUTPUT, uri);

										startActivityForResult(
												getImageByCamera, FROM_CAMERA);
									} else {
										Intent getImage = new Intent(
												Intent.ACTION_GET_CONTENT);
										getImage.addCategory(Intent.CATEGORY_OPENABLE);
										getImage.setType("image/*");
										startActivityForResult(getImage,
												FROM_IMAGE);
									}
								}
							}).create();
			return dlg;

		case WAITTING_DIALOG_ID:
			return NotifyUtilities.createProgressDialog(this);
		case UPDATE_FAILED:
			return NotifyUtilities.createMessageDialog(this, "更新失败");
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case FROM_CAMERA:
			case FROM_IMAGE:
				Uri imageUri = null;
				if (requestCode == FROM_IMAGE) {
					imageUri = data.getData();
				} else {
					imageUri = uri;
				}
				cropImage(imageUri);
				break;
			case CROP_IMAGE:
				Bitmap image = data.getExtras().getParcelable("data");
				if (uri != null) {
					ContentResolver resolver = getContentResolver();
					resolver.delete(uri, null, null);
					uri = null;
				}
				place_image.setImageBitmap(image);
				break;
			default:
				break;
			}
		} else {
			switch (requestCode) {
			case FROM_CAMERA:
			case CROP_IMAGE:
				if (uri != null) {
					ContentResolver resolver = getContentResolver();
					resolver.delete(uri, null, null);
					uri = null;
				}
				break;
			case FROM_IMAGE:
				break;
			default:
				break;
			}
		}

	}

	private void cropImage(Uri imageUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setData(imageUri);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_IMAGE);
	}
}
