package com.rslakra.appsuite.core.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * @author Rohtash Lakra
 * @created 5/7/20 5:43 PM
 */
public class WeekDayDeserializer extends StdDeserializer<WeekDays> {

    public WeekDayDeserializer() {
        super(WeekDays.class);
    }

    /**
     *
     * @param classType
     */
    public WeekDayDeserializer(Class classType) {
        super(classType);
    }

    /**
     * @param jsonParser
     * @param deserializationContext
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public WeekDays deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String name = node.asText();
        for (WeekDays weekDays : WeekDays.values()) {
            if (weekDays.name().equalsIgnoreCase(name)) {
//            if (weekDays.name().equals(name)) {
                return weekDays;
            }
        }

        return null;
    }
}
