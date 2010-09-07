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
import gestalt.render.AnimatorRenderer;
import gestalt.candidates.JoglGLUTBitmapFont;
import java.util.Enumeration;
import java.util.Hashtable;

import com.sun.opengl.util.GLUT;


import mathematik.Vector3f;
import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLParserFactory;
public class ObserverManager implements Constants{

	/* Parsed Observers, hashed by IP (String) */
	private Hashtable<String, Observer> _myObservers = new Hashtable<String, Observer>(CAPACITY);

	private AnimatorRenderer _myRenderer;

	public ObserverManager(AnimatorRenderer theRenderer) {
		_myRenderer = theRenderer;

		/* Parse the XML specified in Constants.java  */
		loadXML(OBSERVER_LIST_FILENAME);
	}


	private void loadXML(String theFilename){

		/* TODO: This parser wants to have a well-formatted XML or it will fail
		 * ... and eat your dog
		 */

		try{
			IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
			IXMLReader reader = StdXMLReader.fileReader(data.Resource.getPath(theFilename));

			parser.setReader(reader);
			IXMLElement xml = (IXMLElement) parser.parse();


			Enumeration rootEnumeration = xml.enumerateChildren();

			IXMLElement observer =null;


			int count = 0;
			/* Search x and y positions */
			while (rootEnumeration.hasMoreElements()) {
				IXMLElement element = (IXMLElement) rootEnumeration.nextElement();



				if(element.getName().equalsIgnoreCase("observer") ){

					observer = element;
					count++;
				}

				String positionString = observer.getAttribute("position");
				positionString = positionString.substring(1, positionString.length()-1);
				String position[] = positionString.split(",");


				Observer ob = new Observer();
				ob.name = observer.getAttribute("name");
				ob.id = observer.getAttribute("id");
				ob.type = observer.getAttribute("type");

				String ip[] = ob.id.split("/");
				String theIp = ip[ip.length-1];

				ob.x = Float.valueOf( position[0]);
				ob.y = Float.valueOf( position[1]);
				ob.z = Float.valueOf( position[2]);

				ob._drawable = _myRenderer.drawablefactory().cube();
				ob._drawable.position().x =  ob.x * 10f;
				ob._drawable.position().y = (ob.y * 10f) + 5f;
				ob._drawable.position().z = -ob.z * 10f;
				 
				ob._MyTag = new JoglGLUTBitmapFont();
				ob._MyTag.color.set(0.0f, 0.0f, 1.0f, 1.0f);
				ob._MyTag.align = JoglGLUTBitmapFont.LEFT;
				ob._MyTag.font = GLUT.BITMAP_HELVETICA_12;
				ob._MyTag.position.set(ob.x * 10, (ob.y * 10 + 10), -ob.z * 10);
				ob._MyTag.text = String.valueOf(ob.name);
				ob._MyTag.setSortValue(1);
//				_myRenderer.bin(Gestalt.BIN_3D).add(ob._MyTag);
				
				ob.position = new Vector3f (ob.x * 10f , ob.y * 10f, -ob.z * 10f);
				ob._drawable.scale().set(10f, 5f, 10f);
				ob._drawable.material().color.set(0.5f,0.5f,0.5f,0.4f);
				ob._drawable.material().depthtest = true;
				ob._drawable.material().blendmode = Gestalt.MATERIAL_BLEND_ALPHA;
//				_myRenderer.bin(Gestalt.BIN_3D).add(ob._drawable);

				
				System.out.println("Tag Reader added: "+ob.name);


				_myObservers.put(theIp,ob);



			}


		}



		catch(Exception ex){
			System.err.println(ex.toString());
		}

		resetStates();
	}




	public Vector3f getPositionForId(String theIpString ){
		if(_myObservers.containsKey(theIpString)){

			return _myObservers.get(theIpString).position;
		}

		return new Vector3f(0,0,0);
	}

	void resetStates(){
		Enumeration<Observer> enumer = _myObservers.elements();
		while(enumer.hasMoreElements()){
			Observer ob = enumer.nextElement();
			ob._drawable.material().color.set(0.5f);
		}
	}


	public void activate(String ipstring, int strength) {
		Observer foo= _myObservers.get(ipstring);
		if(foo != null){
			foo._drawable.material().color.set((float)strength /255f);
		}
	}
}
