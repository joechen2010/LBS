package cn.edu.nju.software.gof.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class NofityUtilities {

	public static Dialog createMessageDialog(Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message).setCancelable(true);
		AlertDialog alert = builder.create();
		return alert;
	}

	public static Toast createMessageToast(Context context, String message) {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, message, duration);
		return toast;
	}

	public static ProgressDialog createProgressDialog(Context context) {
		ProgressDialog dialog = ProgressDialog.show(context, "",
				context.getResources().getString(R.string.waitting), true);
		return dialog;
	}
}
