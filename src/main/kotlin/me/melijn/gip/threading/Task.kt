package me.melijn.gip.threading



class Task(private val runnable: Runnable) : Runnable {

    override fun run() {
        try {
            runnable.run()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}