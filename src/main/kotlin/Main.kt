package shmuly.sternbach.custommishnahlearningprogram.randomtests

import shmuly.sternbach.custommishnahlearningprogram.logic.LearningProgramScheduleMakerByEndDate
import java.io.File
import java.time.LocalDate
import java.time.Period
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    test(2_000)
//    start = System.nanoTime()
//    test()
//    File("b.txt").writeText("Time to finish 2: ${System.nanoTime() - start}")
}
fun test(numEntries: Int = 4_192) {
    val start = System.nanoTime()
    //3 hours, 2200
    val material = List(numEntries) { List(it + 1) { it.toString() } }
    val now = LocalDate.now()
    val days = List(numEntries) { now.plusDays(it.toLong()) }
//    val material = MutableList(1000) { (it + 1).toString() }/*Mishnayos().getAllUnits()*/
//    val twoPerDay = LearningProgramScheduleMakerByEndDate().generateProgram(list, 0, LocalDate.now().plusDays(50),)
//    println("Two per day: ${twoPerDay.joinToString("\n", "\n")}")
    val listOfMetadata = Collections.synchronizedList(ArrayList<LearningProgramScheduleMakerByEndDate.ProgramMetadata>(
        numEntries
    ))
    val maker = LearningProgramScheduleMakerByEndDate()
    val interval = Period.of(0, 0, 1)
    val atomicInt = AtomicInteger(0)
    val total = numEntries * numEntries
    val intRange = 0 until numEntries
    val benchmark = Collections.synchronizedList(ArrayList<Triple<Int, Int, Long>>(numEntries))

    for (numUnits in intRange) {
        for (numDaysAfterToday in intRange) {
//            scope.launch(Dispatchers.Default) {
            try {
                val start = System.nanoTime()
                maker.generateProgram(
                    material[numUnits],
                    0,
                    days[numDaysAfterToday],
                    listOfMetadata,
                    now,
                    interval
                )
                benchmark.add(Triple(numUnits, numDaysAfterToday, System.nanoTime() - start))
                println("Finished computation number ${atomicInt.incrementAndGet()}/$total")
            } catch (t: Throwable) {
                println("Failed: generateProgram(material[$numUnits], 0, days[$numDaysAfterToday], listOfMetadata, now, interval)")
                t.printStackTrace()
            }
//            }
        }
    }
    /*if(atomicInt.get() == total)*/
    System.gc()
    Thread.sleep(5_000)
    val text = "Time to finish 1: ${System.nanoTime() - start}"
    println(text)
    File("a.txt").writeText(text)
    val json = StringBuilder("[")
    for (metadata in listOfMetadata) {
        json.append("{")
        json.append("\"numDays\":${metadata.numDays},")
        json.append("\"numUnits\":${metadata.numUnits},")
        json.append("\"freqMap\":{")
        for (entry in metadata.freqMap) {
            json.append("\"${entry.key}\":${entry.value},")
        }
        json.append("}")
        json.append("},")
    }
    json.append("]")
    File("results${numEntries}.txt").writeText(json.toString())
    File("benchmark${numEntries}.csv").writeText(
        benchmark.joinToString(
            "\n",
            "x,y,z\n"
        ) { "${it.first},${it.second},${it.third}" })
    /*val jsonStringer = JSONStringer().array()
       for (metadata in listOfMetadata) {
           jsonStringer
               .`object`()
               .key("numDays")
               .value(metadata.numDays)
               .key("numUnits")
               .value(metadata.numUnits)
               .apply {
                   val jobject = JSONObject()
                   for (entry in metadata.freqMap) {
                       jobject.put(entry.key.toString(), entry.value)
                   }
                   key("freqMap")
                   value(jobject)
               }
               .endObject()
       }
       jsonStringer.endArray()*/

    /*val twoPerDayWithRemainder = maker.generateProgram(
        material,
        0,
        now.plusDays(49),
        metadata
    )
    println("Two per day with remainder: ${twoPerDayWithRemainder.joinToString("\n", "\n")}")
*///    println("Two per day with remainder: $twoPerDayWithRemainder")
/*val strings = mutableListOf<String>()
    for(i in 1..100_000) strings.add(filterBenchmark())
    populateStringBuilder(strings)
    println(stringBuilder)*/
//    println()
}