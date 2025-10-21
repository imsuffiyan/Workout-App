package com.fitness.workout.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class PrefsManager(context: Context) {
    private val ds = context.dataStore

    object Keys {
        val NAME = stringPreferencesKey("name")
        val AGE = intPreferencesKey("age")
        val TARGET = floatPreferencesKey("target_weight")
        val NOTIFS = booleanPreferencesKey("notifications")

        // extended fields
        val PURPOSE = stringPreferencesKey("purpose")
        val UNITS = stringPreferencesKey("units")
        val HEIGHT = stringPreferencesKey("height")
        val CURRENT = floatPreferencesKey("current_weight")
        val AVATAR = stringPreferencesKey("avatar_uri")

        // water total in milliliters
        val WATER = intPreferencesKey("water_ml")

        // program progress: current day index (1..N)
        val PROGRAM_DAY = intPreferencesKey("program_current_day")
    }

    val profile: Flow<UserProfile> = ds.data.map { prefs ->
        UserProfile(
            name = prefs[Keys.NAME],
            age = prefs[Keys.AGE],
            targetWeight = prefs[Keys.TARGET],
            notificationsEnabled = prefs[Keys.NOTIFS] ?: false,

            // extended
            purpose = prefs[Keys.PURPOSE],
            units = prefs[Keys.UNITS],
            height = prefs[Keys.HEIGHT],
            currentWeight = prefs[Keys.CURRENT],
            avatarUri = prefs[Keys.AVATAR],

            // water total (ml) - may be null if not set
            waterMl = prefs[Keys.WATER],

            // program progress
            programDay = prefs[Keys.PROGRAM_DAY]
        )
    }

    suspend fun saveProfile(name: String?, age: Int?, target: Float?, notifications: Boolean) {
        ds.edit { prefs ->
            if (name != null) prefs[Keys.NAME] = name
            if (age != null) prefs[Keys.AGE] = age
            if (target != null) prefs[Keys.TARGET] = target
            prefs[Keys.NOTIFS] = notifications
        }
    }

    // save extended profile fields; nullable params are ignored when null
    suspend fun saveProfileExtended(purpose: String? = null, units: String? = null, height: String? = null, current: Float? = null) {
        ds.edit { prefs ->
            if (purpose != null) prefs[Keys.PURPOSE] = purpose
            if (units != null) prefs[Keys.UNITS] = units
            if (height != null) prefs[Keys.HEIGHT] = height
            if (current != null) prefs[Keys.CURRENT] = current
        }
    }

    // save only target weight (convenience)
    suspend fun saveTargetWeight(target: Float?) {
        if (target == null) return
        ds.edit { prefs ->
            prefs[Keys.TARGET] = target
        }
    }

    // save avatar uri string
    suspend fun saveAvatarUri(uri: String?) {
        ds.edit { prefs ->
            if (uri != null) prefs[Keys.AVATAR] = uri
        }
    }

    // Save/overwrite water total (ml)
    suspend fun saveWaterMl(value: Int) {
        ds.edit { prefs ->
            prefs[Keys.WATER] = value
        }
    }

    // Increment water total atomically by `add` milliliters
    suspend fun incrementWaterMl(add: Int) {
        ds.edit { prefs ->
            val current = prefs[Keys.WATER] ?: 0
            prefs[Keys.WATER] = current + add
        }
    }

    // Save/overwrite program day
    suspend fun saveProgramDay(day: Int) {
        ds.edit { prefs ->
            prefs[Keys.PROGRAM_DAY] = day
        }
    }

    // Increment program day by 1 (up to a given max handled by callers)
    suspend fun incrementProgramDay() {
        ds.edit { prefs ->
            val current = prefs[Keys.PROGRAM_DAY] ?: 1
            prefs[Keys.PROGRAM_DAY] = current + 1
        }
    }
}

data class UserProfile(
    val name: String? = null,
    val age: Int? = null,
    val targetWeight: Float? = null,
    val notificationsEnabled: Boolean = false,

    // extended
    val purpose: String? = null,
    val units: String? = null,
    val height: String? = null,
    val currentWeight: Float? = null,
    val avatarUri: String? = null,

    // water total in milliliters
    val waterMl: Int? = null,

    // program progress: current day number
    val programDay: Int? = null
)
