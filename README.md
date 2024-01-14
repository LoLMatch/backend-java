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

### Turn me on

```bash
mvn clean install
cd api-gateway
docker build -t lm-api-gateway .
cd ..
docker-compose up
```

### Test purpose config


```bash
realm: lolmatch

keycloak
login: admin
password: admin

client_id: lolmatch-frontend
client_secret: CuLxCfsHCj0ZDuoPcOO79wH07S2SaFS2

client_id: admin-cli
client_secret: wgXrhNpBogS5mcwNDv2Anz2cLx5Gq1Bk

There is one user (there are no groups at the moment)
login: bob
password: bob
```



