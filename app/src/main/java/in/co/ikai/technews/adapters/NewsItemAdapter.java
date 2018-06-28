package in.co.ikai.technews.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.co.ikai.technews.R;
import in.co.ikai.technews.activities.NewsActivity;
import in.co.ikai.technews.dataModels.NewsDataModel;

public class NewsItemAdapter extends ArrayAdapter<NewsDataModel> {

    private Context mContext;

    public NewsItemAdapter(@NonNull Context context, ArrayList<NewsDataModel> models) {
        super(context, 0, models);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        NewsActivity.ViewHolder holder;
        if (convertView == null) {
            convertView
                    = LayoutInflater.from(getContext()).inflate(R.layout.single_news_card
                    , parent, false);
            holder = new NewsActivity.ViewHolder();
            // Find the TextView in the list_item.xml (mapping)
            holder.titleTextView = convertView.findViewById(R.id.txt_news_heading);
            holder.authorNameTextView = convertView.findViewById(R.id.txt_author_name);
            holder.categoryTextView = convertView.findViewById(R.id.txt_category);
            holder.dateTextView = convertView.findViewById(R.id.txt_publishing_time);
            holder.thumbnailImageView = convertView.findViewById(R.id.img_thumbnail);
            convertView.setTag(holder);
        } else {
            holder = (NewsActivity.ViewHolder) convertView.getTag();
        }

        NewsDataModel currentNews = getItem(position);
        if (currentNews != null) {
            holder.titleTextView.setText(currentNews.getTitle());
            Glide.with(mContext)
                    .load(Uri.parse(currentNews.getThumbnail()))
                    .thumbnail(0.3f)
                    .into(holder.thumbnailImageView);

            holder.categoryTextView.setText(currentNews.getSection());

            holder.dateTextView.setText(currentNews.getDate());

            holder.authorNameTextView.setText(currentNews.getAuthor());

        } else {
            holder.titleTextView.setText("");

            holder.authorNameTextView.setText("");

            holder.dateTextView.setText("");

            holder.categoryTextView.setText("");

            Glide.with(mContext)
                    .load(R.drawable.ic_book)
                    .thumbnail(0.3f)
                    .into(holder.thumbnailImageView);
        }

        return convertView;
    }
}
