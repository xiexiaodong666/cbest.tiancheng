git pull
cd ..
mvn clean install -DskipTests
cd ./service-merchant ||exit
scp ./target/e-welfare-merchant.jar root@172.30.37.130:/home/finance/App/e-welfare-merchant.e-cbest.lotest/release/
echo "sleeping 3 secs"
sleep 3
  ssh root@172.30.37.130 "
chown  -R  finance:finance /home/finance/App/e-welfare-merchant.e-cbest.lotest/release;
sh /home/finance/app-shell/run.sh stop e-welfare-merchant;
sleep 5;
sh /home/finance/app-shell/run.sh start e-welfare-merchant;
"