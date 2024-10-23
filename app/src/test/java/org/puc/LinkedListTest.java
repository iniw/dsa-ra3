package org.puc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkedListTest {
    @Test
    void test() {
        var linkedList = new LinkedList();
        assertEquals(linkedList.isEmpty(), true);

        linkedList.push(new Node(0, 1));
        assertEquals(linkedList.isEmpty(), false);

        linkedList.push(new Node(0, 1));
        assertEquals(linkedList.isEmpty(), false);

        assertDoesNotThrow(() -> linkedList.pop());
        assertEquals(linkedList.isEmpty(), false);

        assertDoesNotThrow(() -> linkedList.pop());
        assertEquals(linkedList.isEmpty(), true);

        assertThrows(Exception.class, () -> linkedList.pop());
    }
}
