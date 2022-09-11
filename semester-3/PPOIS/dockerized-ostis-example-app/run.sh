cd /app/ostis-example-app/ostis-web-platform/scripts/

sh run_sctp.sh & # your first application
P1=$!
sh run_scweb.sh & # your second application
P2=$!
wait $P1 $P2
