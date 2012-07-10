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
 * Creation and editing of LooLoo account
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class CreateEditAccount1 extends Activity {

    private int origin;
    private int sessid;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.create_edit_account_1);

        Bundle extras = getIntent().getExtras();
        origin = extras.getInt("com.gnuklub.Looloo.ORIGIN");
        sessid = extras.getInt("com.gnuklub.Looloo.SESSID");

        Button next = (Button) findViewById(R.id.next_button);
        EditText username = (EditText) findViewById(R.id.accountUsername);
        EditText password = (EditText) findViewById(R.id.accountPassword);
        //RadioGroup gender = (RadioGroup) findViewById(R.id.accountGender);
        RadioButton male = (RadioButton) findViewById(R.id.accountGenderMale);
        RadioButton female = (RadioButton) findViewById(R.id.accountGenderFemale);
        EditText DOB = (EditText) findViewById(R.id.accountDOB);
        EditText occupation = (EditText) findViewById(R.id.accountOccupation);
        EditText email = (EditText) findViewById(R.id.accountEmail);
        TextView textUsername  = (TextView) findViewById(R.id.widget234);
       //final TextView errorText = (TextView) findViewById(R.id.errorDialogMessage);
        //DB db = new DB(this);

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Getting user settings parameters and writing them down in
                // database
                ArrayList<Row> dataInput = new ArrayList<Row>();
                Row row = new Row();
                EditText username = (EditText) findViewById(R.id.accountUsername);
                row.username = username.getText().toString();

                EditText password = (EditText) findViewById(R.id.accountPassword);
                row.password = password.getText().toString();

                EditText DOB = (EditText) findViewById(R.id.accountDOB);
                if (DOB.getText().length() > 0)
                    row.dob = Integer.valueOf(DOB.getText().toString());
                else
                    row.dob = 0;
                EditText email = (EditText) findViewById(R.id.accountEmail);
                row.email = email.getText().toString();

                EditText occupation = (EditText) findViewById(R.id.accountOccupation);
                row.occupation = occupation.getText().toString();

                //RadioGroup gender = (RadioGroup) findViewById(R.id.accountGender);
                RadioButton male = (RadioButton) findViewById(R.id.accountGenderMale);
                //RadioButton female = (RadioButton) findViewById(R.id.accountGenderFemale);

                
                if (male.isChecked())
                    row.gender = 0;
                else
                    row.gender = 1;

                row.userID = 1;

                dataInput.add(row);

                if (username.getText().length() > 0
                        && password.getText().length() > 0) {
                    DB db = new DB(CreateEditAccount1.this);
                    db.setUserPrefs(row.userID, row.username, row.password,
                            row.gender, row.dob, row.occupation, row.email);

                    Intent i = new Intent(CreateEditAccount1.this,
                            CreateEditAccount2.class);
                    i.putExtra("com.gnuklub.Looloo.ACCOUNTUSERNAME",
                            row.username);
                    i.putExtra("com.gnuklub.Looloo.ACCOUNTPASSWORD",
                            row.password);
                    i.putExtra("com.gnuklub.Looloo.ACCOUNTGENDER", row.gender);
                    i.putExtra("com.gnuklub.Looloo.ACCOUNTDOB", row.dob);
                    i.putExtra("com.gnuklub.Looloo.ACCOUNTOCCUPATION",
                            row.occupation);
                    i.putExtra("com.gnuklub.Looloo.ACCOUNTEMAIL", row.email);
                    i.putExtra("com.gnuklub.Looloo.ORIGIN", origin);
                    i.putExtra("com.gnuklub.Looloo.SESSID", sessid);
                    startActivity(i);
                } else {
                    final Dialog dialog = new Dialog(CreateEditAccount1.this);
                    dialog.setContentView(R.layout.error_dialog);
                    dialog.setTitle("Please provide username and password.");
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
        });

        // Fetching all of the user parameters from database and refreshing user
        // settings
        male.setChecked(true);
        if (origin != 0) {
            DB db1 = new DB(this);
            List<Row> dataRefresh = db1.getUserPrefs();
            username.setText(dataRefresh.get(0).username);
            username.setVisibility(View.GONE);
            textUsername.setVisibility(View.GONE);
            password.setText(dataRefresh.get(0).password);
            DOB.setText(String.valueOf(dataRefresh.get(0).dob));
            occupation.setText(dataRefresh.get(0).occupation);
            email.setText(dataRefresh.get(0).email);

            if (dataRefresh.get(0).gender.intValue() == 0)
                male.setChecked(true);
            else
                female.setChecked(true);
        }
        else {
            username.setVisibility(View.VISIBLE);
            textUsername.setVisibility(View.VISIBLE);
        }

    }
}