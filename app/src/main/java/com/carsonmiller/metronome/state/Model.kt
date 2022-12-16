package com.carsonmiller.metronome.state

import android.app.Activity

abstract class Model(activity: Activity) : Savable {
    protected val storage: Store = Store(activity)
}