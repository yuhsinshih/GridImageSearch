package yhshih.apps.gridimagesearch;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FilterActivity extends Activity {

	Spinner[] spinner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		setupSpinner();
	}
	
	void setupSpinner(){
		spinner = new Spinner[3];
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
		// Apply the adapter to the spinner
		spinner[0].setAdapter(adapterImageSize);
		spinner[1].setAdapter(adapterColorFilter);
		spinner[2].setAdapter(adapterImageType);
	}
}
