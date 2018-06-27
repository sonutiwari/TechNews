package in.co.ikai.technews.utilities;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.co.ikai.technews.dataModels.NewsDataModel;

public class QueryUtils {
    private static final String TAG = "Sonu";

    /**
     * @return String url.
     */
    private static String createStringUrl() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("show-fields", "thumbnail")
                .appendQueryParameter("order-by", "newest")
                .appendQueryParameter("show-references", "author")
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("q", "Technology")
                .appendQueryParameter("api-key", "dc53aa67-2a48-419a-9cee-9b72a2956aaf");

        return builder.build().toString();
    }

    /**
     * @return Well Formed URL
     */
    public static URL createURL() {
        String url = createStringUrl();
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private static String formatDate(String rawDate) {
        String jsonDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern, Locale.US);
        try {
            Date parsedJsonDate = jsonFormatter.parse(rawDate);
            String finalDatePattern = "MMM d, yyy";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.US);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            return "";
        }
    }

    static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(TAG, "makeHttpRequest: " + e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream
                    , Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    static List<NewsDataModel> parseJson(String response) {
        ArrayList<NewsDataModel> listOfNews = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject jsonResults = jsonResponse.getJSONObject("response");
            JSONArray resultsArray = jsonResults.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject oneResult = resultsArray.getJSONObject(i);
                String webTitle = oneResult.getString("webTitle");
                String url = oneResult.getString("webUrl");
                String date = oneResult.getString("webPublicationDate");
                date = formatDate(date);
                String section = oneResult.getString("sectionName");
                JSONArray tagsArray = oneResult.getJSONArray("tags");
                StringBuilder author = new StringBuilder();

                if (tagsArray.length() == 0) {
                    author = new StringBuilder();
                } else {
                    for (int j = 0; j < tagsArray.length(); j++) {
                        JSONObject firstObject = tagsArray.getJSONObject(j);
                        author.append(firstObject.getString("webTitle")).append(". ");
                    }
                }

                String thumbnail = oneResult.getJSONObject("fields").getString("thumbnail");
                listOfNews.add(new NewsDataModel(webTitle, author.toString(), url, date, section
                        , thumbnail));
            }
        } catch (JSONException e) {
            Log.e(TAG, "parseJson: " + e);

        }
        return listOfNews;
    }


}
