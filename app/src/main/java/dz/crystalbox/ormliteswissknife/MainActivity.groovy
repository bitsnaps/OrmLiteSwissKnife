package dz.crystalbox.ormliteswissknife

import android.content.DialogInterface
import android.content.Intent
import android.os.Parcelable
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.dsl.components.GArrayAdapter
import com.j256.ormlite.dao.Dao
import dz.crystalbox.ormliteswissknife.model.Survey
import groovy.transform.CompileStatic

@CompileStatic
class MainActivity extends AppCompatActivity {

    static final int SURVEY_CREATE = 1
    static final int SURVEY_EDIT = 2

    @InjectView(R.id.list_survey)
    ListView listSurvey

    Dao surveyDao
    DatabaseHelper dbHelper
    GArrayAdapter adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // inject Views
        SwissKnife.inject(this)

        // create a reference for db manipulation
        dbHelper = new DatabaseHelper(this)

        // get a reference to the DAO model object
        surveyDao = dbHelper.getSurveyDao()

//        surveyDao.setAutoCommit(true) // deprecated

        // create and save a new model object
//        try {
//            surveyDao.create(Survey.builder().name("Product Quality").dateCreation(new Date()).build())
//        } catch (SQLException e){
//            log e.message
//        }

        // create a simple ArrayAdapter
//        def adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, surveys.toArray())
//        listSurvey.setAdapter(adapter)

        // create a customized ListView using GArrayAdapter

         listSurvey.onItem(R.layout.item_list_row, surveyDao.queryForAll()){ def survey, View view, int position ->
            TextView txtSurvey = view.text(R.id.txtSurvey){
                it.text = survey.toString()
            } as TextView
            view.button(R.id.btnDelete){
                it.onClick {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Delete?")
                            .setMessage("Are you sure to delete?")
                            .setCancelable(true)
                            .setPositiveButton('Yes', { DialogInterface dialog, int which ->
                                surveyDelete(survey)
                    } as DialogInterface.OnClickListener)
                            .setNegativeButton('No', null)
                            .show()
                }
            }
            // owner == _onCreate_closure1
            // this == MainActivity
            txtSurvey.onClick {
                Intent i = new Intent(this, SurveyActivity)
                i.putExtra('survey', survey as Parcelable)
                i.putExtra('item_id', position)
                startActivityForResult(i, SURVEY_EDIT)
            }
        }

        // save a reference to the adapter
        adapter = listSurvey.adapter as GArrayAdapter
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data){
            switch (requestCode){
                case SURVEY_CREATE:
                    surveyCreate( data.getParcelableExtra('survey') )
                    return
                case SURVEY_EDIT:
                    int item_id = data.getIntExtra("item_id", -1)
                    if (item_id > -1) {
                        surveyUpdate(data.getParcelableExtra('survey'), item_id)
                    }
                    return
            }
        }
    }

    def surveyCreate(def survey){
        if (surveyDao.create(survey))
            adapter.with {
                add(survey)
                notifyDataSetChanged()
            }
    }

    def surveyUpdate(def newSurvey, int position){
        Survey survey = listSurvey.adapter.getItem(position) as Survey
        survey.clone(newSurvey as Survey)
        surveyDao.update(survey)
        adapter.notifyDataSetChanged()
    }

    def surveyDelete(def survey){
        if (surveyDao.delete(survey))
            (listSurvey.adapter as GArrayAdapter).remove(survey)
    }

    @Override
    boolean onCreateOptionsMenu(Menu menu) {
        menuInflater.inflate(R.menu.main, menu)
        true
    }

    @Override
    boolean onOptionsItemSelected(MenuItem item) {
        switch (item.itemId){
            case R.id.menu_create:
                startActivityForResult(intent(SurveyActivity), SURVEY_CREATE)
                return
        }
        super.onOptionsItemSelected(item)
    }
}
