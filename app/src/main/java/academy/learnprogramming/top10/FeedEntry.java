package academy.learnprogramming.top10;

public class FeedEntry {
    private String name;
    private String artist;
    private String releaseDate;
    private String imageURL;
    private String summary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return  "name=" + name + '\n' +
                "artist=" + artist + '\n' +
                "releaseDate=" + releaseDate + '\n' +
                "imageURL=" + imageURL + '\n';
    }
}
