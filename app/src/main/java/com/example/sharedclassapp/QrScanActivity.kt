package com.example.sharedclassapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.journeyapps.barcodescanner.CaptureActivity
import java.io.InputStream

class QrScanActivity : CaptureActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 檢查是否有 IMAGE_URI
        val imageUri = intent.getStringExtra("IMAGE_URI")
        if (imageUri != null) {
            decodeQrFromImage(Uri.parse(imageUri))
        }
        // 若沒有 IMAGE_URI，則走預設的相機掃描流程
    }

    private fun decodeQrFromImage(uri: Uri) {
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            if (bitmap != null) {
                val intArray = IntArray(bitmap.width * bitmap.height)
                bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
                val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
                val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
                val reader = MultiFormatReader()
                val result = try {
                    reader.decode(binaryBitmap)
                } catch (e: NotFoundException) {
                    null
                }
                if (result != null) {
                    val intent = Intent()
                    intent.putExtra("SCAN_RESULT", result.text)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(this, "找不到 QRCode", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            } else {
                Toast.makeText(this, "找不到 QRCode", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "找不到 QRCode", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}