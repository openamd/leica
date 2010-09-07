/* 
 * Copyright (C) 2008 Benjamin Maus < info <at> allesblinkt.com >
 *
 * This file is part of LeicasDream
 *
 * LeicasDream is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LeicasDream is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with LeicasDream.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.allesblinkt.leicasdream;


import gestalt.candidates.JoglGLUTBitmapFont;
import gestalt.context.DisplayCapabilities;
import gestalt.impl.jogl.context.JoglDisplay;
import gestalt.render.AnimatorRenderer;
import gestalt.shape.Plane;
import gestalt.shape.material.TexturePlugin;

import com.sun.opengl.util.GLUT;

public class LeicasDream extends AnimatorRenderer implements Constants {

	private static boolean EEPC_MODE = false;
    private static String _myOwnOid="0";
    private static String _LeicaVersion="Leica2 Ver. 1.0 W";
	public float timeElapsed = 0;
    private boolean _paused = false;

    /* GL stuff */

//	Plane _Floor2;
	Plane _Floor18;
	Plane _logoPlane;
	TexturePlugin _logoTexture;
	TexturePlugin _Floor18Texture;
	TexturePlugin _Floor2Texture;
	JoglGLUTBitmapFont _myActiveTags;

	/* Managers*/
	private SputnikReceiver _sputnikReceiver;
	private ObserverManager _observerManager;
	private SputnikTagManager _tagManager;


	public void setup() {

		/* Set the framerate */
		framerate(60);

		/* Set up the managers for the receivers/observers and the tags */
		_observerManager = new ObserverManager(this);
		_tagManager = new SputnikTagManager(this, _myOwnOid);

		/* Start a receiver thread which talks to the managers */
		_sputnikReceiver = new SputnikReceiver(_tagManager);
		_sputnikReceiver.start();

		/*		
		// Add Floor2 texture map (Needs work)
		_Floor2 = drawablefactory().plane();
		_Floor2Texture = drawablefactory().texture();
		_Floor2Texture.load(bitmapfactory().getBitmap(data.Resource.getPath("hotelpenn2.png")));
		_Floor2.setPlaneSizeToTextureSize();
		_Floor2.material().addPlugin(_Floor2Texture);
		_Floor2.material().depthtest = true;
		_Floor2.scale().set(504f, 504f);
 		_Floor2.rotation().x = -PI_HALF;
 		_Floor2.position().y = 0f;
		bin(BIN_3D).add(_Floor2);
		*/

		// Add Floor18 texture map (Needs work)
		_Floor18 = drawablefactory().plane();
		_Floor18Texture = drawablefactory().texture();
		_Floor18Texture.load(bitmapfactory().getBitmap(data.Resource.getPath("hotelpenn18.png")));
		_Floor18.setPlaneSizeToTextureSize();
		_Floor18.material().addPlugin(_Floor18Texture);
		_Floor18.material().depthtest = true;
		_Floor18.scale().set(504f, 504f);	
 		_Floor18.rotation().x = -PI_HALF;
 		_Floor18.position().y = 0f;
		bin(BIN_3D).add(_Floor18); 

		/* Add the logo and the diagram */

		_logoPlane = drawablefactory().plane();
		_logoTexture = drawablefactory().texture();
		_logoTexture.load(bitmapfactory().getBitmap(data.Resource.getPath("amd-project1.png")));
		_logoPlane.material().addPlugin(_logoTexture);
		_logoPlane.setPlaneSizeToTextureSize();

		if(EEPC_MODE){
			_logoPlane.scale().scale(0.5f);
			_logoPlane.position().x = -180;
			_logoPlane.position().y = 180;
		} else {
			_logoPlane.position().x = -300;
			_logoPlane.position().y = 400;
		}

		/* Only things putÅj in a bin will be rendered */
		bin(BIN_2D_FOREGROUND).add(_logoPlane);



		/* Add a GLUT font for the tagCount display */
		_myActiveTags = new JoglGLUTBitmapFont();
		_myActiveTags.color.set(1f, 1f);
		_myActiveTags.align = JoglGLUTBitmapFont.LEFT;
		if(EEPC_MODE){
			_myActiveTags.position.set(displaycapabilities().width / -2 + 300, displaycapabilities().height / 2 - 37);
		}
		else {
			_myActiveTags.position.set(displaycapabilities().width / -2 + 800, displaycapabilities().height / 2 - 100);
		}

		_myActiveTags.font = GLUT.BITMAP_HELVETICA_12;

		bin(BIN_2D_FOREGROUND).add(_myActiveTags);



		/* Initial camera settings */
		camera().setMode(CAMERA_MODE_LOOK_AT);
		camera().up(200f);
		camera().forward(-300f);



	}

	private void moveCamera(float theDeltaTime) {
		float mySpeed = 300f * theDeltaTime;
		/* Handle manual camera movement */

        if(event().keyCode == KEYCODE_P)  {
            _paused = !_paused;
        }

        if (event().keyCode == KEYCODE_A) {
			camera().forward(mySpeed);
		}
		if (event().keyCode == KEYCODE_Q) {
			camera().forward(-mySpeed);
		}
		if (event().keyCode == KEYCODE_LEFT) {
			camera().side(-mySpeed);
		}
		if (event().keyCode == KEYCODE_RIGHT) {
			camera().side(mySpeed);
		}
		if (event().keyCode == KEYCODE_DOWN) {
			camera().up(-mySpeed);
		}
		if (event().keyCode == KEYCODE_UP) {
			camera().up(mySpeed);
		}


 	if (event().keyCode == KEYCODE_Z) {
  		camera().setMode(CAMERA_MODE_LOOK_AT);
  		camera().fovy += mySpeed;
		}

			
	if (event().keyCode == KEYCODE_X) {
		camera().setMode(CAMERA_MODE_LOOK_AT);
		camera().fovy -= mySpeed;
		}	

	}

	public void finish() {
		// TODO: Shut down the receiver thread
	}

	public static void main(String[] arg) {

		/* Make no attempt to switch the resolution in fullscreen mode.
		 * A resolution switch often causes problems with macs and a second display
		 */
		JoglDisplay.SWITCH_RESOLUTION = false;

		/* Parameters */

		/* -e as the FIRST command line argument resizes for the eeepc */
		if (arg.length > 0 && arg[0].equals("-e")) {
			EEPC_MODE = true;
		} else {
			EEPC_MODE = false;
		}

		/* Supply your tag OID as the second argument if you want it to be highlighted */
		if (arg.length > 1 ) {
			_myOwnOid = arg[1];
			System.out.println(_myOwnOid);
		} else {
			_myOwnOid = "0";
		} 


		new LeicasDream().init();

	}

	public DisplayCapabilities createDisplayCapabilities() {
		/*
		 * create a 'displaycapabilities' object
		 */
		DisplayCapabilities myDisplayCapabilities = new DisplayCapabilities();

		myDisplayCapabilities.name = "Hope-AMD";

		if(EEPC_MODE){
			myDisplayCapabilities.width = 800;
			myDisplayCapabilities.height = 480;
		} else {
			myDisplayCapabilities.width = 1024;
			myDisplayCapabilities.height = 768;
		}

		myDisplayCapabilities.undecorated = true;

		if(EEPC_MODE){
			myDisplayCapabilities.fullscreen = false;

		} else {
			myDisplayCapabilities.fullscreen = true;
		}

		myDisplayCapabilities.centered = true;

		myDisplayCapabilities.backgroundcolor.set(0,0,0, 1f);

		myDisplayCapabilities.antialiasinglevel = 1;

		myDisplayCapabilities.cursor = true;

		myDisplayCapabilities.renderer = ENGINE_JOGL;

		myDisplayCapabilities.headless = false;

		DisplayCapabilities.listDisplayDevices();

		return myDisplayCapabilities;
	}

	/* Main Loop */
	public void loop(float theDeltaTime) {

		/* Rotate the camera (if not _paused) */
		timeElapsed += theDeltaTime;
		moveCamera(theDeltaTime);

		/* Tell the TagManager to update all tags */
		_tagManager.animate();

        if(_paused == false) camera().side(-theDeltaTime * 100f);
        
        /* Update the tagCount OSD */
		_myActiveTags.text = "18th Floor " + String.valueOf(_tagManager.getSeenTagCount()) + " Tags Seen, "+ String.valueOf(_tagManager.getActiveTagCount())  + " Currently Active - " + _LeicaVersion;

	}

}
