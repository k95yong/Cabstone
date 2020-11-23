package com.prac.cabstone.keyword_search_result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.prac.cabstone.GlideApp
import com.prac.cabstone.R
import com.prac.cabstone.models.ResponseGetResultForCurrentData
import com.prac.cabstone.models.ResponseGetResultForKeywordData
import com.prac.cabstone.search_detail.SearchDetailActivity

class KeywordSearchResultBottomSheet(var mContext: Context, var responseGetResultForKeywordData: ResponseGetResultForKeywordData) : BottomSheetDialogFragment() {
    lateinit var mTvTitle : TextView
    lateinit var mTvLocation : TextView
    lateinit var mImageView : ImageView
    lateinit var mBtnDetail : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.bottom_sheet_dialog_search_result, container, false)

        mTvTitle = view.findViewById(R.id.bottom_sheet_search_result_tv_title)
        mTvLocation = view.findViewById(R.id.bottom_sheet_search_result_tv_location)
        mImageView = view.findViewById(R.id.bottom_sheet_search_result_iv_image)
        mBtnDetail = view.findViewById(R.id.bottom_sheet_search_result_btn_detail)

        mTvTitle.text = responseGetResultForKeywordData.getTitle()
        mTvLocation.text = responseGetResultForKeywordData.getAddr1()
        GlideApp.with(mContext).load(responseGetResultForKeywordData.getFirstImage())
            .centerCrop()
            .into(mImageView)

        mBtnDetail.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(mContext, SearchDetailActivity::class.java)
                intent.putExtra("content_id", responseGetResultForKeywordData.getContentId())
                mContext.startActivity(intent)
            }
        })

        // Inflate the layout for this fragment
        return view
    }

    abstract inner class OnSingleClickListener : View.OnClickListener {

        //마지막으로 클릭한 시간
        private var mLastClickTime: Long = 0
        //중복클릭시간차이
        private val MIN_CLICK_INTERVAL: Long = 1000

        abstract fun onSingleClick(v: View)

        override fun onClick(v: View) {
            //현재 클릭한 시간
            val currentClickTime = SystemClock.uptimeMillis()
            //이전에 클릭한 시간과 현재시간의 차이
            val elapsedTime = currentClickTime - mLastClickTime
            //마지막클릭시간 업데이트
            mLastClickTime = currentClickTime

            //내가 정한 중복클릭시간 차이를 안넘었으면 클릭이벤트 발생못하게 return
            if (elapsedTime <= MIN_CLICK_INTERVAL)
                return
            //중복클릭시간 아니면 이벤트 발생
            onSingleClick(v)
        }
    }
}