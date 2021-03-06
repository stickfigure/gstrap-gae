package com.voodoodyne.gstrap.gae.objectify;

import com.googlecode.objectify.Key;

import javax.inject.Inject;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Allows us to use {@code Key<?>} as JAXRS parameters, converting using the shortkey version. Since
 * this requires some state, we need to inject the KeyStringer.
 *
 * To use this, bind a KeyStringer instance and register this class with Guice.
 */
@Provider
public class KeyParamConverterProvider implements ParamConverterProvider {

	private final KeyStringer keyStringer;

	@Inject
	public KeyParamConverterProvider(final KeyStringer keyStringer) {
		this.keyStringer = keyStringer;
	}

	@Override
	public <T> ParamConverter<T> getConverter(final Class<T> aClass, final Type type, final Annotation[] annotations) {
		if (aClass == Key.class) {
			return new ParamConverter<T>() {
				@Override
				public T fromString(final String s) {
					return aClass.cast(keyStringer.keyify(s));
				}

				@Override
				public String toString(final T t) {
					return keyStringer.stringify(((Key<?>)t));
				}
			};
		}

		return null;
	}
}
