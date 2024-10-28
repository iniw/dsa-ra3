package org.puc;

interface Hasher {
    int hash(int key, int bucketSize);

    String name();

    // Simple mod hash
    static Hasher modHasher() {
        return new Hasher() {
            public int hash(int key, int bucketSize) {
                return key % bucketSize;
            }

            public String name() {
                return "Mod";
            }
        };
    }

    // Multiplicative hash
    static Hasher multiplicativeHasher() {
        return new Hasher() {
            public int hash(int key, int bucketSize) {
                // Knuth's constant
                // https://stackoverflow.com/questions/11871245/knuth-multiplicative-hash
                final var A = 2654435769L;
                return (int) MathUtils.abs((A * key) % bucketSize);
            }

            public String name() {
                return "Fracional";
            }
        };
    }

    // Fractional multiplicative hash
    static Hasher fractionalMultiplicativeHasher() {
        return new Hasher() {
            public int hash(int key, int bucketSize) {
                // Knuth's constant
                // https://cstheory.stackexchange.com/questions/9639/how-did-knuth-derive-a
                final var A = 0.6180339887;
                return (int) MathUtils.floor(bucketSize * ((A * key) % 1));
            }

            public String name() {
                return "Multiplicativo";
            }
        };
    }
}
