package yhshih.apps.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {

	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	private String imageSize = "Any";
	private String colorFilter = "Any";
	private String imageType = "Any";
	private String site = "";
	private final String imageAPI = "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&v=1.0";
	
	boolean filterChanged = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("url", imageResult.getFullUrl());
				startActivity(i);
			}
		});
		gvResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
//				Log.d("DEBUG", "OnScroll");
				customLoadMoreDataFromApi(totalItemsCount, false);
			}
		});
	}
	
	public void onSettings(MenuItem mi){
		Intent i = new Intent(this, FilterActivity.class);

		// pass data
		i.putExtra("imageSize", imageSize);
		i.putExtra("colorFilter", colorFilter);
		i.putExtra("imageType", imageType);
		i.putExtra("site", site);
		// executing
		startActivityForResult(i, 50);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if(requestCode == 50) {
				String temp = data.getStringExtra("imageSize");
				if(!temp.equals(imageSize)){
					filterChanged = true;
					imageSize = temp;
				}
				
				temp = data.getStringExtra("colorFilter");
				if(!temp.equals(colorFilter)){
					filterChanged = true;
					colorFilter = temp;
				}
				
				temp = data.getStringExtra("imageType");
				if(!temp.equals(imageType)){
					filterChanged = true;
					imageType = temp;
				}
				
				temp = data.getStringExtra("site");
				if(!temp.equals(site)){
					filterChanged = true;
					site = temp;
				}
				
				if(filterChanged) {
					filterChanged = false;
					customLoadMoreDataFromApi(0, true);
				}
			}
		}
	}
	
	public void customLoadMoreDataFromApi(int offset, boolean clear) {
		// This method probably sends out a network request and appends new data items to your adapter. 
		// Use the offset value and add it as a parameter to your API request to retrieve paginated data.
		// Deserialize API response and then construct new objects to append to the adapter

		final boolean clearResult = clear;
		String query = etQuery.getText().toString();

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(composeQuery(offset, query),
				new JsonHttpResponseHandler() {
			public void onSuccess(JSONObject response){
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject("responseData")
							.getJSONArray("results");
					if(clearResult == true){
						imageResults.clear();
					}
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
//					Log.d("DEBUG",imageResults.toString());
				} catch(JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public String composeQuery(int offset, String keyword) {
		StringBuffer sb = new StringBuffer(imageAPI).append("&start="+offset+"&q="+Uri.encode(keyword));
		if (imageType !=null && !imageType.equals("Any"))
			sb.append("&as_filetype="+imageType);
		if (colorFilter != null && !colorFilter.equals("Any"))
			sb.append("&imgcolor="+ colorFilter);
		if (imageSize != null && !imageSize.equals("Any")){
			String size = null;
			if (imageSize.equals("Small"))
				size = "icon";
			else if (imageSize.equals("Medium"))
				size = Uri.encode("small|medium|large|xlarge");
			else if (imageSize.equals("Large"))
				size = "xxlarge";
			sb.append("&imgsz="+size);
		}
		if (site != null && !site.equals("")) {
			sb.append("&as_sitesearch="+site);
		}
		return sb.toString();
	}
	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
	}
	
	public void onImageSearch (View v) {	
		customLoadMoreDataFromApi(0, true);
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gridimagesearch_menu, menu);
        return true;
    }
}
