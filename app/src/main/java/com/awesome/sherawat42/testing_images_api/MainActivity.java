package com.awesome.sherawat42.testing_images_api;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new test().execute();
    }

    class test extends AsyncTask<Void, Void, Void>{

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Void... params) {
            ClarifaiClient clarifai = new ClarifaiBuilder("JsZUubSQp81Ni1rf9VSlWP__visATvicKk_S9Fp9", "bBf3BcHGtl1nVC2-cnkycsBNlvD7LMuSBZwg0n11")
                    .client(new OkHttpClient()) // OPTIONAL. Allows customization of OkHttp by the user
                    .buildSync();// or use .build() to get a Future<ClarifaiClient>

            // if a Client is registered as a default instance, it will be used
            // automatically, without the user having to keep it around as a field.
            // This can be omitted if you want to manually manage your instance
            //.registerAsDefaultInstance();

            ClarifaiResponse response = clarifai.getDefaultModels().generalModel().predict()
                    .withInputs(
                            ClarifaiInput.forImage(ClarifaiImage.of("https://samples.clarifai.com/metro-north.jpg"))
                    )
                    .executeSync();
            JSONObject responseJSON = null;
            try {
                responseJSON= new JSONObject(response.rawBody());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject output=null;
            JSONArray data=null;
            try {
                output = responseJSON.getJSONArray("outputs").getJSONObject(0);
                data = output.getJSONObject("data").getJSONArray("concepts");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try{
                for(int i=0;i<data.length();i++){
                    JSONObject obj = data.getJSONObject(i);
                    System.out.println(obj.getString("name")+obj.getString("value"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.print("test");
            System.out.print("");
            return null;
        }
    }
}
