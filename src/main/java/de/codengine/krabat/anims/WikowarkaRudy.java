/*
    The Krabat Adventure
    Copyright (C) 2001  Rapaki 
    http://www.rapaki.de

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package de.codengine.krabat.anims;

import de.codengine.krabat.Start;
import de.codengine.krabat.main.Borderrect;
import de.codengine.krabat.main.GenericPoint;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;

public class WikowarkaRudy extends Mainanim {
    // Alle GenericImage - Objekte
    private final GenericImage[] krabat_front;
    private final GenericImage[] krabat_back;
    private final GenericImage[] krabat_talk;
    private final GenericImage[] krabat_extra;
    private final GenericImage[] hrajer_extra;

    // Grundlegende Variablen
    private float xps;
    private float yps;               // genaue Position der Fuesse fuer Offsetberechnung
    private float txps;
    private float typs;             // temporaere Variablen fuer genaue Position
    // public  boolean isWandering = false;  // gilt fuer ganze Route
    // public  boolean isWalking = false;    // gilt bis zum naechsten Rect.
    private int anim_pos = 0;             // Animationsbild
    // public  boolean clearanimpos = true;  // Bewirkt Standsprite nach Laufen 

    // Variablen fuer Bewegung und Richtung
    private GenericPoint walkto = new GenericPoint(0, 0);                 // Zielpunkt fuer Move()
    private GenericPoint Twalkto = new GenericPoint(0, 0);                // Zielpunkt, der in MoveTo() gesetzt und von Move uebernommen wird
    // hier ist das Problem der Threadsynchronisierung !!!!!!!
    private int direction_x = 1;          // Laufrichtung x
    private int Tdirection_x = 1;

    private int direction_y = 1;          // Laufrichtung y
    private int Tdirection_y = 1;

    // private boolean horizontal = true;    // Animationen in x oder y Richtung
    // private boolean Thorizontal = true;

    // public  boolean upsidedown = false;   // Beim Berg - und Tallauf GenericImage wenden

    // Spritevariablen
    private static final int CWIDTH = 33;// Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 35;

    // Abstaende default
    // private static final int[] CHORIZ_DIST = {6, 10, 11, 9, 10, 10, 10, 11, 11, 10};
    private static final int CVERT_DIST = 4;

    private int VerhinderTalk;
    private static final int MAX_VERHINDERTALK = 5;
    private int TalkPos = 0;

    // private boolean isListening = false;
    private boolean isMoving = false;

    private int Guck = 0;
    private int Verhinderguck;
    private static final int MAX_VERHINDERGUCK = 70;

    // Variablen fuer Laufberechnung
    /*private static final int CLOHNENX = 49;  // Werte fuer Entscheidung, ob sich
      private static final int CLOHNENY = 25;  // Laufen ueberhaupt lohnt (halber Schritt)
    */

    // Variablen fuer Animationen
    // public  int nAnimation = 0;           // ID der ggw. Animation
    // public  boolean fAnimHelper = false;  // Hilfsflag bei Animation
    // private int nAnimStep = 0;            // ggw. Pos in Animation

    // Variablen fuer Zooming
    /*public int maxx;                      // X - Koordinate, bis zu der nicht gezoomt wird
      // (Vordergrund) bildabhaengig
      public float zoomf;                   // gibt an, wie stark gezoomt wird, wenn Figur in
      // den Hintergrund geht (bildabhaengig)
      private static final int SLOWX = 14;  // Konstante, die angibt, wie sich die x - Abstaende
      // beim Zoomen veraendern
      private static final int SLOWY = 22;  // dsgl. fuer y - Richtung                                      
      public int defScale;                  // definiert maximale Groesse von Krabat bei x > maxx
      public int minx;                      // "Falschherum" - X - Koordinate, damit Scaling wieder stimmt...
    */

    // Initialisierung ////////////////////////////////////////////////////////////////

    public WikowarkaRudy(Start caller) {
        super(caller);

        krabat_front = new GenericImage[4];
        krabat_back = new GenericImage[4];
        krabat_talk = new GenericImage[8];
        krabat_extra = new GenericImage[2];
        hrajer_extra = new GenericImage[3];

        InitImages();

        VerhinderTalk = MAX_VERHINDERTALK;
        Verhinderguck = MAX_VERHINDERGUCK;
    }

    // Bilder vorbereiten
    private void InitImages() {
        krabat_front[0] = getPicture("gfx-dd/lodz/zona.gif");
        krabat_front[1] = getPicture("gfx-dd/lodz/zona5.gif");
        krabat_front[2] = getPicture("gfx-dd/lodz/zona.gif");
        krabat_front[3] = getPicture("gfx-dd/lodz/zona6.gif");

        krabat_back[0] = getPicture("gfx-dd/lodz/zona4.gif");
        krabat_back[1] = getPicture("gfx-dd/lodz/zona2.gif");
        krabat_back[2] = getPicture("gfx-dd/lodz/zona4.gif");
        krabat_back[3] = getPicture("gfx-dd/lodz/zona3.gif");

        krabat_talk[0] = getPicture("gfx-dd/lodz/zona.gif");
        krabat_talk[1] = getPicture("gfx-dd/lodz/zona9.gif");
        krabat_talk[2] = getPicture("gfx-dd/lodz/zona10.gif");
        krabat_talk[3] = getPicture("gfx-dd/lodz/zona11.gif");
        krabat_talk[4] = getPicture("gfx-dd/lodz/zona12.gif");
        krabat_talk[5] = getPicture("gfx-dd/lodz/zona13.gif");
        krabat_talk[6] = getPicture("gfx-dd/lodz/zona14.gif");
        krabat_talk[7] = getPicture("gfx-dd/lodz/zona15.gif");

        krabat_extra[0] = getPicture("gfx-dd/lodz/zona7.gif");
        krabat_extra[1] = getPicture("gfx-dd/lodz/zona8.gif");

        hrajer_extra[0] = getPicture("gfx-dd/lodz/kzona.gif");
        hrajer_extra[1] = getPicture("gfx-dd/lodz/kzona2.gif");
        hrajer_extra[2] = getPicture("gfx-dd/lodz/kzona3.gif");
    }


    // Laufen mit Plokarka ////////////////////////////////////////////////////////////////

    // Plokarka um einen Schritt weitersetzen
    // false = weiterlaufen, true = stehengebleibt
    public synchronized boolean Move() {
        // Variablen uebernehmen (Threadsynchronisierung)
        // horizontal = Thorizontal;
        walkto = Twalkto;
        direction_x = Tdirection_x;
        direction_y = Tdirection_y;
    
	/*if (horizontal == true)
	  // Horizontal laufen
	  {
	  // neuen Punkt ermitteln und setzen
	  VerschiebeX();
	  xps = txps;
	  yps = typs;
    	
    	// Animationsphase weiterschalten
    	anim_pos++;
    	if (anim_pos == 10) anim_pos = 1;
      
    	// Naechsten Schritt auf Gueltigkeit ueberpruefen
    	VerschiebeX();
      
    	// Ueberschreitung feststellen in X - Richtung
    	if (((walkto.x - (int) txps) * direction_x) <= 0)
    	{    	
	// System.out.println("Ueberschreitung x! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
      	SetKucharPos (walkto);
      	anim_pos = 0;
	return true;
    	}	
	}

	else
	// Vertikal laufen
	{*/
        // neuen Punkt ermitteln und setzen
        VerschiebeY();
        xps = txps;
        yps = typs;

        // Animationsphase weiterschalten
        anim_pos++;
        if (anim_pos == 4) {
            anim_pos = 0;
        }

        // Naechsten Schritt auf Gueltigkeit ueberpruefen
        VerschiebeY();

        // Ueberschreitung feststellen in Y - Richtung
        if ((walkto.y - (int) typs) * direction_y <= 0) {
            // System.out.println("Ueberschreitung y! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
            SetZonaPos(walkto);
            anim_pos = 0;
            isMoving = true;
            return true;
        }
        /*  }  */
        isMoving = false;
        return false;
    }

    // Horizontal - Positions - Verschieberoutine
    /*private void VerschiebeX ()
      {
      // Skalierungsfaktor holen
      int scale = getScale(((int) xps), ((int) yps));
    	
    // Zooming - Faktor beruecksichtigen in x - Richtung
    float horiz_dist = CHORIZ_DIST [anim_pos] - (scale / SLOWX);
    if (horiz_dist < 1) horiz_dist = 1;

    // Verschiebungsoffset berechnen (fuer schraege Bewegung)
    float z = 0;
    if (horiz_dist != 0) z = Math.abs (xps - walkto.x) / horiz_dist;
      
    typs = yps;
    if (z != 0) typs += direction_y * (Math.abs (yps - walkto.y) / z); 
	  
    txps = xps + (direction_x * horiz_dist);
    // System.out.println(xps + " " + txps + " " + yps + " " + typs);
    }	*/

    // Vertikal - Positions - Verschieberoutine
    private void VerschiebeY() {
        // Skalierungsfaktor holen
        // int scale = getScale(((int) xps), ((int) yps));

        // Zooming - Faktor beruecksichtigen in y-Richtung
        float vert_dist = CVERT_DIST; /* - (scale / SLOWY); */
	/*if (vert_dist < 1) 
    {
      vert_dist = 1;
      // hier kann noch eine Entscheidungsroutine hin, die je nach Animationsphase
      // und vert_distance ein Pixel erlaubt oder nicht
    }*/

        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(yps - walkto.y) / vert_dist;

        txps = xps;
        if (z != 0) {
            txps += direction_x * (Math.abs(xps - walkto.x) / z);
        }

        typs = yps + direction_y * vert_dist;
        // System.out.println(xps + " " + txps + " " + yps + " " + typs);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    public synchronized void MoveTo(GenericPoint aim) {
        int xricht, yricht;
        // boolean horiz;

        // Lohnt es sich zu laufen ?
	/*int scale = getScale ((int) xps, (int) yps);
	  int lohnenx = CLOHNENX - (scale / 2);
	  int lohneny = CLOHNENY - (scale / 4);
	  if (lohnenx < 1) lohnenx = 1;
	  if (lohneny < 1) lohneny = 1;
	  if ((Math.abs (aim.x - ((int) xps)) < lohnenx) && 
	  (Math.abs (aim.y - ((int) yps)) < lohneny))
	  {
	  System.out.println("Lohnt sich nicht !");
	  anim_pos = 0;
	  return;
	  }*/

        // Laufrichtung ermitteln
        if (aim.x > (int) xps) {
            xricht = 1;
        } else {
            xricht = -1;
        }
        if (aim.y > (int) yps) {
            yricht = 1;
        } else {
            yricht = -1;
        }

        // Horizontal oder verikal laufen ?
	/*if (aim.x == ((int) xps) ) horiz = false;
	  else
	  {
	  // Winkel berechnen, den Krabat laufen soll
	  double yangle = Math.abs (aim.y - ((int) yps) );
	  double xangle = Math.abs (aim.x - ((int) xps) );
	  double angle = Math.atan (yangle / xangle);
	  // System.out.println ((angle * 180 / Math.PI) + " Grad");
	  if (angle > (22 * Math.PI / 180))  horiz = false;
	  else horiz = true;
	  }*/

        // horiz = false;

        // Variablen an Move uebergeben
        Twalkto = aim;
        // Thorizontal  = horiz;
        Tdirection_x = xricht;
        Tdirection_y = yricht;

        if (anim_pos == 0) {
            anim_pos = 1;       // Animationsimage bei Neubeginn initialis.
        }
        // System.out.println("Animpos ist . " + anim_pos);
    }

    // Krabat an bestimmte Position setzen incl richtigem Zoomfaktor (Fuss-Koordinaten angegeben)
    public void SetZonaPos(GenericPoint aim) {
        // point bezeichnet Fusskoordinaten
        // pos_x = aim.x;
        // pos_y = aim.y;

        xps = aim.x;        // Float - Variablen initialisieren
        yps = aim.y;

        //System.out.println("Setkrabatpos allgemein "+pos_x+" "+pos_y);
    }

    // Krabats Position ermitteln incl richtigem Zoomfaktor (Ausgabe der Fuss-Koordinaten)
    public GenericPoint GetZonaPos() {
        //System.out.println(" Aktuelle Pos : "+pos_x+" "+pos_y);
        return new GenericPoint((int) xps, (int) yps);
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    public void drawZona(GenericDrawingContext offGraph, boolean isListening) {
        // je nach Richtung Sprite auswaehlen und zeichnen
	/*if (horizontal == true)
    {
      // nach links laufen
      if (direction_x  == -1) MaleIhn (offGraph, krabat_left[anim_pos]);
      
      // nach rechts laufen
      if (direction_x == 1) MaleIhn (offGraph, krabat_right[anim_pos]);
    }
    else
    {
      // Bei normaler Darstellung
      if (upsidedown == false)
      {   
        // nach oben laufen
        if (direction_y  == -1) MaleIhn (offGraph, krabat_back[anim_pos]);
      
        // nach unten laufen
        if (direction_y == 1) MaleIhn (offGraph, krabat_front[anim_pos]);
      }
      else
      {*/

        // auch hier Uebergabe von isListening -> schneller
        // this.isListening = isListening;

        // hier evaluieren, ob geguckt werden darf oder nicht
        if (!isMoving && !isListening) {
            // Weiterschalten
            if (--Verhinderguck < 1) {
                Verhinderguck = MAX_VERHINDERGUCK;
                Guck = (int) (Math.random() * 1.9);
            }

            // Wenn Extrawurst, dann diese ausfuehren
            if (Guck == 1) {
                MaleIhn(offGraph, krabat_talk[1]);
                return;
            }
        }

        // nach oben laufen
        if (direction_y == 1) {
            MaleIhn(offGraph, krabat_front[anim_pos]);
        }

        // nach unten laufen
        if (direction_y == -1) {
            MaleIhn(offGraph, krabat_back[anim_pos]);
        }
	/*
	  }  
	  }*/
    }

    // Lasse Krabat in eine bestimmte Richtung schauen (nach Uhrzeit!)
    public void SetFacing(int direction) {
        switch (direction) {
		/*  case 3:
		    horizontal=true;
		    direction_x=1;
		    break;*/
            case 6:
                // horizontal=false;
                direction_y = 1;
                break;
		/*  case 9:
		    horizontal=true;
		    direction_x=-1;
		    break;*/
            case 12:
                // horizontal=false;
                direction_y = -1;
                break;
            default:
                System.out.println("Falsche Uhrzeit zum Witzereissen!");
        }
    }

    // Richtung, in die Krabat schaut, ermitteln (wieder nach Uhrzeit)
    /*public int GetFacing()
      {
      int rgabe = 0;
      if (horizontal == true)
      {
      if (direction_x == 1) rgabe = 3;
      else rgabe = 9;
      }
      else
      {
      if (direction_y == 1) rgabe = 6;
      else rgabe = 12;
      }
      if (rgabe == 0) 
      {
      System.out.println("Fehler bei GetFacing !!!");
      }
      return rgabe;
      }*/


    // Zeichne Hojnt beim Sprechen mit anderen Personen
    public void talkZona(GenericDrawingContext offGraph, boolean isListening) {
        // isListening wird nur bei "draw" ausgewertet
        // d.h. aktiviert -> Gucken gesperrt
        // deaktiviert -> Guckt nach Ende Talk wieder

        // this.isListening = isListening;

        // schraeggucken deaktivieren
        Guck = 0;

        if (--VerhinderTalk < 1) {
            VerhinderTalk = MAX_VERHINDERTALK;
            TalkPos = (int) Math.round(Math.random() * 8);

            if (TalkPos == 8) {
                TalkPos = 7;
            }
        }

        MaleIhn(offGraph, krabat_talk[TalkPos]);
    }

    public void giveWosusk(GenericDrawingContext g, GenericPoint lo) {
        // alles "overriden" und die 2 Images fuer Give zeichnen
        // Achtung ! Nachher per Clipset Neuzeichnen erzwingen !!
        g.setClip(lo.x, lo.y, lo.x + CWIDTH * 2, lo.y + CHEIGHT * 2);
        g.drawImage(krabat_extra[0], lo.x, lo.y, null);
        g.drawImage(hrajer_extra[0], lo.x, lo.y + CHEIGHT, null);
    }

    public void giveMetal(GenericDrawingContext g, GenericPoint lo) {
        // alles "overriden" und Images fuer Give zeichnen
        // Achtung ! Nachher per Clipset Neuzeichnen erzwingen !!
        g.setClip(lo.x, lo.y, lo.x + CWIDTH * 2, lo.y + CHEIGHT * 2);
        g.drawImage(krabat_extra[1], lo.x, lo.y, null);
        g.drawImage(hrajer_extra[1], lo.x, lo.y + CHEIGHT, null);
    }

    public void Kiss(GenericDrawingContext g, GenericPoint lo) {
        // alles "overriden" und 1 Kiss-Image zeichnen
        // Achtung ! Nachher per Clipset Neuzeichnen erzwingen !!
        g.setClip(lo.x, lo.y, lo.x + CWIDTH * 2, lo.y + CHEIGHT * 2);
        g.drawImage(hrajer_extra[2], lo.x, lo.y, null);
    }

    // Hojnt beim Monolog (ohne Gestikulieren)
    /*public void describeHojnt(Graphics offGraph)
      {
      if (mainFrame.talkCount != 1)
      {
      int nTemp;
      do
      nTemp = (int) Math.round(Math.random()*6);
      while ((nTemp==3)||(nTemp==4)||(nTemp==6)||(nTemp==0));
      MaleIhn (offGraph, krabat_talk[nTemp]);
      }
      else drawHojnt (offGraph);
      }*/

    // Zooming-Variablen berechnen

    private int getLeftPos(int pox, int poy) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        // int helper = getScale(pox, poy);
        return pox - CWIDTH / 2;
    }

    private int getUpPos(int pox, int poy) {
        // obere y-Koordinate = untere y-Koordinate - konstante Hoehe
        // + Hoehendifferenz
        // int helper = getScale(pox, poy);
        return poy - CHEIGHT / 2;
    }

    // fuer Debugging public - wird wieder private !!!
    /*public int getScale (int pox, int poy)
      {
    
      // Hier kann override eingeschaltet werden (F7/F8)
      // return mainFrame.override;
    
      // Ermittlung der Hoehendifferenz beim Zooming
      if (upsidedown == false)
      {
      // normale Berechnung	
      float helper = (maxx - poy) / zoomf;
      if (helper < 0) helper = 0;
      helper += defScale;
      return ((int) helper);
      }
      else
      {
      // Berechnung bei "upsidedown" - Berg/Tallauf
      float help2 = (poy - minx) / zoomf;
      if (help2 < 0) help2 = 0;
      help2 += defScale;
      // System.out.println (minx + " + " + poy + " und " + zoomf + " ergeben " + help2); 
      return ((int) help2);
      }
      } */

    // Clipping - Region vor Zeichnen von Krabat setzen
    private void KrabatClip(GenericDrawingContext g, int xx, int yy) {
        // Links - oben - Korrdinaten ermitteln
        int x = getLeftPos(xx, yy);
        int y = getUpPos(xx, yy);
        // System.out.println(xx +  " " + x);

        // Breite und Hoehe ermitteln
        int xd = 2 * (xx - x);
        int yd = 2 * (yy - y);
        g.setClip(x, y, xd, yd);

        // Fuer Debugging ClipRectangle zeichnen
        // g.setColor(Color.white);
        // g.drawRect(x, y, xd - 1, yd - 1);
        // System.out.println(x + " " + y + " " + xd + " " + yd);
    }

    // Routine, die BorderRect zurueckgibt, wo sich Krabat gerade befindet
    public Borderrect ZonaRect() {
        int x = getLeftPos((int) xps, (int) yps);
        int y = getUpPos((int) xps, (int) yps);
        int xd = 2 * ((int) xps - x) + x;
        int yd = 2 * ((int) yps - y) + y;
        // System.out.println(x + " " + y + " " + xd + " " + yd);
        return new Borderrect(x, y, xd, yd);
    }

    private void MaleIhn(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        KrabatClip(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) xps, (int) yps);
        // int scale = getScale   ( ((int) xps), ((int) yps) );

        // Figur zeichnen
        g.drawImage(ktemp, left, up);
    }
}