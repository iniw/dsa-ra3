package org.puc;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class App {
    public static final int[] BUCKET_SIZES = { 1000, 10000, 100000 };

    public static final int[] DATASET_SIZES = { 1_000_000, 5_000_000, 20_000_000, };

    public static double floor(double n) {
        return (int) n;
    }

    public static double abs(double n) {
        if (n <= 0.0) {
            return -n;
        } else {
            return n;
        }
    }

    interface Hash {
        int hash(int key, int bucketSize);
    }

    public static final Hash[] HASHERS = {
            new Hash() {
                // Simple mod hash
                public int hash(int key, int bucketSize) {
                    return key % bucketSize;
                }
            },
            new Hash() {
                // Fractional multiplication hash
                public int hash(int key, int bucketSize) {
                    final var A = 0.47;
                    return (int) floor(bucketSize * ((A * key) % 1));
                }
            },
            new Hash() {
                // Multiplicative hash
                public int hash(int key, int bucketSize) {
                    final var A = 2654435769L;
                    return (int) abs((A * key) % bucketSize);
                }
            },
    };

    public static void main(String[] args) throws IOException {
        var rand = args.length == 0 ? new Random() : new Random(Integer.parseInt(args[0]));

        var csvWriter = new FileWriter("data.csv");
        csvWriter.append("Run,Hasher,Runtime,DatasetSize,BucketSize,BucketIndex,Collisions\n"); // CSV Header

        for (int run = 0; run < 5; ++run) {
            System.out.printf("\n~~~ Rodada %d ~~~ \n", run + 1);

            for (int datasetSizeIndex = 0; datasetSizeIndex < 3; ++datasetSizeIndex) {
                var datasetSize = DATASET_SIZES[datasetSizeIndex];
                var dataset = new Registro[datasetSize];
                for (int d = 0; d < datasetSize; ++d) {
                    dataset[d] = new Registro(rand.nextInt(), rand.nextInt(100000000, 999999999));
                }

                for (int bucketSizeIndex = 0; bucketSizeIndex < 3; ++bucketSizeIndex) {
                    var bucketSize = BUCKET_SIZES[bucketSizeIndex];

                    System.out.printf("\n- Tamanho do vetor: %d\n", bucketSize);
                    System.out.printf("- Quantidade de dados: %d\n\n", datasetSize);

                    for (int h = 0; h < 3; ++h) {
                        var hasher = HASHERS[h];

                        var bucket = new LinkedList[bucketSize];
                        for (int b = 0; b < bucketSize; ++b) {
                            bucket[b] = new LinkedList();
                        }

                        var collisions = new int[bucketSize];

                        var start = System.currentTimeMillis();
                        for (var registro : dataset) {
                            var key = registro.codigo();
                            var value = registro.valor();

                            var hash = hasher.hash(key, bucketSize);
                            if (!bucket[hash].isEmpty())
                                collisions[hash]++;

                            bucket[hash].push(new Node(key, value));
                        }
                        var end = System.currentTimeMillis() - start;

                        System.out.printf("Hasher %d - %dms\n", h, end);

                        for (int b = 0; b < bucketSize; ++b) {
                            csvWriter.append(String.format(
                                    "%d,%d,%d,%d,%d,%d,%d\n",
                                    // Run number
                                    run + 1,
                                    // Hasher index
                                    h,
                                    // Time taken
                                    end,
                                    // Dataset size
                                    datasetSize,
                                    // Bucket size
                                    bucketSize,
                                    // Bucket index
                                    b,
                                    // Number of collisions in the bucket
                                    collisions[b]));
                        }
                    }
                }
            }
        }
        csvWriter.close();
    }
}
