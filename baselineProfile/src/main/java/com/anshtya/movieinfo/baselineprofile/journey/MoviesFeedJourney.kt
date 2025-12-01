package com.anshtya.movieinfo.baselineprofile.journey

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until

fun MacrobenchmarkScope.moviesFeedJourney() {
    waitForAsyncContent()
    scrollMovieFeed()
    goToMovieDetailJourney()
}

private fun MacrobenchmarkScope.waitForAsyncContent() {
    device.wait(Until.hasObject(By.res("movie_feed_list")), 5_000)
    val feedList = device.findObject(By.res("movie_feed_list"))
    // Wait until a content section item within the list is rendered.
    feedList.wait(Until.hasObject(By.res("movie_feed_content")), 5_000)
}

private fun MacrobenchmarkScope.scrollMovieFeed() {
    val snackList = device.findObject(By.res("movie_feed_list"))
    // Set gesture margin to avoid triggering gesture navigation.
    snackList.setGestureMargin(device.displayWidth / 5)
    snackList.fling(Direction.DOWN)
    snackList.fling(Direction.UP)
    device.waitForIdle()
}

fun MacrobenchmarkScope.goToMovieDetailJourney() {
    val feedList = device.findObject(By.res("movie_feed_list"))
    val movies = feedList.findObjects(By.res("movie_item"))
    // Select movie from the list based on running iteration.
    val index = (iteration ?: 0) % movies.size
    movies[index].click()
    // Wait until the screen is gone = the detail is shown.
    device.wait(Until.gone(By.res("movie_feed_list")), 5_000)
}