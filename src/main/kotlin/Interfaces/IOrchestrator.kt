package Interfaces

import java.io.File

interface IOrchestrator {

    fun parse(inputFile: File): File

}