package tima.project.marriagearrangement_broadcast

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.proposel_form.*

class MainActivity : AppCompatActivity() {

    lateinit var myImg : Bitmap
    lateinit var chooser : AlertDialog
    var name : String? = null
    var fname : String? = null
    var age : String? = null
    var desc : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Add your proposel baliz !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            setForum()
        }
    }

    fun setForum(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.proposel_form, null)

        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        //afficher le dialog
        chooser = mBuilder.create()
        chooser.show()


        chooser.img.setOnClickListener(loadPersonnalPhoto)


        chooser.button.setOnClickListener {
            name = chooser.nametf.text.toString()
            fname = chooser.prentf.text.toString()
            age = chooser.agetf.text.toString()
            desc = chooser.desctf.text.toString()
            showNotification("find perfect partner", name.plus(" ").plus(fname))
            chooser.cancel()
        }

    }
    private val loadPersonnalPhoto : View.OnClickListener = View.OnClickListener {
        var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(i,147)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 147)
        {
            myImg = data?.extras?.get("data") as Bitmap
            chooser.img.setImageBitmap(myImg)
        }
    }
    fun showNotification(title: String, message: String) {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("YOUR_CHANNEL_ID",
                "YOUR_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT)

            channel.description = "YOUR_NOTIFICATION_CHANNEL_DISCRIPTION"
            channel.enableVibration(true)
            channel.enableLights(true)
            channel.lightColor = Color.BLUE
            mNotificationManager.createNotificationChannel(channel)
        }
        val mBuilder = NotificationCompat.Builder(applicationContext, "YOUR_CHANNEL_ID")
            .setContentTitle(title) // title for notification
            .setContentText(message)// message for notification
            .setAutoCancel(true) // clear notification after click
            .setSmallIcon(R.drawable.wedding_ring)
            .setLargeIcon(myImg)



        // Add action button in the notification
        val intent_ok = Intent("OK")
        if(intent_ok.hasExtra("age"))
        intent_ok.removeExtra("age")

        intent_ok.putExtra("img", myImg)
        intent_ok.putExtra("name" ,name.plus(" ").plus(fname))
        intent_ok.putExtra("age", age.plus(" ans"))
        intent_ok.putExtra("desc", desc)

        val pIntent = PendingIntent.getBroadcast(applicationContext, 1, intent_ok, PendingIntent.FLAG_CANCEL_CURRENT )

        mBuilder.addAction(R.drawable.okay, "Interesse", pIntent)

        val intent_not = Intent("NOOK")
        val pIntent3 = PendingIntent.getBroadcast(applicationContext, 1, intent_not, 0)
        mBuilder.addAction(R.drawable.not_okay, "Non Interesse", pIntent3)

        mNotificationManager.notify(0, mBuilder.build())
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
