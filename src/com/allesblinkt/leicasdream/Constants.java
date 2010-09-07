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

public interface Constants {
	/* Receiving UDP Port */
	public static final int TCP_PORT = 2342;

	/* The XML file with the receiver ids and positions */
	public static final String OBSERVER_LIST_FILENAME = "locations-lasthope.xml";

	/* Set the weightings of the moving average */
	public static final float TAG_POSITION_WEIGHT = 0.4f;
	public static final float TAG_STRENGTH_FACTOR = 0.5f;

	/* How many milliseconds is a tag considered "active" */
	public static final int LIFESPAN = 10000;


	public static final int CAPACITY = 2000;

}
