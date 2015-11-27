package com.amaker.mdm.cp;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author ����־
 * ͨѶ¼������
 */
public final class Employees {
	// ��Ȩ����
    public static final String AUTHORITY = "com.amaker.mdm.provider.Employees";
    private Employees() {}
    // �ڲ���
    public static final class Employee implements BaseColumns {
    	// ���췽��
        private Employee() {}
        // ����Uri
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/employee");
        // ��������
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.amaker.employees";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.amaker.employees";
        
        // Ĭ��������
        public static final String DEFAULT_SORT_ORDER = "_id desc";// ����������
        // ���ֶγ���
        public static final String NAME = "name";					// ģ����
        public static final String CONTENT = "content";				// ����
    }
}
