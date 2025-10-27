package com.fitness.workout.domain.repository

import android.net.Uri

interface MediaRepository {
    fun resolveRawVideoUri(videoResName: String): Uri?
}