package org.puc;

import java.util.Arrays;
import java.util.Random;

public class App {

    public static final int[] BUCKET_SIZES = { 10, 100, 1000, 10000, 100000 };

    public static final int[] DATASET_SIZES = { 100_000, 500_000, 1_000_000, 5_000_000, 100_000_000 };

    interface Hash {
        int hash(int key, int bucketSize);
    }

    public static final Hash[] HASHERS = {
            new Hash() {
                public int hash(int key, int bucketSize) {
                    return key % bucketSize;
                }
            },
            new Hash() {
                public int hash(int key, int bucketSize) {
                    final double K = 0.44;
                    return (int) Math.floor(bucketSize * ((K * key) % 1));
                }
            },
            new Hash() {
                public int hash(int key, int bucketSize) {
                    return key % bucketSize;
                }
            },
    };

    public static final long SEED = 0xDEADBEEF;

    public static void main(String[] args) {
        var rand = new Random(SEED);
        for (int i = 0; i < 3; ++i) {
            // Fill bucket
            var bucketSize = BUCKET_SIZES[rand.nextInt(BUCKET_SIZES.length)];
            var bucket = new LinkedList[bucketSize];
            for (int b = 0; b < bucketSize; ++b) {
                bucket[b] = new LinkedList();
            }

            // Fill dataset
            var datasetSize = DATASET_SIZES[rand.nextInt(DATASET_SIZES.length)];
            var dataset = new Registro[datasetSize];
            for (int d = 0; d < datasetSize; ++d) {
                dataset[d] = new Registro(rand.nextInt(), rand.nextInt(100000000, 999999999));
            }

            System.out.printf("bucket size: %d\n", bucketSize);
            System.out.printf("dataset size: %d\n", datasetSize);

            for (int h = 0; h < HASHERS.length; ++h) {
                var hasher = HASHERS[h];

                for (int d = 0; d < datasetSize; ++d) {
                    var registro = dataset[d];
                    var key = registro.codigo();
                    var value = registro.valor();

                    var hash = hasher.hash(key, bucketSize);
                    bucket[hash].push(new Node(key, value));
                }
            }

        }
    }
}
