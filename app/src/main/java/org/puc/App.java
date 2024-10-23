package org.puc;

import java.util.Random;

public class App {

    public static final int[] BUCKET_SIZES = { 10, 100, 1000, 10000, 100000 };

    public static final int[] DATASET_SIZES = { 1_000_000, 5_000_000, 10_000_000, };

    interface Hash {
        int hash(int key, int bucketSize);
    }

    public static final Hash[] HASHERS = {
            new Hash() {
                // Hash simples
                public int hash(int key, int bucketSize) {
                    return key % bucketSize;
                }
            },
            new Hash() {
                // Hash de multiplicação fracionária
                public int hash(int key, int bucketSize) {
                    final var A = 0.47;
                    return (int) Math.floor(bucketSize * ((A * key) % 1));
                }
            },
            new Hash() {
                // Hash de multiplicação
                public int hash(int key, int bucketSize) {
                    final var A = 2654435769L;
                    return (int) Math.abs((A * key) % bucketSize);
                }
            },
    };

    public static void main(String[] args) {
        var rand = args.length == 0 ? new Random() : new Random(Integer.parseInt(args[0]));

        for (int i = 0; i < 3; ++i) {
            var bucketSize = BUCKET_SIZES[rand.nextInt(BUCKET_SIZES.length)];

            var datasetSize = DATASET_SIZES[rand.nextInt(DATASET_SIZES.length)];
            var dataset = new Registro[datasetSize];
            for (int d = 0; d < datasetSize; ++d) {
                dataset[d] = new Registro(rand.nextInt(), rand.nextInt(100000000, 999999999));
            }

            System.out.printf("Tamanho do bucket: %d\n", bucketSize);
            System.out.printf("Tamanho dos dados: %d\n", datasetSize);

            for (int h = 0; h < HASHERS.length; ++h) {
                // Fill bucket
                var bucket = new LinkedList[bucketSize];
                for (int b = 0; b < bucketSize; ++b) {
                    bucket[b] = new LinkedList();
                }

                var hasher = HASHERS[h];
                var collisions = 0;

                var start = System.nanoTime();
                for (var registro : dataset) {
                    var key = registro.codigo();
                    var value = registro.valor();

                    var hash = hasher.hash(key, bucketSize);
                    if (!bucket[hash].isEmpty())
                        collisions++;

                    bucket[hash].push(new Node(key, value));
                }
                var end = System.nanoTime() - start;

                System.out.printf("Hasher %d - %dns, %d colisões\n", h, end, collisions);
            }

        }
    }
}
