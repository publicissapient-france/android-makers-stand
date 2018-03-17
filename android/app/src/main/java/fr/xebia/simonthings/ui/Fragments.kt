package fr.xebia.simonthings.ui

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView


class WaitingFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = TextView(context)

        view.text = "Press button to start"

        return view
    }

}

class PlayFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = LinearLayout(context)

        view.setBackgroundColor(Color.GREEN)

        return view
    }
}

class NameFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = LinearLayout(context)

        view.setBackgroundColor(Color.BLUE)

        return view
    }
}