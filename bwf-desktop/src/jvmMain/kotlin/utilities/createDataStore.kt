package utilities

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.SynchronizedObject
import java.io.File

private lateinit var dataStore: DataStore<Preferences>

@OptIn(InternalCoroutinesApi::class)
private val lock = SynchronizedObject()

/**
 * Gets the singleton DataStore instance, creating it if necessary.
 */
fun getDataStore(): DataStore<Preferences> =
    synchronized(lock) {
        if (::dataStore.isInitialized) {
            dataStore
        } else {
            PreferenceDataStoreFactory.create {
                File("${System.getProperty("user.home")}/$dataStoreFileName")
            }.also { dataStore = it }
        }
    }

internal const val dataStoreFileName = "bwf.preferences_pb"