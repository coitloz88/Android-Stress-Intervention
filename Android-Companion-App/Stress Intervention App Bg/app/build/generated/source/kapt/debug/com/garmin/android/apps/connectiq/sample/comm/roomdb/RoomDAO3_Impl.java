package com.garmin.android.apps.connectiq.sample.comm.roomdb;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomDAO3_Impl implements RoomDAO3 {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Locationdata> __insertionAdapterOfLocationdata;

  private final EntityDeletionOrUpdateAdapter<Locationdata> __deletionAdapterOfLocationdata;

  public RoomDAO3_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLocationdata = new EntityInsertionAdapter<Locationdata>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Locationdata` (`current_time`,`Latitude_data`,`Longtitude_data`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Locationdata value) {
        if (value.getCurrent_time() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getCurrent_time());
        }
        stmt.bindDouble(2, value.getLatitudedata());
        stmt.bindDouble(3, value.getLongtitudedata());
      }
    };
    this.__deletionAdapterOfLocationdata = new EntityDeletionOrUpdateAdapter<Locationdata>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Locationdata` WHERE `current_time` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Locationdata value) {
        if (value.getCurrent_time() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getCurrent_time());
        }
      }
    };
  }

  @Override
  public void insert(final Locationdata locationdata) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocationdata.insert(locationdata);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Locationdata locationdata) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfLocationdata.handle(locationdata);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Locationdata> getAllLocationdata() {
    final String _sql = "SELECT * FROM Locationdata";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCurrentTime = CursorUtil.getColumnIndexOrThrow(_cursor, "current_time");
      final int _cursorIndexOfLatitudedata = CursorUtil.getColumnIndexOrThrow(_cursor, "Latitude_data");
      final int _cursorIndexOfLongtitudedata = CursorUtil.getColumnIndexOrThrow(_cursor, "Longtitude_data");
      final List<Locationdata> _result = new ArrayList<Locationdata>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Locationdata _item;
        final String _tmpCurrent_time;
        if (_cursor.isNull(_cursorIndexOfCurrentTime)) {
          _tmpCurrent_time = null;
        } else {
          _tmpCurrent_time = _cursor.getString(_cursorIndexOfCurrentTime);
        }
        final double _tmpLatitudedata;
        _tmpLatitudedata = _cursor.getDouble(_cursorIndexOfLatitudedata);
        final double _tmpLongtitudedata;
        _tmpLongtitudedata = _cursor.getDouble(_cursorIndexOfLongtitudedata);
        _item = new Locationdata(_tmpCurrent_time,_tmpLatitudedata,_tmpLongtitudedata);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Locationdata findByTime(final String currentTime) {
    final String _sql = "SELECT * FROM Locationdata WHERE current_time LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (currentTime == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, currentTime);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCurrentTime = CursorUtil.getColumnIndexOrThrow(_cursor, "current_time");
      final int _cursorIndexOfLatitudedata = CursorUtil.getColumnIndexOrThrow(_cursor, "Latitude_data");
      final int _cursorIndexOfLongtitudedata = CursorUtil.getColumnIndexOrThrow(_cursor, "Longtitude_data");
      final Locationdata _result;
      if(_cursor.moveToFirst()) {
        final String _tmpCurrent_time;
        if (_cursor.isNull(_cursorIndexOfCurrentTime)) {
          _tmpCurrent_time = null;
        } else {
          _tmpCurrent_time = _cursor.getString(_cursorIndexOfCurrentTime);
        }
        final double _tmpLatitudedata;
        _tmpLatitudedata = _cursor.getDouble(_cursorIndexOfLatitudedata);
        final double _tmpLongtitudedata;
        _tmpLongtitudedata = _cursor.getDouble(_cursorIndexOfLongtitudedata);
        _result = new Locationdata(_tmpCurrent_time,_tmpLatitudedata,_tmpLongtitudedata);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
