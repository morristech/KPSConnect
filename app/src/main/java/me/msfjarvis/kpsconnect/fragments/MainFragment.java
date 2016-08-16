package me.msfjarvis.kpsconnect;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainFragment extends Fragment {
    public String link = "http://khaitanpublicschool.com/blog/feed";
    public String TAG = "PostsParser";
    public Map posts = new HashMap();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, parent, false);
    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        new PullInPosts().execute();

    }

    private class PullInPosts extends AsyncTask<Void, Void, String> {
        @Override
        public String doInBackground(Void... Void) {
            try {
                InputStream inputStream = new URL(link).openConnection().getInputStream();
                Feed feed = EarlParser.parseOrThrow(inputStream, 0);
                Log.i(TAG, "Processing feed: " + feed.getTitle());
                for (Item item : feed.getItems()) {
                    String title = item.getTitle();
                    String link = item.getLink();
                    posts.put(title,link);

                    Log.i(TAG, "Item title: " + (title == null ? "N/A" : title));
                }
            } catch (MalformedURLException exc) {
                exc.printStackTrace();
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (java.util.zip.DataFormatException e) {
                e.printStackTrace();
            }
            return "void";
        }
        @Override
        protected void onPostExecute(String result)
        {
            Set set = posts.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry m = (Map.Entry) iterator.next();
                String key = (String) m.getKey();
                Log.i("POST","title:"+key+" title:"+posts.get(key));

            }


            }

        }
    }

