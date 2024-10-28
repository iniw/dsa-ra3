import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
from matplotlib.ticker import FuncFormatter
import pathlib
import argparse
import locale

# Set plot aesthetics
sns.set_theme(style="whitegrid")


# Plot insertion runtime with dataset and bucket size details
def plot_insertion_runtime(data, output_path: pathlib.Path):
    for bucket_size in data["BucketSize"].unique():
        subset = data[data["BucketSize"] == bucket_size]

        plt.figure(figsize=(10, 10))

        sns.barplot(
            data=subset,
            x="Hasher",
            y="InsertionRuntime",
            hue="DatasetSize",
            errorbar=None,
        )
        plt.title(f"Tempo de inserção (vetor com tamanho {bucket_size})")
        plt.legend(title="Quantidade de registros")

        plt.xlabel("Hasher")
        plt.ylabel("Tempo (ms)")

        plt.tight_layout()
        plt.savefig(output_path / f"insertion_runtime_{bucket_size}.png", dpi=300)
        plt.close()


# Plot lookup runtime with dataset and bucket size details
def plot_lookup_runtime(data, output_path: pathlib.Path):
    for bucket_size in data["BucketSize"].unique():
        subset = data[data["BucketSize"] == bucket_size]

        plt.figure(figsize=(10, 10))

        sns.barplot(
            data=subset,
            x="Hasher",
            y="LookupRuntime",
            hue="DatasetSize",
            errorbar=None,
        )
        plt.title(f"Tempo de busca (vetor com tamanho {bucket_size})")
        plt.legend(title="Quantidade de registros")

        plt.xlabel("Hasher")
        plt.ylabel("Tempo (ms)")

        plt.tight_layout()
        plt.savefig(output_path / f"lookup_runtime_{bucket_size}.png", dpi=300)
        plt.close()


def generate_graphs(csv_file: pathlib.Path, output_path: pathlib.Path):
    data = pd.read_csv(csv_file)
    if data.empty:
        raise ValueError(
            f"The CSV file at {csv_file} is empty. Please check the Java code."
        )

    # Ensure that the output path exists
    output_path.mkdir(parents=True, exist_ok=True)

    # Generate all required plots
    plot_insertion_runtime(data, output_path)
    plot_lookup_runtime(data, output_path)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description="Generate graphs from hash collision CSV data."
    )
    parser.add_argument(
        "csv_file",
        type=pathlib.Path,
        help="The path to the CSV file containing the sampled data.",
    )
    parser.add_argument(
        "output_path",
        type=pathlib.Path,
        help="The path that the generated PNGs will be saved to.",
    )
    args = parser.parse_args()

    generate_graphs(args.csv_file, args.output_path)
