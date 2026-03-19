package Lab1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Bai3 {

    static long cross(int[] O, int[] A, int[] B) {
        return (long)(A[0] - O[0]) * (B[1] - O[1])
             - (long)(A[1] - O[1]) * (B[0] - O[0]);
    }

    static List<int[]> grahamScan(int[][] points) {
        int n = points.length;
        if (n <= 2) return Arrays.asList(points);

        int[] pivot = points[0];
        for (int[] p : points)
            if (p[1] < pivot[1] || (p[1] == pivot[1] && p[0] < pivot[0]))
                pivot = p;

        final int[] piv = pivot;

        List<int[]> pts = new ArrayList<>(Arrays.asList(points));
        pts.sort((a, b) -> {
            double angA = Math.atan2(a[1] - piv[1], a[0] - piv[0]);
            double angB = Math.atan2(b[1] - piv[1], b[0] - piv[0]);
            if (Math.abs(angA - angB) > 1e-9) return Double.compare(angA, angB);
            long dA = (long)(a[0]-piv[0])*(a[0]-piv[0]) + (long)(a[1]-piv[1])*(a[1]-piv[1]);
            long dB = (long)(b[0]-piv[0])*(b[0]-piv[0]) + (long)(b[1]-piv[1])*(b[1]-piv[1]);
            return Long.compare(dA, dB);
        });

        Stack<int[]> hull = new Stack<>();
        for (int[] p : pts) {
            while (hull.size() >= 2 && cross(hull.get(hull.size()-2), hull.peek(), p) <= 0)
                hull.pop();
            hull.push(p);
        }

        return new ArrayList<>(hull);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] points = new int[n][2];
        for (int i = 0; i < n; i++) {
            points[i][0] = sc.nextInt();
            points[i][1] = sc.nextInt();
        }

        List<int[]> result = grahamScan(points);

        System.out.println("Co " + result.size() + " tram canh bao:");
        for (int[] p : result) {
            System.out.println(p[0] + " " + p[1]);
        }
        sc.close();
    }
} {
  
}
