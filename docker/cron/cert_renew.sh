COMPOSE="/usr/bin/compose --no-ansi"

cd /root/https
$COMPOSE run certbot renew --dry-run && \
$COMPOSE kill -s SIGHUP nginx