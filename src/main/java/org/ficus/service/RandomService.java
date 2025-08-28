package org.ficus.service;

import java.util.List;
import java.util.Random;

public class RandomService {

    private static final Random random = new Random();

    public static <T> T getRandomEntity(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        int randomIndex = random.nextInt(entities.size());
        return entities.get(randomIndex);
    }
}
