package com.amaker.mdm.cp;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.amaker.mdm.cp.Employees.Employee;

public class EmployeeProvider extends ContentProvider{
	// ���ݿ������
	private DBHelper dbHelper;
    // Uri������
    private static final UriMatcher sUriMatcher;
    // ��ѯ����������
    private static final int EMPLOYEE = 1;
    private static final int EMPLOYEE_ID = 2;
    // ��ѯ�м���
    private static HashMap<String, String> empProjectionMap;
    static {
    	// Uriƥ�乤����
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Employees.AUTHORITY, "employee", EMPLOYEE);
        sUriMatcher.addURI(Employees.AUTHORITY, "employee/#", EMPLOYEE_ID);
        // ʵ������ѯ�м���
        empProjectionMap = new HashMap<String, String>();
        // ��Ӳ�ѯ��
        empProjectionMap.put(Employee._ID, Employee._ID);
        empProjectionMap.put(Employee.NAME, Employee.NAME);
        empProjectionMap.put(Employee.CONTENT, Employee.CONTENT);
    }

	// �����ǵ���
	public boolean onCreate() {
		// ʵ�������ݿ������
		dbHelper = new DBHelper(getContext());
		return true;
	}
	// ��ӷ���
	public Uri insert(Uri uri, ContentValues values) {
		// ������ݿ�ʵ��
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// �������ݣ�������ID
		long rowId = db.insert(DBHelper.EMPLOYEES_TABLE_NAME, null, values);//Employee.NAME
		// �������ɹ�����uri
        if (rowId > 0) {
            Uri empUri = ContentUris.withAppendedId(Employee.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(empUri, null);
            return empUri;
        }
		return null;
	}
	// ɾ������
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// ������ݿ�ʵ��
		SQLiteDatabase db = dbHelper.getWritableDatabase();
	 	// ������ݿ�ʵ��
        int count;
        switch (sUriMatcher.match(uri)) {
        // ����ָ������ɾ��
        case EMPLOYEE:
            count = db.delete(DBHelper.EMPLOYEES_TABLE_NAME, selection, selectionArgs);
            break;
        // ����ָ��������IDɾ��
        case EMPLOYEE_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.delete(DBHelper.EMPLOYEES_TABLE_NAME, Employee._ID + "=" + noteId
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("����� URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	// �������
	public String getType(Uri uri) {
		return null;
	}

	// ��ѯ����
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		 SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	        switch (sUriMatcher.match(uri)) {
	        // ��ѯ����
	        case EMPLOYEE:
	            qb.setTables(DBHelper.EMPLOYEES_TABLE_NAME);
	            qb.setProjectionMap(empProjectionMap);
	            break;
	        // ����ID��ѯ
	        case EMPLOYEE_ID:
	            qb.setTables(DBHelper.EMPLOYEES_TABLE_NAME);
	            qb.setProjectionMap(empProjectionMap);
	            qb.appendWhere(Employee._ID + "=" + uri.getPathSegments().get(1));
	            break;
	        default:
	            throw new IllegalArgumentException("Uri���� " + uri);
	        }

	        // ʹ��Ĭ������
	        String orderBy;
	        if (TextUtils.isEmpty(sortOrder)) {
	            orderBy = Employee.DEFAULT_SORT_ORDER;
	        } else {
	            orderBy = sortOrder;
	        }

	        // ������ݿ�ʵ��
	        SQLiteDatabase db = dbHelper.getReadableDatabase();
	        // �����α꼯��
	        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
	        c.setNotificationUri(getContext().getContentResolver(), uri);
	        return c;
	}

	// ���·���
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
    	// ������ݿ�ʵ��
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        // ����ָ����������
        case EMPLOYEE:
            count = db.update(DBHelper.EMPLOYEES_TABLE_NAME, values, selection, selectionArgs);
            break;
        // ����ָ��������ID����
        case EMPLOYEE_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.update(DBHelper.EMPLOYEES_TABLE_NAME, values, Employee._ID + "=" + noteId
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
            break;
        default:
            throw new IllegalArgumentException("����� URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

}
