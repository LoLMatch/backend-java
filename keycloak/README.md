# keycloak
Żeby zbudować kontener trzeba najpierw skompilować dodatki:
```bash
mvn clean package
```
Potem możemy zrobić obraz:
```bash
docker build -t keycloak .
```
Aktualnie realm jest importowany z pliku `realm.json` szczegóły:
```
konto administratora: admin, hasło: password
realm: lolmatch
clients:
backend - secret: P78fpUt0qVTi5IGUvj2XanwdghgNh7ib
frontend - no secret
users:
bob - password: 1234
```
Powyższa konfiguracja jest testowa i będzie potem zmieniona w odpowiednim momencie.