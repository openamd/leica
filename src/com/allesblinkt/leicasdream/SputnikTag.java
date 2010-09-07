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

import mathematik.Vector3f;
import gestalt.candidates.JoglGLUTBitmapFont;
import gestalt.shape.Sphere;
import gestalt.shape.Cube;

public class SputnikTag implements Constants {


	public Cube _drawable;
	public String oid;
	private Vector3f _position;
	private Vector3f _LabelPosition;
	public JoglGLUTBitmapFont _MyTag;
	
	private boolean inDecay = false;
	private int decay = 0;
	private boolean _inited = false;
	private long _lastSeen;
	public boolean _alive = false;
	public boolean _isMyOwn = false;

	public SputnikTag(String theOid){
		this.oid = theOid;
		_position = new Vector3f(0,0,0);
		_LabelPosition = new Vector3f(0,0,0);
	}

	public void activate(){
		_drawable.material().color.set(0,0.7f,1f,1f);
		decay = 80;
		inDecay = true;
	}

	public void setPosition(Vector3f p){
        _alive = true;
        _inited = true;

        _lastSeen = System.currentTimeMillis();

        _drawable.material().color.set(1f, 0,0,0.6f);
        if(_isMyOwn) _drawable.material().color.set(0f, 1f,0,0.6f);

        float x = 300*p.x;
        float y = 0.0f;
        float z = -300*p.y;
        _position.set(x, y, z);
		_LabelPosition.set(x, y + 15, z);
		_drawable.position().set(_position);
		_MyTag.position.set(_LabelPosition);
	}



	public void animate(){
		if(_inited){


			if(System.currentTimeMillis() - _lastSeen > LIFESPAN && _alive ){
				_alive  = false;
				_drawable.material().color.set(0.2f, 0.6f);
			}


			if(decay > 0 ){
				decay--;
				float size = (float)decay / 10f + 2f;
				_drawable.scale().set(size, size, size);
			}

			if(decay == 0 && inDecay){
				_drawable.material().color.set(1f, 0,0,0.6f);
			}
				
			if(_isMyOwn == true){
				_drawable.material().color.set(0f, 1f,0,0.6f);
				inDecay = false;
				}
		}
	}
}	
