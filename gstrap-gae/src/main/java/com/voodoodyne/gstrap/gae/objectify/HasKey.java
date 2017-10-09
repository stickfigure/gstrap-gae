/*
 */

package com.voodoodyne.gstrap.gae.objectify;

import com.googlecode.objectify.Key;

/**
 * Generic interface that all entities should implement
 */
public interface HasKey<T extends HasKey<T>> {
	Key<T> getKey();
}
