import java.util.*;

public class AhoCorasick {

    private static class Node {

        Map<Character, Integer> next;

        int fail;

        boolean output;

        Node() {

            next = new HashMap<>();

            fail = 0;

            output = false;
        }
    }

    private Node[] nodes;

    private int size;

    public AhoCorasick(String[] patterns) {

        int totalLength = 1;

        if (patterns != null) {

            for (String pattern : patterns) {

                if (pattern != null) {
                    totalLength += pattern.length();
                }
            }
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

        if (patterns == null) {
            return;
        }

        for (String pattern : patterns) {

            if (pattern == null) {
                continue;
            }

            pattern = pattern.trim().toLowerCase();

            if (pattern.isEmpty()) {
                continue;
            }

            int current = 0;

            for (int i = 0; i < pattern.length(); i++) {

                char c = pattern.charAt(i);

                Integer nextNode =
                        nodes[current].next.get(c);

                if (nextNode == null) {

                    nextNode = size;

                    nodes[current].next.put(
                            c,
                            nextNode
                    );

                    size++;
                }

                current = nextNode;
            }

            nodes[current].output = true;
        }
    }

    private void buildFailureLinks() {

        Queue<Integer> queue =
                new LinkedList<>();

        // First level nodes
        for (int next :
                nodes[0].next.values()) {

            nodes[next].fail = 0;

            queue.add(next);
        }

        while (!queue.isEmpty()) {

            int current = queue.poll();

            for (Map.Entry<Character, Integer> entry :
                    nodes[current].next.entrySet()) {

                char character =
                        entry.getKey();

                int child =
                        entry.getValue();

                int failure =
                        nodes[current].fail;

                while (failure != 0
                        && !nodes[failure].next
                        .containsKey(character)) {

                    failure =
                            nodes[failure].fail;
                }

                if (nodes[failure].next
                        .containsKey(character)
                        && nodes[failure].next
                        .get(character) != child) {

                    nodes[child].fail =
                            nodes[failure].next
                                    .get(character);

                } else {

                    nodes[child].fail = 0;
                }

                if (nodes[nodes[child].fail].output) {

                    nodes[child].output = true;
                }

                queue.add(child);
            }
        }
    }

    public boolean search(String text) {

        if (text == null || text.isEmpty()) {
            return false;
        }

        text = text.toLowerCase();

        int current = 0;

        for (int i = 0;
             i < text.length();
             i++) {

            char c = text.charAt(i);

            while (current != 0
                    && !nodes[current].next
                    .containsKey(c)) {

                current =
                        nodes[current].fail;
            }

            Integer next =
                    nodes[current].next.get(c);

            if (next != null) {

                current = next;

            } else {

                current = 0;
            }

            if (nodes[current].output) {

                return true;
            }
        }

        return false;
    }
}
