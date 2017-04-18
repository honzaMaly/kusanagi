package cz.jan.maly.model;

import cz.jan.maly.model.metadata.FactKey;

/**
 * Interface for converter
 * Created by Jan on 17-Apr-17.
 */
public interface Converter {

    /**
     * Get order of converted feature
     *
     * @return
     */
    int getOrder();

    /**
     * Get fact key it worked with
     *
     * @return
     */
    FactKey<?> getFactKey();

}
