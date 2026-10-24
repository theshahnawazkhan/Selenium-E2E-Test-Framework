package com.liskart.utilities;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProgramJsonUtil {

    private static final String JSON_PATH = System.getProperty("user.dir") +
            "src/test/resources/testdata/Programs.json";

    // Load full JSON
    public static Map<String, Map<String, List<String>>> loadAllData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(
                    new File(JSON_PATH),
                    new TypeReference<Map<String, Map<String, List<String>>>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("Unable to read programs.json", e);
        }
    }

    // Load data for one school
    public static Map<String, List<String>> getProgramsForSchool(String schoolName) {
        Map<String, Map<String, List<String>>> allData = loadAllData();
        return allData.get(schoolName);
    }
}
