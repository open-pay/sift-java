/**
 *
 */
package com.mcac0006.siftscience.types.deserializer;

import java.io.IOException;

import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * @author matthew.cachia
 */
public class DateDeserializer extends JsonDeserializer<Date> {

    /*
     * (non-Javadoc)
     * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser,
     * org.codehaus.jackson.map.DeserializationContext)
     */
    @Override
    public Date deserialize(final JsonParser parser, final DeserializationContext context) throws IOException,
            JsonProcessingException {
        return new Date(parser.getLongValue() * 1000);
    }

}
