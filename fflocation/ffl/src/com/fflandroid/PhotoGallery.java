package com.fflandroid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fflandroid.R;
import com.model.Group;
import com.model.KNote;
import com.model.KPhoto;
import com.web_services.ToServer;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This Activity is used to show notes
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 *
 */
public class PhotoGallery extends Activity{
	 
	//Local variables
	private Gallery myGallery;
    private ImageView image;
    private TextView et_comment;
    public static Group selectedGroup;
    private List<KNote> listNotes;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Inflate the activity content
        setContentView(R.layout.gallery);
 
        listNotes = new ArrayList<KNote>();
     
        image= (ImageView)findViewById(R.id.gallery_imageview);
        et_comment = (TextView)findViewById(R.id.gallery_notes);
                

        try {
        	//Read the notes
        	Iterator<KNote> it = selectedGroup.iterator();
        	listNotes = new ArrayList<KNote>();
        	
        	//For each note
        	while(it.hasNext())
        	{
        		//Add the note to the list
        		KNote note = (KNote) it.next();
        		listNotes.add(ToServer.getNote(note.getId()));

        	}
        		
        	//If there is one note or more
        	if(listNotes.size() > 0)
        	{
        		
        		//if the note has photo
        		if(listNotes.get(0).getHasPhoto())
        		{

        			//Fit the first image to the viewer
        			image.setImageDrawable(((KPhoto)listNotes.get(0).getPhoto()).getBitMap());   		
        			et_comment.setText(listNotes.get(0).getNote());
        	
        		}
        		else
        		{
        			//If the note do not have photo show the message
        			et_comment.setText(listNotes.get(0).getNote());
        		}
        	}
    			        		
		} catch (Exception e) {
			 Toast.makeText(getApplicationContext(), "Failed while trying to read photos: " + e.getMessage() , Toast.LENGTH_LONG).show();

			 listNotes.clear();
			 this.finish();
		}

        myGallery = (Gallery) findViewById(R.id.photoGallery);
        myGallery.setAdapter(new ImageAdapter(this));
        
		//Know when the user click in the gallery
        myGallery.setOnItemSelectedListener(new OnItemSelectedListener() {
  
			@Override
			public void onItemSelected(AdapterView parent, View v, int position, long id) {
			
				//If the note have image
				if(listNotes.get(position).getHasPhoto())
				{
					//Show the image and the comment
					image.setImageDrawable(((KPhoto)listNotes.get(position).getPhoto()).getBitMap());
					et_comment.setText(listNotes.get(position).getNote());
				}
				else
				{
					//Show the comment
					image.setImageBitmap(null);
					et_comment.setText(listNotes.get(position).getNote());
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView arg0) {
				
				
			}
        });

    }
    

    public class ImageAdapter extends BaseAdapter {
        int itemBackground;
        private Context cont;

        public ImageAdapter(Context c) {
            cont = c;
            TypedArray typArray = obtainStyledAttributes(R.styleable.PhotoGallery);
            itemBackground = typArray.getResourceId(R.styleable.PhotoGallery_android_galleryItemBackground, 0);
            typArray.recycle();
        }

     
        public int getCount() {
            return listNotes.size();
        }
  
        public Object getItem(int position) {
            return position;
        }

     
        public long getItemId(int position) {
            return position;
        }

        //View the image position in the gallery
        public View getView(int position, View convertView, ViewGroup parent) {

    		ImageView image = new ImageView(cont);

    		//If the note have image
    		if(listNotes.get(position).getHasPhoto())
    		{
    			image.setImageDrawable(((KPhoto)listNotes.get(position).getPhoto()).getBitMap());
    		}
    		else
    		{
    			//If the note do not have image we show a already defined image to notes
    			image.setImageResource(R.drawable.notes);
    	       			
    		}
    		//Sets gallery parameters
    		image.setLayoutParams(new Gallery.LayoutParams(150, 100));
    		image.setScaleType(ImageView.ScaleType.FIT_XY);
    		image.setBackgroundResource(itemBackground);
        
            return image;
        }
    }

}