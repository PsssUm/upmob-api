package com.psssum.upmob

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.DecelerateInterpolator
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_webview_layout.*


class UpmobWebviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setAnimation()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_layout)
        val token = intent.getStringExtra(Constants.TOKEN)
        val device_id = intent.getStringExtra(Constants.DEVICE_ID)
        val api_key = intent.getStringExtra(Constants.API_KEY)
        val uniq_user_id = intent.getStringExtra(Constants.USER_ID)
        //webview.loadUrl("https://app-coins.ru/tasks?device_id=gsagasdag23g2gewag&token_google=eyJhbGciOiJSUzI1NiIsImtpZCI6ImNiNDA0MzgzODQ0YjQ2MzEyNzY5YmI5MjllY2VjNTdkMGFkOGUzYmIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI3OTIzNjc1MDQyMjctOTY1YWU0b2VlMXBmOWxqNWhnMmxmb2RqdTZlZGpnZzUuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI3OTIzNjc1MDQyMjctMjYzNWhtaDZxZjQ0NmZpNGJrcGxscDQ0YjNmMm9waTAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDAwOTkxOTk4Mzg0NTczMTk2OTUiLCJlbWFpbCI6ImJ1bXMzMjMwQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoi0JLQsNGB0LjQu9C40Lkg0J_QtdGC0YDQvtCyIiwicGljdHVyZSI6Imh0dHBzOi8vbGg1Lmdvb2dsZXVzZXJjb250ZW50LmNvbS8tbmJYeHI2THJhNk0vQUFBQUFBQUFBQUkvQUFBQUFBQUFBQUEvQUtGMDVuQjlod2k4NHRNbWgzXzNTM1FPMk1TazNVODJIUS9zOTYtYy9waG90by5qcGciLCJnaXZlbl9uYW1lIjoi0JLQsNGB0LjQu9C40LkiLCJmYW1pbHlfbmFtZSI6ItCf0LXRgtGA0L7QsiIsImxvY2FsZSI6InJ1IiwiaWF0IjoxNTg0MDIxNjEwLCJleHAiOjE1ODQwMjUyMTB9.TRaTRz6NUKyfkNhTs6kWoeGSw3-5uh1Lke6WYT4JNDZnrXN4ylsyV8IOh6zSZQrqTtejdUOImITS19qV3IZ4clmttcb8pV90GSOiOGl86ohvtC3zKDdU_4wJo5xwnv2gywCIEi-SCKyHMzSrP9NMnUIHWVLyWqJKvaUFi5BQWoCAd5BiUc4pGlOfbIaN7vxpZsB4gS6BQj_u0XgDL8bsdvLLVL1fCOaQ-Qky_fxn6q-XiEQoYQEHrATW1WsrblJkQJsjmhzv4mYjv-UB19rdenijPKcK_sGgUxbPIe7RK7T5OwXm1e7VZamBYUuh4YiPdfanel_z8LOgvgrjUUCTxw")
        webview.loadUrl("https://app-coins.ru/tasks?device_id=$device_id&token_google=$token&api_key=$api_key&uniq_user_id=$uniq_user_id")
        val webSettings: WebSettings = webview.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        webview.webViewClient = MyWebViewClient()
        webview.addJavascriptInterface(WebAppInterface(this), "Android")
        webSettings.javaScriptCanOpenWindowsAutomatically = false

    }
    class WebAppInterface internal constructor(c: Activity) {
        var mContext: Activity

        @JavascriptInterface
        fun showToast(toast: String) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        }
        @JavascriptInterface
        fun isAppInstalled(packagename: String): Boolean {
            try {
                mContext.packageManager.getPackageInfo(packagename, 0)
                return true
            } catch (e: Exception) {
                return false
            }
        }
        @JavascriptInterface
        fun openUrl(url : String){

            Log.d("mainActivty","url =" + url   )
            val intent = Intent("android.intent.action.VIEW", Uri.parse(url))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            mContext.startActivity(intent)
        }
        @JavascriptInterface
        fun openApp(packagename: String) {
            try {
                val i = mContext.packageManager!!.getLaunchIntentForPackage(packagename)
                mContext.startActivity(i)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        @JavascriptInterface
        fun copyId(id : String, text : String){
            val clipboard = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("", id)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show()
        }
        @JavascriptInterface
        fun registrationFailed(desc : String){
            if (Constants.onFailListener != null){
                Constants.onFailListener!!.onError(desc)
                mContext.finish()
            }
        }
        @JavascriptInterface
        fun finish(){
            mContext.finish()
        }

        init {
            mContext = c
        }
    }

    override fun onResume() {
        super.onResume()
        webview.loadUrl("javascript:onResume()")
    }
    private class MyWebViewClient : WebViewClient() {
        //HERE IS THE MAIN CHANGE.
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return false
        }
    }
    private fun setAnimation(){

        if (Build.VERSION.SDK_INT > 20) {
            val fadeAnimaton = android.transition.Fade()
            fadeAnimaton.setDuration(200)
            fadeAnimaton.setInterpolator(DecelerateInterpolator())
            getWindow().setExitTransition(fadeAnimaton)
            getWindow().setEnterTransition(fadeAnimaton)
        }
    }
}
