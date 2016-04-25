# Rechner Netzte Working Group 3

# Das Protokoll

## Funktionen
 - Es giebt einen Eindeutige Client identifiere 
 - Korekturen für nachrichten können gesendet werden
 - Nachrichten können gesendet werden
   + singel cast
     - Ende zu ende verschlüsselung bei singel cast ist möglich
   + multi cast
 - sequenz numbers von TCP können verwendet werden
 - wer ist wie wo ereichbar (Lookup)


## verwendetet Technologien
 - TCP wird als Transportprotokoll (Peer to Peer) verwendet
 - Link state Routing ?
 - Uniqe identifier für Peer ist der RSA Public key

## Informationen
 - Ein peer identifier ist immer 4096 bit lang 
 - ein multicast Paket wird unverändert an die empfänger peers gesendet
 - 
 
## Paketaufbau
 Es wird TLV  verwendet
 Unterstützte Typen:
 - Sender, Länge ist immer 1 (peer identifier)
 - Empfänger, variable länge > 0 mehrere peer identifier aneinander gehängt
 
