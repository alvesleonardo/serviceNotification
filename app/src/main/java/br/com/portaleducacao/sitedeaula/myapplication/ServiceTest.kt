package br.com.portaleducacao.sitedeaula.myapplication

import android.app.NotificationChannel
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.widget.Toast

/**
 * Created by bk_leonardo.alves on 10/01/2018.
 */
class ServiceTest : Service() {
    lateinit var mServiceLooper: Looper
    lateinit var mServiceHandler: ServiceHandler

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMainNotificationId(): String {
        return NotificationConstants.SERVICES.CHANNEL_ID
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createMainNotificationChannel() {
        val id = NotificationConstants.SERVICES.CHANNEL_ID
        val name = NotificationConstants.SERVICES.CHANNEL_NAME
        val description = NotificationConstants.SERVICES.CHANNEL_DESCRIPTION
        val importance = android.app.NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(id, name, importance)
        mChannel.description = description
        mChannel.enableLights(true)
        mChannel.lightColor = Color.BLUE
        mChannel.enableVibration(true)
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        mNotificationManager.createNotificationChannel(mChannel)
    }

    fun createNotificationCompatBuilder(context: Context): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createMainNotificationChannel()
            return NotificationCompat.Builder(context, getMainNotificationId())
        } else {
            return NotificationCompat.Builder(context)
        }
    }


    inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                println("Serviço")
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                // Restore interrupt status.
                Thread.currentThread().interrupt()
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        super.onCreate()
        val thread = HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND)
        thread.start()
        mServiceLooper = thread.getLooper();
        mServiceHandler = ServiceHandler(mServiceLooper);
        startForeground(NotificationConstants.SERVICES.UPLOAD_FOREGROUND_SERVICE,
                createNotificationCompatBuilder(this).build())
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        val msg = mServiceHandler.obtainMessage()
        msg.arg1 = startId
        mServiceHandler.sendMessage(msg)
        // If we get killed, after returning from here, restart
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }
}