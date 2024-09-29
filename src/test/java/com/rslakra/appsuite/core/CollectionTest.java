package com.rslakra.appsuite.core;

import com.rslakra.appsuite.core.entity.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Rohtash Lakra
 * @created 9/11/19 12:24 PM
 */
public class CollectionTest {

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionTest.class);

    void testListVsMap() {
        List<Color> listColors = Arrays.asList(new Color(1L, "Red"), new Color(2L, "Green"), new Color(3L, "Blue"));
        LOGGER.debug("{}", listColors);
        final Map<Long, Color> mapColors = new HashMap<>();
        listColors.stream().forEach(color -> mapColors.put(color.getId(), color));
        LOGGER.debug("{}", mapColors);
        Map<Long, Color> colors = listColors.stream().collect(Collectors.toMap(Color::getId, color -> color));
        LOGGER.debug("{}", colors);
    }

    private Set<Long> filledLongSet(int count, Random random) {
        Set<Long> longSet = new HashSet<>();
        for (int ctr = 0; ctr < count; ctr++) {
            longSet.add(Long.valueOf(random.nextInt(10)));
        }

        return longSet;
    }

    void testSetsVsSet() {
        Random random = new Random();
        Set<Long> longSet = filledLongSet(5, random);
        LOGGER.debug("{}", longSet);
        Map<Long, Set<Long>> mapLongSets = new HashMap<>();
        mapLongSets.put(1L, filledLongSet(2, random));
        mapLongSets.put(2L, filledLongSet(3, random));
        mapLongSets.put(3L, filledLongSet(4, random));
        LOGGER.debug("{}", mapLongSets);

        //retain
        mapLongSets.values().forEach(s -> s.retainAll(longSet));
        LOGGER.debug("After Retain");
        LOGGER.debug("{}", mapLongSets);
        Map<Long, Set<Long>> setTemp = new HashMap<>(mapLongSets);

        LOGGER.debug("Remove keys, which value set is empty.");
        //remove entry that value set is empty
        setTemp.forEach((k, v) -> {
            if (v.size() == 0) {
                mapLongSets.remove(k);
            }
        });
        LOGGER.debug("{}", mapLongSets);
    }

    public void convertToMapFromObjectArrayList() {
        LOGGER.debug("convertToMapFromObjectArrayList");
        Object[] objects = new Object[]{
            new Object[]{Long.valueOf(1), Integer.valueOf(10)}
        };

        List<Object[]> listObjects = Arrays.stream(objects).map(entry -> (Object[]) entry).collect(Collectors.toList());
        Map<Long, Integer>
            records =
            listObjects.stream().collect(Collectors.toMap(key -> Long.valueOf(key[0].toString()),
                                                          value -> Integer.valueOf(value[1].toString())));
        LOGGER.debug("{}", records);

        //multi-records
        objects = new Object[]{
            new Object[]{Long.valueOf(333533902L), Integer.valueOf(2)},
            new Object[]{Long.valueOf(7308537810L), Integer.valueOf(1)},
            new Object[]{Long.valueOf(333533993L), Integer.valueOf(2)},
            };

        listObjects = Arrays.stream(objects).map(entry -> (Object[]) entry).collect(Collectors.toList());

        records =
            listObjects.stream().collect(Collectors.toMap(key -> Long.valueOf(key[0].toString()),
                                                          value -> Integer.valueOf(value[1].toString())));
        LOGGER.debug("{}", records);

        // no records
        objects = new Object[0];
        listObjects = Arrays.stream(objects).map(entry -> (Object[]) entry).collect(Collectors.toList());
        records =
            listObjects.stream().collect(Collectors.toMap(key -> Long.valueOf(key[0].toString()),
                                                          value -> Integer.valueOf(value[1].toString())));
        LOGGER.debug("{}", records);
    }

    private final String toKey(final String type, final String parentType, Long parentId) {
        return (String.format("%s-%s-%d", type, parentType, parentId));
    }

    public void convertObjectArrayListToMap() {
        LOGGER.debug("convertObjectArrayListToMap");
        //multi-records
        List<Object[]> listObjects = new ArrayList<Object[]>();
        listObjects.add(new Object[]{"WOEID", "CAMPAIGN", 333533902L, Integer.valueOf(2)});
        listObjects.add(new Object[]{"WOEID", "ADGROUP", 7308537810L, Integer.valueOf(1)});
        listObjects.add(new Object[]{"WOEID", "CAMPAIGN", 333533993L, Integer.valueOf(2)});

        Map<Long, Integer>
            records =
            listObjects.stream().collect(Collectors.toMap(key -> Long.valueOf(key[2].toString()),
                                                          value -> Integer.valueOf(value[3].toString())));
        LOGGER.debug("{}", records);

        Map<String, Integer>
            keyRecords =
            listObjects.stream().collect(
                Collectors.toMap(key -> toKey(key[0].toString(), key[1].toString(), Long.valueOf(key[2].toString())),
                                 value -> Integer.valueOf(value[3].toString())));
        LOGGER.debug("{}", keyRecords);
    }

    public static void main(String[] args) {
        CollectionTest testCollections = new CollectionTest();
        testCollections.testListVsMap();
        testCollections.testSetsVsSet();
        testCollections.convertToMapFromObjectArrayList();
        testCollections.convertObjectArrayListToMap();
    }

}
