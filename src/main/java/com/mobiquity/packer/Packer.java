package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.Item;
import com.mobiquity.model.Record;
import com.mobiquity.util.FileUtil;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yyakup
 * This API is to solve the knapsack problem.
 * Using tabular solution, K[i,w] = max(C[i]+K[i-1][w-w(i)], K[i-1][w]) where
 * K: Item index(row)*Capacity weight(0 to max capacity) matrix, C: Item cost,
 * i: Item index, w: capacity index, w(i): weight of Item(index)
 */
public class Packer {

    private Packer() {
    }

    /**
     * @param filePath file path as a string
     * @return String as solution one line for each test data.
     * @throws APIException
     */

    public static String pack(String filePath) throws APIException {

        //to return the Items indexes as result
        var result = new StringBuilder();

        List<Record> records = FileUtil.getRecords(filePath);
        if (records.isEmpty()) {
            throw new APIException("Empty file");
        }
        records.forEach(r -> {
            String testResult = getResult(r);
            result.append(testResult.isEmpty() ? "-" : testResult.substring(0, testResult.length() - 1)).append("\n");
        });

        return result.toString().trim();
    }

    /**
     * @param r line data in the file(one test case)
     * @return String result of the one test case
     * @throws APIException
     */

    private static String getResult(Record r) {

        //initialize the knapsack matrix
        int[][] matrix = new int[r.getItems().size()][r.getCapacity() + 1];
        //according to the cost and weight of the items, fill the knapsack matrix
        for (int i = 0; i < r.getItems().size(); i++) {
            for (int c = 0; c <= r.getCapacity(); c++) {
                if (i == 0 || c == 0) {
                    matrix[i][c] = 0;
                } else if (r.getItems().get(i).getWeight() <= c) {
                    matrix[i][c] =
                            Math.max(r.getItems().get(i).getCost() + matrix[i - 1]
                                            [Math.max((c - r.getItems().get(i).getWeight().intValue()), 0)],
                                    matrix[i - 1][c]);
                } else {
                    matrix[i][c] = matrix[i - 1][c];
                }
            }
        }

        return getItemIndexes(matrix, r);

    }

    /**
     * @param matrix Matrix result of the one test case
     * @param r      line data in the file(one test case)
     * @return String result of the one test case
     * @throws APIException
     */
    private static String getItemIndexes(int[][] matrix, Record r) {

        String indexes = ""; //comma separated String value as result
        int i = matrix.length - 1; //Row size of the matrix
        int j = matrix[0].length - 1; //column size of the matrix
        List<Integer> indexList = new LinkedList<>();// index list as temporary to use contains function
        while (i > 0 && j > 0) {
            if (matrix[i][j] != matrix[i - 1][j]) {
                indexList.add(i);
                final int finalI = i;//temp local variable to use inside lambda function
                int tmpIndex = i;// temp local variable to swap the index
                List<Item> sameCostItems = r.getItems().stream().filter(item -> item.getCost() == r.getItems().get(finalI).getCost() &&
                        item.getIndex() > finalI && item.getWeight() < r.getItems().get(finalI).getWeight()).collect(Collectors.toList());//same cost items
                if (sameCostItems.size()>0 && !indexList.contains(sameCostItems.stream().min(Comparator.comparingDouble(Item::getWeight)).orElseThrow().getIndex().intValue())) {
                    tmpIndex = sameCostItems.stream().min(Comparator.comparingDouble(Item::getWeight)).orElseThrow().getIndex().intValue();//find index of minimum weight items with same cost
                }
                indexes = tmpIndex + "," + indexes;
                j -= r.getItems().get(i).getWeight().intValue();

            }
            i--;
        }

        return indexes;
    }
}
