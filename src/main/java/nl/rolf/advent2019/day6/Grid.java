package nl.rolf.advent2019.day6;

import java.util.*;

public class Grid {

    private Map<Node, Node> nodes = new HashMap<>();

    public void add(final Node parent, final Node child) {
        final Node one = nodes.computeIfAbsent(parent, p -> parent);
        final Node two = nodes.computeIfAbsent(child, c -> child);
        one.addChild(two);
    }

    int getChildren() {
        int count = 0;
        for (final Node node : nodes.keySet()) {
            count += getChildren(node);
        }
        return count;
    }

    private int getChildren(final Node node) {
        int count = 0;
        for (final Node child : node.getChildren()) {
            count++;
            count += getChildren(child);
        }
        return count;
    }

    int getDistance(final String fromName, final String tillName) {
        final Node from = nodes.get(new Node(fromName));
        final Node till = nodes.get(new Node(tillName));

        // Get all parents till the root
        final List<Node> allParents = getAllParents(from, Collections.emptySet());

        // Get all parents till one of the parents of the from till.
        final List<Node> tillParents = getAllParents(till, new HashSet<>(allParents));
        // Get all parents till one of the parents of the from node.
        final List<Node> fromParents = getAllParents(from, new HashSet<>(tillParents));

        // Now the distance is the number of parents minus 4, since the from, till and the intersection node is in both collections.
        return tillParents.size() + fromParents.size() - 4;
    }

    private List<Node> getAllParents(final Node from, final Set<Node> stopCondition) {
        final List<Node> result = new ArrayList<>();
        // Stop when there is no more parent or when this node is in the stop condition set.
        result.add(from);
        if (from.getParent() == null || stopCondition.contains(from)) {
            return result;
        }
        result.addAll(getAllParents(from.getParent(), stopCondition));
        return result;
    }
}
