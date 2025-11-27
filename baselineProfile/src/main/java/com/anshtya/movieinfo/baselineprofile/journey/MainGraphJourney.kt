package com.anshtya.movieinfo.baselineprofile.journey

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until

fun MacrobenchmarkScope.goToMainGraph() {
    val getStarted = By.res("get_started")
    if (device.wait(Until.hasObject(getStarted), 2000L)) {
        device.findObject(getStarted).click()

        val continueButton = By.res("continue")
        device.wait(Until.hasObject(continueButton), 2000L)
        device.findObject(continueButton).click()

        val bottomBar = By.res("bottom_bar")
        device.wait(Until.hasObject(bottomBar), 2000L)
    }
}