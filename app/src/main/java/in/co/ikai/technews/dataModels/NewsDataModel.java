package in.co.ikai.technews.dataModels;

public class NewsDataModel {
    private String title;
    private String author;
    private String url;
    private String date;
    private String section;
    private String thumbnail;

    public NewsDataModel(String title, String author, String url, String date
            , String section, String thumbnail) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.date = date;
        this.section = section;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public String getSection() {
        return section;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
