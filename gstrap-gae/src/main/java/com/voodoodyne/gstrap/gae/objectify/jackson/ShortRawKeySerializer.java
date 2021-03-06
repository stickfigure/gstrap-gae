package com.voodoodyne.gstrap.gae.objectify.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.appengine.api.datastore.Key;
import com.voodoodyne.gstrap.gae.objectify.RawKeyStringer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * Configuring this serializer will make native datastore Key objects render as their short string.
 */
@RequiredArgsConstructor
public class ShortRawKeySerializer extends JsonSerializer<Key> {

	private final RawKeyStringer keyStringer;

	@Override
	public void serialize(Key value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString(keyStringer.stringify(value));
	}
}
