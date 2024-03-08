import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.*;

class Algorithm {

  // O(N²)

  // SOLUTION
  // dp[i] = dp[i] if dp[i + 1] <= dp[i]
  // dp[i] = dp[i] + min(floor((dp[i-1] - dp[i]) / 2), dp[i + 1] - dp[i]) if dp[i] < dp[i - 1] + 1 && dp[i] < dp[i + 1] && i > 0
  //         dp[i - 1] = dp[i - 1] - min(floor((dp[i-1] - dp[i]) / 2), dp[i + 1] - dp[i])
  // dp[i] = dp[i] + ceil((dp[i+1] - dp[i]) / 2) if dp[i] > dp[i + 1] && i < n - 1


  public long calculateSteps(int[] dp) {
    // Steps to move from on higher tower to a lower tower, is the difference / 2, rounded to ceil

    int n = dp.length;
    int i = 0;
    long steps = 0;
    int spaceSince = 0;

    // I have discovered that if there is a group of towers with the same size, and then it is a higher tower,
    // there is a formula to get the step amount.
    while (i < n) {
      if (i == n - 1) break; // We finished :D
      if (dp[i] > dp[i + 1]) spaceSince = i + 1;
      if (dp[i] >= dp[i + 1]) {
        i += 1;
        continue;
      }

      int difference = dp[i + 1] - dp[i];
      int canGetFromRight = (int) Math.ceil(difference / 2.0);
      int canGetFromLeft = i == 0 ? 0 : (int) Math.floor((dp[i - 1] - dp[i]) / 2.0);

      if (i > 0 && dp[i] < dp[i - 1] + 1 && dp[i] < dp[i + 1] && canGetFromLeft >= difference) { // Can get from the left
        dp[i] += difference;
        dp[i - 1] -= difference;
        steps += difference;
        i++;
      } else if (dp[i] < dp[i + 1] && spaceSince < i) { // Can get from the right and distribute
        // Here we will use the formula just if we have space
        int toFill = i + 1 - spaceSince;
        int initialHeight = dp[i];
        int base = difference / (toFill + 1);
        int rest = difference - base * (toFill + 1);
        int toRemove = toFill - rest;

        int requiredSteps = base * ((toFill * (toFill + 1)) / 2) + ((toFill * (toFill + 1)) / 2) - ((toRemove * (toRemove + 1)) / 2);
        steps += requiredSteps;
        Arrays.fill(dp, spaceSince + rest, i + 2, initialHeight + base);
        Arrays.fill(dp, spaceSince, spaceSince + rest, initialHeight + base + 1);
        if (base == 0 && rest > 0) {
          i++;
          spaceSince += rest;
        } else {
          i = 0;
          spaceSince = 0;
        }
      } else if (dp[i] < dp[i + 1]) { // Can get from the right but we can't distribute
        dp[i] += canGetFromRight;
        dp[i + 1] -= canGetFromRight;
        steps += canGetFromRight;
        spaceSince = i + 1;
        i += (i == 0 || dp[i - 1] >= dp[i]) ? 1 : -1;
      }
    }

    return steps;
  }

  public void exportSolution(String readFrom, String name) throws IOException {
    Scanner scanner = new Scanner(new FileReader(readFrom));
    PrintWriter writer = new PrintWriter(new FileWriter(name));
    int[] towers;
    int amount = scanner.nextInt();
    for (int i = 0; i < amount; i++) {
      towers = new int[scanner.nextInt()];
      for (int j = 0; j < towers.length; j++) {
        towers[j] = scanner.nextInt();
      }
      writer.print(calculateSteps(towers));
      writer.println();
    }
    writer.close();
    scanner.close();
  }
}

class AlgorithmTester {

  public static void validateData(String input, String output) throws FileNotFoundException {
    Algorithm algorithm = new Algorithm();
    Scanner scanner = new Scanner(new FileReader(input));
    Scanner outputScanner = new Scanner(new FileReader(output));
    int amount = scanner.nextInt();
    System.out.println("Testing " + amount + " cases.");
    for (int caseNumber = 0; caseNumber < amount; caseNumber++) {
      int size = scanner.nextInt();
      long expectedSolution = outputScanner.nextLong();
      int[] data = new int[size];
      for (int j = 0; j < data.length; j++) {
        data[j] = scanner.nextInt();
      }
      long init = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
      long calculatedSolution = algorithm.calculateSteps(data);
      double took = (double) (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - init) / 1000000;
      System.out.printf("Case " + (caseNumber + 1) + " took %sms.\n", took);
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
    Algorithm algorithm = new Algorithm();
    AlgorithmTester.validateData("data/P1.in", "data/P1.out");
    algorithm.exportSolution("data/P1.in", "results/P1.out");
  }
}