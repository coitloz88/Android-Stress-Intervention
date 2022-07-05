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
public final class RoomDAO2_Impl implements RoomDAO2 {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ESMdata> __insertionAdapterOfESMdata;

  private final EntityDeletionOrUpdateAdapter<ESMdata> __deletionAdapterOfESMdata;

  public RoomDAO2_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfESMdata = new EntityInsertionAdapter<ESMdata>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `ESMdata` (`current_time`,`ESM_data`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ESMdata value) {
        if (value.getCurrent_time() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getCurrent_time());
        }
        if (value.getESMdata() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getESMdata());
        }
      }
    };
    this.__deletionAdapterOfESMdata = new EntityDeletionOrUpdateAdapter<ESMdata>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `ESMdata` WHERE `current_time` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ESMdata value) {
        if (value.getCurrent_time() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getCurrent_time());
        }
      }
    };
  }

  @Override
  public void insert(final ESMdata esmdata) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfESMdata.insert(esmdata);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final ESMdata esmdata) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfESMdata.handle(esmdata);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<ESMdata> getAllESMdata() {
    final String _sql = "SELECT * FROM ESMdata";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCurrentTime = CursorUtil.getColumnIndexOrThrow(_cursor, "current_time");
      final int _cursorIndexOfESMdata = CursorUtil.getColumnIndexOrThrow(_cursor, "ESM_data");
      final List<ESMdata> _result = new ArrayList<ESMdata>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ESMdata _item;
        final String _tmpCurrent_time;
        if (_cursor.isNull(_cursorIndexOfCurrentTime)) {
          _tmpCurrent_time = null;
        } else {
          _tmpCurrent_time = _cursor.getString(_cursorIndexOfCurrentTime);
        }
        final String _tmpESMdata;
        if (_cursor.isNull(_cursorIndexOfESMdata)) {
          _tmpESMdata = null;
        } else {
          _tmpESMdata = _cursor.getString(_cursorIndexOfESMdata);
        }
        _item = new ESMdata(_tmpCurrent_time,_tmpESMdata);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public ESMdata findByTime(final String currentTime) {
    final String _sql = "SELECT * FROM ESMdata WHERE current_time LIKE ?";
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
      final int _cursorIndexOfESMdata = CursorUtil.getColumnIndexOrThrow(_cursor, "ESM_data");
      final ESMdata _result;
      if(_cursor.moveToFirst()) {
        final String _tmpCurrent_time;
        if (_cursor.isNull(_cursorIndexOfCurrentTime)) {
          _tmpCurrent_time = null;
        } else {
          _tmpCurrent_time = _cursor.getString(_cursorIndexOfCurrentTime);
        }
        final String _tmpESMdata;
        if (_cursor.isNull(_cursorIndexOfESMdata)) {
          _tmpESMdata = null;
        } else {
          _tmpESMdata = _cursor.getString(_cursorIndexOfESMdata);
        }
        _result = new ESMdata(_tmpCurrent_time,_tmpESMdata);
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
