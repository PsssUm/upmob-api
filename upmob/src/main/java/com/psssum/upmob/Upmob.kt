package com.psssum.upmob

import android.content.Context
import android.content.Intent
import android.provider.Settings

class Upmob(context: Context, token : String, apiKey : String, onFailListener: OnFailListener) {
    init {
        Constants.onFailListener = onFailListener
        val intent = Intent(context, UpmobWebviewActivity::class.java)
        val android_id = Settings.Secure.getString(context.getContentResolver(),
            Settings.Secure.ANDROID_ID);
        intent.putExtra(Constants.TOKEN, token)
        intent.putExtra(Constants.DEVICE_ID, android_id)
        intent.putExtra(Constants.API_KEY, apiKey)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        context.startActivity(intent)
    }

}