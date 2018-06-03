package com.lge.architect.tinytalk.database;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;

import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.StatementBuilder;
import com.lge.architect.tinytalk.database.model.DatabaseModel;
import com.lge.architect.tinytalk.permission.Permissions;

import java.sql.SQLException;

public abstract class CursorLoaderFragment<MODEL extends DatabaseModel, ADAPTER extends CursorRecyclerViewAdapter>
    extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
  protected DatabaseHelper databaseHelper;
  protected ADAPTER adapter;
  protected RecyclerView recyclerView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
    adapter = createAdapter();
  }

  protected abstract ADAPTER createAdapter();

  protected abstract static class ModelCursorLoader<T> extends AbstractCursorLoader {
    DatabaseHelper helper;
    Dao<T, Long> dao;
    String tableName;
    String orderBy;

    public ModelCursorLoader(Context context, DatabaseHelper helper, Dao<T, Long> dao, String tableName, String orderBy) {
      super(context);

      this.helper = helper;
      this.dao = dao;
      this.tableName = tableName;
      this.orderBy = orderBy;
    }

    @Override
    public Cursor getCursor() {
      Cursor cursor = null;

      try {
        QueryBuilder<T, Long> builder = dao.queryBuilder();
        builder.orderBy(orderBy, false);
        PreparedQuery<T> query = builder.prepare();

        cursor = ((AndroidCompiledStatement)
            query.compile(helper.getConnectionSource().getReadWriteConnection(tableName),
                StatementBuilder.StatementType.SELECT)).getCursor();
      } catch (SQLException e) {
        e.printStackTrace();
      }

      if (cursor != null) {
        cursor.getCount();
      }

      return cursor;
    }
  }

  @NonNull
  @Override
  public abstract Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args);

  @Override
  public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
    adapter.changeCursor(data);
  }

  @Override
  public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    adapter.changeCursor(null);
  }

  @Override
  public void onStart() {
    super.onStart();

    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, Permissions.REQUEST_READ_EXTERNAL_STORAGE);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    setHasOptionsMenu(true);

    if (Permissions.checkReadExternalStorage(getContext())) {
      if (savedInstanceState == null) {
        getLoaderManager().initLoader(0, null, this);
      } else {
        getLoaderManager().restartLoader(0, null, this);
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    switch (requestCode) {
      case Permissions.REQUEST_READ_EXTERNAL_STORAGE:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          getLoaderManager().restartLoader(0, null, this);
        }
        break;
    }
  }

}
