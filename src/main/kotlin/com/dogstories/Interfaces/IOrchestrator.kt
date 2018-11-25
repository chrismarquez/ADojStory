package com.dogstories.Interfaces

import java.io.File

interface IOrchestrator {

    fun parse(inputFile: File): ArrayList<String>

}