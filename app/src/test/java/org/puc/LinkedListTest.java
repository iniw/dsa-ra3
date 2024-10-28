package org.puc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

class LinkedListTest {
    @Test
    void test() {
        var linkedList = new LinkedList();
        assertEquals(linkedList.isEmpty(), true);

        var numItems = 1000;

        var rand = new Random();
        for (int run = 0; run < numItems; run++) {
            assertEquals(linkedList.isEmpty(), true);

            var codes = new int[] { rand.nextInt(), rand.nextInt() };
            var values = new int[] { rand.nextInt(), rand.nextInt() };

            linkedList.push(new Node(new Entry(codes[0], values[0])));
            linkedList.push(new Node(new Entry(codes[1], values[1])));

            assertEquals(linkedList.isEmpty(), false);

            assertDoesNotThrow(() -> {
                var node = linkedList.pop_front();
                assertEquals(node.entry.code(), codes[0]);
                assertEquals(node.entry.value(), values[0]);

                node = linkedList.pop_front();
                assertEquals(node.entry.code(), codes[1]);
                assertEquals(node.entry.value(), values[1]);
            });

            assertEquals(linkedList.isEmpty(), true);
        }

        assertThrows(Exception.class, () -> linkedList.pop_front());
    }
}
