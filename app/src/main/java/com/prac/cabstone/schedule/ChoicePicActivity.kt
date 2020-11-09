package com.prac.cabstone.schedule

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.prac.cabstone.R
import kotlinx.android.synthetic.main.activity_choice_pic.*

class ChoicePicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_pic)

        // 전체 데이터를 가지고 있고 내부적 레퍼런스가 있어서 다음 커서로 움직일 수 있음.
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
        )
        val adapter = MyCursorAdapter(this, cursor)
        photo_list.adapter = adapter
        photo_list.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                val cursor = parent.adapter.getItem(position) as Cursor
                val path =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                Toast.makeText(this@ChoicePicActivity, "사진경로 : $path", Toast.LENGTH_SHORT).show()

                val intent = Intent()
                intent.putExtra("img_path", path)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        settingPermission()
    }
    fun settingPermission() {
        var permis = object : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                Toast.makeText(this@ChoicePicActivity, "권한 허가", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@ChoicePicActivity, "권한 거부", Toast.LENGTH_SHORT)
                    .show()
                ActivityCompat.finishAffinity(this@ChoicePicActivity) // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            )
            .check()
    }
}