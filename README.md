```

 /$$                 /$$       /$$      /$$             /$$               /$$
| $$                | $$      | $$$    /$$$            | $$              | $$
| $$        /$$$$$$ | $$      | $$$$  /$$$$  /$$$$$$  /$$$$$$    /$$$$$$$| $$$$$$$
| $$       /$$__  $$| $$      | $$ $$/$$ $$ |____  $$|_  $$_/   /$$_____/| $$__  $$
| $$      | $$  \ $$| $$      | $$  $$$| $$  /$$$$$$$  | $$    | $$      | $$  \ $$
| $$      | $$  | $$| $$      | $$\  $ | $$ /$$__  $$  | $$ /$$| $$      | $$  | $$
| $$$$$$$$|  $$$$$$/| $$$$$$$$| $$ \/  | $$|  $$$$$$$  |  $$$$/|  $$$$$$$| $$  | $$
|________/ \______/ |________/|__/     |__/ \_______/   \___/   \_______/|__/  |__/



```

## Uruchomienie testowe
Pozwala na uruchomienie wszystkich części poprzez docker-compose. 
```bash
docker-compose up --build
```
### Ważne uwagi
* Żeby wszystko działało poprawnie trzeba dodać statyczne mapowanie DNS na swoim komputerze: `127.0.0.1 keycloak`.
W ten sposób możemy dostać się do zasobów keycloaka po tym samym adresie z poziomu przeglądarki co nasze kontenery
wewnątrz dockera i wszystko działa.
* Uruchomienie może chwilę trwać, bo wszystko uruchamia się w odpowiedniej kolejności i inicjuje przez dłuższą chwilę.
Jeżeli nie ma errorów to po prostu trzeba czekać

## Działanie
Wszystkie endpointy api dla lokalnego developmentu powinny być dostępne przez Api-gateway na porcie 8080, do tego jest
jeszcze keycloak na porcie 8443 po http.  
Przy uruchomieniu lokalnym przed docker-compose w keycloaku, chacie oraz teamsach są tworzeni 4 użytkownicy: bob, rob, ash, user
każdy z takim samym hasłem 1234, których można używać do prostych testów.