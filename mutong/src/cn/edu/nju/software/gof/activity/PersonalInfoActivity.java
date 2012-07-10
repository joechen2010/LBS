package cn.edu.nju.software.gof.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.edu.nju.software.gof.beans.PersonInformationBean;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.requests.InformationUtilities;
import cn.edu.nju.software.gof.viewbeans.PersonalInfo;

public class PersonalInfoActivity extends Activity {

	private static final int IMAGE_PICKER_DIALOG = 0;
	private final static int DATE_DIALOG_ID = 1;
	private final static int WAITTING_DIALOG_ID = 2;
	private final static int UPDATE_FAILED = 4;
	//
	private static final int FROM_IMAGE = 0;
	private static final int FROM_CAMERA = 1;
	//
	private static final int CROP_IMAGE = 2;

	private Uri uri;

	private ImageView avatarEdit;
	private EditText realNameEdit = null;
	private EditText birthdayEdit = null;
	private EditText schoolEdit = null;
	private EditText placeEdit = null;
	private Button submitButton = null;
	//
	private Resources resources = null;
	//
	private int mYear = -1;
	private int mMonth = -1;
	private int mDay = -1;

	//

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_info);
		//
		retriveViews();
		registerEventHandler();
		//
		getPersonalInfo();
	}

	private void getPersonalInfo() {
		showDialog(WAITTING_DIALOG_ID);
		MyApplication application = (MyApplication) PersonalInfoActivity.this
				.getApplication();
		final String sessionID = (String) application.getData("sessionID");
		(new AsyncTask<Void, Void, Void>() {

			private InputStream avatarStream = null;
			private PersonalInfo personInfo = null;

			@Override
			protected Void doInBackground(Void... params) {

				//
				PersonInformationBean information = InformationUtilities
						.getUserInformation(sessionID);
				avatarStream = CachUtilities.getPersonalAvater(sessionID,
						getExternalCacheDir());
				personInfo = new PersonalInfo(information);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				personInfo.setAvatar(resources, avatarStream);
				//
				dismissDialog(WAITTING_DIALOG_ID);
				//
				realNameEdit.setText(personInfo.getRealName());
				String birthday = personInfo.getBirthday();
				if (birthday != null && !birthday.equals("")) {
					birthdayEdit.setText(personInfo.getBirthday());
					String[] dates = birthday.split("-");
					mYear = Integer.parseInt(dates[0]);
					mMonth = Integer.parseInt(dates[1]) - 1;
					mDay = Integer.parseInt(dates[2]);
				}
				schoolEdit.setText(personInfo.getSchool());
				placeEdit.setText(personInfo.getPlace());
				avatarEdit.setImageDrawable(personInfo.getAvatar());
			}

		}).execute();
	}

	private void retriveViews() {
		avatarEdit = (ImageView) findViewById(R.id.avatar_edit);
		birthdayEdit = (EditText) findViewById(R.id.birthday_edit);
		realNameEdit = (EditText) findViewById(R.id.real_name_edit);
		schoolEdit = (EditText) findViewById(R.id.school_edit);
		placeEdit = (EditText) findViewById(R.id.place_edit);
		submitButton = (Button) findViewById(R.id.submit);
	}

	private void registerEventHandler() {

		avatarEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(IMAGE_PICKER_DIALOG);
			}
		});

		birthdayEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					PersonalInfoActivity.this.showDialog(DATE_DIALOG_ID);
				}

			}
		});

		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(WAITTING_DIALOG_ID);
				//
				MyApplication application = (MyApplication) PersonalInfoActivity.this
						.getApplication();
				final String sessionID = (String) application
						.getData("sessionID");
				//
				String realName = realNameEdit.getText().toString();
				String place = placeEdit.getText().toString();
				String school = schoolEdit.getText().toString();
				String birthday = null;
				if (mYear != -1) {
					birthday = mYear + "-" + (mMonth + 1) + "-" + mDay;
				}
				final PersonInformationBean bean = new PersonInformationBean(
						realName, birthday, school, place);
				//
				Drawable avatar = avatarEdit.getDrawable();
				ByteArrayInputStream in = null;
				byte[] buffer = null;
				if (avatar != null) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					((BitmapDrawable) avatar).getBitmap().compress(
							Bitmap.CompressFormat.JPEG, 100, out);
					buffer = out.toByteArray();
					in = new ByteArrayInputStream(buffer);
				}
				final InputStream avatarStream = in;
				final int contentLength = buffer.length;
				//
				(new AsyncTask<Void, Void, Void>() {

					private boolean success = false;

					@Override
					protected Void doInBackground(Void... params) {
						success = InformationUtilities.setUserInformation(
								sessionID, bean);
						if (avatarStream != null) {
							success = success
									&& CachUtilities.updateAvatar(sessionID,
											avatarStream, contentLength,
											getExternalCacheDir());
						}
						return null;

					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						dismissDialog(WAITTING_DIALOG_ID);
						if (success) {
							Toast toast = NotifyUtilities.createMessageToast(
									PersonalInfoActivity.this, "更新成功");
							toast.show();
							finish();
						} else {
							showDialog(UPDATE_FAILED);
						}
					}

				}).execute();
			}
		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case IMAGE_PICKER_DIALOG:
			AlertDialog dlg = new AlertDialog.Builder(PersonalInfoActivity.this)
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
		case DATE_DIALOG_ID:
			final Calendar c = Calendar.getInstance();
			int tempYear = -1;
			int tempMonth = -1;
			int tempDay = -1;
			if (mYear == -1) {
				tempYear = c.get(Calendar.YEAR);
				tempMonth = c.get(Calendar.MONTH);
				tempDay = c.get(Calendar.DAY_OF_MONTH);
			} else {
				tempYear = mYear;
				tempMonth = mMonth;
				tempDay = mDay;
			}
			return new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							mYear = year;
							mMonth = monthOfYear;
							mDay = dayOfMonth;
							birthdayEdit.setText(mYear + "-" + (mMonth + 1)
									+ "-" + mDay);
						}
					}, tempYear, tempMonth, tempDay);
		case WAITTING_DIALOG_ID:
			return NotifyUtilities.createProgressDialog(this);
		case UPDATE_FAILED:
			return NotifyUtilities.createMessageDialog(this, "更新失败");
		}
		return null;
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
				avatarEdit.setImageBitmap(image);
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
}
