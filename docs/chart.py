import matplotlib.pyplot as plt
import numpy as np
from scipy.optimize import curve_fit

def parse_data(file_path):
    data = []
    with open(file_path, 'r') as file:
        for line in file:
            for elem in line.strip().split(";"):
                n, time = elem.strip().split(',')
                time = float(time)
                n = int(n)
                data.append((n, time))
    return data

def quadratic(n, a, b, c):
    return a * n**2 + b * n + c

def plot_data(data, output_file):
    ns, times = zip(*data)
    ns, times = np.array(ns), np.array(times)

    plt.figure(figsize=(10, 6))
    plt.scatter(ns, times, label='Data', s=0.1)

    # Fit quadratic function
    # quadratic_params, _ = curve_fit(quadratic, ns, times)
    # quadratic_line = [quadratic(n, *quadratic_params) for n in ns]
    # plt.plot(ns, quadratic_line, 'r-', label=f'n^2 fit')

    plt.xlabel('N')
    plt.ylabel('Time (ms)')
    plt.title('Time vs N')
    plt.legend()
    plt.savefig(output_file, dpi=1000, bbox_inches='tight')

if __name__ == '__main__':
    file_path = 'time.txt'  # Replace with your file path
    data = parse_data(file_path)
    output_file = 'chart.png'  # Replace with your desired output file name
    plot_data(data, output_file)