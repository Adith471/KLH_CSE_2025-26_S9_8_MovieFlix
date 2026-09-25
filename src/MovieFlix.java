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

        System.out.println("======================================");
        System.out.println("          MOVIEFLIX SEARCH ENGINE");
        System.out.println("======================================");

        String corpusPath = "corpus";

        movies = FileReaderUtil.readCorpus(corpusPath);

        System.out.println(
                "Movies loaded: " + movies.length
        );

        if (movies.length == 0) {

            System.out.println(
                    "No movie records found."
            );

            System.out.println(
                    "Check the corpus folder."
            );

            return;
        }

        while (true) {

            showMenu();

            String choice = input.readLine();

            if (choice == null) {
                break;
            }

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

                System.out.println(
                        "Thank you for using MovieFlix!"
                );

                break;

            } else {

                System.out.println(
                        "Invalid choice."
                );
            }
        }
    }

    private static void showMenu() {

        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("1. KMP Pattern Search");
        System.out.println("2. Aho-Corasick Multi-Keyword Search");
        System.out.println("3. Fuzzy Movie Search");
        System.out.println("4. Movie Similarity");
        System.out.println("5. Display Movies");
        System.out.println("0. Exit");
        System.out.println("--------------------------------------");
        System.out.print("Enter your choice: ");
    }

    private static void kmpSearch()
            throws IOException {

        System.out.print(
                "Enter movie title, actor, genre or keyword: "
        );

        String pattern = input.readLine();

        int matches = 0;

        long start = System.nanoTime();

        for (int i = 0; i < movies.length; i++) {

            String text = movies[i].getFullText();

            if (KMP.search(text, pattern)) {

                System.out.println();
                System.out.println(movies[i]);

                matches++;
            }
        }

        long end = System.nanoTime();

        System.out.println();
        System.out.println(
                "Matches found: " + matches
        );

        System.out.println(
                "Search time: " +
                (end - start) +
                " ns"
        );
    }

    private static void ahoSearch()
            throws IOException {

        System.out.print(
                "Enter keywords separated by comma: "
        );

        String line = input.readLine();

        String[] patterns = line.split(",");

        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = patterns[i].trim();
        }

        AhoCorasick aho =
                new AhoCorasick(patterns);

        int matches = 0;

        long start = System.nanoTime();

        for (int i = 0; i < movies.length; i++) {

            if (aho.search(
                    movies[i].getFullText())) {

                System.out.println();
                System.out.println(movies[i]);

                matches++;
            }
        }

        long end = System.nanoTime();

        System.out.println();
        System.out.println(
                "Matching records: " + matches
        );

        System.out.println(
                "Search time: " +
                (end - start) +
                " ns"
        );
    }

    private static void fuzzySearch()
            throws IOException {

        System.out.print(
                "Enter movie title: "
        );

        String query = input.readLine();

        System.out.print(
                "Maximum edit distance: "
        );

        int maxDistance =
                Integer.parseInt(input.readLine());

        int matches = 0;

        for (int i = 0; i < movies.length; i++) {

            int distance =
                    Levenshtein.distance(
                            query,
                            movies[i].getTitle()
                    );

            if (distance <= maxDistance) {

                System.out.println();
                System.out.println(
                        movies[i].getTitle()
                );

                System.out.println(
                        "Edit distance: " +
                        distance
                );

                System.out.println(
                        movies[i]
                );

                matches++;
            }
        }

        System.out.println();
        System.out.println(
                "Similar movies found: " +
                matches
        );
    }

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

        int first =
                Integer.parseInt(input.readLine());

        System.out.print(
                "Enter second movie number: "
        );

        int second =
                Integer.parseInt(input.readLine());

        if (first < 1 ||
                first > movies.length ||
                second < 1 ||
                second > movies.length) {

            System.out.println(
                    "Invalid movie number."
            );

            return;
        }

        MovieRecord movie1 =
                movies[first - 1];

        MovieRecord movie2 =
                movies[second - 1];

        double similarity =
                TFIDFCosine.cosineSimilarity(
                        movie1.getFullText(),
                        movie2.getFullText()
                );

        System.out.println();
        System.out.println(
                "Movie 1: " + movie1.getTitle()
        );

        System.out.println(
                "Movie 2: " + movie2.getTitle()
        );

        System.out.println(
                "Cosine Similarity: " +
                similarity
        );
    }

    private static void showMovieNumbers() {

        int limit = movies.length;

        if (limit > 30) {
            limit = 30;
        }

        System.out.println();

        for (int i = 0; i < limit; i++) {

            System.out.println(
                    (i + 1) +
                    ". " +
                    movies[i].getTitle()
            );
        }

        if (movies.length > 30) {

            System.out.println(
                    "... " +
                    (movies.length - 30) +
                    " more movies loaded."
            );
        }
    }

    private static void showAllMovies() {

        for (int i = 0; i < movies.length; i++) {

            System.out.println(
                    (i + 1) +
                    ". " +
                    movies[i]
            );
        }
    }
}
