import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import pathlib
import argparse
from matplotlib.backends.backend_pdf import PdfPages

# Set plot aesthetics
sns.set_theme(style="whitegrid")


# Generate a summary of collisions across different hashers, bucket sizes, and runs
def plot_collisions_by_hasher(data, pdf_pages):
    plt.figure(figsize=(16, 10))
    sns.barplot(
        data=data,
        x="Hasher",
        y="Collisions",
        hue="BucketSize",
        errorbar=None,
        estimator=sum,
    )
    plt.title("Total Collisions per Hasher with Different Bucket Sizes")
    plt.xlabel("Hasher (0 = Mod, 1 = Fractional, 2 = Multiplicative)")
    plt.ylabel("Total Collisions")
    plt.legend(title="Bucket Size", loc="upper right")
    plt.tight_layout()
    pdf_pages.savefig()


# Plot collisions per bucket index for each hasher
def plot_collisions_distribution(data, pdf_pages):
    plt.figure(figsize=(14, 8))
    sns.lineplot(
        data=data,
        x="DatasetSize",
        y="Collisions",
        hue="Hasher",
        style="BucketSize",
        markers=True,
        errorbar=None,
    )
    plt.title("Collision Trends by Dataset Size and Hasher")
    plt.xlabel("Dataset Size")
    plt.ylabel("Total Collisions")
    plt.legend(title="Hasher and Bucket Size")
    plt.tight_layout()
    pdf_pages.savefig()


# Plot heatmap of collisions by hasher and run
def plot_runtime(data, pdf_pages):
    plt.figure(figsize=(16, 10))
    sns.barplot(data=data, x="Hasher", y="Runtime", hue="BucketSize", errorbar=None)
    plt.title("Comparação de tempo de execução de cada hasher")
    plt.xlabel("Hasher (0 = Simples, 1 = Fracional, 2 = Multiplicativo)")
    plt.ylabel("Runtime (ms)")
    plt.legend(title="Tamanho do bucket", loc="upper right")
    plt.tight_layout()
    pdf_pages.savefig()


def generate_graphs(csv_file: pathlib.Path, output_pdf: pathlib.Path):
    data = pd.read_csv(csv_file)
    if data.empty:
        raise ValueError(
            f"The CSV file at {csv_file} is empty. Please check the Java code."
        )

    # Open a PdfPages object to save multiple plots in one PDF
    with PdfPages(output_pdf) as pdf_pages:
        # Generate all required plots
        plot_collisions_by_hasher(data, pdf_pages)
        plot_collisions_distribution(data, pdf_pages)
        plot_runtime(data, pdf_pages)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description="Generate graphs from hash collision CSV data."
    )
    parser.add_argument(
        "csv_file",
        type=pathlib.Path,
        help="The path to the CSV file containing hash collision data.",
    )
    parser.add_argument(
        "--output_pdf",
        type=pathlib.Path,
        default="collision_graphs.pdf",
        help="The path to the output PDF file (default: collision_graphs.pdf).",
    )

    args = parser.parse_args()

    generate_graphs(args.csv_file, args.output_pdf)
