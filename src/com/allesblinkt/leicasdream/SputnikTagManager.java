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

import gestalt.Gestalt;
import gestalt.candidates.JoglGLUTBitmapFont;
import gestalt.render.AnimatorRenderer;

import java.util.Enumeration;
import java.util.Hashtable;

import com.sun.opengl.util.GLUT;

import mathematik.Vector3f;

public class SputnikTagManager implements Constants{

	private Hashtable<String, SputnikTag> _myTags = new Hashtable<String, SputnikTag>(CAPACITY);
	private AnimatorRenderer _myRenderer;
	String _myOwnId ="0";

	private int _tagCount = 0;
	private int _activeTagCount;

	public SputnikTagManager(AnimatorRenderer theRenderer , String ownId) {
		_myRenderer = theRenderer;
		_myOwnId = ownId;


	}

	public void activateTag(String oid){
		if(_myTags.containsKey(oid)){
			SputnikTag tag   = _myTags.get(oid);
			tag.activate();
			System.out.println("[Button pressed] "+oid);
		}

	}

	public void updateTag(String oid, Vector3f sightingPosition){

		if(!_myTags.containsKey(oid)){
			SputnikTag tag = new SputnikTag(oid);
			addTag(oid, tag );
			System.out.println("[Tag added] "+oid+" tagCount:"+_tagCount);

		}

		SputnikTag tag   = _myTags.get(oid);
		tag.setPosition(sightingPosition);



	}

	private void addTag(String oid, SputnikTag theTag){

		if(_myOwnId.equals(oid)){
			theTag._isMyOwn = true;
		}

		theTag._drawable = _myRenderer.drawablefactory().cube();
		theTag._drawable.scale().set(7f, 7f, 7f);
		theTag._drawable.material().color.set(1f, 0, 0,0.6f);
	    theTag._MyTag = new JoglGLUTBitmapFont();
	    theTag._MyTag.color.set(1f, 1f, 1f, 1f);
	    theTag._MyTag.align = JoglGLUTBitmapFont.LEFT;
	    theTag._MyTag.font = GLUT.BITMAP_HELVETICA_12;
	    theTag._MyTag.text = String.valueOf(theTag.oid);
		
		/* Add to render bin */

		_myRenderer.bin(Gestalt.BIN_3D).add(theTag._drawable);
		_myRenderer.bin(Gestalt.BIN_3D).add(theTag._MyTag);
		_myTags.put(oid, theTag);
		_tagCount++;
	}

	public void animate(){
		_activeTagCount = 0;
		Enumeration<SputnikTag> enumer = _myTags.elements();
		while(enumer.hasMoreElements()){
			SputnikTag tag = enumer.nextElement();
			tag.animate();
			if(tag._alive) _activeTagCount++;
		}

	}
	public int getActiveTagCount(){
		return _activeTagCount;
	}

	public int getSeenTagCount(){
		return _tagCount;
	}
}
