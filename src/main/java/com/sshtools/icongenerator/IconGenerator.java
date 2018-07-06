package com.sshtools.icongenerator;

/**
 * Implement by classes that can generate a particular type of icon from an
 * {@link IconBuilder}.
 *
 * @param <T>
 *            type of icon
 * @see IconBuilder#generator(Class, IconGenerator)
 */
public interface IconGenerator<T> {
	T generate(IconBuilder builder);
}
