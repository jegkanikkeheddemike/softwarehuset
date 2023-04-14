while true
do
  git reset --hard
  git pull
  /usr/bin/mvn compile exec:java -Dexec.mainClass="dtu.mennesker.softwarehusetas.backend.javadb.networking.DBServer"
done