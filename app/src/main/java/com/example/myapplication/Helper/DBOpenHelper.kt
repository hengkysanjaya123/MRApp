package com.example.myapplication.Helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.myapplication.Model.User
import com.example.myapplication.Model.UserReferral

class DBOpenHelper(var mContext: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(mContext, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_USER =
            "CREATE TABLE ${TABLE_NAME_USER} (${COL_USER_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${COL_USER_NAME} TEXT, " +
                    "${COL_USER_EMAIL} TEXT, " +
                    "${COL_USER_PASSWORD} TEXT, " +
                    "${COL_USER_PHONE_NUMBER} TEXT," +
                    "${COL_USER_ROLE} TEXT)"
        db?.execSQL(CREATE_TABLE_USER)

        val CREATE_TABLE_USER_REFERRAL = "CREATE TABLE ${TABLE_NAME_USER_REFERRAL} " +
                "(${COL_USERREFERRAL_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${COL_USERREFERRAL_USERID} INTEGER, " +
                "${COL_USERREFERRAL_NAME} TEXT, " +
                "${COL_USERREFERRAL_EMAIL} TEXT, " +
                "${COL_USERREFERRAL_PHONENUMBER} TEXT)"
        db?.execSQL(CREATE_TABLE_USER_REFERRAL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE_USER = "DROP TABLE IF EXISTS ${TABLE_NAME_USER}"
        db?.execSQL(DROP_TABLE_USER)

        val DROP_TABLE_USER_REFERRAL = "DROP TABLE IF EXISTS ${TABLE_NAME_USER_REFERRAL}"
        db?.execSQL(DROP_TABLE_USER_REFERRAL)

        onCreate(db)
    }

    fun login(email: String, password: String, userLogin: User): Boolean {
        try {
            val db = this.readableDatabase

            val cursor = db.rawQuery(
                "SELECT * FROM ${TABLE_NAME_USER} WHERE ${COL_USER_EMAIL} = '${email}' AND ${COL_USER_PASSWORD} = '${password}'",
                null
            )

            var res = cursor.moveToFirst()

            if (res) {
                val u = User(
                    id = cursor.getInt(cursor.getColumnIndex(COL_USER_ID)),
                    name = cursor.getString(cursor.getColumnIndex(COL_USER_NAME)),
                    password = cursor.getString(cursor.getColumnIndex(COL_USER_PASSWORD)),
                    phoneNumber = cursor.getString(cursor.getColumnIndex(COL_USER_PHONE_NUMBER)),
                    email = cursor.getString(cursor.getColumnIndex(COL_USER_EMAIL)),
                    role = cursor.getString(cursor.getColumnIndex(COL_USER_ROLE))
                )
                userLogin.id = u.id
                userLogin.name = u.name
                userLogin.password = u.password
                userLogin.phoneNumber = u.phoneNumber
                userLogin.email = u.email
                userLogin.role = u.role

                return true
            }
            return false
        } catch (ex: Exception) {
            Log.d("DBOpenHelper", "Login function" + ex.toString())
            return false
        }
    }

    fun deleteAllUsers() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM ${TABLE_NAME_USER}")
    }

    fun IsUserExist(email: String): Boolean {
        val db = this.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM ${TABLE_NAME_USER} WHERE " +
                    "${COL_USER_EMAIL} = '${email}'", null
        )

        return cursor.moveToFirst()
    }

    fun addUser(user: User): Boolean {
        try {
            val values = ContentValues()
            values.put(COL_USER_NAME, user.name)
            values.put(COL_USER_EMAIL, user.email)
            values.put(COL_USER_PHONE_NUMBER, user.phoneNumber)
            values.put(COL_USER_PASSWORD, user.password)
            values.put(COL_USER_ROLE, user.role)

            val db = this.writableDatabase
            db.insert(TABLE_NAME_USER, null, values)
            db.close()
            return true
        } catch (ex: Exception) {
            Log.d("Error:DBOpenHelper", "(AddUser Function)" + ex.toString())
            return false
        }
    }

    fun deleteUser(user: User): Boolean {
        try {
            val db = this.writableDatabase
            db.execSQL("DELETE FROM ${TABLE_NAME_USER} WHERE ${COL_USER_ID} = ${user.id}")

            return true
        } catch (ex: Exception) {
            Log.d("Error:DBOpenHelper", "(DeleteUserFunction)" + ex.toString())
            return false
        }
    }

    fun editUser(userOld: User, userNew: User): Boolean {
        try {
            val db = this.writableDatabase
            db.execSQL(
                "UPDATE ${TABLE_NAME_USER} SET ${COL_USER_NAME} = '${userNew.name}'," +
                        "${COL_USER_EMAIL} = '${userNew.email}'," +
                        "${COL_USER_PHONE_NUMBER} = '${userNew.phoneNumber}'," +
                        "${COL_USER_PASSWORD} = '${userNew.password}' " +
                        "WHERE ${COL_USER_ID} = ${userOld.id}"
            )
            return true
        } catch (ex: Exception) {
            Log.d("Error:DBOpenHelper", "(editUser function)" + ex.toString())
            return false
        }
    }

    // only get users with role = 'user'
    fun getUsers(): ArrayList<User> {
        var listUser = arrayListOf<User>()

        try {

            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM ${TABLE_NAME_USER} WHERE ROLE = 'user'", null)

            cursor.moveToFirst()
            listUser.add(
                User(
                    cursor.getInt(cursor.getColumnIndex(COL_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_USER_NAME)),
                    cursor.getString(cursor.getColumnIndex(COL_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(COL_USER_PHONE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(COL_USER_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(COL_USER_ROLE))
                )
            )

            while (cursor.moveToNext()) {
                listUser.add(
                    User(
                        cursor.getInt(cursor.getColumnIndex(COL_USER_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_USER_NAME)),
                        cursor.getString(cursor.getColumnIndex(COL_USER_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(COL_USER_PHONE_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(COL_USER_PASSWORD)),
                        cursor.getString(cursor.getColumnIndex(COL_USER_ROLE))
                    )
                )
            }
            cursor.close()

        } catch (ex: Exception) {
            Log.d("DBOpenHelper:getUser()", ex.toString())
        }

        return listUser
    }

    fun addUserReferral(user: User, name: String, email: String, phoneNumber: String): Boolean {
        try {
            val values = ContentValues()
            values.put(COL_USERREFERRAL_USERID, user.id)
            values.put(COL_USERREFERRAL_NAME, name)
            values.put(COL_USERREFERRAL_EMAIL, email)
            values.put(COL_USERREFERRAL_PHONENUMBER, phoneNumber)

            Log.d("test:DBOpenHelper", "(addUserReferral) " + values.toString())
            val db = this.writableDatabase
            db.insert(TABLE_NAME_USER_REFERRAL, null, values)
            db.close()
            return true
        } catch (ex: Exception) {
            Log.d("test:DBOpenHelper", "(addUserReferral Function)" + ex.toString())
            return false;
        }
    }

    fun getReferralByUser(user: User): ArrayList<UserReferral> {
        val listUserReferral = arrayListOf<UserReferral>()
        val db = this.readableDatabase

        val query = "SELECT ${TABLE_NAME_USER_REFERRAL}.${COL_USERREFERRAL_ID}," +
                "${TABLE_NAME_USER_REFERRAL}.${COL_USERREFERRAL_NAME} as 'RefName' ," +
                "${TABLE_NAME_USER_REFERRAL}.${COL_USERREFERRAL_EMAIL} ," +
                "${TABLE_NAME_USER_REFERRAL}.${COL_USERREFERRAL_PHONENUMBER} ," +
                "${TABLE_NAME_USER_REFERRAL}.${COL_USERREFERRAL_USERID} ," +
                "${TABLE_NAME_USER}.${COL_USER_NAME} " +
                "FROM ${TABLE_NAME_USER_REFERRAL} INNER JOIN ${TABLE_NAME_USER} " +
                "ON ${TABLE_NAME_USER_REFERRAL}.${COL_USERREFERRAL_USERID} = ${TABLE_NAME_USER}.${COL_USER_ID}" +
                " WHERE ${COL_USERREFERRAL_USERID} = ${user.id}"

        val cursor = db.rawQuery(query, null)
//        val cursor =
//            db.rawQuery("SELECT * FROM ${TABLE_NAME_USER_REFERRAL} WHERE ${COL_USERREFERRAL_USERID} = ${user.id}", null)

        if (cursor.moveToFirst()) {
            listUserReferral.add(
                UserReferral(
                    cursor.getInt(cursor.getColumnIndex(COL_USERREFERRAL_ID)),
                    cursor.getString(cursor.getColumnIndex("RefName")),
                    cursor.getString(cursor.getColumnIndex(COL_USERREFERRAL_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(COL_USERREFERRAL_PHONENUMBER)),
                    cursor.getInt(cursor.getColumnIndex(COL_USERREFERRAL_USERID)),
                    cursor.getString(cursor.getColumnIndex(COL_USER_NAME))
                )
            )

            while (cursor.moveToNext()) {
                listUserReferral.add(
                    UserReferral(
                        cursor.getInt(cursor.getColumnIndex(COL_USERREFERRAL_ID)),
                        cursor.getString(cursor.getColumnIndex("RefName")),
                        cursor.getString(cursor.getColumnIndex(COL_USERREFERRAL_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(COL_USERREFERRAL_PHONENUMBER)),
                        cursor.getInt(cursor.getColumnIndex(COL_USERREFERRAL_USERID)),
                        cursor.getString(cursor.getColumnIndex(COL_USER_NAME))
                    )
                )
            }
        }

        return listUserReferral
    }

    fun getAllReferrals(): ArrayList<UserReferral> {
        val listUserReferral = arrayListOf<UserReferral>()
        try {
            val db = this.readableDatabase

            val query = "SELECT ${TABLE_NAME_USER_REFERRAL}.${COL_USERREFERRAL_ID}," +
                    "${TABLE_NAME_USER_REFERRAL}.${COL_USERREFERRAL_NAME} as 'RefName' ," +
                    "${TABLE_NAME_USER_REFERRAL}.${COL_USERREFERRAL_EMAIL} ," +
                    "${TABLE_NAME_USER_REFERRAL}.${COL_USERREFERRAL_PHONENUMBER} ," +
                    "${TABLE_NAME_USER_REFERRAL}.${COL_USERREFERRAL_USERID} ," +
                    "${TABLE_NAME_USER}.${COL_USER_NAME} " +
                    "FROM ${TABLE_NAME_USER_REFERRAL} INNER JOIN ${TABLE_NAME_USER} " +
                    "ON ${TABLE_NAME_USER_REFERRAL}.${COL_USERREFERRAL_USERID} = ${TABLE_NAME_USER}.${COL_USER_ID}"
            val cursor =
                db.rawQuery(
                    query,
                    null
                )

            Log.d("test:DBOpenHelper", "(getAllReferrals)" + query)
            if (cursor.moveToFirst()) {
                Log.d(
                    "test:DBOpenHelper",
                    "(getAllReferrals) " + cursor.getString(cursor.getColumnIndex(COL_USERREFERRAL_NAME))
                )
                listUserReferral.add(
                    UserReferral(
                        cursor.getInt(cursor.getColumnIndex(COL_USERREFERRAL_ID)),
                        cursor.getString(cursor.getColumnIndex("RefName")),
                        cursor.getString(cursor.getColumnIndex(COL_USERREFERRAL_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(COL_USERREFERRAL_PHONENUMBER)),
                        cursor.getInt(cursor.getColumnIndex(COL_USERREFERRAL_USERID)),
                        cursor.getString(cursor.getColumnIndex(COL_USER_NAME))
                    )
                )

                while (cursor.moveToNext()) {
                    Log.d(
                        "test:DBOpenHelper",
                        "(getAllReferrals) " + cursor.getString(cursor.getColumnIndex(COL_USERREFERRAL_NAME))
                    )
                    listUserReferral.add(
                        UserReferral(
                            cursor.getInt(cursor.getColumnIndex(COL_USERREFERRAL_ID)),
                            cursor.getString(cursor.getColumnIndex("RefName")),
                            cursor.getString(cursor.getColumnIndex(COL_USERREFERRAL_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COL_USERREFERRAL_PHONENUMBER)),
                            cursor.getInt(cursor.getColumnIndex(COL_USERREFERRAL_USERID)),
                            cursor.getString(cursor.getColumnIndex(COL_USER_NAME))
                        )
                    )
                }
            }
        } catch (ex: Exception) {
            Log.d("test:DBOpenHelper", "getAllReferrals function " + ex.toString())
        }

        return listUserReferral
    }

    companion object {

        private val DATABASE_VERSION = 4
        private val DATABASE_NAME = "referralldb"

        val TABLE_NAME_USER = "User"
        val TABLE_NAME_USER_REFERRAL = "UserReferral"

        // User table
        val COL_USER_ID = "ID"
        val COL_USER_NAME = "Name"
        val COL_USER_EMAIL = "Email"
        val COL_USER_PASSWORD = "Password"
        val COL_USER_PHONE_NUMBER = "PhoneNumber"
        val COL_USER_ROLE = "Role"

        // UserReferral Table
        val COL_USERREFERRAL_ID = "ID"
        val COL_USERREFERRAL_USERID = "UserID"
        val COL_USERREFERRAL_NAME = "Name"
        val COL_USERREFERRAL_EMAIL = "Email"
        val COL_USERREFERRAL_PHONENUMBER = "PhoneNumber"

    }
}