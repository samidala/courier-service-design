package com.everestengg.code.challenge.model;

import com.everestengg.code.challenge.vo.Package;

/**
 * used to get the package property value
 * @param <T> type of value to be read from package like string, number, array etc..
 */
public interface ValueHandler<T> {
    T getValue(Package aPackage);
}
