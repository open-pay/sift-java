/**
 *
 */
package com.mcac0006.siftscience.types.serializer;

import java.io.IOException;

import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * @author matthew.cachia
 *
 */
public class DateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(final Date date, final JsonGenerator gen, final SerializerProvider pro) throws IOException, JsonProcessingException {
		gen.writeNumber(date.getTime() / 1000);
	}

}
