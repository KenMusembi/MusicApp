package com.nextken.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] items;
    Button btncurrentlyplaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing the listView that we will populate with all mp3 and wav files in the device
        listView = findViewById(R.id.listViewSong);
        //calling runtimePermission that handles permission events
        runtimePermission();

        btncurrentlyplaying = findViewById(R.id.btncurrentlyplaying);

        btncurrentlyplaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //try to redirect to currently playing audio on button click
                getLayoutInflater().inflate(R.layout.activity_player, null);
            }
        });
    }

    //Using Dexter to handle runtime permissions
    public void runtimePermission(){
        //ask for these two permissions at runtime
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        //goes to the displaysongs method if permissions are granted
                        displaySongs();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    //array to parse and hold the song, this array is resizable
    public ArrayList<File> findSong (File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

        if(files!= null){
        for (File singlefile : files) {
            if (singlefile.isDirectory() && !singlefile.isHidden()) {
                arrayList.addAll(findSong(singlefile));
            } else {
                if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".mp4")) {
                    arrayList.add(singlefile);
                }
            }
        }
    }
        return arrayList;
    }

    void displaySongs(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());

        items = new String[mySongs.size()];
        for (int i = 0; i<mySongs.size(); i++){
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
//        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
//        listView.setAdapter(myAdapter);

        customAdapter customAdapter = new customAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songName = (String) listView.getItemAtPosition(position);
                startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                .putExtra("songs", mySongs)
                .putExtra("songname", songName)
                .putExtra("pos", position));
            }
        });


    }

    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View myView = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView textsong = myView.findViewById(R.id.txtsongname);
            ImageView imageView = myView.findViewById(R.id.imgsong);
            textsong.setSelected(true);
            if(items[position].toString().contains(".mp4")){
                imageView.setImageResource(R.drawable.ic_baseline_video_library_24);
                textsong.setText(items[position].replace(".mp4", ""));
                textsong.setTextColor(getResources().getColor(R.color.black_light));
            }else {
                textsong.setText(items[position]);
            }


            return myView;
        }
    }



}