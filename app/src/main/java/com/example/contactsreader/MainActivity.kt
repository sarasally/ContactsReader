package com.example.contactsreader


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.content.ContentResolver
import android.os.Build
import android.widget.*
import androidx.annotation.RequiresApi
class MainActivity:AppCompatActivity() {

     val cols= listOf<String>(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
         ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone._ID)
         .toTypedArray()


    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)
            !=PackageManager.PERMISSION_GRANTED)
        {
           ActivityCompat.requestPermissions(this,Array(1){android.Manifest.permission.READ_CONTACTS},12)

        }
        else{
            readcontact()

        }
}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
       if(requestCode==12&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        readcontact()
    }

    private fun readcontact(){
        var rs= contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,cols,null,null,cols[0])
        var from=listOf<String>(cols[1],cols[0]).toTypedArray()
        var to= intArrayOf(android.R.id.text2,android.R.id.text1)

        var adapter=SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,rs,from,to,0)
        val contractLV:ListView=findViewById(R.id.listViewContacts)
        contractLV.adapter=adapter


        val nasa: SearchView = findViewById(R.id.searchViewContacts)
       nasa.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
           override fun onQueryTextSubmit(query: String?): Boolean {
               return false
           }

           override fun onQueryTextChange(newText: String?): Boolean {
               var rs= contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,cols,"${cols[0]} LIKE ?",
                   Array(1){"%$newText%"},cols[0])
                    adapter.changeCursor(rs)
               return false
           }
       })}}



