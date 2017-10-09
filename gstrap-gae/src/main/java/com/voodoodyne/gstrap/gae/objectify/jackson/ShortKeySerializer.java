package com.voodoodyne.gstrap.gae.objectify.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.googlecode.objectify.Key;
import com.voodoodyne.gstrap.gae.objectify.KeyStringer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * Configuring this serializer will make Key<?> objects render as their short string.
 */
@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
public class ShortKeySerializer extends JsonSerializer<Key> {

	private final KeyStringer keyStringer;

	@Override
	public void serialize(Key value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString(keyStringer.stringify(value));
	}
}
