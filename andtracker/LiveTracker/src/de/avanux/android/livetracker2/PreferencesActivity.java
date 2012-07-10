/* Copyright (C) 2009  Axel Müller <axel.mueller@avanux.de> 
 * 
 * This file is part of LiveTracker.
 * 
 * LiveTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * LiveTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with LiveTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.avanux.android.livetracker2;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * There is not need to propagate changed preferences to Configuration since it implements OnPreferenceChangeListener itself.
 * Minimum values can be changed at any time by the server. Therefore, it makes no sense to verify that current values don't not
 * fall below minimum values. This has to be checked whenever the current values are used.
 * 
 * @author axel
 *
 */
public class PreferencesActivity extends PreferenceActivity implements OnPreferenceChangeListener, OnSharedPreferenceChangeListener {

    private static final String TAG = "LiveTracker:PreferencesActivity";
    
    public static final String DEFAULT_TIME_INTERVAL = "defaultTimeInterval";
    
    public static final String DEFAULT_DISTANCE = "defaultDistance";
    
    private static final TransmissionMode DEFAULT_TRANSMISSION_MODE = TransmissionMode.REALTIME;

    private TransmissionMode transmissionMode;
    
    /**
     * the default time interval represents also the minimum configurable time interval
     */
    private Long defaultTimeInterval;

    /**
     * the default distance represents also the minimum configurable distance
     */
    private Float defaultDistance;
    
    
    // ~ Life cycle callbacks -------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate was called.");
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        this.transmissionMode = TransmissionMode.valueOf(sharedPreferences.getString(getString(R.string.preference_transmissionMode_key), TransmissionMode.REALTIME.toString()));
        initTransmissionMode(this.transmissionMode);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.defaultTimeInterval = extras.getLong(DEFAULT_TIME_INTERVAL);
            this.defaultDistance = extras.getFloat(DEFAULT_DISTANCE);
        }
        // FIXME: look in savedInstanceState for values including transmissionModes
        
        setTimeIntervalSummary(sharedPreferences);
        setDistanceSummary(sharedPreferences);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.d(TAG, "onPreferenceChange was called: " + preference.getKey() + "=" + newValue);
        if (preference.getKey().equals(getString(R.string.preference_timeInterval_key))) {
            boolean invalidValue = false;
            try {
                long newTimeInterval = Long.parseLong(newValue.toString());
                if (newTimeInterval < 0) {
                    invalidValue = true;
                }
            }
            catch(NumberFormatException e) {
                invalidValue = true;
            }
            if(invalidValue) {
                Toast.makeText(this, getString(R.string.toast_timeIntervalValueInvalid) + " " + this.defaultTimeInterval, Toast.LENGTH_LONG).show();
                return false;
            }
        } else if (preference.getKey().equals(getString(R.string.preference_distance_key))) {
            boolean invalidValue = false;
            try {
                float newDistance = Float.parseFloat(newValue.toString());
                if (newDistance < 0) {
                    invalidValue = true;
                }
            } catch (NumberFormatException e) {
                invalidValue = true;
            }
            if (invalidValue) {
                Toast.makeText(this, getString(R.string.toast_timeIntervalValueInvalid) + " " + this.defaultDistance.intValue(), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged was called: key=" + key);
        if (key.equals(getString(R.string.preference_transmissionMode_key))) {
            String transmissionMode = sharedPreferences.getString(getString(R.string.preference_transmissionMode_key), DEFAULT_TRANSMISSION_MODE.toString());
            this.transmissionMode = TransmissionMode.valueOf(transmissionMode);
        }
        setTransmissionModeSummary();
        setTimeIntervalSummary(sharedPreferences);
        setDistanceSummary(sharedPreferences);
    }

    // ~ UI handling ----------------------------------------------------------

    private void initTransmissionMode(TransmissionMode transmissionMode) {
        ListPreference preference = (ListPreference) getPreferenceScreen().findPreference(getString(R.string.preference_transmissionMode_key));
        preference.setEntries(R.array.preference_entries_transmission_mode);

        // the transmission modes are defined in an enum - no need to define them again in xml file
        List<String> transmissionModeEntryValues = new ArrayList<String>();
        for (TransmissionMode transmissionModeEntryValue : TransmissionMode.values()) {
            transmissionModeEntryValues.add(transmissionModeEntryValue.toString());
        }
        preference.setEntryValues((String[]) transmissionModeEntryValues.toArray(new String[] {}));
        
        // set default transmission mode
        preference.setDefaultValue(DEFAULT_TRANSMISSION_MODE);
        setTransmissionModeSummary();
        
        preference.setOnPreferenceChangeListener(this);
    }
    
    private void setTransmissionModeSummary() {
        Preference preference = getPreferenceScreen().findPreference(getString(R.string.preference_transmissionMode_key));
        int preferenceEntriesTransmissionModeIndex = getArrayIndex(R.array.preference_entryvalues_transmission_mode, transmissionMode.toString());
        CharSequence transmissionMode = getResources().getTextArray(R.array.preference_entries_transmission_mode)[preferenceEntriesTransmissionModeIndex];
        preference.setSummary(transmissionMode);
    }

    private void setTimeIntervalSummary(SharedPreferences sharedPreferences) {
        Preference preference = getPreferenceScreen().findPreference(getString(R.string.preference_timeInterval_key));
        preference.setOnPreferenceChangeListener(this);
        
        String timeInterval = null;
        if(TransmissionMode.REALTIME.equals(this.transmissionMode)) {
            timeInterval = "" + this.defaultTimeInterval;
            preference.setEnabled(false);
        }
        else if(TransmissionMode.MANUAL.equals(this.transmissionMode)) {
            preference.setEnabled(true);
            String preferenceTimeInterval = sharedPreferences.getString(getString(R.string.preference_timeInterval_key), null);
            if(preferenceTimeInterval != null) {
                timeInterval = preferenceTimeInterval;
            }
            else {
                timeInterval = "" + this.defaultTimeInterval;
            }
        }
        
        preference.setSummary(timeInterval + " " + getString(R.string.preference_timeInterval_unit));
    }

    private void setDistanceSummary(SharedPreferences sharedPreferences) {
        Preference preference = getPreferenceScreen().findPreference(getString(R.string.preference_distance_key));
        preference.setOnPreferenceChangeListener(this);
        
        String distance = null;
        if(TransmissionMode.REALTIME.equals(this.transmissionMode)) {
            distance = "" + this.defaultDistance.intValue();
            preference.setEnabled(false);
        }
        else if(TransmissionMode.MANUAL.equals(this.transmissionMode)) {
            preference.setEnabled(true);
            String preferenceDistance = sharedPreferences.getString(getString(R.string.preference_distance_key), null);
            if(preferenceDistance != null) {
                distance = "" + preferenceDistance;
            }
            else {
                distance = "" + this.defaultDistance.intValue();
            }
        }
        
        preference.setSummary(distance + " " + getString(R.string.preference_distance_unit));
    }
    
    private int getArrayIndex(int arrayID, String value) {
        int matchIndex = -1;
        for(int index=0; index<getResources().getTextArray(arrayID).length; index++) {
            if(getResources().getTextArray(arrayID)[index].toString().equals(value)) {
                matchIndex = index;
                break;
            }
        }
        return matchIndex;
    }
}
