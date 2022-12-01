import com.umair.aoc.common.Day;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

@SuppressWarnings("rawtypes")
public class Runner {
  private static final DecimalFormat dayFormat = new DecimalFormat("00");
  private static final DecimalFormat yearFormat = new DecimalFormat("0000");


  public static void main(String[] args) {
    Runner runner = new Runner();
    if (args.length == 0) {
      runner.run(2021);
      runner.run(2022);
    } else {
      int year = Integer.parseInt(args[0]);
      runner.run(year);
    }
  }

  public void run(int year) {
    System.out.printf("== Running Tests for the year %s ==%n", yearFormat.format(year));

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
      System.out.printf("-- Day %s:%n", day);
      dayInstance.solvePart1();
      dayInstance.solvePart2();
      System.out.println();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
             NoSuchMethodException ie) {
      System.out.printf("Error executing day %s for year %s", day, year);
      ie.printStackTrace();
    }
  }


  private Class getClassForName(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException cnfe) {
      // Absorb this exception, as some classes might not be implemented.
    }

    return null;
  }
}
