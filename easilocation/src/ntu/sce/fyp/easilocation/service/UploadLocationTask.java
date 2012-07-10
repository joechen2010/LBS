package ntu.sce.fyp.easilocation.service;

import android.location.Location;
import android.os.AsyncTask;

public class UploadLocationTask extends AsyncTask<Location, Void, Boolean> {

	@Override
	protected Boolean doInBackground(Location... arg0) {
		boolean flag = false;
		// TODO upload location here
		return flag;
	}
	
	@Override
	protected void onPostExecute(Boolean flag) {
		// TODO handles post location uploading activity
	}

}
