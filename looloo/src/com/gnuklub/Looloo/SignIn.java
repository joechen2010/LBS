/*
  Authors 
  Dragan Jovev <dragan.jovev@gmail.com>
  Mladen Djordjevic <mladen.djordjevic@gmail.com>
  
  Released under the GPL, as follows:

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.gnuklub.Looloo;

/*
 * Sign In view
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignIn extends Activity {

	private int sessid = -1;
	ProgressDialog pd;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.sign_in);
		Button signinButton = (Button) findViewById(R.id.signin_signin_button);

		signinButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Runnable r0 = new Runnable() {
					public void run() {
						pd = ProgressDialog.show(SignIn.this, "Please wait",
								"Logging in...", false, false);
					}
				};
				runOnUIThread(r0);
				Handler h = new Handler();
				Runnable r1 = new Runnable() {
					public void run() {

						try {
							EditText signinEditUsername = (EditText) findViewById(R.id.signin_edit_username);
							EditText signinEditPassword = (EditText) findViewById(R.id.signin_edit_password);
							String username = signinEditUsername.getText()
									.toString();
							String password = signinEditPassword.getText()
									.toString();
							RPC rpc = new RPC(SignIn.this);
							sessid = rpc.login(username, password);
							if (sessid == -1) {
								pd.dismiss();
								final Dialog dialog1 = new Dialog(SignIn.this);
								dialog1
										.setContentView(R.layout.error_dialog);
								dialog1
										.setTitle("Wrong username and/or password");
								dialog1.show();
								Button ok = (Button) dialog1
										.findViewById(R.id.errorDialogOk);
								ok
										.setOnClickListener(new View.OnClickListener() {
											public void onClick(View arg0) {
												dialog1.dismiss();
											}
										});
							} else {
								Intent i = new Intent(SignIn.this,
										SearchView.class);
								i.putExtra("com.gnuklub.Looloo.SESSIONID",
										sessid);
								i.putExtra("com.gnuklub.Looloo.REFRESH_ON_START",
										true);
								startActivity(i);
							}
						} catch (ConnectionException ce) {
							pd.dismiss();
							final Dialog dialog = new Dialog(SignIn.this);
							dialog
									.setContentView(R.layout.error_dialog);
							dialog.setTitle("Server not responding");
							dialog.show();
							Button ok = (Button) dialog
									.findViewById(R.id.errorDialogOk);
							ok.setOnClickListener(new View.OnClickListener() {
								public void onClick(View arg0) {
									dialog.dismiss();
								}
							});
						}

					}
				};
				h.post(r1);
				Runnable r2 = new Runnable() {
					public void run() {
						pd.dismiss();
					}
				};
				h.post(r2);
			}
		});
	}
}