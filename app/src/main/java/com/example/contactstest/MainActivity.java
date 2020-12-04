package com.example.contactstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ListAdapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int GO_READ_CONTACTS = 1;
    private ListView lv_main;
    private ArrayAdapter<String> adapter;
    private  List<String> list = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1.初始化
        lv_main = findViewById(R.id.lv_main);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1
        ,list);
        //2.加载适配器
        lv_main.setAdapter(adapter);
         //3.判断权限
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){//没权限 申请
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},GO_READ_CONTACTS);
        }else{//有权限  直接读
            readContact();
        }


    }
    //
    private void readContact() {//读取的是 表 所以用contentreslover
        Cursor curso = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                , null, null, null);
        if(curso != null){
            while (curso.moveToNext()){
                String name = curso.getString(curso.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = curso.getString(curso.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.d("main", "readContact: " + name  +"  "+ number);
                list.add(name + "\n" + number);

            }
        }
        adapter.notifyDataSetChanged();
        curso.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case GO_READ_CONTACTS:
                if(grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContact();
                }else{
                    Toast.makeText(this,"没权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}