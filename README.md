# DZKv1 (Die zwei Kranken Version 1)

## USECASES:

### **(01) Login**
  - Anmeldung auf dem System bei einem Peer
  - Information an alle, neuer User angemeldet (Broadcast)
  - Server/Peer stellt Liste aller User bereit
  - Server/Peer sendet seinen Namen an den User
  - Peers senden Ihre Namen an den User
  - Sollte nach 10 Sekunden Auf einer verbindung nichts gesendet werden, steht es dem server frei, diese zu schließen 
  - #Propagate
  - #Verteilung der Infratruktur

### **(02) Logout**
  - User meldet sich vom Server/Peer ab
  - Server/Peer informiert alle anderen Peers

### **(03) Message: User to Group**
  - User sendet Nachricht an mehrere User (Gruppe)
  - #An alle möglich (~Broadcast)
  - #beinhaltet User to User
  
### **(04) User: Change Name**
  - User sendet seinen neuen Anzeigenamen an eine Gruppe

## **Fehlerbehandlung:**

### **USECASE(01) - Server does not response after user Joined**
  - Der Server ist für den Userlist request nicht ereichbar -> Fehler, User bekommt keine Informationen über andere Peers
 
### **USECASE(03) - User does not response**
  - Ein nicht erreichbarer User wird aus dem System geworfen
  - #Heartbeat nicht notwendig
  
### **USECASE(04) - User does not response**
  - Ein nicht erreichbarer User wird aus dem System geworfen
  - #Heartbeat nicht notwendig
  
# INFOMATIONEN
  - ip + Port => Anmeldedaten
  - Def. Gruppe 1-alle
  
# Technisches
  - Peer-to-Peer -> Vollvermascht
  - Byteorientiert
  - 32Bit Alignment
  - dynamisch mit commonheader (TLV kodierung)
  
## Aufbau des commonheader 
Der aufbau des commonheader ist wie folgt:

|  8bit   | 8bit           | 16bit      | 32bit     | 16bit       | 16bit             | 
|---------|----------------|------------|-----------|-------------|-------------------| 
| Version |Nachrichten Typ | Reserviert | Sender IP |Sender Port  | Anzahl der Felder |
  
## Aufbau einer Nachricht

| 96 Bit       | x bit  | 
|--------------|--------|
| commonheader | Felder | 
  
### Nachrichten Typen

| Codierung | Nachrichten Typ | Felder(Verflichtend) | Felder(Optional) | Beschreibung |
|-----------|-----------------|----------------------|------------------|--------------|
| 0x01      | Login           | IP, Port             | Name             | Ein User meldet sich beim System an. Der Client wird vom Empfänger in seine Peerlist aufgenommen. Falls eine Änderung in der Liste Auftritt wird der neue User an alle in dieser Liste enthaltenen Peers weitergeleitet. Dadurch erhält ein neu angemeldeter Client alle Adressen der anderen Peers (Er erhält die Login Nachricht welche weiter gereicht wird von jedem anderen Peer -> Absenderadresse). Sollte der Ermpfänger einen Namen haben, so wird eine myName Message an den neu angemeldeten User gesendet. | 
| 0x02      | Logout          | IP, Port             |                  | Der User meldet sich ab. Die Nachricht wird an alle dem Empfänger bekansten User weitergeleitet, sollte der Empfänger durch diese Nachricht eine Änderung an seiner User Liste vornehmen | 
| 0x03      | TextMessage     | Text, UserList       |                  | Eine Textnachricht wird an eine Gruppe von Peers gesendet |
| 0x04      | myName          | Name                 |                  | Gibt den Namen des Senders dem Empfänger bekannt |

### Aufbau eines Feldes 

| 16 Bit    | 16 Bit |
|-----------|--------|
| Kodierung | Laenge |

### Felder Typen

| Kodierung | Name     | Laenge             | Beschreibung             |
|-----------|----------|--------------------|--------------------------|
| 0x01      | IP       | 4 Byte             | Eine IPv4 Adresse        | 
| 0x02      | Port     | 2 Byte             | Ein 16bit Port           | 
| 0x03      | UserList | (IP+Port+2)*x Byte | Siehe Aufbau User List   |
| 0x04      | Name     | 3 < x < 24 Byte    | Ein Name, x Zeichen lang |
| 0x05      | Text     | x Byte             | Freitext, x Zeichen lang |

# Aufbau Userlist
Die Liste besteht aus x aneinander gereihten Elemente, welche je aus 64Byte bestehen. Der Aufbau eines Elementes sieht folgendermaßen aus:

| 32 Bit | 16 Bit     | 16 Bit |
|--------|------------|--------|
| IP     | Reserviert | Port   |
