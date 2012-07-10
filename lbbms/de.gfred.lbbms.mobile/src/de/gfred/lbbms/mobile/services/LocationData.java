package de.gfred.lbbms.mobile.services;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationData implements Parcelable {

    public Double longitude;
    public Double latitude;
    public Long timestamp;

    public LocationData(final Double longitude, final Double latitude, final Long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    private LocationData(final Parcel source) {
        readFromParcel(source);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeLong(timestamp);
    }

    public void readFromParcel(final Parcel source) {
        longitude = source.readDouble();
        latitude = source.readDouble();
        timestamp = source.readLong();
    }

    public static final Parcelable.Creator<LocationData> CREATOR = new Parcelable.Creator<LocationData>() {

        @Override
        public LocationData createFromParcel(Parcel source) {
            return new LocationData(source);
        }

        @Override
        public LocationData[] newArray(int size) {
            return new LocationData[size];
        }
    };

}
