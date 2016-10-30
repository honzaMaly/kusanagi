package cz.jan.maly.convertors;

import cz.jan.maly.enums.Race;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter to convert enumeration of races to string value to be saved/loaded from db
 * Created by Jan on 30-Oct-16.
 */
@Converter(autoApply = true)
public class RaceConverter implements AttributeConverter<Race, String> {
    @Override
    public String convertToDatabaseColumn(Race race) {
        if (race == null) {
            return null;
        }
        return race.name();
    }

    @Override
    public Race convertToEntityAttribute(String s) {
        for (Race race : Race.values()) {
            if (race.name().equals(s)) {
                return race;
            }
        }
        return Race.UNKNOWN;
    }
}
