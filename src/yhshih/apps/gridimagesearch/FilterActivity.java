package yhshih.apps.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

public class FilterActivity extends Activity implements OnItemSelectedListener {

	Spinner[] spinner;
	int[] spinnerPos;
	private String imageSize;
	private String colorFilter;
	private String imageType;
	TextView siteTV;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);

		imageSize = getIntent().getStringExtra("imageSize");
		colorFilter = getIntent().getStringExtra("colorFilter");
		imageType = getIntent().getStringExtra("imageType");
		
		siteTV = (TextView) findViewById(R.id.siteFilterInput);
		siteTV.setText(getIntent().getStringExtra("site"));
		setupSpinner();
	}
	
	void setupSpinner(){
		spinner = new Spinner[3];
		spinnerPos = new int[3];
		spinner[0] = (Spinner) findViewById(R.id.spinnerImageSize);
		spinner[1] = (Spinner) findViewById(R.id.spinnerColorFliter);
		spinner[2] = (Spinner) findViewById(R.id.spinnerImageType);

		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapterImageSize = ArrayAdapter.createFromResource(this,
		        R.array.image_size_array , android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> adapterColorFilter = ArrayAdapter.createFromResource(this,
		        R.array.color_filter_array , android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> adapterImageType = ArrayAdapter.createFromResource(this,
		        R.array.image_type_array , android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapterImageSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterColorFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterImageType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinnerPos[0] = adapterImageSize.getPosition(imageSize);
		spinnerPos[1] = adapterColorFilter.getPosition(colorFilter);
		spinnerPos[2] = adapterImageType.getPosition(imageType);
		// Apply the adapter to the spinner
		spinner[0].setAdapter(adapterImageSize);
		spinner[1].setAdapter(adapterColorFilter);
		spinner[2].setAdapter(adapterImageType);
		
		for (int i = 0; i < 3; i++) {
			if(spinnerPos[i] != 0)
				spinner[i].setSelection(spinnerPos[i]);	
			spinner[i].setOnItemSelectedListener(this);
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		int parentID = parent.getId();
		if(parentID == R.id.spinnerImageSize) {
			imageSize = parent.getItemAtPosition(position).toString();
		} else if (parentID == R.id.spinnerColorFliter) {
			colorFilter = parent.getItemAtPosition(position).toString();
		} else if (parentID == R.id.spinnerImageType) {
			imageType = parent.getItemAtPosition(position).toString();
		}
	}

	public void onSubmit(View v) {
		Intent i = new Intent();
		i.putExtra("imageSize", imageSize);
		i.putExtra("colorFilter", colorFilter);
		i.putExtra("imageType", imageType);
		i.putExtra("site", siteTV.getText().toString());
		setResult(RESULT_OK, i);
		finish();
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
