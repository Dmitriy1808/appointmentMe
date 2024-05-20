COMPOSE="/usr/bin/compose"
PROJECT_HOME="~/appointme/"

cd $PROJECT_HOME
# Команда обновит сертификат без интерактивных действий со стороны пользователя и рестартнет nginx, чтобы новые сертификаты подхватились
$COMPOSE run certbot renew --quite --cert-name appointme.ru && \
$COMPOSE restart nginx
#$COMPOSE kill -s SIGHUP nginx

# 0 0 1 */2 * - крон для crontab полночь каждого первого числа каждые 2 месяца
