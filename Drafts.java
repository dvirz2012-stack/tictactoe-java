public class Drafts {
    public static void main(String[] args) {    
        

        int[][] matrix = createMultiArray();
        PrintMultiArray(matrix);
        // int[] arr = {1, 2, 3, 2, 1};
        // System.out.println(isPalindrome(arr));

        // int[] arr2 = {1, 2 , 3 , 4 ,5};
        // int sum = 9;
        // System.out.println(isPairEqual(arr2, sum));


        // double[] arr3 = {2.5 , 4.3 , 5.5 , 2.1};
        // sort(arr3);
        // System.out.println(Arrays.toString(arr3));

        // int[][] arr4 = {
        //     {1}, {1}, {2}, {3}, {4}, {5}, 
        //     {6}, {7}, {8}, {9}, {10}
        // };

    }

    // private static boolean isPalindrome(int [] arr) {
    //     for (int i = 0; i >= 0 && i < arr.length ; i++){
    //             if(arr[i] != arr[arr.length-1-i]){
    //                 return false;
    //             }
    //     }
    //     return true;
    // }

    // private static boolean isPairEqual(int[] arr, int sum) {
    //     for(int i=0; i<arr.length; i++){
    //         for(int j=i+1; j<arr.length; j++){
    //             if (arr[j] + arr[i] == sum){
    //                 return true;
    //             }
    //         }
    //     }
    //     return false;
    // }
    
    // private static void sort(double[] arr){
    //     for (int j=1; j<arr.length; j++){
    //         double k = arr[j];
    //         int i = j-1;
    //         while (i>=0 && arr[i] > k){
    //             arr[i+1] = arr[i];
    //             i--;

    //         }
    //         arr[i+1] = k;
    //     }
        // for(int j=1; j<arr.length;j++){
        //     double k = arr[j];
        //     for(int i=j-1;i>=0 && arr[i] > k ;i--){
        //         if (arr[i] > k){
        //             arr[i+1] = arr[i];
        //         }
        //         arr[i+1] = k;
        //     }
        // }
    //}
    private static int[][] createMultiArray(){
        int[][] matrix = new int[10][];
        for(int i=0; i<matrix.length; i++){
            // ITERATION NUMBER 1: i=0 , smaller than matrix.length is true , new array in matrix in the length of i+1 = 1
            // ITERATION NUMBER 2: i=1, smaller than matrix.length is true , new array in matrix in the length of i+1 = 2
            matrix[i] = new int[i+1];
            for (int j=0; j<i+1; j++){
            // ITERATION NUMBER 1 CONTINUES: i=0 , j=0 , j smaller than matrix.length is true , we put the number 1 in the second row first number (matrix[1][0] = 1)
                matrix[i][j] = j+1;
            }
            // ITERATION NUMBER 1 ENDS: i=1 , j=1
        }
        return matrix;
    }

    private static void PrintMultiArray(int[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }
}
