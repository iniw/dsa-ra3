package org.puc;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class App {
    public static final int[] BUCKET_SIZES = { 100_000, 500_000, 1_000_000 };

    public static final int[] DATASET_SIZES = { 1_000_000, 5_000_000, 20_000_000, };

    public static final Hasher[] HASHERS = {
            Hasher.modHasher(),
            Hasher.multiplicativeHasher(),
            Hasher.fractionalMultiplicativeHasher(),
    };

    public static void main(String[] args) throws IOException {
        var rand = args.length == 0 ? new Random() : new Random(Integer.parseInt(args[0]));

        var csvWriter = new FileWriter("data.csv");
        csvWriter.append("Hasher,DatasetSize,BucketSize,InsertionRuntime,LookupRuntime\n");

        for (int datasetSizeIndex = 0; datasetSizeIndex < 3; ++datasetSizeIndex) {
            var datasetSize = DATASET_SIZES[datasetSizeIndex];
            var dataset = new Entry[datasetSize];
            for (int d = 0; d < datasetSize; ++d) {
                dataset[d] = new Entry(rand.nextInt(100000000, 999999999), rand.nextInt());
            }

            for (int bucketSizeIndex = 0; bucketSizeIndex < 3; ++bucketSizeIndex) {
                var bucketSize = BUCKET_SIZES[bucketSizeIndex];

                System.out.printf("\n- Tamanho do vetor: %d\n", bucketSize);
                System.out.printf("- Quantidade de dados: %d\n\n", datasetSize);

                for (int h = 0; h < 3; ++h) {
                    var hasher = HASHERS[h];

                    var hashmap = new HashMap(bucketSize, hasher);

                    var start = System.currentTimeMillis();
                    for (var entry : dataset) {
                        var key = entry.code();
                        var value = entry.value();

                        hashmap.insert(key, value);
                    }
                    var insertionRuntime = System.currentTimeMillis() - start;

                    System.out.printf("Hasher %d (insertion) - %dms\n", h, insertionRuntime);

                    start = System.currentTimeMillis();
                    for (var entry : dataset) {
                        var key = entry.code();

                        hashmap.lookup(key);
                    }
                    var lookupRuntime = System.currentTimeMillis() - start;

                    System.out.printf("Hasher %d (lookup) - %dms\n", h, lookupRuntime);

                    csvWriter.append(String.format(
                            "%s,%d,%d,%d,%d\n",
                            hasher.name(),
                            datasetSize,
                            bucketSize,
                            insertionRuntime,
                            lookupRuntime));
                }
            }
        }
        csvWriter.close();
    }
}
