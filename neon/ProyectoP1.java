import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class ProyectoP1 {
  public long calculateSteps(int[] dp) {
    // Steps to move from on higher tower to a lower tower, is the difference / 2, rounded to ceil

    int n = dp.length;
    int i = 0;
    long steps = 0;
    int spaceSince = 0;

    // I have discovered that if there is a group of towers with the same size, and then it is a higher tower,
    // there is a formula to get the required steps to sort those towers.
    while (i < n) {
      if (i >= n - 1) break; // >= is used in case that dp is empty
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
        if (base > 0) {
          Arrays.fill(dp, spaceSince + rest, i + 2, initialHeight + base);
        } else dp[i + 1] = initialHeight;
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

  public static void main(String[] args) throws IOException {
    ProyectoP1 algorithm = new ProyectoP1();
    System.out.println("Running!");
    long start = System.nanoTime();
    algorithm.exportSolution("./P1.in", "./P1.out");
    long end = System.nanoTime();
    System.out.printf("./P1.out exported successfully in %sms\n", (end - start) / 1000000);
  }
}
