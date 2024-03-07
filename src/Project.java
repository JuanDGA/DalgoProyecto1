import java.io.*;
import java.util.*;

class Algorithm {
  private final Map<Integer, int[]> cases;

  private final int casesAmount;

  public Algorithm(String input) throws FileNotFoundException {
    Scanner inputScanner = new Scanner(new FileReader(input));

    int casesAmount = Integer.parseInt(inputScanner.nextLine().strip());

    this.casesAmount = casesAmount;
    this.cases = new HashMap<>(casesAmount);

    for (int i = 0; i < this.casesAmount; i++) {
      List<Integer> result = Arrays.stream(inputScanner.nextLine().strip().split(" "))
          .map(Integer::parseInt)
          .skip(1)
          .toList();

      int[] values = new int[result.size()];
      for (int j = 0; j < result.size(); j++) values[j] = result.get(j);
      cases.put(i, values);
    }
  }

  // O(N²)

  // SOLUTION
  // dp[i] = dp[i] if dp[i + 1] <= dp[i]
  // dp[i] = dp[i] + min(floor((dp[i-1] - dp[i]) / 2), dp[i + 1] - dp[i]) if dp[i] < dp[i - 1] + 1 && dp[i] < dp[i + 1] && i > 0
  //         dp[i - 1] = dp[i - 1] - min(floor((dp[i-1] - dp[i]) / 2), dp[i + 1] - dp[i])
  // dp[i] = dp[i] + ceil((dp[i+1] - dp[i]) / 2) if dp[i] > dp[i + 1] && i < n - 1


  public long calculateSteps(int caseToCalculate) {
    int[] dp = cases.get(caseToCalculate);

    // Steps to move from on higher tower to a lower tower, is the difference / 2, rounded to ceil

    int n = dp.length;
    int i = 0;
    long steps = 0;
    int spaceSince = 0;

    // I have discovered that if there is a group of towers with the same size, and then it is a higher tower,
    // there is a formula to get the step amount.
    int max_i = 0;
    while (i < n) {
      if (i > max_i) max_i = i;
      if (i == n - 1) break; // We finished :D
      if (dp[i] > dp[i + 1]) spaceSince = i + 1;
      if (dp[i] >= dp[i + 1]) {
        i += 1;
        continue;
      }

      int difference = dp[i + 1] - dp[i];
      int canGetFromRight = (int) Math.ceil((dp[i + 1] - dp[i]) / 2.0);
      int canGetFromLeft = i == 0 ? 0 : (int) Math.floor((dp[i - 1] - dp[i]) / 2.0);

      if (i > 0 && dp[i] < dp[i - 1] + 1 && dp[i] < dp[i + 1] && canGetFromLeft >= difference) { // Can get from the left
        dp[i] += difference;
        dp[i - 1] -= difference;
        steps += difference;
        i++;
      } else if (dp[i] < dp[i + 1]) { // Can get from the right
        // Here we will use the formula just if we have space
        if (spaceSince < i) {
          int toFill = i + 1 - spaceSince;
          int initialHeight = dp[i];
          int base = (dp[i + 1] - dp[i]) / (toFill + 1);
          int rest = (dp[i + 1] - dp[i]) - base * (toFill + 1);
          int toRemove = toFill - rest;

          int requiredSteps = base * ((toFill * (toFill + 1)) / 2) + ((toFill * (toFill + 1)) / 2) - ((toRemove * (toRemove + 1)) / 2);
          steps += requiredSteps;
          Arrays.fill(dp, spaceSince, i + 2, initialHeight + base);
          Arrays.fill(dp, spaceSince, spaceSince + rest, initialHeight + base + 1);
          if (spaceSince == 0) {
            i++;
            spaceSince = rest;
          } else {
            i = 0;
            spaceSince = 0;
          }
        } else {
          dp[i] += canGetFromRight;
          dp[i + 1] -= canGetFromRight;
          steps += canGetFromRight;
          spaceSince = i + 1;
          if (i == 0 || dp[i - 1] >= dp[i]) {
            i++;
          } else {
            i--;
          }
        }
      }
    }

    for (int k = 0; k < n - 1; k++) assert dp[k] >= dp[k + 1];

    return steps;
  }

  public void exportSolution(String name) throws IOException {
    PrintWriter writer = new PrintWriter(new FileWriter(name));
    for (int i = 0; i < casesAmount; i++) {
      writer.print(calculateSteps(i));
      if (i < casesAmount - 1) writer.println();
    }
    writer.close();
  }

  public int getCasesAmount() {
    return casesAmount;
  }

  public int[] getCase(int n) {
    return cases.get(n);
  }
}

class AlgorithmTester {
  public static void validateData(String input, String output) throws FileNotFoundException {
    Algorithm algorithm = new Algorithm(input);
    Scanner outputScanner = new Scanner(new FileReader(output));
    System.out.println("Testing " + algorithm.getCasesAmount() + " cases.");
    for (int caseNumber = 0; caseNumber < algorithm.getCasesAmount(); caseNumber++) {
      System.out.println("Case " + (caseNumber + 1) + ": " + Arrays.toString(algorithm.getCase(caseNumber)));
      long expectedSolution = outputScanner.nextLong();
      long init = System.nanoTime();
      long calculatedSolution = algorithm.calculateSteps(caseNumber);
      double took = (double) (System.nanoTime() - init) / 1000000;
      System.out.printf("Case " + (caseNumber + 1) + " took %sms\n", took);
      if (expectedSolution != calculatedSolution) {
        System.out.println("Failed in case " + (caseNumber + 1) + "! Expected: " + expectedSolution + ", Calculated: " + calculatedSolution + ", exiting program.");
        System.exit(1);
      } else {
        System.out.println("Success! Result: " + expectedSolution);
      }
      System.out.println("=".repeat(10));
    }
  }
}

public class Project {

  public static void main(String[] args) throws IOException {
    Algorithm algorithm = new Algorithm("data/P1.in");
    AlgorithmTester.validateData("data/P1.in", "data/P1.out");
    algorithm.exportSolution("results/P1.out");
  }
}