package com.example.vladislav.ccash.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.example.vladislav.ccash.Frontend.*;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class DBFuncs extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myDB";

    private static final String TABLE_INVESTINFO = "iInfo";
    private static final String TABLE_DEBTORSINFO ="dInfo";
    private static final String TABLE_CONTACTS = "contactsTable";

    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_SUMMARY = "summary";
    private static final String KEY_MYDEBT = "mydebt";

    private static final String KEY_DEBTOR_ID   = "debt_userid";
    private static final String KEY_DEBTOR_NAME = "debt_name";
    private static final String KEY_DEBTOR_UID  = "debt_uid";
    private static final String KEY_DEBTOR_DEBT = "debt_debt";

    private static final String KEY_CONTACT_NAME    = "contact_name";
    private static final String KEY_CONTACT_UID     = "contact_uid";


    private static final String CREATE_DBINVESTINFO = "create table " + TABLE_INVESTINFO + "(" + KEY_ID +
            " integer primary key," + KEY_NAME + " text," + KEY_DESCRIPTION +
            " text," + KEY_SUMMARY + " real," + KEY_MYDEBT + " real" + ")";

    private static final String CREATE_DBDEBTORSINFO = "create table " + TABLE_DEBTORSINFO + "(" + KEY_DEBTOR_UID +
            " integer," + KEY_DEBTOR_ID + " integer," + KEY_DEBTOR_NAME +
            " text," + KEY_DEBTOR_DEBT + " real" + ")";

    private static final String CREATE_DBCONTACTS = "create table " + TABLE_CONTACTS + "(" + KEY_CONTACT_NAME +
            " text," + KEY_CONTACT_UID + " text" + ")";

    public DBFuncs(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_DBINVESTINFO);
        db.execSQL(CREATE_DBDEBTORSINFO);
        db.execSQL(CREATE_DBCONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVESTINFO);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEBTORSINFO);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long DBinsertInvestItem(InvestItem investItem)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, investItem.getInvestName());
        values.put(KEY_DESCRIPTION, investItem.getInvestDescription());
        values.put(KEY_SUMMARY, investItem.getInvestSum());
        values.put(KEY_MYDEBT, investItem.getInvestMyDebts());

        long return_id = db.insert(TABLE_INVESTINFO, null, values);

        DBinsertDebtorsList(investItem.getDebts(), return_id);

        return return_id;
    }

    public ArrayList<InvestItem> DBgetAllInvestItems()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sqlQuery = "SELECT  * FROM " + TABLE_INVESTINFO;

        Cursor c = sqLiteDatabase.rawQuery(sqlQuery, null);

        ArrayList<InvestItem> investItems = new ArrayList<>();

        if(c != null && c.moveToFirst())
        {
            do
            {
                InvestItem newInvestItem = new InvestItem();
                newInvestItem.setInvestMyDebts(c.getDouble(c.getColumnIndex(KEY_MYDEBT)));
                newInvestItem.setInvestDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                newInvestItem.setInvestName(c.getString(c.getColumnIndex(KEY_NAME)));
                newInvestItem.setInvestSum(c.getDouble(c.getColumnIndex(KEY_SUMMARY)));
                newInvestItem.setDb_id(c.getInt(c.getColumnIndex(KEY_ID)));
                newInvestItem.setDebts(DBgetDebtorsListByInvestId(newInvestItem.getDb_id()));

                investItems.add(newInvestItem);
            }
            while (c.moveToNext());
        }
        c.close();

        return investItems;
    }

    public int getFreeID()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlQuery = "SELECT  * FROM " + TABLE_INVESTINFO + " WHERE " + KEY_ID;

        Cursor c = db.rawQuery(sqlQuery, null);


        int maxid = -1;
        if (c != null && c.moveToFirst())
        {
            do
            {
                int cur_id = c.getInt(c.getColumnIndex(KEY_DEBTOR_ID));
                if (maxid < cur_id)
                {
                    maxid = cur_id;
                }

            }
            while (c.moveToNext());
        }
        else return 0;
        c.close();

        return maxid + 1;
    }


    private InvestItem DBgetInvestItem(long invest_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery =  "SELECT  * FROM " + TABLE_INVESTINFO + " WHERE " + KEY_ID +
                " = " + invest_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null && c.moveToFirst())
        {
            InvestItem investItem =  new InvestItem();

            investItem.setDb_id(c.getInt(c.getColumnIndex(KEY_ID)));
            investItem.setInvestName(c.getString(c.getColumnIndex(KEY_NAME)));
            investItem.setInvestDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
            investItem.setInvestMyDebts(c.getDouble(c.getColumnIndex(KEY_MYDEBT)));
            investItem.setDebts(DBgetDebtorsListByInvestId(invest_id));

            c.close();

            return investItem;
        }
            c.close();
            return null;
    }

    private ArrayList<Debtor> DBgetDebtorsListByInvestId(long invest_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DEBTORSINFO + " WHERE " + KEY_DEBTOR_ID + " = " + invest_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst())
        {
            ArrayList<Debtor> newDebtorList = new ArrayList<>();
            newDebtorList.add(new Debtor(c.getString(c.getColumnIndex(KEY_DEBTOR_NAME)),
                                         c.getString(c.getColumnIndex(KEY_DEBTOR_UID)),
                                         c.getDouble(c.getColumnIndex(KEY_DEBTOR_DEBT))));
            c.close();
            return newDebtorList;
        }
        c.close();

        return null;
    }

    private void DBinsertDebtorsList(ArrayList<Debtor> debtors, long invest_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        for(Debtor debtor : debtors)
        {
            ContentValues values = new ContentValues();
            values.put(KEY_DEBTOR_ID, invest_id);
            values.put(KEY_DEBTOR_NAME, debtor.getDebtorName());
            values.put(KEY_DEBTOR_UID, debtor.getDebtorUID());
            values.put(KEY_DEBTOR_DEBT, debtor.getDebtorDebt());
            db.insert(TABLE_DEBTORSINFO, null, values);
        }
    }

    public void DBinsertContacts(Contact contact)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_NAME, contact.getName());
        values.put(KEY_CONTACT_UID, contact.getUID());

        db.insert(TABLE_CONTACTS, null, values);
    }

    public ArrayList<Contact> DBgetAllContacts()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null && c.moveToFirst())
        {

            ArrayList<Contact> contacts = new ArrayList<>();

            do
            {
                contacts.add(new Contact(
                        c.getString(c.getColumnIndex(KEY_CONTACT_NAME)),
                        c.getString(c.getColumnIndex(KEY_CONTACT_UID))
                ));
            }
            while (c.moveToNext());
            c.close();
            return contacts;
        }
        c.close();
        return null;
    }
}