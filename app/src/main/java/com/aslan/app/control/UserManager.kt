package com.aslan.app.control

import android.annotation.SuppressLint
import android.content.Context
import com.aslan.app.model.User
import com.elvishew.xlog.XLog
import com.tencent.mmkv.MMKV

/**
 * 用户控制类
 *
 * @author chenhengfei(Aslanchen)
 * @date 4/26/2021
 */
class UserManager private constructor(val context: Context) {
    companion object {
        const val TAG_USER = "user"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: UserManager? = null

        @Synchronized
        fun Instance(context: Context): UserManager {
            if (instance == null) {
                synchronized(UserManager::class.java) {
                    if (instance == null) {
                        instance = UserManager(context.applicationContext)
                    }
                }
            }
            return instance!!
        }

        private val mLogger = XLog.tag("UserManager")

        @Volatile
        private var mUser: User? = null

        @Synchronized
        fun getUser(): User {
            if (mUser != null) {
                return mUser!!
            }

            mUser = checkLogin()
            if (mUser == null) {
                mUser = User()
            }
            return mUser!!
        }

        @Synchronized
        fun onLogin(mUser: User) {
            mLogger.d("onLogin() called with: mUser = $mUser")
            Companion.mUser = mUser

            val mmkv = MMKV.defaultMMKV()
            mmkv.encode(TAG_USER, mUser)
        }

        @Synchronized
        fun onUpdate(mUser: User) {
            mLogger.d("onUpdate() called with: mUser = $mUser")
            Companion.mUser = mUser

            val mmkv = MMKV.defaultMMKV()
            mmkv.encode(TAG_USER, mUser)
        }

        @Synchronized
        fun onLogout() {
            mLogger.d("onLogout() called")
            val mmkv = MMKV.defaultMMKV()
            mmkv.removeValueForKey(TAG_USER)

            mUser = null
        }

        @Synchronized
        fun checkLogin(): User? {
            val mmkv = MMKV.defaultMMKV()
            if (mmkv.containsKey(TAG_USER) == false) {
                return null
            }
            return mmkv.decodeParcelable(TAG_USER, User::class.java)
        }
    }
}