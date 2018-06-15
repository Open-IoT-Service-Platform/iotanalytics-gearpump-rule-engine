source /app/target/classes/resources/version.properties &&
export RULE_ENGINE_PACKAGE_NAME=gearpump-rule-engine-${VERSION}-jar-with-dependencies.jar &&

/app/wait-for-it.sh localhost:8090 -t 300 -- /app/local-deploy.sh &

/gearpump-2.11-0.8.0/bin/local & /gearpump-2.11-0.8.0/bin/services
