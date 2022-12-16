package com.carsonmiller.metronome.state

interface Savable {

    /**
     * saves data to hard drive
     */
    fun save()

    /**
     * loads data from hard drive
     */
    fun load()
}