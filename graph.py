import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
from pathlib import Path
import argparse

sns.set_theme(style="whitegrid")


def plot(data, output_path: Path, yaxis: str, title: str):
    for bucket_size in data["BucketSize"].unique():
        subset = data[data["BucketSize"] == bucket_size]

        plt.figure(figsize=(10, 10))

        ax = sns.barplot(
            data=subset,
            x="Hasher",
            y=yaxis,
            hue="DatasetSize",
            errorbar=None,
        )
        plt.title(title)
        plt.legend(title="Quantidade de registros")

        plt.xlabel("Função de Hash")
        plt.ylabel("Tempo (ms)")

        for container in ax.containers:
            ax.bar_label(container, label_type="edge", padding=3)  # type: ignore

        plt.tight_layout()
        plt.savefig(output_path / f"{yaxis}_{bucket_size}.png", dpi=300)
        plt.close()


def generate_graphs(csv_file: Path, output_path: Path):
    data = pd.read_csv(csv_file)
    if data.empty:
        raise ValueError(
            f"The CSV file at {csv_file} is empty. Please check the Java code."
        )

    # Ensure that the output path exists
    output_path.mkdir(parents=True, exist_ok=True)

    plot(data, output_path, "InsertionRuntime", "Tempo de inserção")
    plot(data, output_path, "LookupRuntime", "Tempo de busca")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description="Generate graphs from hash collision CSV data."
    )
    parser.add_argument(
        "csv_file",
        type=Path,
        help="The path to the CSV file containing the sampled data.",
    )
    parser.add_argument(
        "output_path",
        type=Path,
        help="The path that the generated PNGs will be saved to.",
    )
    args = parser.parse_args()

    generate_graphs(args.csv_file, args.output_path)
