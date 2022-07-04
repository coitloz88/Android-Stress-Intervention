package com.garmin.android.apps.connectiq.sample.comm.roomdb;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
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
public final class RoomDAO_Impl implements RoomDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Data> __insertionAdapterOfHRVdata;

  private final EntityDeletionOrUpdateAdapter<Data> __deletionAdapterOfHRVdata;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RoomDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHRVdata = new EntityInsertionAdapter<Data>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `HRVdata` (`current_time`,`HRV_data`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Data value) {
        if (value.getCurrent_time() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getCurrent_time());
        }
        stmt.bindDouble(2, value.getHRVdata());
      }
    };
    this.__deletionAdapterOfHRVdata = new EntityDeletionOrUpdateAdapter<Data>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `HRVdata` WHERE `current_time` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Data value) {
        if (value.getCurrent_time() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getCurrent_time());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM HRVdata";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Data hrvdata) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfHRVdata.insert(hrvdata);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Data hrvdata) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfHRVdata.handle(hrvdata);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<Data> getAllHRVdata() {
    final String _sql = "SELECT * FROM HRVdata";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCurrentTime = CursorUtil.getColumnIndexOrThrow(_cursor, "current_time");
      final int _cursorIndexOfHRVdata = CursorUtil.getColumnIndexOrThrow(_cursor, "HRV_data");
      final List<Data> _result = new ArrayList<Data>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Data _item;
        final String _tmpCurrent_time;
        if (_cursor.isNull(_cursorIndexOfCurrentTime)) {
          _tmpCurrent_time = null;
        } else {
          _tmpCurrent_time = _cursor.getString(_cursorIndexOfCurrentTime);
        }
        final double _tmpHRVdata;
        _tmpHRVdata = _cursor.getDouble(_cursorIndexOfHRVdata);
        _item = new Data(_tmpCurrent_time,_tmpHRVdata);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Data findByTime(final String currentTime) {
    final String _sql = "SELECT * FROM HRVdata WHERE current_time LIKE ?";
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
      final int _cursorIndexOfHRVdata = CursorUtil.getColumnIndexOrThrow(_cursor, "HRV_data");
      final Data _result;
      if(_cursor.moveToFirst()) {
        final String _tmpCurrent_time;
        if (_cursor.isNull(_cursorIndexOfCurrentTime)) {
          _tmpCurrent_time = null;
        } else {
          _tmpCurrent_time = _cursor.getString(_cursorIndexOfCurrentTime);
        }
        final double _tmpHRVdata;
        _tmpHRVdata = _cursor.getDouble(_cursorIndexOfHRVdata);
        _result = new Data(_tmpCurrent_time,_tmpHRVdata);
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
