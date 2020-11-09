package com.prac.cabstone.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.prac.cabstone.BaseActivity
import com.prac.cabstone.R
import com.prac.cabstone.models.ResponseGetAreaCode
import com.prac.cabstone.search_result.SearchResultActivity
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        search_btn_location.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                getAreaCode()
            }
        })
    }

    private fun getAreaCode() {
        showProgressDialog()
        val api = GetAreaCodeAPI.create()

        api.getAreaCode().enqueue(object : Callback<ResponseGetAreaCode> {
            override fun onResponse(call: Call<ResponseGetAreaCode>, response: Response<ResponseGetAreaCode>) {
                hideProgressDialog()
                var responseGetAreaCode = response.body()
                if (responseGetAreaCode != null) {
                    showLocationDialog(responseGetAreaCode)
                }
            }

            override fun onFailure(call: Call<ResponseGetAreaCode>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
                t.printStackTrace()
            }
        })
    }

    private fun showLocationDialog(responseGetAreaCode: ResponseGetAreaCode){
        var list : ArrayList<String> = ArrayList()
        for (i in responseGetAreaCode.getData()) {
            list.add(i.getName())
        }

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this)

        // Set a title for alert dialog
        builder.setTitle("지역을 선택해주세요")

        // Set items form alert dialog
        builder.setItems(list.toTypedArray()) { _, which ->
            // Get the dialog selected item
            val selected = list[which]
            val selectedCode = responseGetAreaCode.getData()[which].getCode()

            var intent = Intent(this@SearchActivity, SearchResultActivity::class.java)
            intent.putExtra("area_code", selectedCode)
            startActivity(intent)

            showCustomToast(selected + "선택, " + selectedCode)
        }

        // Create a new AlertDialog using builder object
        val dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }
}