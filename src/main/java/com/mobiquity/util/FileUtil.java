package com.mobiquity.util;

import com.mobiquity.constant.Constraint;
import com.mobiquity.exception.APIConstraintErrorException;
import com.mobiquity.exception.APIException;
import com.mobiquity.model.Item;
import com.mobiquity.model.Record;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Util class to read from file into the List of Record Model.
 */


public class FileUtil {
    private FileUtil() {

    }

    /**
     * @param filePath file path as a string
     * @return List of record model
     * @throws APIException
     */
    public static List<Record> getRecords(String filePath) throws APIException {
        Path path = Paths.get(filePath);
        List<Record> records = new LinkedList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> records.add(getRecord(line)));
        } catch (IOException e) {
            throw new APIException(e.getMessage());
        }
        return records;
    }

    /**
     * @param line Take a line of string from test file as argument
     * @return record model
     * @throws APIException
     */
    private static Record getRecord(String line) throws APIConstraintErrorException {
        List<String> stringItems = Arrays.asList(line.split(" "));


        try {
            List<Item> items = new LinkedList<>();
            items.add(Item.builder().index(0L)
                    .weight(0d)
                    .cost(0)
                    .build());
            items.addAll(stringItems.stream().filter(item -> item.startsWith("("))
                    .map(FileUtil::getItem).collect(Collectors.toList()));
            int capacity = Integer.parseInt(stringItems.get(0));
            if (capacity > Constraint.PACKAGE_MAX_WEIGHT) {
                throw new APIConstraintErrorException("Package weight exceeded the MAX WEIGHT");
            }
            if (items.stream().filter(item -> item.getCost() > Constraint.ITEM_MAX_COST).count() > 0) {
                throw new APIConstraintErrorException("Item Cost exceeded the MAX COST");
            }
            if (items.stream().filter(item -> item.getWeight() / 100 > Constraint.ITEM_MAX_WEIGHT).count() > 0) {
                throw new APIConstraintErrorException("Item weight exceeded the MAX WEIGHT");
            }
            if (items.size() > Constraint.PACKAGE_ITEMS_NUM) {
                throw new APIConstraintErrorException("Total number of items exceeded the MAX NUMBER");
            }
            return Record.builder().capacity(capacity * 100)
                    .items(items)
                    .build();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * @param stringItem take string text after colon
     * @return Item model
     * @throws NumberFormatException
     */
    private static Item getItem(String stringItem) throws NumberFormatException {
        String[] values = stringItem.split(",");
        return Item.builder().index(Long.parseLong(values[0].substring(1)))
                .weight(Double.parseDouble(values[1]) * 100)
                .cost(Integer.parseInt(values[2].substring(1, values[2].length() - 1)))
                .build();
    }
}
