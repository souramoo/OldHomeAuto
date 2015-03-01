package com.moosd.housecontrol;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;


public class ControllerList extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> adapter=new MyAdapter(getApplicationContext(),R.layout.listview,R.id.textView1,getResources().getStringArray(R.array.Strings));
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        xec("nightlight", "get", new Handler() {
            @Override
            public void handle(final String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MyAdapter)ControllerList.this.getListAdapter()).buts[0].setChecked((result.startsWith("0")? false: true));
                    }
                });
            }
        });
        xec("daylight", "get", new Handler() {
            @Override
            public void handle(final String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MyAdapter)ControllerList.this.getListAdapter()).buts[1].setChecked((result.startsWith("0")? false: true));
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.controller_list, menu);
        return true;
    }

    private class MyAdapter extends ArrayAdapter<String> {
        ToggleButton[] buts;

        public MyAdapter(Context context, int resource, int textViewResourceId,
                         String[] objects) {
            super(context, resource, textViewResourceId, objects);
            buts = new ToggleButton[objects.length];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String text = getItem(position);
            if (null == convertView) {
                convertView = ControllerList.this.getLayoutInflater().inflate(R.layout.listview, null);
            }
            ToggleButton btn = (ToggleButton) convertView.findViewById(R.id.toggleButton1);
            btn.setOnCheckedChangeListener(new OnItemClickListener(position));
            buts[position] = btn;
            TextView tv = (TextView) convertView.findViewById(R.id.textView1);
            tv.setText(text);
            return convertView;
        }
    }

    class OnItemClickListener implements CompoundButton.OnCheckedChangeListener {
        private int mPosition;
        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(mPosition == 0) {
                // Star lights
                xec("nightlight", (b?"on":"off"), null);
            }
            if(mPosition == 1) {
                // Room lights
                xec("daylight", (b?"on":"off"), null);
            }
            Log.v("TEST", "onItemClick at position" + mPosition+" - "+b);
        }
    }

    public static void xec(final String module, final String setting, final Handler h) {
        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                BufferedReader in = null;
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();
                    request.setURI(new URI("http://192.168.2.224:8090/"+module+setting));
                    HttpResponse response = client.execute(request);
                    in = new BufferedReader
                            (new InputStreamReader(response.getEntity().getContent()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    String NL = System.getProperty("line.separator");
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    in.close();
                    String page = sb.toString();
                    System.out.println(page);
                    if(h != null) h.handle(page);
                } catch (Exception ee) {
                    ee.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }
        }.execute((Void)null);
    }

    public abstract class Handler {
        public abstract void handle(String result);
    }
}
