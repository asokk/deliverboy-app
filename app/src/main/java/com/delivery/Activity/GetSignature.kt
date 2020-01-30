package com.delivery.Activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.delivery.Activity.GetSignature
import com.delivery.Config.BaseURL
import com.delivery.MainActivity
import com.delivery.R
import com.franmontiel.localechanger.LocaleChanger
import com.kyanogen.signatureview.SignatureView
import net.gotev.uploadservice.MultipartUploadRequest
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class GetSignature : AppCompatActivity() {
    var bitmap: Bitmap? = null
    var clear: Button? = null
    var save: Button? = null
    var upload: Button? = null
    var choose: Button? = null
    var signImage: ImageView? = null
    var get_order_id = ""
    var path: String? = null
    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    var signatureView: SignatureView? = null
    var saveimage = "false"
    var linearLayout: LinearLayout? = null
    var choose_capture: ImageView? = null
    var chooseimage = "false"
    override fun attachBaseContext(newBase: Context) {
        var newBase: Context? = newBase
        newBase = LocaleChanger.configureBaseContext(newBase)
        super.attachBaseContext(newBase)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_signature)
        requestStoragePermission()
        linearLayout = findViewById<View>(R.id.linear_layout) as LinearLayout
        choose_capture = findViewById<View>(R.id.choose_capture) as ImageView
        signatureView = findViewById<View>(R.id.signature_view) as SignatureView
        clear = findViewById<View>(R.id.clear) as Button
        clear!!.setOnClickListener { signatureView!!.clearCanvas() }
        choose = findViewById<View>(R.id.choose) as Button
        choose!!.setOnClickListener {
            chooseimage = "true"
            showFileChooser()
        }
        upload = findViewById<View>(R.id.upload) as Button
        upload!!.setOnClickListener {
            if (saveimage.contains("true")) {
                if (chooseimage.contains("true")) {
                    uploadMultipart()
                } else {
                    Toast.makeText(this@GetSignature, "Please Choose Signature Image ", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@GetSignature, "First Save Signature", Toast.LENGTH_SHORT).show()
            }
        }
        save = findViewById<View>(R.id.save) as Button
        save!!.setOnClickListener {
            saveimage = "true"
            bitmap = signatureView!!.signatureBitmap
            signatureView!!.clearCanvas()
            path = saveImage(bitmap)
        }
        get_order_id = intent.extras.getString("sale")
    }

    //Save Signature
    fun saveImage(myBitmap: Bitmap?): String {
        val bytes = ByteArrayOutputStream()
        myBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY)
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
            Log.d("hhhhh", wallpaperDirectory.toString())
        }
        try {
            val f = File(wallpaperDirectory, Calendar.getInstance()
                    .timeInMillis.toString() + ".jpg")
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this@GetSignature, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)
            Toast.makeText(this, "Signature Saved", Toast.LENGTH_SHORT).show()
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

    //Upload To Server
    fun uploadMultipart() {
        val path = getPath(filePath)
        try {
            val uploadId = UUID.randomUUID().toString()
            MultipartUploadRequest(this, BaseURL.urlUpload)
                    .addFileToUpload(path, "signature") //Adding file
                    .addParameter("id", get_order_id) //Adding text parameter to the request
                    .setMaxRetries(2)
                    .startUpload() //Starting the upload
            val intent = Intent(this@GetSignature, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Upload Successfully", Toast.LENGTH_SHORT).show()
        } catch (exc: Exception) {
            Toast.makeText(this, exc.message, Toast.LENGTH_SHORT).show()
        }
    }

    //Choose File From Gallery
    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    //handling the image chooser activity result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                choose_capture!!.setImageBitmap(bitmap)
                linearLayout!!.visibility = View.VISIBLE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    //Get Image Path
    fun getPath(uri: Uri?): String {
        var cursor = contentResolver.query(uri, null, null, null, null)
        cursor.moveToFirst()
        var document_id = cursor.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor.close()
        cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", arrayOf(document_id), null)
        cursor.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor.close()
        return path
    }

    //Requesting permission
    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) return
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        }
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    inner class signature(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
        var paint = Paint()
        var path = Path()
        var lastTouchX = 0f
        var lastTouchY = 0f
        val dirtyRect = RectF()
        fun clear() {
            path.reset()
            invalidate()
            save!!.isEnabled = false
        }

        fun save() {
            val returnedBitmap = Bitmap.createBitmap(signatureView!!.width,
                    signatureView!!.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(returnedBitmap)
            val bgDrawable = signatureView!!.background
            if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
            signatureView!!.draw(canvas)
            val bs = ByteArrayOutputStream()
            returnedBitmap.compress(Bitmap.CompressFormat.PNG, 50, bs)
            val intent = Intent()
            intent.putExtra("byteArray", bs.toByteArray())
            setResult(1, intent)
            finish()
        }

        override fun onDraw(canvas: Canvas) { // TODO Auto-generated method stub
            canvas.drawPath(path, paint)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val eventX = event.x
            val eventY = event.y
            save!!.isEnabled = true
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    path.moveTo(eventX, eventY)
                    lastTouchX = eventX
                    lastTouchY = eventY
                    return true
                }
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                    resetDirtyRect(eventX, eventY)
                    val historySize = event.historySize
                    var i = 0
                    while (i < historySize) {
                        val historicalX = event.getHistoricalX(i)
                        val historicalY = event.getHistoricalY(i)
                        path.lineTo(historicalX, historicalY)
                        i++
                    }
                    path.lineTo(eventX, eventY)
                }
            }
            invalidate((dirtyRect.left - Companion.HALF_STROKE_WIDTH).toInt(),
                    (dirtyRect.top - Companion.HALF_STROKE_WIDTH).toInt(),
                    (dirtyRect.right + Companion.HALF_STROKE_WIDTH).toInt(),
                    (dirtyRect.bottom + Companion.HALF_STROKE_WIDTH).toInt())
            lastTouchX = eventX
            lastTouchY = eventY
            return true
        }

        private fun resetDirtyRect(eventX: Float, eventY: Float) {
            dirtyRect.left = Math.min(lastTouchX, eventX)
            dirtyRect.right = Math.max(lastTouchX, eventX)
            dirtyRect.top = Math.min(lastTouchY, eventY)
            dirtyRect.bottom = Math.max(lastTouchY, eventY)
        }


        init {
            paint.isAntiAlias = true
            paint.color = Color.BLACK
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeWidth = Companion.STROKE_WIDTH
        }
    }



    companion object {
        private const val IMAGE_DIRECTORY = "/GroceryDeliverySignature"
        private const val STORAGE_PERMISSION_CODE = 123
        const val STROKE_WIDTH = 10f
        const val HALF_STROKE_WIDTH = STROKE_WIDTH / 2
    }
}