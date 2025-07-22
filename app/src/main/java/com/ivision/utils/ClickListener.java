package com.ivision.utils;

import com.ivision.model.Size;

public interface ClickListener {
    default void onItemSelected(int position) {

    }

    default void onItemSelected(int position, Size model) {

    }

    default void onItemSelected(int position, int childPosition, Size model) {

    }

    default void onItemSelected(int position, String str) {

    }
}