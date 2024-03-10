#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include <fstream>
#include <random>
#include <algorithm>

std::string generate_case(int n) {
    std::vector<int> array(n, 0);
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> dis(0, 10000);
    std::uniform_int_distribution<> idxs(0, n - 1);

    for (int i = 0; i < n; i++) {
        array[i] = dis(gen);
    }

    int sum = std::accumulate(array.begin(), array.end(), 0);
    while (sum > 10000) {
        int idx = idxs(gen);
        while (array[idx] <= 0) {
            idx = idxs(gen);
        }
        array[idx] -= 1;
        sum -= 1;
    }

    std::ostringstream oss;
    for (int num : array) {
        oss << num << " ";
    }

    return oss.str();
}

int main() {
    std::ofstream file("tests.in");
    if (file.is_open()) {
        file << 10000 << "\n";
        for (int i = 0; i < 10000; i++) {
            file << (i + 1) << " " << generate_case(i + 1) << "\n";
            std::cout << "\r" << i << std::flush;
        }
        file.close();
    } else {
        std::cerr << "Unable to open file" << std::endl;
    }
    return 0;
}
