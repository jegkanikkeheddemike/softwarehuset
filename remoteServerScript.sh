while true
do
  git reset --hard
  git pull
  /usr/bin/mvn install compile exec:java -Dexec.mainClass=dtu.mennekser.softwarehuset.backend.javadb.networking.DBServer
done