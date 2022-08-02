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

/**
 *
 * @author Francesco Lopez
 */
public class MenuContatti {

     public String nickname = "";
     public String email    = "";
     public String stato    = "";
     

    public int Id;

    /**
     * Id opzione padre
     */
    public int IdPadre;

    /**
     * Id Applicazione
     */
    public int IdApp;

    /**
     * Posizione relativa opzione all'interno del sub-menu
     */
    public int Posizione;

    /**
     * Descrizione opzione
     */
    public String Descr;

    /**
     * LABEL comando da eseguire
     */
    public String Command;

    /**
     * TRUE = Opzione attiva
     */
    public boolean Enable;

    /**
     * TRUE = Opzione Visibile
     */
    public boolean Visible;

    /**
     * Nome icona per opzione abilitata o cartella aperta
     */
    public String IconaAbil;

    /**
     * Nome icona per opzione disabilitata o cartella chiusa
     */
    public String IconaDis;

    /**
     * TRUE = Opzione attiva (tramite processo dei campi "attivo" e "visibile")
     */
    public boolean Opzioni;

    public String getEmail() {
        return email;
    }
}
