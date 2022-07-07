package shmuly.sternbach.custommishnahlearningprogram.data

import java.time.LocalDate
import java.util.*

data class ProgramUnit(
    val material: String,

    val date: LocalDate,
    val isReview: Boolean,
    val programID: Int,//TODO make this a string of the name of the program
    val group: Int/*if 20th unit in program, will be 20, so that reviews are sorted chronologically*/,
    val positionInGroup: Int,
    var completedStatus: Int,
)
