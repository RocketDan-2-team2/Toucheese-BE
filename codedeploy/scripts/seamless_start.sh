#!/usr/bin/env bash

sudo docker pull toucheeseteam2/toucheese:latest

next_run=8081
last_run=$(cat "$HOME/last_run")

if [ $? -ne 0 ]; then
  echo "First Run"
  last_run=-1
elif [ $last_run -eq 8081 ]; then
  echo "Before Run: 8081"
  next_run=8082
elif [ $last_run -eq 8082 ]; then
  echo "Before Run: 8082"
  next_run=8081
fi

echo "$next_run" > "$HOME/last_run" # 여기까진 문제 없이 된거임

sudo docker run --env-file /home/ubuntu/.env -d --name "toucheese-$next_run" -p "$next_run:8080" toucheeseteam2/toucheese:latest

if [ $last_run -ne -1 ]; then
    sudo rm "/etc/nginx/sites-enabled/app_$last_run"
fi

sudo ln -s "/etc/nginx/sites-available/app_$next_run" "/etc/nginx/sites-enabled/app_$next_run"
sudo systemctl restart nginx.service

if [ $last_run -ne -1 ]; then
  sudo docker stop "toucheese-$last_run"
  sudo docker rm "toucheese-$last_run"
fi

# 컨테이너가 정상적으로 실행 중인지 확인
if ! sudo docker ps | grep "toucheese-$next_run"; then
    echo "Error: Container toucheese-$next_run failed to start."
    exit 1
fi