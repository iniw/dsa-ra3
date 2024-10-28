package org.puc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

class MathUtilsTest {
    @Test
    void test() {
        var rand = new Random();
        for (int run = 1; run < 10000; ++run) {
            var d = rand.nextDouble();
            assertEquals(Math.abs(d), MathUtils.abs(d));
            assertEquals(Math.floor(d), MathUtils.floor(d));

            var i = rand.nextInt();
            assertEquals(Math.abs(i), MathUtils.abs(i));
            assertEquals(Math.floor(i), MathUtils.floor(i));
        }
    }
}
