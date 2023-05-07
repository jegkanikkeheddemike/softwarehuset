# streamDB er navnet på denne database.

Det virker ved at når Clienter forbinder til databasen, siger de om de vil:
1. Lytte til en query (Listener)
elller
2. Opdate state i databasen. (Task)

Bemærk at alle Querys er lambdafunktioner som tager databasens state (DataLayer) som argument, og returner den
værdi som vil findes (Query). Ligende sker alle opdateringer af state (Task) som en lambdafunktion der tager
state (DataLayer) som arguement, og mutere staten.
Disse lambdafunktioner sender over en tcpforbindelse.

Hele pointen med denne database er at en Query kan ligge i en længere periode og lytte til en Query, hvor hver
gang DataLayers state ændres, testes det om Query'en returnere det samme. Hvis Query'en ikke returner det samme
som sidste gang bliver det nye resultat sendt til clienten.

Clienten har så et callback som beskriver hvad der skal ske hver gang den får data. Ved at gøre dette kan man
lave en reaktiv GUI som automatisk opdatare når den data den repræsentere ændrer sig.

StreamDB er bygget op af tre lag:
1. SocketLayer
2. ExecutionLayer
3. DataLayer

Socketlayer er ansvarlig for at modtage tcp forbindelser og parse deres queries. Når den parser en listener/task
bliver det sendt videre til ExecutionLayer

ExecutionLayer har en liste af alle aktive listernes. Når den får en ny listener bliver dens query kørt, hvorefter
det er listeners ansvar et sende dataen til clienten.
Når ExecutionLayer modtager et Task, kører en det task på DataLayer, hvilket opdatere dets state. Derefter bliver ALLE
aktive listeners kørt igen, hvor den hvis resultat ændrer sig sender den nye data til clienten.

Når en listener bliver denkørt tester den også om forbindelsen stadig er aktiv. Hvis forbindelsen ikke længere er
levende, gør den ExecutionLayer opmærksom på det, og den bliver fjernet fra listen af aktive listeners.

DataLayer er en instans af et objekt som extender DataLayer klassen. Der bruges her Generics for at garantere Type Safety
Det betyder også at SocketLayer og ExecutionLayer er 100% ligeglade med hvilken type DataLayer er, og StreamDB er derfor
100% afkoblet fra business logikken. Derfor er det derfor blevet valgt at teste dem seperat.


