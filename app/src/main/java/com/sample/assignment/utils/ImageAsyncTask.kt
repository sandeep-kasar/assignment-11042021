package com.sample.assignment.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.os.AsyncTask
import android.view.Window
import com.sample.assignment.R
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


@Suppress("DEPRECATION")
class ImageAsyncTask(
        private val mContext: Context,
        private val imageCallback: ImageCallback,
        private val imageNo: Int
) : AsyncTask<String, Void, Bitmap>()
{
    private lateinit var scaledBitmap: Bitmap
    private lateinit var filePath: String
    private lateinit var out: FileOutputStream
    private lateinit var dialog: Dialog

    interface ImageCallback {

        fun setImageInfo(imageNo:Int,bitmap: String)
        fun setImage(imageNo:Int,bitmap: Bitmap)
    }

    override fun onPreExecute() {
        super.onPreExecute()

        dialog = Dialog(mContext)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.layout_progressbar)
        dialog!!.setCancelable(false)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog!!.show()

    }

    override fun doInBackground(vararg params: String): Bitmap {

        //init bitmap for further use
        scaledBitmap = BitmapFactory.decodeFile(params[0], BitmapFactory.Options())

        //check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface("" + params[0])

            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)

            val matrix = Matrix()
            when (orientation) {
                6 -> matrix.postRotate(90f)
                3 -> matrix.postRotate(180f)
                8 -> matrix.postRotate(270f)
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap!!, 0, 0, scaledBitmap!!.width,
                    scaledBitmap!!.height, matrix, true)

        } catch (e: IOException) {
            e.printStackTrace()
        }

        // file path
        filePath = params[0]

        try {

            // reduce image size < 500kb
            val m = Matrix()
            m.setRectToRect(
                    RectF(0f, 0f, scaledBitmap.width.toFloat(), scaledBitmap.height.toFloat()),
                    RectF(0f, 0f, 500f, 500f), Matrix.ScaleToFit.CENTER
            )
            scaledBitmap = Bitmap.createBitmap(
                    scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, m, true
            )
            out = FileOutputStream(params[0])

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return scaledBitmap
    }

    override fun onPostExecute(bitmap: Bitmap) {
        super.onPostExecute(bitmap)

        dialog!!.dismiss()

        imageCallback.setImage(imageNo,bitmap)

        imageCallback.setImageInfo(imageNo,filePath)
    }
}
