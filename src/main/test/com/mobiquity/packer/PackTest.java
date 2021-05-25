package com.mobiquity.packer;

import com.mobiquity.exception.APIConstraintErrorException;
import com.mobiquity.model.Record;
import com.mobiquity.util.FileUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Parker Unit Test
 */
class PackTest {

    String inputFilePath;
    String outputFilePath;

    @BeforeEach
    void setup() {
        inputFilePath = "src/main/test/resources/example_input";
        outputFilePath = "src/main/test/resources/example_output";
    }

    @Test
    @SneakyThrows
    void readFileTest() {

        int testCount = 4;
        final List<Record> actualRecords = FileUtil.getRecords(inputFilePath);
        assertEquals(testCount, actualRecords.size());

    }

    @SneakyThrows
    @Test
    public void packerTest() {
        System.out.println(Packer.pack(inputFilePath));

        Path path = Paths.get(outputFilePath);
        String expectedResult = String.join("\n", Files.readAllLines(path));
        String actualResult = Packer.pack(inputFilePath);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void packageMaxWeightExceptionTest() {

        Exception exception = assertThrows(APIConstraintErrorException.class, () -> {
            Packer.pack("src/main/test/resources/example_input_max_package_weight_exception");
        });
        String expectedMessage = "Package weight exceeded the MAX WEIGHT";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void itemMaxCostExceptionTest() {

        Exception exception = assertThrows(APIConstraintErrorException.class, () -> {
            Packer.pack("src/main/test/resources/example_input_max_item_cost_exception");
        });
        String expectedMessage = "Item Cost exceeded the MAX COST";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void itemMaxWeightExceptionTest() {

        Exception exception = assertThrows(APIConstraintErrorException.class, () -> {
            Packer.pack("src/main/test/resources/example_input_max_item_weight_exception");
        });
        String expectedMessage = "tem weight exceeded the MAX WEIGHT";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void itemCountExceptionTest() {
        Exception exception = assertThrows(APIConstraintErrorException.class, () -> {
            Packer.pack("src/main/test/resources/example_input_max_item_count_exception");
        });
        String expectedMessage = "Total number of items exceeded the MAX NUMBER";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
