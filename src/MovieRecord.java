public class MovieRecord {

    private String title;
    private String actors;
    private String genre;
    private String keywords;

    public MovieRecord(
            String title,
            String actors,
            String genre,
            String keywords) {

        this.title = title;
        this.actors = actors;
        this.genre = genre;
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public String getActors() {
        return actors;
    }

    public String getGenre() {
        return genre;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getFullText() {

        return title + " "
                + actors + " "
                + genre + " "
                + keywords;
    }

    public boolean contains(String pattern) {

        if (pattern == null) {
            return false;
        }

        return getFullText()
                .toLowerCase()
                .contains(pattern.toLowerCase());
    }

    @Override
    public String toString() {

        return title
                + " | "
                + actors
                + " | "
                + genre
                + " | "
                + keywords;
    }
}
