package cn.edu.nju.software.gof.activity;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

public class MyDatePicker {

	private Context context = null;
	private EditText birthdayEdit = null;

	private int mYear = -1;
	private int mMonth = -1;
	private int mDay = -1;

	public MyDatePicker(Context context, EditText birthdayEdit) {
		this.context = context;
		this.birthdayEdit = birthdayEdit;
	}

	public String getDateStr() {
		if (mYear == -1) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(mYear);
			sb.append("-");
			sb.append(mMonth + 1);
			sb.append("-");
			sb.append(mDay);
			return sb.toString();
		}
	}

	public DatePickerDialog getDialog() {
		final Calendar c = Calendar.getInstance();
		int currentYear = c.get(Calendar.YEAR);
		int currentMonth = c.get(Calendar.MONTH);
		int currentDay = c.get(Calendar.DAY_OF_MONTH);

		return new DatePickerDialog(context,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						mYear = year;
						mMonth = monthOfYear;
						mDay = dayOfMonth;
						birthdayEdit.setText(getDateStr());
					}
				}, currentYear, currentMonth, currentDay);
	}
}
