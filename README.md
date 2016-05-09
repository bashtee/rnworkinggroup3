# RN Chat Protokoll

Beschreibung hier folgt bald 

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

Ein client fragte bei einem server seine client liste an.

### Auslöser
Kein spezieller auslöser, von client implementation abhäng.
 
### Auswirkungen / Antwort

Der Server Schickt ein ClientListResponse



 
