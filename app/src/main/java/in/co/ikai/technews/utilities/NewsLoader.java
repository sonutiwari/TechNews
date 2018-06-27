package in.co.ikai.technews.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import in.co.ikai.technews.dataModels.NewsDataModel;

public final class NewsLoader extends AsyncTaskLoader<List<NewsDataModel>> {

    public NewsLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public List<NewsDataModel> loadInBackground() {
        List<NewsDataModel> listOfNews = null;
        try {
            URL url = QueryUtils.createURL();
            String jsonResponse = QueryUtils.makeHttpRequest(url);
            listOfNews = QueryUtils.parseJson(jsonResponse);
        } catch (IOException e) {
            Toast.makeText(getContext(), "This error caused: " + e, Toast.LENGTH_SHORT).show();
        }
        return listOfNews;
    }
}
