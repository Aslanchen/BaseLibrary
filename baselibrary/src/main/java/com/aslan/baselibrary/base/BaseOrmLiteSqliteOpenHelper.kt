package com.aslan.baselibrary.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库操作类
 *
 * @author Aslanchen
 * @date 2017/4/27
 */

public abstract class BaseOrmLiteSqliteOpenHelper extends OrmLiteSqliteOpenHelper {

  private Map<String, Dao> daos = new HashMap<String, Dao>();

  public BaseOrmLiteSqliteOpenHelper(Context context, String databaseName,
      SQLiteDatabase.CursorFactory factory, int databaseVersion) {
    super(context, databaseName, factory, databaseVersion);
  }

  public BaseOrmLiteSqliteOpenHelper(Context context, String databaseName,
      SQLiteDatabase.CursorFactory factory, int databaseVersion, int configFileId) {
    super(context, databaseName, factory, databaseVersion, configFileId);
  }

  public BaseOrmLiteSqliteOpenHelper(Context context, String databaseName,
      SQLiteDatabase.CursorFactory factory, int databaseVersion, File configFile) {
    super(context, databaseName, factory, databaseVersion, configFile);
  }

  public BaseOrmLiteSqliteOpenHelper(Context context, String databaseName,
      SQLiteDatabase.CursorFactory factory, int databaseVersion, InputStream stream) {
    super(context, databaseName, factory, databaseVersion, stream);
  }

  /**
   * 释放资源
   */
  @Override
  public void close() {
    super.close();

    for (String key : daos.keySet()) {
      Dao dao = daos.get(key);
      dao = null;
    }
  }

  /**
   * 获取对象
   */
  public synchronized <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
    Dao dao = null;
    String className = clazz.getSimpleName();

    if (daos.containsKey(className)) {
      dao = daos.get(className);
    }
    if (dao == null) {
      dao = super.getDao(clazz);
      daos.put(className, dao);
    }
    return (D) dao;
  }
}
