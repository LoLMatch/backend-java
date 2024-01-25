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
* Uruchomienie może chwilę trwać bo wszystko uruchamia się w odpowiedniej kolejności i inicjuje przez dłuższą chwilę.
Jeżeli nie ma errorów to po prostu trzeba czekać

## Działanie
Można testować 2 endpointy:
* publiczny `http://localhost:8080/api/lolmatch/public` - dostęp po prostu
* prywatny `http://localhost:8080/api/lolmatch/private` - trzeba dodać token uzyskany od keycloaka do zapytania.  
Szczegóły konfiguracji keycloaka są w README.md w folderze keycloak