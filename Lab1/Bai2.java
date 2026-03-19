package Lab1;

import java.util.Random;
import java.util.Scanner;

public class Bai2 {
  public static void main(String[] args) {
    try (Scanner sc = new Scanner(System.in)) {
      long numPoints = sc.nextLong();
      if (numPoints <= 0) {
        System.out.print((double) 0);
        return;
      }

      double r = (double) numPoints / (double) numPoints; // r = 1 for unit circle
      double zero = r - r;
      double one = r / r;
      double two = one + one;
      double four = two + two;
      double radiusSquared = r * r;

      Random rnd = new Random();
      long insideCount = 0;

      long oneL = (long) one;
      long zeroL = (long) zero;

      for (long i = zeroL; i < numPoints; i = i + oneL) {
        double u1 = rnd.nextDouble();
        double u2 = rnd.nextDouble();

        double x = (two * u1 - one) * r;
        double y = (two * u2 - one) * r;

        if (x * x + y * y <= radiusSquared) insideCount = insideCount + oneL;
      }

      double piApprox = four * ((double) insideCount / (double) numPoints);
      System.out.print(piApprox);
    }
  }
}
