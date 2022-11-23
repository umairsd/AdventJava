import com.umair.aoc.common.Day;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

public class Runner {
  private static final DecimalFormat dayFormat = new DecimalFormat("00");
  private static final DecimalFormat yearFormat = new DecimalFormat("0000");


  public static void main(String[] args) {
    Runner runner = new Runner();
    runner.run(2021);
  }

  public void run(int year) {
    System.out.printf("== Running Tests for the year %s ==%n", yearFormat.format(year));

    try {
      // Note: This line is not working.
      // String packageName = this.getClass().getPackageName();

      for (int day = 1; day <= 25; day++) {
        // E.g. com.umair.aoc.y2021.Day01
        String className = String.format("com.umair.aoc.y%s.Day%s",
            yearFormat.format(year),
            dayFormat.format(day));
        Class c = getClassForName(className);
        if (c == null) {
          continue;
        }

        Day dayInstance = (Day) c.getDeclaredConstructor().newInstance();
        System.out.printf("-- Tests for day %s:%n", day);
        dayInstance.solvePart1();
        dayInstance.solvePart2();
        System.out.println();
      }
    } catch (InstantiationException ie) {
      ie.printStackTrace();
    } catch (IllegalAccessException iae) {
      iae.printStackTrace();
    } catch (InvocationTargetException ite) {
      ite.printStackTrace();
    } catch (NoSuchMethodException nsme) {
      nsme.printStackTrace();
    }
  }


  private Class getClassForName(String className) {
    try {
      Class c = Class.forName(className);
      return c;
    } catch (ClassNotFoundException cnfe) {
      // Absorb this exception, as some classes might not be implemented.
    }

    return null;
  }
}
