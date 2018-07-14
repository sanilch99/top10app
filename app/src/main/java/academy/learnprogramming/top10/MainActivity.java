package academy.learnprogramming.top10;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView listApps;
    String feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private int FeedLimit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApps = (ListView) findViewById(R.id.xmlListView);
        downloadURL(String.format(feedURL, FeedLimit));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mnuFA: {
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                break;
            }
            case R.id.mnuPA: {
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                break;
            }
            case R.id.mnuSongs: {
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                break;
            }
            case R.id.mnutt:
            case R.id.mnuttf: {
                if (!item.isChecked()){
                    item.setChecked(true);
                    FeedLimit = 35-FeedLimit;
                }
                break;

            }
            default:
                return super.onOptionsItemSelected(item);

        }
        downloadURL(String.format(feedURL, FeedLimit));
        return true;

    }

    private void downloadURL(String feedURL) {
        Log.d(TAG, "downloadURL: Starting AsyncTask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute(feedURL);
        Log.d(TAG, "downloadURL: done");
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is" + s);
            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(s);
//            ArrayAdapter<FeedEntry> arrayAdapter=new ArrayAdapter<>(
//                    MainActivity.this,R.layout.list_item,parseApplications.getApplications());
//            listApps.setAdapter(arrayAdapter);
            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.list_record, parseApplications.getApplications());
            listApps.setAdapter(feedAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: ErrorDownloading");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                int response = httpURLConnection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was" + response);
                //InputStream inputStream = httpURLConnection.getInputStream();
                //InputStreamReader inputStreamReader = new InputStreamReader(inputStream);buffered reader reads char
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                int charsread;
                char[] inputBuffer = new char[500];
                while (true) {
                    charsread = reader.read(inputBuffer);
                    if (charsread < 0) {
                        break;
                    }
                    if (charsread > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsread));
                    }
                }
                reader.close();
                return xmlResult.toString();
            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: InvalidURL" + e.getMessage() + "\n");
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: Exception in reading data " + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "downloadXML: permissions req " + e.getMessage());
            }
            return null;
        }
    }
}
