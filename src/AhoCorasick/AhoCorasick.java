public class AhoCorasick {

    private static final int ALPHABET_SIZE = 128;

    private static class Node {

        int[] next;
        int fail;
        boolean output;

        Node() {

            next = new int[ALPHABET_SIZE];

            for (int i = 0; i < ALPHABET_SIZE; i++) {
                next[i] = -1;
            }

            fail = 0;
            output = false;
        }
    }

    private Node[] nodes;
    private int size;

    public AhoCorasick(String[] patterns) {

        int totalLength = 1;

        for (int i = 0; i < patterns.length; i++) {
            totalLength += patterns[i].length();
        }

        nodes = new Node[totalLength + 1];

        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node();
        }

        size = 1;

        buildTrie(patterns);
        buildFailureLinks();
    }

    private void buildTrie(String[] patterns) {

        for (int p = 0; p < patterns.length; p++) {

            String pattern = patterns[p].toLowerCase();

            int current = 0;

            for (int i = 0; i < pattern.length(); i++) {

                char c = pattern.charAt(i);

                if (c >= ALPHABET_SIZE) {
                    continue;
                }

                if (nodes[current].next[c] == -1) {

                    nodes[current].next[c] = size;
                    size++;
                }

                current = nodes[current].next[c];
            }

            nodes[current].output = true;
        }
    }

    private void buildFailureLinks() {

        int[] queue = new int[size];

        int front = 0;
        int rear = 0;

        for (int c = 0; c < ALPHABET_SIZE; c++) {

            int next = nodes[0].next[c];

            if (next != -1) {

                nodes[next].fail = 0;
                queue[rear++] = next;

            } else {

                nodes[0].next[c] = 0;
            }
        }

        while (front < rear) {

            int current = queue[front++];

            for (int c = 0; c < ALPHABET_SIZE; c++) {

                int next = nodes[current].next[c];

                if (next != -1) {

                    nodes[next].fail =
                            nodes[nodes[current].fail].next[c];

                    if (nodes[nodes[next].fail].output) {
                        nodes[next].output = true;
                    }

                    queue[rear++] = next;

                } else {

                    nodes[current].next[c] =
                            nodes[nodes[current].fail].next[c];
                }
            }
        }
    }

    public boolean search(String text) {

        text = text.toLowerCase();

        int current = 0;

        for (int i = 0; i < text.length(); i++) {

            char c = text.charAt(i);

            if (c >= ALPHABET_SIZE) {
                current = 0;
                continue;
            }

            current = nodes[current].next[c];

            if (nodes[current].output) {
                return true;
            }
        }

        return false;
    }
}
