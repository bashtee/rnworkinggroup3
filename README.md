# RN Chat Protokoll

Dies ist die beschreibung eines peer to peer Protokolles welches im rahmen des RN praktikums implementiert werden soll.
Die daten ereichen die clients per Byte Stream.

Alle Peers müssen auf einem Port eine schnittstelle bereit stellen, gegen welche sich alle anderen verbinden können.
Des weiteren muss ein Peer sich gegen diese schnittstellen aller anderen bekannten Peers verbinden.
Es wird also eine vollvermaschung auf gebaut.


## Use Cases

### Login

Ein Peer meldet sich im Netzwerk an, der Name muss hierbei nicht eindeutig sein, es wird die IP adresse in Komination mit dem Server Port und name verwendet um den client zu identifizieren/ anzuzeigen .

### Update Peer List
Jeder Peer baut eine verbindung, zu allen anderen auf, dazu ist es notwendig eine aktuelle liste (name, IP, Port) Alles Peers zu führen.
Es giebt mehrere mechanismen, welche hierführ genutzt werden können,
   + Get List      - Die ganze liste wird von angefragten Peer zum Anfragesteller übertragen, eine zeitstempel wird ebenfals übermittelt, fals unterstützt
   + Remove Client - Ein Client wird aus der List des Peer ausgetragen, Der Server berbreitet diese anfrage.

### Send Message
Es wird in direkte Verbindung zwischen 2 Peer und eine Nachricht ausgetauscht.




## Aufbau Eines Nachriten Paketes

Ein Paket des Protokolles besteht aus einer Liste von Feldern welcher allerdings in beliebiger reinfolge auftreten können, diese liste kann auch lehr sein.

| Typ (8Bit) | Anzahl Felder(8Bit) | List An Feldern | 
|------------|---------------------|-----------------|


Aufbau eines Feldes 

| Typ (8Bit) | Länge (16Bit) | Daten (Länge Byte)  | 
|------------|---------------|---------------------|


### Unterstützte Nachriten Typen

| ID   | Typ   | Felder (Verpflichtend enthalten) | Felder (optional enthalten) | Beschreibung |
|------|-------|----------------------------------|-----------------------------|--------------|
| 0x01 | login |                                  | Name                        | Ein Peer trägt sich bei einem anderen Peer ein, der Peer verbreitet die informationen über die ereichbarkeit des neuen Peers. |
| 0x02 | propagate | IP, Port                     | Name                        | Informationen über ein neuen Peer werden an andere Peers weiter gegeben. |
| 0x03 | ClientListRequest |                       |                             | Eine anfrage, nach der Aktuellen Peer list eines Peers | 
| 0x04 | ClientListResponse | ClientList          | TimeStemp                   | Die antwort auf eine Peer List Anfrage | 
| 0x05 | Remove Client | IP, Port                 |                             | Ein Peer wird aus einem server ausgetragen |
| 0x06 | Name Request |                           |                             | Die anfrage zur namensauflösung eines teilnehmers |
| 0x07 | Name Response | Name                     |                             | Die antwort auf eine Namensauflösung |                    
| 0x08 | Send Message | Text, TimeStemp           | ClientList	                | Sendet eine text nachricht, Die client list signalisiert CC |
| 0x09 | New Clients Request | TimeStemp          |                             | Erfragt neue Peers seit X, ist dank propagate nicht notwendig |                    
| 0x0A | New Clients Response | TimeStemp ClientList  |                         | eine liste von Peers welche seit X hinzugefügt wurden |



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

Ein Peer meldet sich an einem Peer an, ein name kann Optional mitgesendet werden, dieser kann allerdings auch später über einen Name Request 

### Auslöser

Ein Peer möchte an  der Komunikation teilnehemn, sendet eine login anfrage an einen Peer

### Auswirkungen / Antwort
Der Peer, an welchen die anfrage gestellt wurde nimmt Den anfragesteller in seine Client Liste auf, fals noch nicht enthalten. 
Fals nicht enthalten, sendet er an alle, auch den neuen Peer, ein propagate Paket mit den informationen des Neuen Peer.


## Propagate

Informationen über ein neues Peer werden im netzwerk verbeitet

### Auslöser

 - Ein Login 
 - Der Erhalt einer Propagate nachricht, welche ein Peer enthält welcher nicht in der eigenen client liste enthalten ist.
 
### Auswirkungen / Antwort

Der Peer welcher in der Nachricht beschreiben war wird in die eigene client liste aufgenommen.
Ein Propagte wird an alle Teilnehmer geschicht, welche in  der eigenen client liste enthalten sind.


## ClientListRequest

Ein Peer fragte bei einem anderen Peer Seine Client liste an.

### Auslöser

Kein spezieller auslöser, von Peer implementation abhäng.
 
### Auswirkungen / Antwort

Der Server Schickt ein ClientListResponse


## ClientListResponse

Ein server sendet an einen Client eine Client Liste, welche alle ihm bekanten Peer enthält
ggf. wird ein Timestemp mit geschickt, fals gesendet, kann dieser später verwendet werden um neue Peer seit zeitpunkt X zu erfragen. 
### Auslöser

Ein ClientListRequest
 
### Auswirkungen / Antwort

Der empfänger kann die client liste des servers übernehmen.
Keine Antwort


## Remove Client

### Auslöser

Ein Peer meldet sich ab, oder wird von einem anderen als nicht mehr ereichbar deklariert.
 
### Auswirkungen / Antwort

Sollte das Peer nicht in der eigenen client list enthalten sein, so geschieht nichts und die nachricht wird ignoriert.
Das Peer wird aus der eigenen Client liste ausgetragen und die nachricht an alle anderen Peer, wie ein propagate weiter geleitet.


## Name Request 

Die nachfrage nach dem Namen eines Peers

### Auslöser

Kein spezieller auslöser, von Peer implementation abhäng.
 
### Auswirkungen / Antwort

Ein Name Response wird von dem angefragten Peer gesendet


## Name Response

Die antwort auf einen Name Request, muss keinen Namen enthalten, falls keiner enthalten ist hat der Peer keinen.

### Auslöser

Ein Name Request

### Auswirkungen / Antwort

Der Peer welcher ursprünglich den name Request gesendete hat, kann nun den namen des Peers übernehmen, falls der Peer über einen verfügt




## Send Message 

Ein Peer sendet eine Nachricht an einen anderen Peer

### Auslöser

Kein Spezifischer auslöser von implementation des Peers abhängig

### Auswirkungen / Antwort

Keine antwort, auswirkungen von implementation des Anderen Peers abhängig


## New Clients Request

Die anfrage an einen Server alle neuen clients seit zeitpunkt X in einer Client list zu senden.

### Auslöser

Kein Spezifischer auslöser von implementation des Peer abhängigi

### Auswirkungen / Antwort

Falls unterstützt vom empfänger ein New Clients Response

## New Clients Response

Die antwort auf einen new clients Request, enthält eine liste aller Peer die seit zeitpunkt X sich eingetragen haben und noch in der Clients List des servers enthalten sind.


### Auslöser

ein new Clients Request

### Auswirkungen / Antwort

Keine antwort, der anfragesteller kann seine client liste mit der des servers abgleichen.


# Beschreibung der einzelnden felder

Folgt in kürze
