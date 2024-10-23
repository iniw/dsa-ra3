package org.puc;

public class LinkedList {
    Node head;
    Node tail;

    void push(Node item) {
        if (head == null) {
            tail = head = item;
        } else {
            tail = tail.next = item;
        }
    }

    Node pop() throws Exception {
        if (head == null)
            throw new Exception("Cannot pop an empty queue.");

        var last = head;
        head = head.next;
        return last;
    }

    public boolean is_empty() {
        return head == null;
    }
}
