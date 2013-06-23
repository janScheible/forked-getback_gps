/**
 * Main Activity
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
 * @author Dieter Adriaenssens <ruleant@users.sourceforge.net>
 */
package org.ruleant.ariadne;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Main Activity class.
 *
 * @author Dieter Adriaenssens <ruleant@users.sourceforge.net>
 */
public class MainActivity extends AbstractAriadneActivity {
    /**
     * Rotation of the direction pointer image.
     */
    private static final int POINTER_ROT = 90;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Refresh display : refresh the values of Location Provider, Location, ...
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected final void refreshDisplay() {
        LocationService service = getService();
        Resources res = getResources();
        AriadneLocation destination = null;
        AriadneLocation currentLocation = null;

        // Refresh locationProvider
        TextView tvProvider
            = (TextView) findViewById(R.id.textView_LocationProvider);
        String providerText = res.getString(R.string.location_provider) + ": ";
        String providerName = service.getLocationProvider();
        if (providerName.isEmpty()) {
            providerText += res.getString(R.string.none);
        } else {
            providerText
                += FormatUtils.localizeProviderName(this, providerName);
        }
        tvProvider.setText(providerText);

        // Refresh Location
        TextView tvLocation
            = (TextView) findViewById(R.id.textView_Location);
        String locationText
            = res.getString(R.string.curr_location) + ":\n";
        currentLocation = service.getLocation();
        if (currentLocation == null) {
            locationText += " " + res.getString(R.string.unknown);
        } else {
            locationText += currentLocation.toString(this);
        }
        tvLocation.setText(locationText);

        // Refresh Destination
        TextView tvDestination
            = (TextView) findViewById(R.id.textView_Destination);
        String destinationText
            = res.getString(R.string.destination) + ":\n";

        // get Destination from service
        try {
            destination = new AriadneLocation(service.getDestination());
        } catch (Exception e) {
            e.printStackTrace();
            destination = null;
        }

        if (destination == null) {
            destinationText += " "
                + res.getString(R.string.notset);

            // display notice when no destination is set
            // and there is a current location
            if (currentLocation != null) {
                destinationText += "\n "
                    + res.getString(R.string.notice_no_dest);
            }
        } else {
            destinationText += destination.toString(this);
        }
        tvDestination.setText(destinationText);

        // Refresh Directions to destination
        TextView tvToDestination
            = (TextView) findViewById(R.id.textView_ToDestination);
        TextView tvInaccurateDirection
        = (TextView) findViewById(R.id.textView_InaccurateDirection);
        ImageView ivDestPointer
        = (ImageView) findViewById(R.id.image_DestinationPointer);
        String toDestinationText
            = res.getString(R.string.to_dest) + ":\n";
        if (destination == null || currentLocation == null) {
            toDestinationText += " "
                + res.getString(R.string.unknown);
            tvInaccurateDirection.setVisibility(TextView.INVISIBLE);
            ivDestPointer.setVisibility(ImageView.INVISIBLE);
        } else {
            // Print distance and bearing
            toDestinationText += " "
                + res.getString(R.string.distance) + ": "
                + FormatUtils.formatDist(service.getDistance()) + "\n";
            toDestinationText += " "
                + res.getString(R.string.direction) + ": "
                + FormatUtils.formatAngle(service.getDirection());
            tvInaccurateDirection.setVisibility(TextView.VISIBLE);

            // setRotation require API level 11
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ivDestPointer.setVisibility(ImageView.VISIBLE);
                // rotate 90° counter clockwise,
                // current image is pointing right.
                ivDestPointer.setRotation(
                        (float) FormatUtils.normalizeAngle(
                                service.getDirection() - POINTER_ROT));
            } else {
                ivDestPointer.setVisibility(ImageView.INVISIBLE);
            }
        }
        tvToDestination.setText(toDestinationText);
    }
}
