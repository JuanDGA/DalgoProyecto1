import java.io.*;
import java.util.*;

class Algorithm {
  private final Map<Integer, Integer[]> cases;

  private final int casesAmount;

  public Algorithm(String input) throws FileNotFoundException {
    Scanner inputScanner = new Scanner(new FileReader(input));

    int casesAmount = Integer.parseInt(inputScanner.nextLine().strip());

    this.casesAmount = casesAmount;
    this.cases = new HashMap<>(casesAmount);

    for (int i = 0; i < this.casesAmount; i++) {
      cases.put(i, Arrays.stream(inputScanner.nextLine().strip().split(" "))
          .map(Integer::parseInt)
          .skip(1)
          .toArray(Integer[]::new));
    }
  }

  public int calculateCase(int caseToCalculate) {
    Integer[] blocks = cases.get(caseToCalculate);

    return 0;
  }

  public void exportSolution(String name) throws IOException {
    PrintWriter writer = new PrintWriter(new FileWriter(name));
    for (int i = 0; i < casesAmount; i++) {
      writer.print(calculateCase(i));
      if (i < casesAmount - 1) writer.println();
    }
    writer.close();
  }

  public int getCasesAmount() {
    return casesAmount;
  }

  public Integer[] getCase(int n) {
    return cases.get(n);
  }
}

class AlgorithmTester {
  public static void validateData(String input, String output) throws FileNotFoundException {
    Algorithm algorithm = new Algorithm(input);
    Scanner outputScanner = new Scanner(new FileReader(output));
    for (int caseNumber = 0; caseNumber < algorithm.getCasesAmount(); caseNumber++) {
      int expectedSolution = outputScanner.nextInt();
      int calculatedSolution = algorithm.calculateCase(caseNumber);
      if (expectedSolution != calculatedSolution) {
        System.err.println("Test case: " + Arrays.toString(algorithm.getCase(caseNumber)));
        System.err.println("Failed! Expected: " + expectedSolution + ", Calculated: " + calculatedSolution + ", exiting program.");
        System.exit(1);
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