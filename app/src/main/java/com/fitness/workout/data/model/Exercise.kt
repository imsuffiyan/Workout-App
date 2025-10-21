package com.fitness.workout.data.model

import android.os.Parcel
import android.os.Parcelable


data class Exercise(
    val id: Int,
    val title: String,
    val durationSec: Int,
    val calories: Int,
    val description: String,
    val thumbnailRes: Int = 0,
    val videoResName: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeInt(durationSec)
        parcel.writeInt(calories)
        parcel.writeString(description)
        parcel.writeInt(thumbnailRes)
        parcel.writeString(videoResName)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Exercise> {
        override fun createFromParcel(parcel: Parcel): Exercise = Exercise(parcel)
        override fun newArray(size: Int): Array<Exercise?> = arrayOfNulls(size)
    }
}
