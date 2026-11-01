package ma7moud3ly.fast.compose.playground.managers

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

/** What the app remembers between visits. On the web it is kept in the browser's local storage. */
class StorageManager(private val settings: Settings = Settings()) {

    /** The last playground, in the form [UrlCodec] writes. Empty on a first visit. */
    var playground: String
        get() = settings.getString(KEY_PLAYGROUND, "")
        set(value) {
            settings[KEY_PLAYGROUND] = value
        }

    private companion object {
        const val KEY_PLAYGROUND = "playground"
    }
}
