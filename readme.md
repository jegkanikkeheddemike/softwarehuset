# Softwarehuset
## Hvordan man kører applikationen
Applikationen er et maven projekt, hvor /AppMain.java er main klassen.
Som standard vil programmet forsøge at forbinde serveren som er hostet på
en VM i tyskland. Denne server burde gerne være fyldt med brugere, 
projekter og aktiviteter som kan leges med.

## Hvordan man bruger applikationen
### Home
Efter at have logget ind som en af de viste brugere, kommer man til en 
"home" side, hvor de mest brugte funktionaliteter kan findes. Til venstre 
kan der vælges et eksisterende projekt eller man kan oprette et nyt.
### Projekt
Under et projekt kan man oprette og se aktiviteter. Til højre kan det ses
hvilke medarbejdere som er "assigned" til projektet. Man kan også tilføre 
nye medarbejdere til projektet. Hvis der vælges en aktivitet kommer man
til aktivitetens side.

#### Manage project
Hvis man ikke er projektleder af det valgte projekt, og der ikke allerede 
er en projektleder, kan der trykkes på knappen "become project leader". 
Det gør den nuværende logget ind medarbejder til projektlederen. Hvis man
er projektleder kan der trykkes på knappen "manage project". Hvis man
trykker på knappen kommer man til en side hvor der kan ses alle de
aktiviteter der er i projektet, alle de aktiviteter som medarbejder der er
i projektet er en del af (aktiviteter fra andre projekter) og
medarbejdernes ferie / sygemeldinger. Her kan der let redigeres i 
aktiviteter ved at trykke på dem. Der kan også ses hvor lang tid der er
tilbage i projektet.

### Aktiviteter
Under en aktivitet kan man ændre i beskrivelsen af aktiviteten. Man kan 
i stil med projektet også ændre hvilke medarbejdere som er "assigned" til 
aktiviteten. Der kan også ses og oprettes tidsregistreringer i aktiviteten 
her. 

## Selfhosting
Hvis man ikke har lyst til at forbinde til serveren, er det muligt at 
selfhoste serveren. Dette kæver tre tring:
1. Ændre AppSettings.remoteLocation til "localhost"
2. Spin op en lokal version af serveren. Dette gøres ved at køre 
ServerMain.java filen. Det kan eventuelt også køres 
```
mvn clean install compile exec:java -Dexec.mainClass=dtu.mennekser.softwarehuset.ServerMain -Dmaven.test.skip=true
```
3. Mens serveren stadig kører skal der køres java klassen 
/backend/actions/fillWithJunkData.java. Dette fylder serveren med brugere.

Efter de tre trin er blevet kørt, og den lokale server stadig kører vil AppMain
forbinde til den lokale server i stedet for serveren i tyskland.
