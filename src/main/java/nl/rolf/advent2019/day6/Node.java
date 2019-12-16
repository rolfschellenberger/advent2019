package nl.rolf.advent2019.day6;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Node {

    private final String name;
    private final Set<Node> children = new HashSet<>();
    private Node parent;

    Node(final String name) {
        this.name = name;
    }

    void addChild(final Node node) {
        this.children.add(node);
        node.setParent(this);
    }

    Set<Node> getChildren() {
        return children;
    }

    Node getParent() {
        return parent;
    }

    private void setParent(final Node parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Node node = (Node) o;
        return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
