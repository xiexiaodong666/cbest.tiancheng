git pull
mvn clean install

scp ./target/e-welfare-settlement.jar root@172.30.37.188:/home/finance/App/e-welfare-settlement.e-cbest.lotest/release/
echo "sleeping 3 secs"
sleep 3
  ssh root@172.30.37.188 "
chown  -R  finance:finance /home/finance/App/e-welfare-settlement.e-cbest.lotest/release;
sh /home/finance/app-shell/run.sh stop e-welfare-settlement;
sleep 5;
sh /home/finance/app-shell/run.sh start e-welfare-settlement;
"