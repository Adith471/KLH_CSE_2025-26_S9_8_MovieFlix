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

        System.out.println(
                "Movies loaded: "
                        + movies.length
        );

        if (movies.length == 0) {

            System.out.println(
                    "No movie records found."
            );

            System.out.println(
                    "Check the docs folder."
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

            choice = choice.trim();

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
                        "Invalid choice. Please enter 0-5."
                );
            }
        }
    }

    // =========================================================
    // MENU
    // =========================================================

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

    // =========================================================
    // 1. KMP PATTERN SEARCH
    // =========================================================

    private static void kmpSearch()
            throws IOException {

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

        int matches = 0;

        long start =
                System.nanoTime();

        for (MovieRecord movie :
                movies) {

            if (KMP.search(
                    movie.getFullText(),
                    pattern)) {

                System.out.println();

                System.out.println(movie);

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

    // =========================================================
    // 2. AHO-CORASICK MULTI-KEYWORD SEARCH
    // =========================================================

    private static void ahoSearch()
            throws IOException {

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
                    patterns[i]
                            .trim()
                            .toLowerCase();

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

        for (MovieRecord movie :
                movies) {

            if (aho.search(
                    movie.getFullText())) {

                System.out.println();

                System.out.println(movie);

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

    // =========================================================
    // 3. FUZZY MOVIE SEARCH
    //    LEVENSHTEIN DISTANCE
    // =========================================================

    private static void fuzzySearch()
            throws IOException {

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
                query.trim().toLowerCase();

        /*
         * The edit-distance threshold is controlled
         * internally by the application.
         *
         * The user/client does NOT enter it.
         */
        int maxDistance = 2;

        int matches = 0;

        long start =
                System.nanoTime();

        for (MovieRecord movie :
                movies) {

            String title =
                    movie.getTitle();

            if (title == null
                    || title.trim().isEmpty()) {

                continue;
            }

            /*
             * Split the movie title into individual
             * words.
             *
             * Example:
             *
             * "Baahubali: The Beginning"
             *
             * becomes:
             *
             * Baahubali
             * The
             * Beginning
             */
            String[] words =
                    title.toLowerCase()
                            .split("[^a-z0-9]+");

            int bestDistance =
                    Integer.MAX_VALUE;

            /*
             * Compare the user's query with
             * every individual word.
             */
            for (String word : words) {

                if (word.isEmpty()) {
                    continue;
                }

                int distance =
                        Levenshtein.distance(
                                query,
                                word
                        );

                if (distance < bestDistance) {

                    bestDistance =
                            distance;
                }
            }

            /*
             * If the closest word is within
             * the allowed edit distance,
             * display the movie.
             */
            if (bestDistance <= maxDistance) {

                System.out.println();

                System.out.println(
                        "Title: "
                                + title
                );

                System.out.println(
                        "Edit distance: "
                                + bestDistance
                );

                System.out.println(movie);

                matches++;
            }
        }

        long end =
                System.nanoTime();

        System.out.println();

        System.out.println(
                "Similar movies found: "
                        + matches
        );

        System.out.println(
                "Search time: "
                        + (end - start)
                        + " ns"
        );
    }

    // =========================================================
    // 4. MOVIE SIMILARITY
    //    TF-IDF + COSINE SIMILARITY
    // =========================================================

    private static void similaritySearch()
            throws IOException {

        if (movies.length < 2) {

            System.out.println(
                    "At least two movies are required."
            );

            return;
        }

        showMovieNumbers();

        System.out.print(
                "Enter first movie number: "
        );

        int first;

        try {

            first =
                    Integer.parseInt(
                            input.readLine().trim()
                    );

        } catch (Exception e) {

            System.out.println(
                    "Invalid movie number."
            );

            return;
        }

        System.out.print(
                "Enter second movie number: "
        );

        int second;

        try {

            second =
                    Integer.parseInt(
                            input.readLine().trim()
                    );

        } catch (Exception e) {

            System.out.println(
                    "Invalid movie number."
            );

            return;
        }

        if (first < 1
                || first > movies.length
                || second < 1
                || second > movies.length) {

            System.out.println(
                    "Invalid movie number."
            );

            return;
        }

        if (first == second) {

            System.out.println(
                    "Please select two different movies."
            );

            return;
        }

        MovieRecord movie1 =
                movies[first - 1];

        MovieRecord movie2 =
                movies[second - 1];

        /*
         * Build the complete movie corpus.
         *
         * TF-IDF should calculate IDF using
         * all movies, not only the two selected
         * movies.
         */
        String[] corpus =
                new String[movies.length];

        for (int i = 0;
             i < movies.length;
             i++) {

            corpus[i] =
                    movies[i].getFullText();
        }

        long start =
                System.nanoTime();

        double similarity =
                TFIDFCosine.cosineSimilarity(
                        movie1.getFullText(),
                        movie2.getFullText(),
                        corpus
                );

        long end =
                System.nanoTime();

        System.out.println();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "        MOVIE SIMILARITY RESULT"
        );

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Movie 1: "
                        + movie1.getTitle()
        );

        System.out.println(
                "Movie 2: "
                        + movie2.getTitle()
        );

        System.out.printf(
                "Cosine Similarity: %.4f%n",
                similarity
        );

        System.out.printf(
                "Similarity Percentage: %.2f%%%n",
                similarity * 100
        );

        System.out.println(
                "Calculation time: "
                        + (end - start)
                        + " ns"
        );

        System.out.println(
                "======================================"
        );
    }

    // =========================================================
    // DISPLAY MOVIE NUMBERS
    // =========================================================

    private static void showMovieNumbers() {

        int limit =
                movies.length;

        /*
         * Display only the first 30 movies
         * when selecting movies for similarity.
         */
        if (limit > 30) {

            limit = 30;
        }

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

            System.out.println(
                    "... "
                            + (movies.length - 30)
                            + " more movies loaded."
            );
        }
    }

    // =========================================================
    // DISPLAY ALL MOVIES
    // =========================================================

    private static void showAllMovies() {

        for (int i = 0;
             i < movies.length;
             i++) {

            System.out.println(
                    (i + 1)
                            + ". "
                            + movies[i]
            );
        }
    }
}
