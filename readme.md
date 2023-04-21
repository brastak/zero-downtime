## How to update

### Pre-requisites

1. You prepared environment with instructions from `main` branch

### Update service

1. Run load with JMeter
2. Prepare database for migration
   ```shell
   gradle updateToTag \
     -PliquibaseCommandValue=1.1_BEFORE_MIGRATION \
     -Pdb_url=jdbc:postgresql://localhost:32770/postgres \
     -Pdb_password=postgrespw
   ```
3. Build new version of service
   ```shell
   docker build . \
     -f docker/Dockerfile \
     -t zero-dt-app:1.1.0
   ```
4. Update service
   ```shell
   kubectl set image deployment/zero-dt-deployment \
     zero-dt-app=zero-dt-app:1.1.0
   ```
5. Run database migration
   ```shell
   gradle updateToTag \
     -PliquibaseCommandValue=1.1_AFTER_MIGRATION \
     -Pdb_url=jdbc:postgresql://localhost:32770/postgres \
     -Pdb_password=postgrespw
   ```

6. Turn on FEATURE_READ_POST_STATUS feature flag
   ```shell
   curl -X POST \
     -d '{ "enabled": true }' \
     -H "Content-Type: application/json" \
     "localhost:32172/actuator/togglz/FEATURE_READ_POST_STATUS"
   ```

7. Turn on FEATURE_WRITE_POST_STATUS_ONLY feature flag
   ```shell
   curl -X POST \
     -d '{ "enabled": true }' \
     -H "Content-Type: application/json" \
     "localhost:32172/actuator/togglz/FEATURE_WRITE_POST_STATUS_ONLY"
   ```

8. Cleanup database after migration
   ```shell
   gradle update \
     -Pdb_url=jdbc:postgresql://localhost:32770/postgres \
     -Pdb_password=postgrespw
   ```