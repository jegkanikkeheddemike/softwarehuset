while true
do
  git reset --hard
  git pull
  /usr/bin/mvn clean install compile exec:java -Dexec.mainClass=dtu.mennekser.softwarehuset.ServerMain -Dmaven.test.skip=true
done