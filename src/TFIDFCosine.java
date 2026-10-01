import java.util.*;

public class TFIDFCosine {

    private static String[] tokenize(
            String text) {

        if (text == null) {
            return new String[0];
        }

        text = text.toLowerCase();

        String[] temp =
                text.split("[^a-z0-9]+");

        ArrayList<String> words =
                new ArrayList<>();

        for (String word : temp) {

            if (!word.isEmpty()) {

                words.add(word);
            }
        }

        return words.toArray(
                new String[0]);
    }

    private static int countOccurrences(
            String[] words,
            String word) {

        int count = 0;

        for (String current : words) {

            if (current.equals(word)) {

                count++;
            }
        }

        return count;
    }

    private static boolean contains(
            Set<String> set,
            String word) {

        return set.contains(word);
    }

    private static double calculateTF(
            String[] words,
            String term) {

        if (words.length == 0) {
            return 0.0;
        }

        int count =
                countOccurrences(
                        words,
                        term);

        return (double) count
                / words.length;
    }

    private static int documentFrequency(
            String term,
            List<String[]> documents) {

        int count = 0;

        for (String[] document :
                documents) {

            Set<String> uniqueWords =
                    new HashSet<>(
                            Arrays.asList(document));

            if (contains(
                    uniqueWords,
                    term)) {

                count++;
            }
        }

        return count;
    }

    private static double calculateIDF(
            String term,
            List<String[]> documents) {

        int totalDocuments =
                documents.size();

        int df =
                documentFrequency(
                        term,
                        documents);

        if (df == 0) {
            return 0.0;
        }

        /*
         * Smoothed IDF:
         *
         * log((N + 1) / (df + 1)) + 1
         *
         * This avoids zero/undefined values.
         */
        return Math.log(
                (double) (totalDocuments + 1)
                        / (df + 1)
        ) + 1.0;
    }

    public static double cosineSimilarity(
            String document1,
            String document2,
            String[] corpus) {

        if (document1 == null
                || document2 == null) {

            return 0.0;
        }

        if (corpus == null
                || corpus.length == 0) {

            return cosineSimilarityUsingTwoDocuments(
                    document1,
                    document2
            );
        }

        List<String[]> documents =
                new ArrayList<>();

        for (String document : corpus) {

            documents.add(
                    tokenize(document)
            );
        }

        String[] words1 =
                tokenize(document1);

        String[] words2 =
                tokenize(document2);

        Set<String> vocabulary =
                new HashSet<>();

        vocabulary.addAll(
                Arrays.asList(words1));

        vocabulary.addAll(
                Arrays.asList(words2));

        double dotProduct = 0.0;

        double magnitude1 = 0.0;

        double magnitude2 = 0.0;

        for (String word :
                vocabulary) {

            double tf1 =
                    calculateTF(
                            words1,
                            word);

            double tf2 =
                    calculateTF(
                            words2,
                            word);

            double idf =
                    calculateIDF(
                            word,
                            documents);

            double weight1 =
                    tf1 * idf;

            double weight2 =
                    tf2 * idf;

            dotProduct +=
                    weight1 * weight2;

            magnitude1 +=
                    weight1 * weight1;

            magnitude2 +=
                    weight2 * weight2;
        }

        if (magnitude1 == 0.0
                || magnitude2 == 0.0) {

            return 0.0;
        }

        return dotProduct /
                (
                        Math.sqrt(magnitude1)
                                * Math.sqrt(magnitude2)
                );
    }

    /*
     * Compatibility method.
     *
     * If another part of the program calls:
     *
     * cosineSimilarity(document1, document2)
     *
     * it will still work.
     */
    public static double cosineSimilarity(
            String document1,
            String document2) {

        return cosineSimilarityUsingTwoDocuments(
                document1,
                document2
        );
    }

    private static double cosineSimilarityUsingTwoDocuments(
            String document1,
            String document2) {

        String[] words1 =
                tokenize(document1);

        String[] words2 =
                tokenize(document2);

        Set<String> vocabulary =
                new HashSet<>();

        vocabulary.addAll(
                Arrays.asList(words1));

        vocabulary.addAll(
                Arrays.asList(words2));

        double dotProduct = 0.0;

        double magnitude1 = 0.0;

        double magnitude2 = 0.0;

        for (String word :
                vocabulary) {

            double tf1 =
                    calculateTF(
                            words1,
                            word);

            double tf2 =
                    calculateTF(
                            words2,
                            word);

            double weight1 = tf1;

            double weight2 = tf2;

            dotProduct +=
                    weight1 * weight2;

            magnitude1 +=
                    weight1 * weight1;

            magnitude2 +=
                    weight2 * weight2;
        }

        if (magnitude1 == 0.0
                || magnitude2 == 0.0) {

            return 0.0;
        }

        return dotProduct /
                (
                        Math.sqrt(magnitude1)
                                * Math.sqrt(magnitude2)
                );
    }
}
