package com.udacity.gradle.builditbigger.listeners;

import com.udacity.gradle.builditbigger.SimpleIdlingResource;

/**
 * Created by lamch on 10/8/2017.
 */

public interface OnIdleChangeListener {
    SimpleIdlingResource SIMPLE_IDLING_RESOURCE = new SimpleIdlingResource();
    void onIdle();
    void onBusy();
    SimpleIdlingResource getIdlingResource();
}
