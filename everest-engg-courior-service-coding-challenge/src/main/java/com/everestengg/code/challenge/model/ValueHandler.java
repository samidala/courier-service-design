package com.everestengg.code.challenge.model;

import com.everestengg.code.challenge.bo.Package;

public interface ValueHandler<T> {
    T getValue(Package aPackage);
}
