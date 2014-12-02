package com.example.contentproviderdemo;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {
	// adapter that binds a ListView to a Cursor dataset
	private SimpleCursorAdapter adapter;
	
	// Defines the id of the loader for later reference
    public static final int CONTACT_LOADER_ID = 78;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupCursorAdapter();

		// Find list and bind to adapter
		ListView lvContacts = (ListView) findViewById(R.id.lvContacts);
		lvContacts.setAdapter(adapter);
		
		// Initialize the loader with a special ID and the defined callbacks from above
        getSupportLoaderManager().initLoader(CONTACT_LOADER_ID, new Bundle(), contactsLoader);
	}

	// Create simple cursor adapter to connect the cursor dataset we load with a
	// listview
	private void setupCursorAdapter() {
		// Column data that it will request from the Contacts content provider
		// Column data from cursor to bind views from
		String[] uiBindFrom = { ContactsContract.Contacts.DISPLAY_NAME };

		// View IDs which will have the respective column data inserted
		int[] uiBindTo = { R.id.tvName };

		// Create the simple cursor adapter to use for our list
		// specifying the template to inflate (item_contact)
		// Fields to bind from and to and mark the adapter as observing for
		// changes
		adapter = new SimpleCursorAdapter(this, R.layout.item_contact, null,
				uiBindFrom, uiBindTo,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	}

	// Defines the asynchronous callback for the contacts data loader
	private LoaderManager.LoaderCallbacks<Cursor> contactsLoader = new LoaderCallbacks<Cursor>() {

		// Create and return the actual cursor loader for the contacts data
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			// Define the columns to retrieve
			String[] projectionFields =  new String[] { ContactsContract.Contacts._ID, 
 	               ContactsContract.Contacts.DISPLAY_NAME };
			
			// Construct the loader
    		CursorLoader cursorLoader = new CursorLoader(MainActivity.this,
    				ContactsContract.Contacts.CONTENT_URI, // URI
    				projectionFields,  // projection fields
    				null, // the selection criteria
    				null, // the selection args
    				null // the sort order
    		);
    		
    		// Return the loader for use
    		return cursorLoader;
		}

		// When the system finishes retrieving the Cursor,
		// a call to the onLoadFinished() method takes place.
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			// The swapCursor() method assigns the new Cursor to the adapter
    		adapter.swapCursor(cursor);
		}

		// This method is triggered when the loader is being reset
		// and the loader data is no longer available.
		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			// Clear the Cursor we were using with another call to the swapCursor()
    		adapter.swapCursor(null);
		}
	};
}
