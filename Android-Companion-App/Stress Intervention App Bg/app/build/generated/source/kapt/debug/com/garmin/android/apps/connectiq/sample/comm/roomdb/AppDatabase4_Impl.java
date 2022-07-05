package com.garmin.android.apps.connectiq.sample.comm.roomdb;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase4_Impl extends AppDatabase4 {
  private volatile RoomDAO4 _roomDAO4;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Accdata` (`current_time` TEXT NOT NULL, `Acc_X_data` REAL NOT NULL, `Acc_Y_data` REAL NOT NULL, `Acc_Z_data` REAL NOT NULL, PRIMARY KEY(`current_time`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'bc7610feac92e04d89c14d880ace5de9')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Accdata`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsAccdata = new HashMap<String, TableInfo.Column>(4);
        _columnsAccdata.put("current_time", new TableInfo.Column("current_time", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccdata.put("Acc_X_data", new TableInfo.Column("Acc_X_data", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccdata.put("Acc_Y_data", new TableInfo.Column("Acc_Y_data", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccdata.put("Acc_Z_data", new TableInfo.Column("Acc_Z_data", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAccdata = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAccdata = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAccdata = new TableInfo("Accdata", _columnsAccdata, _foreignKeysAccdata, _indicesAccdata);
        final TableInfo _existingAccdata = TableInfo.read(_db, "Accdata");
        if (! _infoAccdata.equals(_existingAccdata)) {
          return new RoomOpenHelper.ValidationResult(false, "Accdata(com.garmin.android.apps.connectiq.sample.comm.roomdb.Accdata).\n"
                  + " Expected:\n" + _infoAccdata + "\n"
                  + " Found:\n" + _existingAccdata);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "bc7610feac92e04d89c14d880ace5de9", "86201895e4c7923f04e98acaf8faf9b6");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Accdata");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Accdata`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(RoomDAO4.class, RoomDAO4_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public RoomDAO4 roomDAO() {
    if (_roomDAO4 != null) {
      return _roomDAO4;
    } else {
      synchronized(this) {
        if(_roomDAO4 == null) {
          _roomDAO4 = new RoomDAO4_Impl(this);
        }
        return _roomDAO4;
      }
    }
  }
}
