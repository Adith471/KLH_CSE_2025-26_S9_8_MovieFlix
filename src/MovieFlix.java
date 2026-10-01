import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class MovieFlix {

    private static MovieRecord[] movies;

    private static BufferedReader input =
            new BufferedReader(
                    new InputStreamReader(System.in)
            );

    public static void main(String[] args)
            throws IOException {

        System.out.println(
                "======================================"
        );

        System.out.println(
                "          MOVIEFLIX SEARCH ENGINE"
        );

        System.out.println(
                "======================================"
        );

        String corpusPath = "docs";

        movies =
                FileReaderUtil.readCorpus(
                        corpusPath
                );

        System.out.println();

        System.out.println(
                "Movies loaded: "
                + movies.length
        );

        if (movies.length == 0) {

            System.out.println();

            System.out.println(
                    "No movie records found."
            );

            System.out.println(
                    "Make sure the docs folder contains .txt files."
            );

            return;
        }

        while (true) {

            showMenu();

            String choice =
                    input.readLine();

            if (choice == null) {
                break;
            }

            choice =
                    choice.trim();

            if (choice.equals("1")) {

                kmpSearch();

            } else if (choice.equals("2")) {

                ahoSearch();

            } else if (choice.equals("3")) {

                fuzzySearch();

            } else if (choice.equals("4")) {

                similaritySearch();

            } else if (choice.equals("5")) {

                showAllMovies();

            } else if (choice.equals("0")) {

                System.out.println();

                System.out.println(
                        "Thank you for using MovieFlix!"
                );

                break;

            } else {

                System.out.println();

                System.out.println(
                        "Invalid choice. Please enter 0 to 5."
                );
            }
        }
    }

    private static void showMenu() {

        System.out.println();

        System.out.println(
                "--------------------------------------"
        );

        System.out.println(
                "1. KMP Pattern Search"
        );

        System.out.println(
                "2. Aho-Corasick Multi-Keyword Search"
        );

        System.out.println(
                "3. Fuzzy Movie Search"
        );

        System.out.println(
                "4. Movie Similarity"
        );

        System.out.println(
                "5. Display Movies"
        );

        System.out.println(
                "0. Exit"
        );

        System.out.println(
                "--------------------------------------"
        );

        System.out.print(
                "Enter your choice: "
        );
    }

    private static void kmpSearch()
            throws IOException {

        System.out.println();

        System.out.println(
                "========== KMP PATTERN SEARCH =========="
        );

        System.out.print(
                "Enter movie title, actor, genre or keyword: "
        );

        String pattern =
                input.readLine();

        if (pattern == null
                || pattern.trim().isEmpty()) {

            System.out.println(
                    "Search pattern cannot be empty."
            );

            return;
        }

        pattern =
                pattern.trim();

        int matches = 0;

        long start =
                System.nanoTime();

        for (int i = 0;
                i < movies.length;
                i++) {

            String text =
                    movies[i].getFullText();

            if (KMP.search(
                    text,
                    pattern)) {

                System.out.println();

                System.out.println(
                        (matches + 1)
                        + ". "
                        + movies[i]
                );

                matches++;
            }
        }

        long end =
                System.nanoTime();

        System.out.println();

        System.out.println(
                "Matches found: "
                + matches
        );

        System.out.println(
                "Search time: "
                + (end - start)
                + " ns"
        );
    }

    private static void ahoSearch()
            throws IOException {

        System.out.println();

        System.out.println(
                "====== AHO-CORASICK MULTI-KEYWORD SEARCH ======"
        );

        System.out.print(
                "Enter keywords separated by comma: "
        );

        String line =
                input.readLine();

        if (line == null
                || line.trim().isEmpty()) {

            System.out.println(
                    "Please enter at least one keyword."
            );

            return;
        }

        String[] patterns =
                line.split(",");

        int validPatterns = 0;

        for (int i = 0;
                i < patterns.length;
                i++) {

            patterns[i] =
                    patterns[i].trim();

            if (!patterns[i].isEmpty()) {

                validPatterns++;
            }
        }

        if (validPatterns == 0) {

            System.out.println(
                    "No valid keywords entered."
            );

            return;
        }

        AhoCorasick aho =
                new AhoCorasick(patterns);

        int matches = 0;

        long start =
                System.nanoTime();

        for (int i = 0;
                i < movies.length;
                i++) {

            if (aho.search(
                    movies[i].getFullText())) {

                System.out.println();

                System.out.println(
                        (matches + 1)
                        + ". "
                        + movies[i]
                );

                matches++;
            }
        }

        long end =
                System.nanoTime();

        System.out.println();

        System.out.println(
                "Matching records: "
                + matches
        );

        System.out.println(
                "Search time: "
                + (end - start)
                + " ns"
        );
    }

    private static void fuzzySearch()
            throws IOException {

        System.out.println();

        System.out.println(
                "========== LEVENSHTEIN FUZZY SEARCH =========="
        );

        System.out.print(
                "Enter movie title: "
        );

        String query =
                input.readLine();

        if (query == null
                || query.trim().isEmpty()) {

            System.out.println(
                    "Movie title cannot be empty."
            );

            return;
        }

        query =
                query.trim();

        System.out.print(
                "Maximum edit distance (example: 3): "
        );

        String distanceInput =
                input.readLine();

        int maxDistance;

        try {

            maxDistance =
                    Integer.parseInt(
                            distanceInput.trim()
                    );

        } catch (Exception e) {

            System.out.println(
                    "Please enter a valid number."
            );

            return;
        }

        if (maxDistance < 0) {

            System.out.println(
                    "Distance cannot be negative."
            );

            return;
        }

        int matches = 0;

        for (int i = 0;
                i < movies.length;
                i++) {

            int distance =
                    Levenshtein.distance(
                            query,
                            movies[i].getTitle()
                    );

            if (distance <= maxDistance) {

                System.out.println();

                System.out.println(
                        (matches + 1)
                        + ". "
                        + movies[i].getTitle()
                );

                System.out.println(
                        "Edit distance: "
                        + distance
                );

                System.out.println(
                        movies[i]
                );

                matches++;
            }
        }

        System.out.println();

        System.out.println(
                "Similar movies found: "
                + matches
        );
    }

    private static void similaritySearch()
            throws IOException {

        System.out.println();

        System.out.println(
                "========== TF-IDF + COSINE SIMILARITY =========="
        );

        if (movies.length < 2) {

            System.out.println(
                    "At least two movies are required."
            );

            return;
        }

        showMovieNumbers();

        System.out.println();

        System.out.print(
                "Enter first movie number: "
        );

        String firstInput =
                input.readLine();

        System.out.print(
                "Enter second movie number: "
        );

        String secondInput =
                input.readLine();

        int first;
        int second;

        try {

            first =
                    Integer.parseInt(
                            firstInput.trim()
                    );

            second =
                    Integer.parseInt(
                            secondInput.trim()
                    );

        } catch (Exception e) {

            System.out.println();

            System.out.println(
                    "Please enter valid movie numbers."
            );

            return;
        }

        if (first < 1
                || first > movies.length
                || second < 1
                || second > movies.length) {

            System.out.println();

            System.out.println(
                    "Invalid movie number."
            );

            return;
        }

        if (first == second) {

            System.out.println();

            System.out.println(
                    "Please select two different movies."
            );

            return;
        }

        MovieRecord movie1 =
                movies[first - 1];

        MovieRecord movie2 =
                movies[second - 1];

        long start =
                System.nanoTime();

        double similarity =
                TFIDFCosine.cosineSimilarity(
                        movie1.getFullText(),
                        movie2.getFullText()
                );

        long end =
                System.nanoTime();

        System.out.println();

        System.out.println(
                "Movie 1: "
                + movie1.getTitle()
        );

        System.out.println(
                "Movie 2: "
                + movie2.getTitle()
        );

        System.out.println();

        System.out.printf(
                "Cosine Similarity: %.4f%n",
                similarity
        );

        System.out.println(
                "Similarity Percentage: "
                + String.format(
                        "%.2f",
                        similarity * 100
                )
                + "%"
        );

        System.out.println();

        System.out.println(
                "Calculation time: "
                + (end - start)
                + " ns"
        );
    }

    private static void showMovieNumbers() {

        int limit =
                movies.length;

        if (limit > 30) {

            limit = 30;
        }

        System.out.println();

        System.out.println(
                "Available Movies:"
        );

        System.out.println();

        for (int i = 0;
                i < limit;
                i++) {

            System.out.println(
                    (i + 1)
                    + ". "
                    + movies[i].getTitle()
            );
        }

        if (movies.length > 30) {

            System.out.println();

            System.out.println(
                    "... "
                    + (movies.length - 30)
                    + " more movies loaded."
            );
        }
    }

    private static void showAllMovies() {

        System.out.println();

        System.out.println(
                "========== MOVIE DATABASE =========="
        );

        for (int i = 0;
                i < movies.length;
                i++) {

            System.out.println();

            System.out.println(
                    (i + 1)
                    + ". "
                    + movies[i]
            );
        }

        System.out.println();

        System.out.println(
                "Total movies: "
                + movies.length
        );
    }
}
