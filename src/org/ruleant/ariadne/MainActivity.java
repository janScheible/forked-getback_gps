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

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.ruleant.ariadne.lib.AriadneLocation;
import org.ruleant.ariadne.lib.DebugLevel;
import org.ruleant.ariadne.lib.FormatUtils;
import org.ruleant.ariadne.lib.Navigator;

/**
 * Main Activity class.
 *
 * @author Dieter Adriaenssens <ruleant@users.sourceforge.net>
 */
public class MainActivity extends AbstractAriadneActivity {
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        boolean superResult = super.onCreateOptionsMenu(menu);

        DebugLevel debug = new DebugLevel(this);

        // don't add details button when debugging is disabled
        if (debug.checkDebugLevel(DebugLevel.DEBUG_LEVEL_LOW)) {
            // Inflate the menu;
            // this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
        }

        return superResult;
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by onOptionsItemSelected()
        switch (item.getItemId()) {
            case R.id.menu_details:
                displayDetails(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        MenuItem miDetails = menu.findItem(R.id.menu_details);
        DebugLevel debug = new DebugLevel(this);

        if (miDetails != null) {
            // hide details button when debugging is disabled
            miDetails.setVisible(
                    debug.checkDebugLevel(DebugLevel.DEBUG_LEVEL_LOW));
        }

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Called when the user clicks the About menu item.
     *
     * @param item MenuItem object that was clicked
     */
    final void displayDetails(final MenuItem item) {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
    }

    /**
     * Refresh display : refresh the values of Location Provider, Location, ...
     */
    protected final void refreshDisplay() {
        super.refreshDisplay();

        // only refresh items if activity is bound to service
        // connection state is checked in getNavigator
        Navigator navigator = getNavigator();

        if (navigator == null) {
            return;
        }

        Resources res = getResources();
        AriadneLocation destination;

        // get Destination from service
        try {
            destination = new AriadneLocation(navigator.getDestination());
        } catch (Exception e) {
            e.printStackTrace();
            destination = null;
        }

        // Refresh Directions to destination
        NavigationView nvToDestination
                = (NavigationView) findViewById(R.id.navigationView_ToDest);
        TextView tvToDestinationDistance
                = (TextView) findViewById(R.id.textView_toDestDist);
        TextView tvToDestinationDirection
                = (TextView) findViewById(R.id.textView_toDestDir);
        TextView tvCurrentSpeed
                = (TextView) findViewById(R.id.textView_currSpeed);
        TextView tvCurrentBearing
                = (TextView) findViewById(R.id.textView_currBearing);

        String toDestinationDistanceText = res.getString(R.string.unknown);
        String toDestinationDirectionText = res.getString(R.string.unknown);
        String currentSpeedText = res.getString(R.string.inaccurate);
        String currentBearingText = res.getString(R.string.inaccurate);

        nvToDestination.setMode(NavigationView.DISABLED);

        if (destination != null
                && navigator.isLocationAccurate()) {
            // Print distance and bearing
            toDestinationDistanceText
                    = FormatUtils.formatDist(navigator.getDistance());

            double direction;

            // if bearing is accurate, display relative direction
            // if not, display absolute direction
            if (navigator.isBearingAccurate()) {
                direction = navigator.getRelativeDirection();
                nvToDestination.setMode(NavigationView.ACCURATE);
            } else {
                direction = navigator.getAbsoluteDirection();
                nvToDestination.setMode(NavigationView.INACCURATE);
            }

            toDestinationDirectionText
                    = FormatUtils.formatAngle(FormatUtils.normalizeAngle(
                    navigator.getAbsoluteDirection()));
            nvToDestination.setDirection(direction);
        }

        // current speed
        if (navigator.isLocationAccurate()) {
            currentSpeedText = FormatUtils.formatSpeed(
                    navigator.getCurrentSpeed(), this);
        }

        // current bearing
        if (navigator.isBearingAccurate()) {
            currentBearingText = FormatUtils.formatAngle(
                    FormatUtils.normalizeAngle(navigator.getCurrentBearing()));
        }

        // update views
        nvToDestination.invalidate();
        tvToDestinationDistance.setText(toDestinationDistanceText);
        tvToDestinationDirection.setText(toDestinationDirectionText);
        tvCurrentSpeed.setText(currentSpeedText);
        tvCurrentBearing.setText(currentBearingText);
    }
}
