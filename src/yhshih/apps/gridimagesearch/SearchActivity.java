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
				Log.d("DEBUG", "OnScroll");
				customLoadMoreDataFromApi(totalItemsCount, false);
			}
			
		});
	}
	
	public void onSettings(MenuItem mi){
		Toast.makeText(this, "clicked!",
				Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, FilterActivity.class);
//		// pass data
		i.putExtra("foo", "foo");
		// executing
		startActivityForResult(i, 50);
	}
	public void customLoadMoreDataFromApi(int offset, final boolean clear) {
		// This method probably sends out a network request and appends new data items to your adapter. 
		// Use the offset value and add it as a parameter to your API request to retrieve paginated data.
		// Deserialize API response and then construct new objects to append to the adapter

		final boolean clearResult = clear;
		String query = etQuery.getText().toString();
		Toast.makeText(this, "Searching for "+query, Toast.LENGTH_SHORT)
				.show();
		AsyncHttpClient client = new AsyncHttpClient();
		//https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=barack%20obama&userip=INSERT-USER-IP
		client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&v=1.0&start="+offset+"&q="+Uri.encode(query),
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
