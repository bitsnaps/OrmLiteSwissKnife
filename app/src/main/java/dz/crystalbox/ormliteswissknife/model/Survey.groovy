package dz.crystalbox.ormliteswissknife.model

import com.arasthel.swissknife.annotations.Parcelable
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import groovy.transform.AutoClone
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import groovy.transform.builder.Builder

/**
 * Created by Master on 07/11/2017.
 */

@Canonical
@ToString(includePackage = false)
//@DatabaseTable(tableName = "survey")
@Builder
@Parcelable
@CompileStatic
class Survey {

    @DatabaseField(generatedId = true)
    long id

    @DatabaseField//(columnName = "name")
    String name

    @DatabaseField
    Date dateCreation

    def clone(Survey survey) {
        this.name = survey.name
        this.dateCreation = survey.dateCreation
        this
    }

    String toString(){
        "${name} ${dateCreation.format('dd/MM/yyyy')}"
    }

}
