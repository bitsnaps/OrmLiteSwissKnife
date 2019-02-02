package dz.crystalbox.ormliteswissknife

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import dz.crystalbox.ormliteswissknife.model.Survey
import groovy.transform.CompileStatic

@CompileStatic
class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    static final String DATABASE_NAME = "dynsurvey.db"

    // any time you make changes to your database objects, you may have to increase the database version
    static final int DATABASE_VERSION = 1

    // the DAO object we use to access the SimpleData table
    private Dao surveyDAO = null

    def DatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION)
    }

    @Override
    void onCreate(SQLiteDatabase db, ConnectionSource cnt) {
        // Create Survey table when creating the database
        TableUtils.createTable(cnt, Survey)
    }

    @Override
    void onUpgrade(SQLiteDatabase db, ConnectionSource cnt, int oldVer, int ver) {
        List<String> allSql = []
        allSql.each {
            db.execSQL(it)
        }
    }

    Dao getSurveyDao(){
        if (!surveyDAO)
            surveyDAO = getDao(Survey)
        surveyDAO
    }
}
