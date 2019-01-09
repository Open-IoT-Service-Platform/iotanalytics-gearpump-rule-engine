# Copyright (c) 2015-2019 Intel Corporation
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import json
import os
import pprint
import requests
import time

import oisp

from gearpump_api import GearpumpApi

def load_config_from_env(varname, seen_keys=None):
    """Read OISP config, which is an extended JSON format

    Values starting with @@ or %% are further ENV variables."""
    if seen_keys is None:
        seen_keys = []
    conf = json.loads(os.environ[varname])
    for key, value in conf.items():
        try:
            if value[:2] in ['@@', '%%']:
                assert key not in seen_keys, "Cyclic config"
                seen_keys.append(key)
                conf[key] = load_config_from_env(value[2:], seen_keys[:])
        except TypeError: #value not indexable = not string or unicode
            pass
    return conf


if __name__ == "__main__":
    print("Starting deployment script")
    conf = load_config_from_env("OISP_RULEENGINE_CONFIG")
    rule_engine_jar_name = os.environ["RULE_ENGINE_PACKAGE_NAME"]
    uri = "http://{}/v1/api".format(conf["frontendUri"])
    while True:
        try:
            oisp_client = oisp.Client(uri)
            break
        except requests.exceptions.ConnectionError:
            print("Can not connect to {}, retrying".format(uri))
            time.sleep(1)
    oisp_client.auth(conf["username"], conf["password"])
    token = oisp_client.user_token.value

    pprint.pprint(conf)

    app_conf = {"application_name": "rule_engine_dashboard",
                "dashboard_strict_ssl": False,
                "dashboard_url": "http://{}".format(conf["frontendUri"]),
                "kafka_servers": conf["kafkaConfig"]["uri"],
                "kafka_zookeeper_quorum": conf["zookeeperConfig"]["zkCluster"],
                "kafka_observations_topic": conf["kafkaConfig"]["topicsObservations"],
                "kafka_rule_engine_topic": conf["kafkaConfig"]["topicsRuleEngine"],
                "kafka_heartbeat_topic": conf["kafkaConfig"]["topicsHeartbeatName"],
                "kafka_heartbeat_interval": conf["kafkaConfig"]["topicsHeartbeatInterval"],
                "hadoop_security_authentication": conf["hbaseConfig"]["hadoopProperties"]["hadoop.securit\
y.authentication"],
                "hbase_table_prefix": "local",
                "token": token,
                "zookeeper_hbase_quorum": conf["zookeeperConfig"]["zkCluster"].split(":")[0]
                }

    # We are only interested in port number because we deploy locally
    gearpump_port = conf["uri"].split(":")[1]
    gearpump_api = GearpumpApi(uri="http://localhost:{}".format(gearpump_port),
                               credentials={"username": conf["gearpumpUsername"],
                                            "password": conf["gearpumpPassword"]})
    print("Submitting application '{}' into gearpump ...".format(rule_engine_jar_name))
    gearpump_api.submit_app(filename=rule_engine_jar_name,
                            app_name=app_conf['application_name'],
                            gearpump_app_config=app_conf, force=True)
