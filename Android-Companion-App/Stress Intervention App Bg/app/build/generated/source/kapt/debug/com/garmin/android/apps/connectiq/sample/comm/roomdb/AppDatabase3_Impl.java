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
public final class AppDatabase3_Impl extends AppDatabase3 {
  private volatile RoomDAO3 _roomDAO3;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Locationdata` (`current_time` TEXT NOT NULL, `Latitude_data` REAL NOT NULL, `Longtitude_data` REAL NOT NULL, PRIMARY KEY(`current_time`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'bced1bfcdcd895ce06cbe8a20dc2d4a4')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Locationdata`");
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
        final HashMap<String, TableInfo.Column> _columnsLocationdata = new HashMap<String, TableInfo.Column>(3);
        _columnsLocationdata.put("current_time", new TableInfo.Column("current_time", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationdata.put("Latitude_data", new TableInfo.Column("Latitude_data", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationdata.put("Longtitude_data", new TableInfo.Column("Longtitude_data", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocationdata = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocationdata = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocationdata = new TableInfo("Locationdata", _columnsLocationdata, _foreignKeysLocationdata, _indicesLocationdata);
        final TableInfo _existingLocationdata = TableInfo.read(_db, "Locationdata");
        if (! _infoLocationdata.equals(_existingLocationdata)) {
          return new RoomOpenHelper.ValidationResult(false, "Locationdata(com.garmin.android.apps.connectiq.sample.comm.roomdb.Locationdata).\n"
                  + " Expected:\n" + _infoLocationdata + "\n"
                  + " Found:\n" + _existingLocationdata);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "bced1bfcdcd895ce06cbe8a20dc2d4a4", "ea48bbc8a1e2811e24cb16a5a28869c2");
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
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Locationdata");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Locationdata`");
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
    _typeConvertersMap.put(RoomDAO3.class, RoomDAO3_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public RoomDAO3 roomDAO() {
    if (_roomDAO3 != null) {
      return _roomDAO3;
    } else {
      synchronized(this) {
        if(_roomDAO3 == null) {
          _roomDAO3 = new RoomDAO3_Impl(this);
        }
        return _roomDAO3;
      }
    }
  }
}
