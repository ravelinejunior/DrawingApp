package br.com.drawingapp

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*

class MainActivity : AppCompatActivity() {

    private var mImageButtonCurrentPaint: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView_layout_id.setSizeForBrush(20.toFloat())

        mImageButtonCurrentPaint = ll_paint_colors[1] as ImageButton

        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.pallet_pressed
            )
        )

        imageButton_brush.setOnClickListener {
            showBrushSizeChooserDialog()
        }

        imageButton_openFromGallery.setOnClickListener {
            if (isReadStorageAllow()) {
                // do it
            } else {
                requestStoragePermission()
            }
        }


    }


    private fun showBrushSizeChooserDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Choose Brush Size")

        brushDialog.show()

        val smallButton = brushDialog.imageButton_small_id
        val mediumButton = brushDialog.imageButton_medium_id
        val largeButton = brushDialog.imageButton_large_id

        smallButton.setOnClickListener {
            drawingView_layout_id.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }

        mediumButton.setOnClickListener {
            drawingView_layout_id.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
        }

        largeButton.setOnClickListener {
            drawingView_layout_id.setSizeForBrush(30.toFloat())
            brushDialog.dismiss()
        }


    }


    fun paintClicked(view: View) {
        if (view !== mImageButtonCurrentPaint) {
            val imageButton = view as ImageButton

            val colorTag = imageButton.tag.toString()
            drawingView_layout_id.setColor(colorTag)

            imageButton.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.pallet_pressed
                )
            )

            mImageButtonCurrentPaint!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.pallet_normal
                )
            )

            mImageButtonCurrentPaint = view
        }
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).toString()
            )
        ) {
            Toast.makeText(this, "Need permission to add an Image", Toast.LENGTH_SHORT).show()
        }

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), STORAGE_PERMISSION_CODE
        )
    }

    private fun isReadStorageAllow(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
            }
        } else Toast.makeText(this, "Permissions denied!", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val STORAGE_PERMISSION_CODE = 1
    }

}