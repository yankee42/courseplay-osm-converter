ENGLISH VERSION BELOW

Courseplay to OpenStreetMap (deutsch)
===========================
Um die gespeicherten Kurse aus Courseplay einfach bearbeiten zu können, konvertiert dieses Programm die
von Courseplay generierten XML Dateien in das Openstreetmap-Format, welches dann mit
[JOSM](https://josm.openstreetmap.de/) editiert werden kann:

![Screenshot Courseplay mit JOSM editor](doc/screenshot-edit.jpeg)

Verwendung
----------
[Videotutorial von Gadarol auf YouTube](https://youtu.be/Cz7IJIzb3a8)

1. JAR-Datei herunterladen, speichern und ausführen.
2. `Course -> OSM` wählen und aus dem Courseplay-Ordner der gewünschten Map die `courseManager.xml` auswählen
3. Die Kartengröße in Meter Kantenlänge angeben (2048 für Normale- oder 4096 für Vierfachmaps). Der Wert wird später verwendet um
dabei zu helfen ein Hintergrundbild auszurichten.
4. Eine Datei wählen, in die geschrieben werden soll. Erzeugt wird eine einzelne OSM-Datei, die alle gespeicherten Kurse
enthält.
5. Den [Java OpenStreetMap Editor](https://josm.openstreetmap.de/) herunterladen.
6. Die in Schritt 4 generierte Datei öffnen. Zu sehen ist zum Beispiel:
    
    ![Screenshot nach dem Laden](doc/anzeige-geladen.png)
7. Optional: Eine Hintergrundbild (map pda) hinterlegen, dazu:
    1. Im OSM-Converter "Extract Background map" wählen
    2. Die `.dds`-Datei oder die `.zip` der Mod auswählen. Bei Originalmaps findest du diese im
    Installationsverzeichnis (z.B.: `Steam\steamapps\common\Farming Simulator 19\sdk\maps\mapD\maps\pda_map_H.dds`)  
    3. Eine Ausgabedatei angeben. Es wird die angegebene Datei (zum Beispiel `map.png`) erzeugt und dazu eine
       Kalibrierungsdatei (in dem Beispiel `map.png.cal`)
    4. In JOSM unter Bearbeiten -> Einstellungen -> Plugins das Plugin "PicLayer" installieren
    5. Im Menü über Hintergrundbilder -> Neue Bildebene aus Datei da in Schritt 3 erzeugte Bild laden. Die Kalibrierung
    wird automatisch mitgeladen.  
    6. Die Karte sollte automatisch korrekt kalibriert sein. Wenn du die Karte trotzdem anpassen möchtest:
        1. Bei "Ebenen" oben rechts die Bildebene setzen (den weißen Haken im grünen Kreis auf die Bildebene setzen)
        2. Mit den Werkzeugen in der Leiste links die Karte passend verschieben. Dabei hilft wieder das äußere Quadrat.
        Siehe dazu auch [die Anleitung von PicLayer](https://wiki.openstreetmap.org/wiki/JOSM/Plugins/PicLayer).
8. Die Kurse nach belieben bearbeiten, Punkte verschieben oder löschen und danach speichern.
9. Im Konverter `OSM -> Course` wählen
10. Die gespeicherte OSM-Datei angeben
11. Ein Verzeichnis angeben, in dem die neuen Courseplay-Dateien gespeichert werden sollen. Achtung: Bestehende Dateien
    werden ohne Nachfrage überschrieben! Verwende ein leeres Verzeichnis oder erstelle vorher ein Backup.       

Spenden
-------
Dieses Programm ist kostenlos. Wenn es dir gefällt und du dich bedanken möchtest, kannst du mir jedoch eine Spende
zukommen lassen:
[![](https://www.paypalobjects.com/en_US/DK/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=24ACR27QZT5L6&source=url)

Courseplay to OpenStreetMap (english)
===========================
This program converts courses from the Courseplay format to the OpenStreetMap-Format, so that the courses can be edited
using [JOSM](https://josm.openstreetmap.de/).

Usage
-----
1. Download JAR-File from releases, and execute it.
2. Choose `Course -> OSM` and from the Courseplay-folder of the desired map, choose the file `courseManager.xml`.
3. Enter the size of the map in meters length of the edge. This will later be helpful when adding a background image.
4. Choose an output file. All courses will be written to this file.
5. Download the [Java OpenStreetMap Editor](https://josm.openstreetmap.de/).
6. Open the file generated in step 4. What you see is something like this: 
    
    ![Screenshot nach dem Laden](doc/anzeige-geladen.png)
    The outer square represents the map size that you entered in step 3.
7. Optional: Add a background image (map pda):
    1. In the OSM-Converter choose "Extract Background map"
    2. Choose the `.dds`-File or the `.zip` of the mod. In case of original maps, you can find them in the installation
    directory (e.g.: `Steam\steamapps\common\Farming Simulator 19\sdk\maps\mapD\maps\pda_map_H.dds`)  
    3. Select an output filename. The file while be created (e.g. `map.png`) together with a calibration file (in this
    example `map.png.cal`).
    4. In JOSM go to Edit -> Preferences -> Plugins and install the plugin "PicLayer".
    5. In the menu select Imagery -> Add picture layer from file and select the image created in step 3. The calibration
    file is loaded automatically.
    6. The map should be calibrated correctly. If you still like to adjust it, you can do so as follows:
        1. In the layers panel at the top right, move the white check mark in the green circle on the image layer.
        2. Use the toolbar on the left to adjust the map. See also the
        [manual of PicLayer](https://wiki.openstreetmap.org/wiki/JOSM/Plugins/PicLayer).
8. Edit the courses as desired, then save.
9. In the converter choose `OSM -> Course`
10. Choose the saved OSM-file
11. Choose a directory to save the output to. Any existing files are overwritten without confirmation. Choose an empty
   directory or create a backup of your files before.       

Donations
-------
This application is free of charge. If you like it and want to thank me, you can send me a donation:
[![](https://www.paypalobjects.com/en_US/DK/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=24ACR27QZT5L6&source=url)

