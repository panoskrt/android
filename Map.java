/**
########################################################################
# Copyright (C) 2013 Panagiotis Kritikakos <panoskrt@gmail.com> #
# #
# This program is free software: you can redistribute it and/or modify #
# it under the terms of the GNU General Public License as published by #
# the Free Software Foundation, either version 3 of the License, or #
# (at your option) any later version. #
# #
# This program is distributed in the hope that it will be useful, #
# but WITHOUT ANY WARRANTY; without even the implied warranty of #
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the #
# GNU General Public License for more details. #
# #
# You should have received a copy of the GNU General Public License #
# along with this program. If not, see <http://www.gnu.org/licenses/>. #
########################################################################
**/

package com.panoskrt.map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.widget.Toast;

public class Map {

  private Context context;
	private String destination;

	public Map(Context context, String destination) {
		this.context = context;
		this.destination = destination;
	}

	public void openMap() {
		double lat = 0;
		double lon = 0;
		try {
			LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			lat = location.getLatitude();
			lon = location.getLongitude();
			Toast.makeText(context.getApplicationContext(), "Getting route...", Toast.LENGTH_SHORT).show();
		} catch (Exception ex) {
			Toast.makeText(context.getApplicationContext(), "Error while getting GPS signal.", Toast.LENGTH_SHORT).show();
		}
		String address = "http://maps.google.com/maps?saddr=" + lat + "," + lon + "&daddr=" + this.destination;
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(address));
		context.startActivity(intent);
	}
}
