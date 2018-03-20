package fr.xebia.simonthings

import android.app.Application
import fr.xebia.simonthings.persistance.FirebasePersister

class SimonApplication : Application() {
    override fun onCreate() {
        super.onCreate()

       FirebasePersister.init(this)
    }
}