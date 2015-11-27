package com.amaker.mdm.cp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.amaker.mdm.cp.Employees.Employee;
/**
 * 
 * @author ����־
 * ���ݿ⹤����
 */
public class DBHelper extends SQLiteOpenHelper{
    // ���ݿ����Ƴ���
    private static final String DATABASE_NAME = "MDM.db";
    // ���ݿ�汾����
    private static final int DATABASE_VERSION = 1;
    // �����Ƴ���
    public static final String EMPLOYEES_TABLE_NAME = "employee";
	// ���췽��
	public DBHelper(Context context) {
		// �������ݿ�
		super(context, DATABASE_NAME,null, DATABASE_VERSION);
	}

	// ����ʱ����
	public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EMPLOYEES_TABLE_NAME + " ("
                + Employee._ID + " INTEGER PRIMARY KEY,"
                + Employee.NAME + " TEXT,"
                + Employee.CONTENT + " TEXT"
                + ");");
	}

	// �汾����ʱ����
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// ɾ����
		db.execSQL("DROP TABLE IF EXISTS employee");
        onCreate(db);
	}

}
