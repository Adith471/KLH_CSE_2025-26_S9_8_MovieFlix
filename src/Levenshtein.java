public class Levenshtein {

    public static int distance(
            String a,
            String b) {

        if (a == null) {
            a = "";
        }

        if (b == null) {
            b = "";
        }

        a = a.toLowerCase();
        b = b.toLowerCase();

        int n = a.length();
        int m = b.length();

        int[][] dp =
                new int[n + 1][m + 1];

        // Convert a prefix of a to empty string
        for (int i = 0; i <= n; i++) {

            dp[i][0] = i;
        }

        // Convert empty string to prefix of b
        for (int j = 0; j <= m; j++) {

            dp[0][j] = j;
        }

        // Dynamic Programming
        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                int cost;

                if (a.charAt(i - 1)
                        == b.charAt(j - 1)) {

                    cost = 0;

                } else {

                    cost = 1;
                }

                int deletion =
                        dp[i - 1][j] + 1;

                int insertion =
                        dp[i][j - 1] + 1;

                int substitution =
                        dp[i - 1][j - 1]
                                + cost;

                dp[i][j] =
                        Math.min(
                                Math.min(
                                        deletion,
                                        insertion
                                ),
                                substitution
                        );
            }
        }

        return dp[n][m];
    }

    public static boolean isSimilar(
            String a,
            String b,
            int maximumDistance) {

        if (maximumDistance < 0) {
            return false;
        }

        return distance(a, b)
                <= maximumDistance;
    }
}
