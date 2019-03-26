package com.example.roman.appfortest.forNetworkGifs.ui;

import android.support.annotation.Nullable;

public final class QueryValidator {

    private static final int MIN_QUERY_LENGTH = 3;

    private QueryValidator() {
        throw new RuntimeException("There is must be no instance!");
    }


    public static boolean isValid(@Nullable String search) {
        return search != null && search.length() >= MIN_QUERY_LENGTH;
    }
}
