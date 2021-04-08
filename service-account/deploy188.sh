#mvn clean install
cd ..
git pull
#mvn clean install -DskipTests
cd ./service-account || exit
scp ./target/e-welfare-account.jar root@172.30.37.188:/home/finance/App/e-welfare-account.e-cbest.lotest/release/
echo "sleeping 3 secs"
sleep 3
  ssh root@172.30.37.188 "
chown  -R  finance:finance /home/finance/App/e-welfare-account.e-cbest.lotest/release;
sh /home/finance/app-shell/run.sh stop e-welfare-account;
sleep 5;
sh /home/finance/app-shell/run.sh start e-welfare-account;
tail -f /home/finance/Logs/e-welfare-account.e-cbest.lotest/console.log
"