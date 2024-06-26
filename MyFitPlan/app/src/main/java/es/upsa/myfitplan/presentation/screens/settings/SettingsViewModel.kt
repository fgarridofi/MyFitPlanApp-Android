package es.upsa.myfitplan.presentation.screens.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.upsa.myfitplan.domain.model.settings.WeightUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val weightUnitPreferenceKey = stringPreferencesKey("weightUnit")

    // Flow unidades de peso
    val weightUnitFlow: Flow<WeightUnit> = dataStore.data
        .map { preferences ->
            preferences[weightUnitPreferenceKey]?.let { WeightUnit.valueOf(it) } ?: WeightUnit.Kg
        }

    suspend fun saveWeightUnit(weightUnit: WeightUnit) {
        dataStore.edit { preferences ->
            preferences[weightUnitPreferenceKey] = weightUnit.name
        }
    }
}
