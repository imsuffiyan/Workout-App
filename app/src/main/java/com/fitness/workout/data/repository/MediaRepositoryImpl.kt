package com.fitness.workout.data.repository

import android.content.Context
import android.net.Uri
import com.fitness.workout.domain.repository.MediaRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepositoryImpl @Inject constructor(
    private val context: Context
) : MediaRepository {
    override fun resolveRawVideoUri(videoResName: String): Uri? {
        if (videoResName.isBlank()) return null
        val resId = context.resources.getIdentifier(videoResName, "raw", context.packageName)
        if (resId == 0) return null
        return Uri.parse("android.resource://${context.packageName}/$resId")
    }
}
