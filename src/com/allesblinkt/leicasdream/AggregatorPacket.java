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

public class AggregatorPacket {

	/* Datastructure of the aggregator response */

	public long oid = 0;
	public long ip = 0;
	public long seq = 0;
	public int strength = 0;
	public long localTimestamp = 0;

	public boolean button = false;

	public String ipstring = "";


	public String toString(){
		return "srq:" + seq + " ip:" + ip + " oid:" + oid + " str:" + strength;		
	}
}
