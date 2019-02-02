package dz.crystalbox.ormliteswissknife

import android.content.Intent
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.Extra
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnClick
import dz.crystalbox.ormliteswissknife.model.Survey
import groovy.transform.CompileStatic

@CompileStatic
class SurveyActivity extends AppCompatActivity {

    @InjectView(R.id.edSurveyName)
    EditText edSurveyName
    @InjectView(R.id.edSurveyDate)
    EditText edSurveyDate

    @Extra
    Survey survey

    @Extra
    int item_id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        SwissKnife.inject(this)

        SwissKnife.loadExtras(this)

        if (survey){
            edSurveyName.setText(survey.name)
            edSurveyDate.setText(survey.dateCreation.toString()/*.format('dd/MM/yyyy')*/)
        } else {
            edSurveyName.setText('')
            edSurveyDate.setText('')
        }

    }

    @OnClick(R.id.btnSave)
    def saveSurvey(View v){
        Intent intent = new Intent()
        if (survey){
            survey.name = edSurveyName.text
            survey.dateCreation = Date.parseToStringDate(edSurveyDate.text.toString())
            intent.putExtra('item_id', item_id)
        } else {
            survey = Survey.builder()
                    .name(edSurveyName.text.toString())
                    .dateCreation(Date.parseToStringDate(edSurveyDate.text.toString()))
                    .build()
        }
        intent.putExtra('survey', survey as Parcelable)
        setResult(RESULT_OK, intent)
        finish()
    }

    @OnClick(R.id.btnCancel)
    def cancel(View v){
        finish()
    }
}
