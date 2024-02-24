import com.umair.aoc.common.Day;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

import static java.lang.System.out;

@SuppressWarnings("ALL")
public class Runner {
  private static final DecimalFormat dayFormat = new DecimalFormat("00");
  private static final DecimalFormat yearFormat = new DecimalFormat("0000");


  public static void main(String[] args) {
    Runner runner = new Runner();
    if (args.length == 0) {
      runner.run(2021);
      runner.run(2022);
    } else if (args.length == 1) {
      int year = Integer.parseInt(args[0]);
      runner.run(year);
    } else if (args.length == 2) {
      int year = Integer.parseInt(args[0]);
      int day = Integer.parseInt(args[1]);
      runner.run(year, day);
    }
  }

  public void run(int year) {
    out.printf("== Running Tests for the year %s ==%n", yearFormat.format(year));

    for (int day = 1; day <= 25; day++) {
      run(year, day);
    }
  }

  public void run(int year, int day) {
    try {
      // Note: This line is not working.
      // String packageName = this.getClass().getPackageName();

        // E.g. com.umair.aoc.y2021.Day01
      String className = String.format("com.umair.aoc.y%s.Day%s",
          yearFormat.format(year),
          dayFormat.format(day));
      Class c = getClassForName(className);
      if (c == null) {
        return;
      }

      Day dayInstance = (Day) c.getDeclaredConstructor().newInstance();

      out.printf("-- Day %s:%n", day);
      out.printf("Part 1: %s%n", timeAndExecute(dayInstance::solvePart1));
      out.printf("Part 2: %s%n", timeAndExecute(dayInstance::solvePart2));
      out.println();

    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
             NoSuchMethodException ie) {
      out.printf("Error executing day %s for year %s", day, year);
      ie.printStackTrace();
    }
  }

  @FunctionalInterface
  private interface Executor {
    String execute();
  }

  private static String timeAndExecute(Executor executor) {
    long startTime = System.currentTimeMillis();
    String executionResult = executor.execute();
    long endTime = System.currentTimeMillis();

    String result = String.format("%s (in %sms)", executionResult, (endTime - startTime));
    return result;
  }

  private Class getClassForName(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      // Absorb this exception, as some classes might not be implemented.
    }

    return null;
  }
}
