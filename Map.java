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
