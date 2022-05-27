package bedesi.mapmyrule;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class CheckService extends Service implements LocationListener {
	double xx1;
	double xx2;
	double yy1;
	double yy2;
	char ch;
	double curx = 2;
	double cury = 22;
	boolean checkSound = false;
	boolean stop=false;
	LocationManager manager;
	AudioManager mobilemode;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1,
				this);
	}

	@Override
	public int onStartCommand(Intent i, int flags, int startId) {
		// TODO do something useful
		ch = i.getExtras().getChar("procho");
		xx1 = i.getExtras().getDouble("xx1");
		xx2 = i.getExtras().getDouble("xx2");
		yy1 = i.getExtras().getDouble("yy1");
		yy2 = i.getExtras().getDouble("yy2");
		stop = i.getExtras().getBoolean("stop");
		checkSound = i.getExtras().getBoolean("chks");

		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location loc) {
		// TODO Auto-generated method stub
		if (!sameprof() && !stop) {
			if (loc != null) {
				curx = loc.getLatitude();
				cury = loc.getLongitude();
			}
			if (curx >= xx1 && curx <= xx2 && cury >= yy1 && cury <= yy2) {
				setpof();
				if (checkSound) {
					MediaPlayer mp = MediaPlayer.create(this, R.raw.al);
					mp.start();
				}

			}
		}
	}

	private boolean sameprof() {
		// TODO Auto-generated method stub
		mobilemode = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		int curpro=0;
		switch(ch)
		{
		case '1':
			curpro = AudioManager.RINGER_MODE_NORMAL;
		break;
		case '2':
			curpro = AudioManager.RINGER_MODE_SILENT;
		break;
		case '3':
			curpro = AudioManager.RINGER_MODE_VIBRATE;
		break;
		default:
			return true;
		}
		if(mobilemode.getRingerMode() == curpro)
		{
			return true;
		}
		return false;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void setpof() {
		String s = "Profile location reached.\nSet to ";
		switch (ch) {
		case '1':
			mobilemode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			s+="NORMAL";
			break;
		case '2':
			mobilemode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			s+="SILENT";
			break;
		case '3':
			mobilemode.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			s+="VIBRATE";
			break;
		}
		toast(s+".");
	}

	protected void toast(String s) {
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}
}