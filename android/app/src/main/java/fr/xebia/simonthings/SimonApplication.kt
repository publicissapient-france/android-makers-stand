package fr.xebia.simonthings

import android.app.Application
import fr.xebia.simonthings.persistance.FirebasePersister
import java.lang.Exception

class SimonApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            FirebasePersister.init(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}