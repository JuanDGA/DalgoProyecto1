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
  public int calculateSteps(int caseToCalculate) {
    DpBlock[] dp = buildDp(cases.get(caseToCalculate));

    // Steps to move from on higher tower to a lower tower, is the difference / 2, rounded to ceil

    int current = 0;
    int steps = 0;
    int executed = 0;

    while (current < dp.length) {
      executed += 1;
      if (current == dp.length - 1) break; // We finished :D
      if (dp[current].height < dp[current + 1].height) {
        if (current == 0) {
          int requiredSteps = (int) Math.ceil((dp[current + 1].height - dp[current].height) / 2.0);
          dp[current].height += requiredSteps;
          dp[current + 1].left += requiredSteps;
          dp[current + 1].height -= requiredSteps;
          steps += requiredSteps;
          current += 1;
        } else {
          if (dp[current - 1].height <= dp[current - 1].left) {
            int requires = dp[current + 1].height - dp[current].height;
            // We move all possible from the left, then the required from the right
            int availableFromLeft = (int) Math.floor((dp[current - 1].height - dp[current].height) / 2.0);
            int canGet = Math.min(availableFromLeft, dp[current - 1].left - dp[current - 1].height);
            int fromLeft = Math.min(requires, canGet);
            dp[current].height += fromLeft;
            dp[current - 1].height -= fromLeft;
            dp[current].left -= fromLeft;
            steps += fromLeft;
          }
          if (dp[current].height >= dp[current + 1].height) {
            current += 1;
            continue;
          }

          int requiredSteps = (int) Math.ceil((dp[current + 1].height - dp[current].height) / 2.0);
          dp[current].height += requiredSteps;
          dp[current + 1].height -= requiredSteps;
          dp[current + 1].left -= requiredSteps;
          steps += requiredSteps;
          if (dp[current].height > dp[current - 1].height) current -= 1;
          else current += 1;
        }
      } else {
        current += 1;
      }
    }

    System.out.printf("%s -> %s\n", dp.length, executed);
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
      int expectedSolution = outputScanner.nextInt();
      System.out.println("Test case: " + Arrays.toString(algorithm.getCase(caseNumber)));
      long init = System.nanoTime();
      int calculatedSolution = algorithm.calculateSteps(caseNumber);
      System.out.printf("Took %sms\n", (double) (System.nanoTime() - init) / 1000000);
      if (expectedSolution != calculatedSolution) {
        System.out.println("Failed! Expected: " + expectedSolution + ", Calculated: " + calculatedSolution + ", exiting program.");
        System.exit(1);
      } else {
        System.out.println("Success! Expected: " + expectedSolution);
      }
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