
package org.puc;

public class Node {
    Entry entry;
    Node next;

    Node(Entry entry) {
        this.entry = entry;
    }

    int key() {
        return entry.code();
    }

    int value() {
        return entry.value();
    }
}
