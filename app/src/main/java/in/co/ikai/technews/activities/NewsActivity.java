package in.co.ikai.technews.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.co.ikai.technews.R;
import in.co.ikai.technews.adapters.NewsItemAdapter;
import in.co.ikai.technews.dataModels.NewsDataModel;
import in.co.ikai.technews.utilities.NewsLoader;

import static android.view.View.GONE;

public class NewsActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsDataModel>> {

    private NewsItemAdapter adapter;

    private boolean isConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Declaration and initialization ConnectivityManager for checking internet connection
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        /* At the beginning check the connection with internet and save result
         *  to (boolean) variable isConnected
         * Checking if network is available
         * If TRUE - work with LoaderManager
         * If FALSE - hide loading spinner and show emptyStateTextView
         */
        checkConnection(cm);


        ListView listView = findViewById(R.id.news_list_view);
        adapter = new NewsItemAdapter(this, new ArrayList<NewsDataModel>());
        listView.setAdapter(adapter);

        // Find a reference to the empty view
        TextView mEmptyStateTextView = findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        // Circle progress
        /*
      Circle progress bar
     */
        View circleProgressBar = findViewById(R.id.loading_spinner);


        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            // Initialize the loader.
            int LOADER_ID = 0;
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        } else {
            // Progress bar mapping.
            circleProgressBar.setVisibility(GONE);
            // Set empty state text to display "No internet connection."
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsDataModel news = adapter.getItem(i);
                String url;
                if (news != null) {
                    url = news.getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } else {
                    Toast.makeText(NewsActivity.this, "Null url"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @NonNull
    @Override
    public Loader<List<NewsDataModel>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsDataModel>> loader
            , List<NewsDataModel> data) {
        if (data != null) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsDataModel>> loader) {

    }

    public void checkConnection(ConnectivityManager connectivityManager) {
        // Status of internet connection
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


    public static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView authorNameTextView;
        public TextView dateTextView;
        public TextView categoryTextView;
    }
}
