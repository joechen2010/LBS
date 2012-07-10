package com.camera;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


/**
 * Provides a dedicated drawing surface embedded inside of a view hierarchy and
 * implements SurfaceHolder.Callback to receive information about changes to the surface
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 * 
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

	private SurfaceHolder mHolder;
	private Camera camera;
	private Context context;
	
	
	/**
	 * Constructor
	 * @param context The context where the preview appears
	 */
	public CameraPreview(Context context) {
		super(context);
		this.context = context;
		
		// Install a SurfaceHolder.Callback so we get notified when the surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	
	public void surfaceCreated(SurfaceHolder holder){
	
		//Activate the camera
		camera = Camera.open();
			try {
				//Sets the surface holder to be used for a picture preview.
				camera.setPreviewDisplay(holder);
			} catch (IOException e) {
				 Toast.makeText(context, "Error while trying to sets the surface holder: " + e.getMessage(), Toast.LENGTH_LONG).show();
			}

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		//Surface will be destroyed, We stop the preview
		camera.stopPreview();
		
		//Release the camera
		camera.release();
		
	}


	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		
		//Set up the camara parameters.
		Camera.Parameters parameters = camera.getParameters();
		parameters.setPreviewSize(w, h);
		camera.setParameters(parameters);
		
		//Start the preview
		camera.startPreview();
	}
	

	/**
	 * Get the camera
	 * @return the camera
	 */
	public Camera getCamera() {
	
		return camera;
	}

}
