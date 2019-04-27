# Energize
### A game by Jacob Allen and Matt Eden
###### Created for COMPSYS 302 at the University of Auckland

Energize is a side-scrolling action adventure game set in the far future. It follows the quest of the main character Otto and their journey to recover the McGuffin from Evil Bad Guy.


#### Compile instructions - Eclipse
1. File > Open Projects from File System.
2. Locate the project and Finish.
3. Project > Properties.
4. Java Build Path > Add Folder > Select src and resources (If not allready selected) > OK > Apply.
5. Run/Debug Settings > New > Java Application > OK.
6. Name apropriatly. Set Main class as Main-sample > OK > Apply.
7. File > Export > Java > Runnable JAR file.
8. Set Launch configuration to the configuration made in step 6.
9. Give an apropriate name and location.
10. Select Package required libraries into generated JAR from Library handling > Finish.


#### Launch instructions
##### For single / local multiplayer:
Open a new Terminal window `ctrl + alt + t` and enter the comand `java -jar <path>/2019-Java-Group34.jar`

##### For networked multiplayer:
On both the host and client computer run the comand `java -jar <path>/2019-Java-Group34.jar [<host_ip_address>][<port_number>]`.

`host_ip_address` Default value is localhost. For networked multiplayer run `hostname -I` on the host computer to find the ip address.</br>
-If there are multiple addresses displayed pick the one that only consists of numbers (usualy in the form 192.168.##.##).

`port_number` Defauly value is 4200. Only change this if receiving `*There is already a host at this address. Joining that game instead*` error message

On the host computer, click "Host Game", while on any connecting computers click "Join Game"
