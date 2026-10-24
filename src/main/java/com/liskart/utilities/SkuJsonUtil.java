package com.liskart.utilities;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class SkuJsonUtil {
    private static final String JSON_PATH = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "testdata", "skus.json").toString();
    private static List<String> skuList;
    private static int currentIndex = 0;

    static {
        loadSkus();
    }

    private static void loadSkus() {
        try (FileReader reader = new FileReader(JSON_PATH)) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            skuList = gson.fromJson(jsonObject.getAsJsonArray("skus"), new TypeToken<List<String>>(){}.getType());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load SKUs from JSON file: " + JSON_PATH, e);
        }
    }

    // Get a random SKU
    public static String getRandomSku() {
        if (skuList == null || skuList.isEmpty()) {
            throw new IllegalStateException("SKU list is empty or not loaded.");
        }
        Random random = new Random();
        return skuList.get(random.nextInt(skuList.size()));
    }

    // Get the next SKU sequentially (wraps around)
    public static String getNextSku() {
        if (skuList == null || skuList.isEmpty()) {
            throw new IllegalStateException("SKU list is empty or not loaded.");
        }
        String sku = skuList.get(currentIndex);
        currentIndex = (currentIndex + 1) % skuList.size();
        return sku;
    }

    // Optionally, get all SKUs
    public static List<String> getAllSkus() {
        return skuList;
    }
}
