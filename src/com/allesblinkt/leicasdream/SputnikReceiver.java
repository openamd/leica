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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class SputnikReceiver extends Thread implements Constants {

    private SputnikTagManager _theTagManager;
    private static final String whitespaceRegex = "^\\s*$";

    public SputnikReceiver(SputnikTagManager tagManager) {
        this._theTagManager = tagManager;
    }


    public void run() {
        URL url;
        try {
            url = new URL("http://api.hope.net/api/location?leica=");
        } catch (MalformedURLException e) {
            throw new RuntimeException("invalid URL", e);
        }

        //noinspection InfiniteLoopStatement
        while (true) {
            InputStream stream = null;
            BufferedReader reader = null;

            try {
                stream = (InputStream) url.getContent();
            } catch (IOException e) {
                System.out.println("web service failed");
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    break;
                }
                continue;
            }
            reader = new BufferedReader(new InputStreamReader(stream));

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.matches(whitespaceRegex)) {
                        continue;
                    }
                    String[] fields = line.split("\\|");
                    if (fields.length < 6) {
                        //logger.debug("ignoring invalid line: "+line);
                        continue;
                    }
                    String xs = fields[2];
                    String ys = fields[3];
                    String zs = fields[4];
                    if (xs.matches(whitespaceRegex) || ys.matches(whitespaceRegex) ||
                            zs.matches(whitespaceRegex)) {
                        continue;
                    }
                    float x = Float.valueOf(xs);
                    float y = Float.valueOf(ys);
                    float z = Float.valueOf(ys);
                    String fs = fields[5];
                    int flags = Integer.parseInt(fs);

                    if (z == 0) {
                        continue;
                    }
                    Vector3f position = new Vector3f(x, y, z);
                    _theTagManager.updateTag(String.valueOf(fields[1]), position);

                    if (flags != 0) {
                        _theTagManager.activateTag(String.valueOf(fields[1]));
                    }
                }

                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("web service failed");
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    break;
                }
            }
        }
    }

}
