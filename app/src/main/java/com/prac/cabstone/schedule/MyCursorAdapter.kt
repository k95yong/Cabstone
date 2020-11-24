package com.prac.cabstone.schedule

import android.content.Context
import android.database.Cursor
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.prac.cabstone.R
import java.io.File


class MyCursorAdapter : CursorAdapter {
    constructor(context: Context?, c: Cursor?) : super(context, c, false) {
    }


    // 레이아웃이 없을때 이걸 가져와서 바인드뷰에 넘김
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View? {
        return LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false)
    }


    override fun bindView(view: View, context: Context?, cursor: Cursor) {
        val imageView = view.findViewById<ImageView>(R.id.photo_image)
        var uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
        Log.e("uri:", uri)

        // 이미지를 비동기로 부드럽게 로딩
        Glide.with(context!!).load(uri).into(imageView)
    }
}
