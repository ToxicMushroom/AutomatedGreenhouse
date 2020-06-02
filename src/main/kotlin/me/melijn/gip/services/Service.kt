package me.melijn.gip.services

import com.google.common.util.concurrent.ThreadFactoryBuilder
import me.melijn.gip.threading.Task
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.*

// Een service is een opdeling in het programma, elke sensor/taak krijgt een eigen service
abstract class Service(
    val name: String,
    private val period: Long, // Herhaalende wachttijd
    private val initialDelay: Long = 0, // Eerste wachttijd
    private val unit: TimeUnit = TimeUnit.SECONDS // Tijdseenheid
) {

    // Thread aanmaken voor de service
    private val threadFactory: ThreadFactory = ThreadFactoryBuilder().setNameFormat("[$name-Service]").build()
    private val scheduledExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(threadFactory)

    // variabele die de sceduler kan besturen
    private lateinit var future: ScheduledFuture<*>
    val logger: Logger = LoggerFactory.getLogger(name)

    // De taak die wordt herhaald in de service en moet worden geimplementeerd
    abstract val service: Task

    open fun start() {
        // scheduler wordt aangemaakt en bestuurbaar object wordt ingesteld
        future = scheduledExecutor.scheduleAtFixedRate(service, initialDelay, period, unit)
    }
}