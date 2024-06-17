package com.aslan.baselibrary.widget

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import java.io.File
import java.io.InputStream
import java.sql.SQLException

/**
 * 数据库操作类
 *
 * @author Aslanchen
 * @date 2017/4/27
 */
abstract class BaseOrmLiteSqliteOpenHelper : OrmLiteSqliteOpenHelper {
    private val daos: MutableMap<String, Dao<*, *>?> = HashMap()

    constructor(
        context: Context?, databaseName: String?,
        factory: SQLiteDatabase.CursorFactory?, databaseVersion: Int
    ) : super(context, databaseName, factory, databaseVersion) {
    }

    constructor(
        context: Context?, databaseName: String?,
        factory: SQLiteDatabase.CursorFactory?, databaseVersion: Int, configFileId: Int
    ) : super(context, databaseName, factory, databaseVersion, configFileId) {
    }

    constructor(
        context: Context?, databaseName: String?,
        factory: SQLiteDatabase.CursorFactory?, databaseVersion: Int, configFile: File?
    ) : super(context, databaseName, factory, databaseVersion, configFile) {
    }

    constructor(
        context: Context?, databaseName: String?,
        factory: SQLiteDatabase.CursorFactory?, databaseVersion: Int, stream: InputStream?
    ) : super(context, databaseName, factory, databaseVersion, stream) {
    }

    /**
     * 释放资源
     */
    override fun close() {
        super.close()
        for (key in daos.keys) {
            var dao = daos[key]
            dao = null
        }
    }

    /**
     * 获取对象
     */
    @Synchronized
    @Throws(SQLException::class)
    override fun <D : Dao<T, *>?, T> getDao(clazz: Class<T>): D {
        var dao: Dao<*, *>? = null
        val className = clazz.simpleName
        if (daos.containsKey(className)) {
            dao = daos[className]
        }
        if (dao == null) {
            dao = super.getDao(clazz)
            daos[className] = dao
        }
        return dao as D
    }
}