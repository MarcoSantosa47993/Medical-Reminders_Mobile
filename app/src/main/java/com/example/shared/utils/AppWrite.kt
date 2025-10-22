package com.example.shared.utils


import android.content.Context
import android.graphics.Bitmap
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.File
import io.appwrite.models.InputFile
import io.appwrite.services.Account
import io.appwrite.services.Storage

object AppWrite {
    lateinit var client: Client
    lateinit var account: Account
    lateinit var storage: Storage

    fun init(context: Context) {
        client = Client(context)
            .setEndpoint("https://fra.cloud.appwrite.io/v1")
            .setProject("682c867a001212613738")

        account = Account(client)
        storage = Storage(client)
    }

    suspend fun getFile(imageId: String) = storage.getFileView(
        bucketId = "682c876e0026f488f9d3",
        fileId = imageId
    )

    suspend fun removeFile(imageId: String) = storage.deleteFile(
        bucketId = "682c876e0026f488f9d3",
        fileId = imageId
    )

    suspend fun uploadFile(bitmap: Bitmap): File? {
        val id = ID.unique()
        return storage.createFile(
            fileId = id,
            bucketId = "682c876e0026f488f9d3",
            file = InputFile.fromBytes(bitmap.toByteArray(), "$id.png", "image/png")
        )
    }


}