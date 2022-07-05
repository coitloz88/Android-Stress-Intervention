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
public final class RoomDAO4_Impl implements RoomDAO4 {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Accdata> __insertionAdapterOfAccdata;

  private final EntityDeletionOrUpdateAdapter<Accdata> __deletionAdapterOfAccdata;

  public RoomDAO4_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAccdata = new EntityInsertionAdapter<Accdata>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Accdata` (`current_time`,`Acc_X_data`,`Acc_Y_data`,`Acc_Z_data`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Accdata value) {
        if (value.getCurrent_time() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getCurrent_time());
        }
        stmt.bindDouble(2, value.getAccXdata());
        stmt.bindDouble(3, value.getAccYdata());
        stmt.bindDouble(4, value.getAccZdata());
      }
    };
    this.__deletionAdapterOfAccdata = new EntityDeletionOrUpdateAdapter<Accdata>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Accdata` WHERE `current_time` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Accdata value) {
        if (value.getCurrent_time() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getCurrent_time());
        }
      }
    };
  }

  @Override
  public void insert(final Accdata accdata) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfAccdata.insert(accdata);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Accdata accdata) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfAccdata.handle(accdata);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Accdata> getAllESMdata() {
    final String _sql = "SELECT * FROM Accdata";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCurrentTime = CursorUtil.getColumnIndexOrThrow(_cursor, "current_time");
      final int _cursorIndexOfAccXdata = CursorUtil.getColumnIndexOrThrow(_cursor, "Acc_X_data");
      final int _cursorIndexOfAccYdata = CursorUtil.getColumnIndexOrThrow(_cursor, "Acc_Y_data");
      final int _cursorIndexOfAccZdata = CursorUtil.getColumnIndexOrThrow(_cursor, "Acc_Z_data");
      final List<Accdata> _result = new ArrayList<Accdata>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Accdata _item;
        final String _tmpCurrent_time;
        if (_cursor.isNull(_cursorIndexOfCurrentTime)) {
          _tmpCurrent_time = null;
        } else {
          _tmpCurrent_time = _cursor.getString(_cursorIndexOfCurrentTime);
        }
        final float _tmpAccXdata;
        _tmpAccXdata = _cursor.getFloat(_cursorIndexOfAccXdata);
        final float _tmpAccYdata;
        _tmpAccYdata = _cursor.getFloat(_cursorIndexOfAccYdata);
        final float _tmpAccZdata;
        _tmpAccZdata = _cursor.getFloat(_cursorIndexOfAccZdata);
        _item = new Accdata(_tmpCurrent_time,_tmpAccXdata,_tmpAccYdata,_tmpAccZdata);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Accdata findByTime(final String currentTime) {
    final String _sql = "SELECT * FROM Accdata WHERE current_time LIKE ?";
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
      final int _cursorIndexOfAccXdata = CursorUtil.getColumnIndexOrThrow(_cursor, "Acc_X_data");
      final int _cursorIndexOfAccYdata = CursorUtil.getColumnIndexOrThrow(_cursor, "Acc_Y_data");
      final int _cursorIndexOfAccZdata = CursorUtil.getColumnIndexOrThrow(_cursor, "Acc_Z_data");
      final Accdata _result;
      if(_cursor.moveToFirst()) {
        final String _tmpCurrent_time;
        if (_cursor.isNull(_cursorIndexOfCurrentTime)) {
          _tmpCurrent_time = null;
        } else {
          _tmpCurrent_time = _cursor.getString(_cursorIndexOfCurrentTime);
        }
        final float _tmpAccXdata;
        _tmpAccXdata = _cursor.getFloat(_cursorIndexOfAccXdata);
        final float _tmpAccYdata;
        _tmpAccYdata = _cursor.getFloat(_cursorIndexOfAccYdata);
        final float _tmpAccZdata;
        _tmpAccZdata = _cursor.getFloat(_cursorIndexOfAccZdata);
        _result = new Accdata(_tmpCurrent_time,_tmpAccXdata,_tmpAccYdata,_tmpAccZdata);
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
