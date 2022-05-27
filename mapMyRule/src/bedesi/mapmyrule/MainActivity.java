package bedesi.mapmyrule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements LocationListener {

	ToggleButton gpsTog;
	EditText x1;
	EditText x2;
	EditText y1;
	EditText y2;
	TextView x;
	TextView y;
	CheckBox cs;
	Button onoff;
	double xx1;
	double xx2;
	double yy1;
	double yy2;
	char ch;
	double curx;
	double cury;
	Intent i;
	boolean checkSound = false;
	LocationManager manager;
	AudioManager mobilemode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// connecting all buttons
		gpsTog = (ToggleButton) findViewById(R.id.gpstag);
		x = (TextView) findViewById(R.id.x);
		y = (TextView) findViewById(R.id.y);
		x1 = (EditText) findViewById(R.id.x1);
		x2 = (EditText) findViewById(R.id.x2);
		y1 = (EditText) findViewById(R.id.y1);
		y2 = (EditText) findViewById(R.id.y2);
		RadioGroup pg = (RadioGroup) findViewById(R.id.profilegroup);
		cs = (CheckBox) findViewById(R.id.cs);
		onoff = (Button) findViewById(R.id.onoff);
		onoff.setText("Activate Rule");

		// initializing from previous setting

		SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
		x1.setText(sp.getString("xx1", "" + 0.000000));
		x2.setText(sp.getString("xx2", "" + 0.000000));
		y1.setText(sp.getString("yy1", "" + 0.000000));
		y2.setText(sp.getString("yy2", "" + 0.000000));
		if (sp.getBoolean("chks", false))
			cs.setChecked(true);

		switch (sp.getString("ch", ""+1)) {
		case "1":
			ch = '1';
			pg.check(R.id.gen);
			break;
		case "2":
			ch = '2';
			pg.check(R.id.sil);
			break;
		case "3":
			ch = '3';
			pg.check(R.id.vib);
			break;
		}
		cs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked)
					checkSound = true;
				else
					checkSound = false;
			}
		});
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
				this);

		// Saving profile index
		pg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.gen:
					ch = '1';
					break;
				case R.id.sil:
					ch = '2';
					break;
				case R.id.vib:
					ch = '3';
					break;
				}
			}
		});

		// Get the status of GPS and show it in GPS Toggle button
		setGPSToggle(true);

		// Setting action on GPS toogle button
		gpsTog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		});
	}

	private void setGPSToggle(boolean flag) {
		// TODO Auto-generated method stub

		if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			gpsTog.setChecked(true);
			if(flag)
			toast("GPS is enable");
		} else {
			gpsTog.setChecked(false);
			if(flag)
			toast("GPS is disable");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setGPSToggle(false);
	}

	public void testing(View v) {
		if (onoff.getText().toString().charAt(0) == 'A') {
			xx1 = Double.parseDouble(x1.getText().toString());
			xx2 = Double.parseDouble(x2.getText().toString());
			yy1 = Double.parseDouble(y1.getText().toString());
			yy2 = Double.parseDouble(y2.getText().toString());
			i = new Intent(MainActivity.this, CheckService.class);
			i.putExtra("procho", ch);
			i.putExtra("xx1", xx1);
			i.putExtra("xx2", xx2);
			i.putExtra("yy1", yy1);
			i.putExtra("yy2", yy2);
			i.putExtra("chks", checkSound);
			i.putExtra("stop", false);
			startService(i);
			toast("Rule Activated");
			SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor e = sp.edit();
			e.putString("ch", "" + ch);
			e.putString("xx1", "" + xx1);
			e.putString("xx2", "" + xx2);
			e.putString("yy1", "" + yy1);
			e.putString("yy2", "" + yy2);
			e.putBoolean("chks", checkSound);
			e.commit();
			onoff.setText("Deactivate Rule");
		} else {
			toast("Rule Deactivated");
			i.putExtra("stop", true);
			startService(i);
			onoff.setText("Activate Rule");
		}
	}

	public void one(View v) {
		x1.setText(Double.valueOf(curx).toString());
		y1.setText(Double.valueOf(cury).toString());
	}

	public void two(View v) {
		x2.setText(Double.valueOf(curx).toString());
		y2.setText(Double.valueOf(cury).toString());
	}

	protected void toast(String s) {
		Toast.makeText(this, s, 0).show();
	}

	@Override
	public void onLocationChanged(Location loc) {
		// TODO Auto-generated method stub
		if (loc != null) {
			curx = loc.getLatitude();
			cury = loc.getLongitude();
		}
		x.setText(Double.valueOf(curx).toString());
		y.setText(Double.valueOf(cury).toString());
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
}
