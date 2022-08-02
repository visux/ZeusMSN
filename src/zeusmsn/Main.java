/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zeusmsn;

import gui.ZeusMsn;

/**
 *
 * @author Francesco Lopez
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

         try
        {
            ClassLoader c = ClassLoader.getSystemClassLoader();
            Generic.codebase = c.getResource("").toString();
        }
        catch(Exception e)
        {
            System.out.println("codebase non inizializzato");
        }

        ZeusMsn oLogin = new ZeusMsn();
        oLogin.setVisible(true);
    }

}
