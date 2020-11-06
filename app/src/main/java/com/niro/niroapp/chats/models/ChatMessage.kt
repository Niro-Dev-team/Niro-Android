package com.niro.niroapp.chats.models

import android.os.Parcel
import android.os.Parcelable
import com.niro.niroapp.models.responsemodels.MandiLocation

data class ChatMessage(
    var userId: String? = null,
    var senderName: String? = null,
    var message: String? = null,
    var location: MandiLocation? = null,
    var fileName: String? = null


) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readParcelable<MandiLocation>(MandiLocation::class.java.classLoader),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(userId)
        writeString(senderName)
        writeString(message)
        writeParcelable(location, 0)
        writeString(fileName)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ChatMessage> = object : Parcelable.Creator<ChatMessage> {
            override fun createFromParcel(source: Parcel): ChatMessage = ChatMessage(source)
            override fun newArray(size: Int): Array<ChatMessage?> = arrayOfNulls(size)
        }
    }
}
