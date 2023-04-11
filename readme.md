## How to run

### Pre-requisites

1. Gradle
1. Docker
2. K8S (Docker Desktop used in this README)
3. JMeter

### Prepare DB

```bash
docker run --name zero-dt-db \
  -d \
  --shm-size=1g \
  -e POSTGRES_PASSWORD=postgrespw \
  -p 32770:5432 postgres
```

### Install DB schema

Use port and password from Prepare DB step

```shell
gradle update \
  -Pdb_url=jdbc:postgresql://localhost:32770/postgres \
  -Pdb_password=postgrespw
```

### Build docker image
```shell
docker build . \
  -f docker/Dockerfile \
  -t zero-dt-app:1.0.0
```

### Deploy K8S service

Take a look at DB host - it uses `host.docker.internal` special value

```shell
kubectl create configmap zero-dt-config \
  --from-literal SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:32770/postgres

kubectl create secret generic zero-dt-secret \
  --from-literal SPRING_DATASOURCE_PASSWORD=postgrespw

kubectl apply -f k8s/deployment.yaml

kubectl apply -f k8s/service.yaml
```

### Get K8S service node port

```shell
kubectl get svc zero-dt-service \
  -o=jsonpath='{.spec.ports[].nodePort}'
```

### Run JMeter

1. Copy PostgreSQL JDBC driver to JMeter `lib` directory
2. Open JMeter
3. Load `jmeter/loader.jmx` file
4. Specify parameters:
   1. Test Plan (top level) - `HTTP_PORT` (use K8S node port)
   2. JDBC Connection configuration - `Database URL` and `Password`
5. Run load test. You will get `Unpublish post` errors initially - this
is OK. `select from table tablesample` query is used to get random post
to deactivate. It can return no data when table has just few rows
6. You need to generate significant amount of data ~ 1M rows. You can
increase Publisher thread and decrease Readers to do in quicker

### Update version of the service

1. Switch to `feature/toggled-status` branch. It contains:
   1. Updated application code
   2. Updated liquibase migration scripts
   3. Instructions in `readme.md` how to upgrade service with 
   zero downtime