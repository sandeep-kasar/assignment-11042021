package com.sample.assignment.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.sample.assignment.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


object Util {


    //get real path getRealPathFromUri
    @kotlin.jvm.JvmStatic
    fun getRealPathFromUri(context: Context, contentURI: String): String {
        val contentUri = Uri.parse(contentURI)
        val cursor = context.getContentResolver().query(contentUri, null, null, null, null)

        return if (cursor == null) {
            contentUri.path.toString()
        } else {
            cursor!!.moveToFirst()
            val index = cursor!!.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor!!.getString(index)
        }
    }

    //create image file
    fun getOutputMediaFile(directoryName: String): File? {

        // External sdcard location
        val mediaStorageDir = File(
                Environment.getExternalStorageDirectory().getPath(), directoryName)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(directoryName, "Oops! Failed create "
                        + directoryName + " directory")
                return null
            }
        }
        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.ENGLISH).format(Date())
        val mediaFile: File
        mediaFile = File(mediaStorageDir.path + File.separator
                + "IMG_" + timeStamp + ".png")

        return mediaFile
    }

    @kotlin.jvm.JvmStatic
    fun getOutputMediaFileUri(context: Context, direName: String): Uri {

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",
                    getOutputMediaFile(direName)!!)

        } else {

            Uri.fromFile(getOutputMediaFile(direName))
        }
    }

}