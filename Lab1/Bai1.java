package Lab1;
import java.util.Scanner;

public class Bai1 {

    public static double estimateCircleArea(double r) {
        int insideCount = 0;
        int numPoints = 10000000;
        double max = r;
        double min = -r;
        for (int i = 0; i < numPoints; i++) {
            double x = (Math.random() * (max - min)) + min;
            double y = (Math.random() * (max - min)) + min;
            if (Math.pow(x, 2) + Math.pow(y, 2) <= r * r) {
                insideCount += 1;
            }
        }
        double squareArea = Math.pow((2 * r), 2);
        return ((double) insideCount / numPoints) * squareArea;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Nhap ban kinh: ");
        double r = sc.nextDouble();
        System.out.printf("Dien tich hinh tron la: %.2f%n", estimateCircleArea(r));

        sc.close();
    }
}