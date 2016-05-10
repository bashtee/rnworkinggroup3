# RN Chat Protokoll

Dies ist die beschreibung eines peer to peer Protokolles welches im rahmen des RN praktikums implementiert werden soll.


## Use Cases

### Login
Ein Client meldet sich beim server an, der Name muss hierbei nicht eindeutig sein, es wird die IP adresse in Komination mit Port und name verwendet um den client zu identifizieren/ anzuzeigen .

### Update Client List
Jeder Client baut eine verbindung, zu jedem anderen auf, dazu ist es notwendig eine aktuelle liste (name, IP, Port) Alles Clients zu führen.
Es giebt mehrere mechanismen, welche hierführ genutzt werden können,
   + Get List      - Die ganze liste wird von dem server zum anfragesteller übertragen, eine zeitstempel wird ebenfals übermittelt
   + Remove Client - Ein Client wird aus der List des servers ausgetragen, Der Server berbreitet diese anfrage.

### Send Message
Es wird eine direkte verbindung zwischen 2 clients aufgebaut und eine nachricht ausgetauscht.




## Aufbau Eines Nachriten Paketes

Ein Paket des Protokolles besteht aus einer Liste von Feldern welcher allerdings in beliebiger reinfolge auftreten können.

| Typ (8Bit) | Anzahl Felder(8Bit) | List An Feldern | 
|------------|---------------------|-----------------|


Aufbau eines Feldes 

| Typ (8Bit) | Länge (16Bit) | Daten (Länge Byte)  | 
|------------|---------------|---------------------|


### Unterstützte Nachriten Typen

| ID   | Typ   | Felder (Verpflichtend enthalten) | Felder (optional enthalten) | Beschreibung |
|------|-------|----------------------------------|-----------------------------|--------------|
| 0x01 | login |                                  | Name                        | Ein Client trägt sich In einem server ein, der Server verbreitet die informationen über die ereichbarkeit des Clients. |
| 0x02 | propagate | IP, Port                     | Name                        | Informationen über ein neuen client werden an andere Server weiter gegeben. |
| 0x03 | ClientListRequest |                       |                             | Eine anfrage, nach der Aktuellen Client list eines servers | 
| 0x04 | ClientListResponse | ClientList          | TimeStemp                   | Die antwort auf eine Client List Anfrage des servers | 
| 0x05 | Remove Client | IP, Port                 |                             | Ein Client wird aus einem server ausgetragen |
| 0x06 | Name Request |                           |                             | Die anfrage zur namensauflösung eines teilnehmers |
| 0x07 | Name Response | Name                     |                             | Die antwort auf eine Namensauflösung |                    
| 0x08 | Send Message | Text, TimeStemp           | ClientList	                | Sendet eine text nachricht, Die client list signalisiert CC |
| 0x09 | New Clients Request | TimeStemp          |                             | Erfragt neue Clients seit X, ist dank propagate nicht notwendig |                    
| 0x0A | New Clients Response | TimeStemp ClientList  |                         | eine liste von clients welche seit X hinzugefügt wurden |



### Unterstützte Feld Typen


| ID   | Bedeutung   | Länge   | Beschreibung |
|------|-------------|---------|--------------|
| 0x01 | IP          | 4       | Eine IP adresse Hat immer die länge 4 Byte |
| 0x02 | Port        | 2 	   | Die port nummer wird immer in einem 16 bit Integer(unsigniert) abgelegt |
| 0x03 | Name        | 3<L<32  | der anzeige name eines Clients L ist die Länge deies Namens |
| 0x04 | ClientList  | X * (IP + Port) | X ist die Anzahl der Clients für jeden Client wird sowohl die IP Adresse Alsauch der Port Üvertragen |
| 0x05 | TimeStemp   | 8       | Ein 64 bit unix timestemp  | 
| 0x06 | Text        | X       | Freitext , beliebige länge |

# Genaueres zu den einzelnen Nachriten Typen

## Login 

Ein Client meldet sich an einem server an, ein name kann Optional mitgesendet werden, dieser kann allerdings auch später über einen Name Request 

### Auslöser

Ein Client möchte an  der Komunikation teilnehemn, sendet eine login anfrage an den server

### Auswirkungen / Antwort
Der Server, an welchen die anfrage gestellt wurde nimmt Den client in seine Client Liste auf, fals noch nicht enthalten. 
Er sendet an alle, auch den neuen client, ein propagate Paket mit den informationen des Neuen Clients.


## Propagate

Informationen über ein neues Client werden im netzwerk verbeitet

### Auslöser

 - Ein Login 
 - Der Erhalt einer Propagate nachricht, welche ein Client enthält welcher nicht in der eigenen client liste enthalten ist.
 
### Auswirkungen / Antwort

Der Client welcher in der Nachricht beschreiben war wird in die eigene client liste aufgenommen.
Ein Propagte wird an alle Teilnehmer geschicht, welche in  der eigenen client liste enthalten sind.


## ClientListRequest

Ein Client fragte bei einem Server Seine Client liste an.

### Auslöser

Kein spezieller auslöser, von Client implementation abhäng.
 
### Auswirkungen / Antwort

Der Server Schickt ein ClientListResponse


## ClientListResponse

Ein server sendet an einen Client eine Client Liste, welche alle ihm bekanten clients enthält
ggf. wird ein Timestemp mit geschickt, fals gesendet, kann dieser später verwendet werden um neue Clients seit zeitpunkt X zu erfragen. 
### Auslöser

Ein ClientListRequest
 
### Auswirkungen / Antwort

Der empfänger kann die client liste des servers übernehmen.
Keine Antwort


## Remove Client

### Auslöser

Ein Client meldet sich ab, oder wird von einem anderen als nicht mehr ereichbar deklariert.
 
### Auswirkungen / Antwort

Sollte das Client nicht in der eigenen client list enthalten sein, so geschieht nichts und die nachricht wird ignoriert.
Das client wird aus der eigenen Client liste ausgetragen und die nachricht an alle anderen Clients, wie ein propagate weiter geleitet.


## Name Request 

Die nachfrage nach dem Namen eines Clients

### Auslöser

Kein spezieller auslöser, von Client implementation abhäng.
 
### Auswirkungen / Antwort

Ein Name Response wird von dem angefragten client gesendet


## Name Response

Die antwort auf einen Name Request, muss keinen Namen enthalten, falls keiner enthalten ist hat das Client keinen.

### Auslöser

Ein Name Request

### Auswirkungen / Antwort

Der Client welcher ursprünglich den name Request gesendete hat, kann nun den namen des Clients übernehmen, falls das client über einen verfügt




## Send Message 

Ein Client sendet eine Nachricht an einen anderen Client

### Auslöser

Kein Spezifischer auslöser von implementation des Client abhängigi

### Auswirkungen / Antwort

Keine antwort, auswirkungen von implementation des Anderen Clients abhängig


## New Clients Request

Die anfrage an einen Server alle neuen clients seit zeitpunkt X in einer Client list zu senden.

### Auslöser

Kein Spezifischer auslöser von implementation des Client abhängigi

### Auswirkungen / Antwort

Falls unterstützt vom empfänger ein New Clients Response

## New Clients Response

Die antwort auf einen new clients Request, enthält eine liste aller clients die seit zeitpunkt X sich eingetragen haben und noch in der Clients List des servers enthalten sind.


### Auslöser

ein new Clients Request

### Auswirkungen / Antwort

Keine antwort, der anfragesteller kann seine client liste mit der des servers abgleichen.


# Beschreibung der einzelnden felder

Folgt in kürze
