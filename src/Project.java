import java.io.*;
import java.util.*;

class Algorithm {
  private final Map<Integer, int[]> cases;

  private final int casesAmount;

  static class DpBlock {
    int height;
    int left;

    public DpBlock(int height, int left) {
      this.height = height;
      this.left = left;
    }

    public String toString() {
      return Integer.toString(height);
    }
  }

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

  private void printTowers(DpBlock[] towers) {
    StringBuilder result = new StringBuilder();
    int max = 0;
    for (DpBlock t: towers) if (t.height > max) max = t.height;
    for (int i = max; i > 0; i--) {
      for (DpBlock tower : towers) {
        if (tower.height >= i) {
          result.append("#");
        } else {
          result.append(" ");
        }
      }
      result.append("\n");
    }
    System.out.print(result);
    System.out.println("*".repeat(towers.length));
  }

  /**
   * Build a dynamic programming table for a given array of blocks.
   * Each entry in the table represents a block and its corresponding value.
   *
   * @param blocks an array of blocks
   * @return a 2D array representing the dynamic programming table
   */
  private DpBlock[] buildDp(int[] blocks) {
    DpBlock[] dp = new DpBlock[blocks.length];
    for (int i = 0; i < blocks.length; i++) {
      int left = i == 0 ? Integer.MAX_VALUE : blocks[i - 1];
      dp[i] = new DpBlock(blocks[i], left);
    }
    return dp;
  }


  // O(N²)

  // SOLUTION
  // dp[i] = dp[i] if dp[i + 1] <= dp[i]
  // dp[i] = dp[i] + min(floor((dp[i-1] - dp[i]) / 2), dp[i + 1] - dp[i]) if dp[i] < dp[i - 1] + 1 && dp[i] < dp[i + 1] && i > 0
  //         dp[i - 1] = dp[i - 1] - min(floor((dp[i-1] - dp[i]) / 2), dp[i + 1] - dp[i])
  // dp[i] = dp[i] + ceil((dp[i+1] - dp[i]) / 2) if dp[i] > dp[i + 1] && i < n - 1


  public int calculateSteps(int caseToCalculate) {
    DpBlock[] dp2 = buildDp(cases.get(caseToCalculate));
    int[] dp = cases.get(caseToCalculate);

    // Steps to move from on higher tower to a lower tower, is the difference / 2, rounded to ceil

    int n = dp.length;
    int i = 0;
    int steps = 0;
    int executed = 0;
    int spaceSince = 0;

    // I have discovered that if there is a group of towers with the same size, and then it is a higher tower,
    // there is a formula to get the step amount.
    int max_i = 0;
    while (i < n) {
      if (i > max_i) max_i = i;
      System.out.print("\r" + i + "-" + max_i + "-" + executed + "-" + (executed > n * n));
      executed += 1;
      if (i == n - 1) break; // We finished :D
      if (dp[i] >= dp[i + 1]) {
        i += 1;
        if (dp[i + 1] < dp[i]) spaceSince = i + 1;
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
      } else if (dp[i] < dp[i + 1] && i < n - 1) { // Can get from the right
        dp[i] += canGetFromRight;
        dp[i + 1] -= canGetFromRight;
        steps += canGetFromRight;
        if (i == 0 || dp[i - 1] >= dp[i]) {
          i++;
        } else {
          i--;
        }
      }
    }

    System.out.println("Result: " + Arrays.toString(dp));

    System.out.printf("%s -> %s\n", dp.length, executed);

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
    for (int caseNumber = 0; caseNumber < algorithm.getCasesAmount(); caseNumber++) {
      System.out.println("=".repeat(10));
      int expectedSolution = outputScanner.nextInt();
      long init = System.nanoTime();
      int calculatedSolution = algorithm.calculateSteps(caseNumber);
      System.out.printf("Case " + caseNumber + " took %sms\n", (double) (System.nanoTime() - init) / 1000000);
      if (expectedSolution != calculatedSolution) {
        System.err.println("Failed in case " + caseNumber + "! Expected: " + expectedSolution + ", Calculated: " + calculatedSolution + ", exiting program.");
        System.err.println("Case: " + Arrays.toString(algorithm.getCase(caseNumber)));
//        System.exit(1);
      } else {
        System.out.println("Success! Expected: " + expectedSolution);
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