package fr.xebia.simonthings.persistance

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.xebia.simonthings.BuildConfig
import fr.xebia.simonthings.engine.Player
import io.reactivex.Single
import java.lang.Exception

object FirebasePersister {
    fun init(context: Context) {
        FirebaseApp.initializeApp(context)
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(BuildConfig.FIREBASE_EMAIL, BuildConfig.FIREBASE_PASSWORD)
    }

    fun persistPlayer(player: Player) {
        FirebaseDatabase.getInstance().getReference("/player").setValue(player)
    }

    fun clearPlayer() {
        FirebaseDatabase.getInstance().getReference("/player").removeValue()
    }

    fun addScore(player: Player) {
        FirebaseDatabase.getInstance().getReference("/score").push().setValue(player)
    }
}