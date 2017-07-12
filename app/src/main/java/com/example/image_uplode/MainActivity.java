package com.example.image_uplode;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.support.v7.app.ActionBarActivity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	 String imagepaths="/storage/sdcard/Pictures/academic_contract_notification.png";
	 MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	 String responseString ;
	 File mypath;
	 public String current = null;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mypath = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+current);
        uploadUserPhoto("http://192.168.1.104/pms/androidpms/upload_user_photo");
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    public void uploadUserPhoto(final String url) 
	 {
		 Thread thread = new Thread(new Runnable()
		 {
			    @Override
			    public void run() 
			    {
			        try 
			        {
				    	 BitmapFactory.Options options = new BitmapFactory.Options();
				         options.inSampleSize = 4;
				        
				    	  Bitmap image = null;
				    	
				    		if(imagepaths.equalsIgnoreCase(""))
				    		{
				    			
				    			Toast.makeText(getApplicationContext(), "select Image.....",Toast.LENGTH_SHORT).show();
				    				
				    		}
				    		else
				    		{
				    			Log.e(" path",""+imagepaths);
				    			image = BitmapFactory.decodeFile(imagepaths,options);
				    		}
				    	
				    	if(image != null)
				    	{
					    	FileOutputStream mFileOutStream = new FileOutputStream(mypath);
					    	image.compress(Bitmap.CompressFormat.PNG, 60, mFileOutStream); 
				            mFileOutStream.flush();
				            mFileOutStream.close();
				              //  String url = Images.Media.insertImage(getContentResolver(), image, "title", null);
					    	//Log.e("Image url",""+url);
					    	//File file1 = new File(imagepath);
					    	//File file1 = new File(mypath);
				           // Toast.makeText(getActivity().getApplicationContext(), imagepaths.toString(), Toast.LENGTH_LONG).show();
					    	File f = new File(imagepaths);
					    	// Toast.makeText(getActivity().getApplicationContext(),  f.toString(), Toast.LENGTH_LONG).show();
					    	multipartEntity.addPart("photo", new FileBody(f));
				    	}
				    	
				    	DefaultHttpClient mHttpClient;
				    	HttpParams params = new BasicHttpParams();
				        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
				        mHttpClient = new DefaultHttpClient(params);
				        HttpPost httppost = new HttpPost(url);

				        Log.e("Multipart Entity",""+multipartEntity);
				        httppost.setEntity(multipartEntity);
				        //mHttpClient.execute(httppost, new PhotoUploadResponseHandler());
				        HttpResponse response= mHttpClient.execute(httppost);
				        HttpEntity r_entity = response.getEntity();
				        Log.e("Multipart Entity",""+r_entity.toString());
				       responseString = EntityUtils.toString(r_entity);
				       
				       
				       
				       	SharedPreferences prefs;
					    String prefName = "userpic_sharep"; 
					    prefs = getSharedPreferences(prefName, MODE_PRIVATE);
		        		SharedPreferences.Editor editor = prefs.edit();
		        		editor.putString("userpic_sharep", responseString);
		        
		               editor.commit();
		               
	         
				        Log.e("UPLOAD", responseString);
				        
				      
			        	
			        }
			        catch (Exception e) 
			        {
			        	e.printStackTrace();
				        Log.e("upload file", e.getLocalizedMessage(), e);
			        }
			    }
			});
		 thread.start(); 
		}
}
