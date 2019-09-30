package com.erolc.expermissionlib.permission

import android.Manifest.permission.READ_CALENDAR
import android.Manifest.permission.WRITE_CALENDAR
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_CONTACTS
import android.Manifest.permission.WRITE_CONTACTS
import android.Manifest.permission.GET_ACCOUNTS
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.READ_PHONE_STATE
import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.READ_CALL_LOG
import android.Manifest.permission.WRITE_CALL_LOG
import android.Manifest.permission.ADD_VOICEMAIL
import android.Manifest.permission.USE_SIP
import android.Manifest.permission.PROCESS_OUTGOING_CALLS
import android.Manifest.permission.BODY_SENSORS
import android.Manifest.permission.SEND_SMS
import android.Manifest.permission.RECEIVE_SMS
import android.Manifest.permission.READ_SMS
import android.Manifest.permission.RECEIVE_WAP_PUSH
import android.Manifest.permission.RECEIVE_MMS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.res.Resources
import com.erolc.expermissionlib.R

/**
 *
 *  权限组           权限
 *  CALENDAR
 *
 *                  READ_CALENDAR
 *                  WRITE_CALENDAR
 *
 *  CAMERA
 *
 *                  CAMERA
 *
 *  CONTACTS
 *
 *                  READ_CONTACTS
 *                  WRITE_CONTACTS
 *                  GET_ACCOUNTS
 *
 *  LOCATION
 *
 *                  ACCESS_FINE_LOCATION
 *                  ACCESS_COARSE_LOCATION
 *
 *  MICROPHONE
 *
 *                  RECORD_AUDIO
 *
 *  PHONE
 *
 *                  READ_PHONE_STATE
 *                  CALL_PHONE
 *                  READ_CALL_LOG
 *                  WRITE_CALL_LOG
 *                  ADD_VOICEMAIL
 *                  USE_SIP
 *                  PROCESS_OUTGOING_CALLS
 *
 *  SENSORS
 *
 *                  BODY_SENSORS
 *
 *  SMS
 *
 *                  SEND_SMS
 *                  RECEIVE_SMS
 *                  READ_SMS
 *                  RECEIVE_WAP_PUSH
 *                  RECEIVE_MMS
 *
 *  STORAGE
 *
 *                  READ_EXTERNAL_STORAGE
 *                  WRITE_EXTERNAL_STORAGE
 */
internal object PermissionTable {

    fun getPermissionName(resources: Resources, permission: String): String? {
        val table = mapOf(
            READ_CALENDAR to resources.getString(R.string.READ_CALENDAR),
            WRITE_CALENDAR to resources.getString(R.string.WRITE_CALENDAR),
            CAMERA to resources.getString(R.string.CAMERA),
            READ_CONTACTS to resources.getString(R.string.READ_CONTACTS),
            WRITE_CONTACTS to resources.getString(R.string.WRITE_CONTACTS),
            GET_ACCOUNTS to resources.getString(R.string.GET_ACCOUNTS),
            ACCESS_FINE_LOCATION to resources.getString(R.string.ACCESS_FINE_LOCATION),
            ACCESS_COARSE_LOCATION to resources.getString(R.string.ACCESS_COARSE_LOCATION),
            RECORD_AUDIO to resources.getString(R.string.RECORD_AUDIO),
            READ_PHONE_STATE to resources.getString(R.string.READ_PHONE_STATE),
            CALL_PHONE to resources.getString(R.string.CALL_PHONE),
            READ_CALL_LOG to resources.getString(R.string.READ_CALL_LOG),
            WRITE_CALL_LOG to resources.getString(R.string.WRITE_CALL_LOG),
            ADD_VOICEMAIL to resources.getString(R.string.ADD_VOICEMAIL),
            USE_SIP to resources.getString(R.string.USE_SIP),
            PROCESS_OUTGOING_CALLS to resources.getString(R.string.PROCESS_OUTGOING_CALLS),
            BODY_SENSORS to resources.getString(R.string.BODY_SENSORS),
            SEND_SMS to resources.getString(R.string.SEND_SMS),
            RECEIVE_SMS to resources.getString(R.string.RECEIVE_SMS),
            READ_SMS to resources.getString(R.string.READ_SMS),
            RECEIVE_WAP_PUSH to resources.getString(R.string.RECEIVE_WAP_PUSH),
            READ_EXTERNAL_STORAGE to resources.getString(R.string.READ_EXTERNAL_STORAGE),
            WRITE_EXTERNAL_STORAGE to resources.getString(R.string.WRITE_EXTERNAL_STORAGE)
        )
        return table[permission]
    }
}

