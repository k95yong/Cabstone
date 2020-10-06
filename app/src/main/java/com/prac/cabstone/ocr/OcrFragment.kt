package com.prac.cabstone.ocr

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.googlecode.tesseract.android.TessBaseAPI
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.prac.cabstone.R
import kotlinx.android.synthetic.main.fragment_ocr.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class OcrFragment : Fragment() {
    val REQUEST_IMAGE_CAPTURE = 1
    val mLanguageList = arrayListOf("eng", "kor")
    var mDataPath= context?.filesDir.toString() + "/tesseract/"
    lateinit var currentPhotoPath: String
    val mTess = TessBaseAPI()
    val m_messageHandler = MessageHandler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_ocr, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingPermission() // 권한체크 시작

        initTesseract()

        btn_picture.setOnClickListener {
            startCapture()
        }
    }

    fun settingPermission() {
        var permis = object : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                Toast.makeText(activity, "권한 허가", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(activity, "권한 거부", Toast.LENGTH_SHORT)
                    .show()
                ActivityCompat.finishAffinity(activity!!) // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(this.context)
            .setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            )
            .check()
    }

    fun startCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this.context!!,
                        "com.prac.cabstone",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media
                    .getBitmap(activity!!.contentResolver, Uri.fromFile(file))
                iv_picture.setImageBitmap(bitmap)
            } else {
                val decode = ImageDecoder.createSource(
                    activity!!.contentResolver,
                    Uri.fromFile(file)
                )
                val bitmap = ImageDecoder.decodeBitmap(decode)

                if (bitmap != null) {
                    lateinit var rotatedBitmap: Bitmap
                    val ei = ExifInterface(currentPhotoPath)
                    val orientation = ei.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                    iv_picture.setImageBitmap(bitmap)
                    when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> {
                            rotatedBitmap = rotate(bitmap, 90f)
                        }
                        ExifInterface.ORIENTATION_ROTATE_180 -> {
                            rotatedBitmap = rotate(bitmap, 180f)
                        }
                        ExifInterface.ORIENTATION_TRANSVERSE -> {
                            rotatedBitmap = rotate(bitmap, 270f)
                        }
                        ExifInterface.ORIENTATION_ROTATE_270 -> {
                            rotatedBitmap = rotate(bitmap, 270f)
                        }
                        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> {
                            rotatedBitmap = flip(bitmap, true, vertical = false)
                        }
                        ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                            rotatedBitmap = flip(bitmap, false, vertical = true)
                        }
                        else -> {
                            rotatedBitmap = bitmap
                        }
                    }
                    if(bitmap != null){
                        //iv_picture.setImageBitmap(rotatedBitmap)
//                        val ocrThread = ARGBBitmap(bitmap)?.let { OCRThread(it) }
                        val ocrThread = ARGBBitmap(bitmap)?.let {
                            OCRThread(it).apply{
                                isDaemon = true
                            }
                        }
                        ocrThread?.start()
                        tv_ocr_result.text = "please wait..."
                    }
                }
            }
        }
    }

    private fun initTesseract(){
        mDataPath= context?.filesDir.toString() + "/tesseract/"
        var lang = ""
        for (lan in mLanguageList){
            checkFile(File(mDataPath + "tessdata/"), lan)
            lang += ("$lan+")
        }
        Log.e("check", "$mDataPath$lang")
        mTess.init(mDataPath, lang)
    }

    inner class OCRThread: Thread{
        val image: Bitmap
        constructor(image: Bitmap){
           this.image = image
        }

        override fun run() {
            super.run()
            var result: String? = null
            mTess.setImage(image)
            result = mTess.utF8Text

            val message = Message.obtain()
            message.what = ConstantDefine.RESULT_OCR
            message.obj = result
            m_messageHandler.sendMessage(message)
            Log.e("msg : ", message.toString())
        }
    }

    private fun checkFile(dir: File, language: String){
        if(!dir.exists()){
            dir.mkdirs()
            copyFiles(language)
        }
        if(dir.exists()){
            var datafilepath = mDataPath + "tessdata/" + language + ".traineddata"
            var datafile = File(datafilepath)
            if(!datafile.exists()){
                copyFiles(language)
            }
        }
    }
    private fun copyFiles(language: String){
        try {
            var filepath = mDataPath + "/tessdata/" + language + ".traineddata"
            var assetManager = context?.assets
            var instream = assetManager?.open("tessdata/" + language + ".traineddata")
            var outstream = FileOutputStream(filepath)
            val buffer = ByteArray(1024)
            var read: Int

            while (instream!!.read(buffer).also { read = it } != -1) {
                outstream.write(buffer, 0, read)
            }
            outstream.flush()
            outstream.close()
            instream.close()

        }catch (e: FileNotFoundException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    private fun rotate(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap {
        val matrix = Matrix()
        matrix.preScale(if (horizontal) (-1f) else 1f, if (vertical) (-1f) else 1f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true);
    }

    inner class MessageHandler: Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                ConstantDefine.RESULT_OCR->{
                    tv_ocr_result.text = msg.obj.toString()
                }
            }
        }
    }

    fun ARGBBitmap(img: Bitmap): Bitmap? {
        return img.copy(Bitmap.Config.ARGB_8888, true)
    }
}