package Lab1;
import java.util.ArrayList;
import java.util.List;

public class Bai4 {
    private List<Integer> res = new ArrayList<>();
    private int maxLen = 0;

    public List<Integer> solve(int[] arr, int k) {
        res = new ArrayList<>();
        maxLen = 0;
        
        backtrack(arr, k, 0, new ArrayList<>(), 0);
        return res;
    }

    private void backtrack(int[] arr, int k, int index, List<Integer> path, int sum) {
        if (sum == k) {
            if (path.size() > maxLen) {
                maxLen = path.size();
                res = new ArrayList<>(path);
            }
        }

        // Điều kiện dừng
        if (index >= arr.length || sum > k) {
            return;
        }

        path.add(arr[index]);
        backtrack(arr, k, index + 1, path, sum + arr[index]);

        path.remove(path.size() - 1);
        backtrack(arr, k, index + 1, path, sum);
    }

    public static void main(String[] args) {
        Bai4 sol = new Bai4();
        int[] arr = {1, 2, 7, 4, 3, 3, 10};
        int k = 8;
        
        List<Integer> result = sol.solve(arr, k);
        System.out.println("Dãy con dài nhất có tổng " + k + " là: " + result);
    }
}