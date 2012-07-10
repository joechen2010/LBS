package cn.edu.nju.software.gof.activity;

import cn.edu.nju.software.gof.beans.PlaceCreationBean;
import cn.edu.nju.software.gof.requests.AccountUtilities;
import cn.edu.nju.software.gof.requests.InformationUtilities;
import cn.edu.nju.software.gof.requests.PlaceUtilities;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SettingActivity extends ListActivity {

	private static final int WAITTING_DIALOG = 0;

	private final static int STATE_ID = 0;
	private final static int INFORMATION_ID = 1;
	private final static int PASSWORD_ID = 2;
	private final static int SYNCHRONOUS_ID = 3;

	AlertDialog alert;
	AlertDialog dlg;
	private String oldP, newP, newPA;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		registerEventHandler();
	}

	private void registerEventHandler() {
		ListView listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case STATE_ID:
					changeState();
					break;
				case INFORMATION_ID:
					Intent intent = new Intent(getApplicationContext(),
							PersonalInfoActivity.class);
					startActivity(intent);
					break;
				case PASSWORD_ID:
					changePassword();
					break;
				case SYNCHRONOUS_ID:
					intent = new Intent(getApplicationContext(),
							SynchronousActivity.class);
					startActivity(intent);
					break;
				}
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case WAITTING_DIALOG:
			return NotifyUtilities.createProgressDialog(this);
		}
		return null;
	}

	public void changeState() {
		final CharSequence[] items = { "在线", "隐身" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择一状态");
		builder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						showDialog(WAITTING_DIALOG);
						MyApplication application = (MyApplication) getApplication();
						final String sessionID = (String) application
								.getData("sessionID");
						final int state = item;
						(new AsyncTask<Void, Void, Void>() {

							@Override
							protected Void doInBackground(Void... params) {
								InformationUtilities.changeOnLineState(
										sessionID, state);
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								super.onPostExecute(result);
								alert.cancel();
								dismissDialog(WAITTING_DIALOG);
							}
						}).execute();
					}
				});
		alert = builder.create();
		alert.show();
	}

	public void changePassword() {
		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");
		LayoutInflater factory = LayoutInflater.from(this);
		final View dialogView = factory.inflate(R.layout.change_password, null);
		dlg = new AlertDialog.Builder(this)
				.setTitle(R.string.create_title)
				.setView(dialogView)
				.setPositiveButton(R.string.create_ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								showDialog(WAITTING_DIALOG);

								EditText oldPassword = (EditText) dialogView
										.findViewById(R.id.old_password);
								EditText newPassword = (EditText) dialogView
										.findViewById(R.id.new_password);
								EditText newPasswordAgain = (EditText) dialogView
										.findViewById(R.id.new_password_again);

								oldP = oldPassword.getText().toString();
								newP = newPassword.getText().toString();
								newPA = newPasswordAgain.getText().toString();

								(new AsyncTask<Void, Void, Void>() {
									int success = 0;

									@Override
									protected Void doInBackground(
											Void... params) {
										if (newP.equals(newPA)) {
											if (AccountUtilities
													.modifyPassword(sessionID,
															oldP, newP)) {
												success = 1;
											} else {
												success = 2;
											}
										} else {
											success = 3;
										}
										return null;
									}

									@Override
									protected void onPostExecute(Void result) {
										super.onPostExecute(result);
										dismissDialog(WAITTING_DIALOG);
										switch (success) {
										case 1:
											Toast.makeText(
													SettingActivity.this,
													"修改成功", Toast.LENGTH_SHORT)
													.show();
											break;
										case 2:
											Toast.makeText(
													SettingActivity.this,
													"修改失败", Toast.LENGTH_SHORT)
													.show();
											break;
										case 3:
											Toast.makeText(
													SettingActivity.this,
													"确认密码有错",
													Toast.LENGTH_SHORT).show();
											dlg.show();
											break;
										}
									}
								}).execute();
							}
						})
				.setNegativeButton(R.string.create_cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						}).create();
		dlg.show();
	}
}