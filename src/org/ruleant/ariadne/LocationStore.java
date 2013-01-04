/**
 * Location Store
 * 
 * Copyright (C) 2012-2013 Dieter Adriaenssens
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * @package org.ruleant.ariadne
 * @author  Dieter Adriaenssens <ruleant@users.sourceforge.net>
 */
package org.ruleant.ariadne;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

/**
 * Location Store saves a location
 * 
 * This object will store a location for future use, and will the save the location
 * when the application is stopped
 * 
 * @package org.ruleant.ariadne
 * @author  Dieter Adriaenssens <ruleant@users.sourceforge.net>
 */
public class LocationStore {
	private Context mContext;
	private Location mLocation;
	private SharedPreferences mPrefs;
	public static final String PREFS_STORE_LOC = "LocationStore";
	private static final String LONGITUDE = "Longitude";
	private static final String LATITUDE = "Latitude";
	
	/**
	 * Constructor
	 *
	 * @param Context context Context of the Android app
	 */
	public LocationStore(Context context) {
		mContext = context;
		mLocation = new Location("stored");
		mPrefs = mContext.getSharedPreferences(PREFS_STORE_LOC, Context.MODE_PRIVATE);

		restore();
	}

	/**
	 * Get Location
	 *
	 * @return Location location
	 */
	public Location getLocation() {
		return mLocation;
	}

	/**
	 * Set Location
	 *
	 * @param Location location
	 */
	public void setLocation(Location location) {
		mLocation = location;
	}

	/**
	 * Save stored location in Shared Preferences
	 *
	 * @return void
	 */
	public void save() {
		// don't save if mLocation is not set
		if (mLocation == null) {
			return;
		}

		// save location to a SharedPreferences file
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putString(
				LONGITUDE,
				Location.convert(mLocation.getLongitude(), Location.FORMAT_DEGREES)
		);
		editor.putString(
				LATITUDE,
				Location.convert(mLocation.getLatitude(), Location.FORMAT_DEGREES)
		);
		// Commit the edits!
		editor.commit();
	}

	/**
	 * Restore stored location from Shared Preferences
	 *
	 * @return Location location retrieved from Preferences
	 */
	public Location restore() {
		// restore location from a SharedPreferences file
		// TODO convert crashes when String has a "," as a decimal separator 
		try {
			String longitudeString = mPrefs.getString(LONGITUDE, "0.0");
			double longitudeDouble = Location.convert(longitudeString);
			mLocation.setLongitude(longitudeDouble);
			mLocation.setLatitude(Location.convert(mPrefs.getString(LATITUDE, "0.0")));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			mLocation.setLongitude(0);
			mLocation.setLatitude(0);
		}
		
		return mLocation;
	}
}
