package org.puc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

class HashMapTest {
    @Test
    void test() {
        var hashmapSize = 100;

        var hashmap = new HashMap(hashmapSize, Hasher.modHasher());

        var rand = new Random(hashmapSize);
        for (int i = 1; i < hashmapSize * hashmapSize; ++i) {
            var key = i;
            var value = rand.nextInt();

            hashmap.insert(key, value);

            var lookup = hashmap.lookup(key);
            assertNotNull(lookup);

            assertEquals(lookup.key(), key);
            assertEquals(lookup.value(), value);
        }
    }
}
