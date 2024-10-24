import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import pathlib
import argparse
from matplotlib.backends.backend_pdf import PdfPages

# Set plot aesthetics
sns.set_theme(style="whitegrid")


# Generate a summary of collisions across different hashers, bucket sizes, and runs
def plot_collisions_by_hasher(df, pdf_pages):
    plt.figure(figsize=(14, 8))
    sns.barplot(
        data=df,
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
    pdf_pages.savefig()  # Save this plot to the PDF


# Plot collisions per bucket index for each hasher
def plot_collisions_distribution(df, pdf_pages):
    plt.figure(figsize=(16, 10))
    sns.histplot(
        data=df, x="Collisions", hue="Hasher", multiple="stack", bins=30, kde=True
    )
    plt.title("Distribution of Collisions Across Bucket Indices for Each Hasher")
    plt.xlabel("Collisions per Bucket Index")
    plt.ylabel("Frequency")
    plt.legend(title="Hasher", loc="upper right")
    plt.tight_layout()
    pdf_pages.savefig()  # Save this plot to the PDF


# Generate collisions over runs for insight into consistency
def plot_collisions_over_runs(df, pdf_pages):
    plt.figure(figsize=(14, 8))
    sns.lineplot(
        data=df,
        x="Run",
        y="Collisions",
        hue="Hasher",
        marker="o",
        style="BucketSize",
        errorbar=None,
    )
    plt.title("Collisions Over Runs for Each Hasher and Bucket Size")
    plt.xlabel("Run Number")
    plt.ylabel("Collisions")
    plt.legend(title="Hasher and Bucket Size")
    plt.tight_layout()
    pdf_pages.savefig()  # Save this plot to the PDF


# Main function to generate the plots and output them to a single PDF file
def generate_graphs(csv_file: pathlib.Path, output_pdf: pathlib.Path):
    # Read the CSV file
    df = pd.read_csv(csv_file)

    if df.empty:
        raise ValueError(
            f"The CSV file at {csv_file} is empty. Please check the data generation process."
        )

    # Open a PdfPages object to save multiple plots in one PDF
    with PdfPages(output_pdf) as pdf_pages:
        # Generate all required plots
        plot_collisions_by_hasher(df, pdf_pages)
        plot_collisions_distribution(df, pdf_pages)
        plot_collisions_over_runs(df, pdf_pages)


if __name__ == "__main__":
    # Set up argument parsing
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

    # Generate graphs based on the provided CSV file path and output to a single PDF
    generate_graphs(args.csv_file, args.output_pdf)
