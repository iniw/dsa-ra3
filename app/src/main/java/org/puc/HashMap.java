package org.puc;

public class HashMap {
    private LinkedList[] bucket;
    private int bucketSize;
    private Hasher hasher;

    HashMap(int size, Hasher hasher) {
        bucketSize = size;
        bucket = new LinkedList[bucketSize];
        this.hasher = hasher;
    }

    void insert(int key, int value) {
        var hash = hasher.hash(key, bucketSize);
        if (bucket[hash] == null)
            bucket[hash] = new LinkedList();

        bucket[hash].push(new Node(new Entry(key, value)));
    }

    Node lookup(int key) {
        var list = bucket[hasher.hash(key, bucketSize)];
        if (list == null || list.isEmpty())
            return null;

        for (var node = list.head; node != null; node = node.next)
            if (node.key() == key)
                return node;

        return null;
    }
}
