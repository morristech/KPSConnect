package me.msfjarvis.kpsconnect.rssmanager;

/**
 * Created by Kartik_ch on 11/15/2015.
 */
public class RssItem {
    private String title;
    private String description;
    private String link;
    private String sourceName;
    private String sourceUrl;
    private String sourceUrlShort;
    private String imageUrl;
    private String category;
    private String pubDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getSourceUrlShort() {
        return sourceUrlShort;
    }

    public void setSourceUrlShort(String sourceUrlShort) {
        this.sourceUrlShort = sourceUrlShort;
    }
}
