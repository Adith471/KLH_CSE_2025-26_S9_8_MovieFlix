public class TFIDFCosine {

    private static String[] tokenize(String text) {

        text = text.toLowerCase();

        String[] temp = text.split("[^a-z0-9]+");

        int count = 0;

        for (int i = 0; i < temp.length; i++) {

            if (temp[i].length() > 0) {
                count++;
            }
        }

        String[] result = new String[count];

        int index = 0;

        for (int i = 0; i < temp.length; i++) {

            if (temp[i].length() > 0) {
                result[index++] = temp[i];
            }
        }

        return result;
    }

    private static boolean contains(
            String[] array,
            int length,
            String word) {

        for (int i = 0; i < length; i++) {

            if (array[i].equals(word)) {
                return true;
            }
        }

        return false;
    }

    private static int countOccurrences(
            String[] words,
            String word) {

        int count = 0;

        for (int i = 0; i < words.length; i++) {

            if (words[i].equals(word)) {
                count++;
            }
        }

        return count;
    }

    public static double cosineSimilarity(
            String document1,
            String document2) {

        String[] words1 = tokenize(document1);
        String[] words2 = tokenize(document2);

        String[] vocabulary =
                new String[words1.length + words2.length];

        int vocabSize = 0;

        for (int i = 0; i < words1.length; i++) {

            if (!contains(vocabulary, vocabSize, words1[i])) {
                vocabulary[vocabSize++] = words1[i];
            }
        }

        for (int i = 0; i < words2.length; i++) {

            if (!contains(vocabulary, vocabSize, words2[i])) {
                vocabulary[vocabSize++] = words2[i];
            }
        }

        double dotProduct = 0;
        double magnitude1 = 0;
        double magnitude2 = 0;

        for (int i = 0; i < vocabSize; i++) {

            String word = vocabulary[i];

            int tf1 = countOccurrences(words1, word);
            int tf2 = countOccurrences(words2, word);

            int df = 0;

            if (contains(words1, words1.length, word)) {
                df++;
            }

            if (contains(words2, words2.length, word)) {
                df++;
            }

            double idf;

            if (df == 1) {
                idf = Math.log(2.0);
            } else {
                idf = 0.0;
            }

            double weight1 = tf1 * idf;
            double weight2 = tf2 * idf;

            dotProduct += weight1 * weight2;

            magnitude1 += weight1 * weight1;
            magnitude2 += weight2 * weight2;
        }

        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0.0;
        }

        return dotProduct /
                (Math.sqrt(magnitude1) *
                 Math.sqrt(magnitude2));
    }
}
