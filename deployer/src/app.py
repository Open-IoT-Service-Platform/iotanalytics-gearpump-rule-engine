# Copyright (c) 2015-2018 Intel Corporation
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
"""Deploy gearpump application locally

Application is a jar located at $RULE_ENGINE_PACKAGE_NAME,
Gearpump must run locally, port number is read from $GEARPUMP,
which stores a whole URL.
"""
import os
import time

import kafka

from gearpump_api import GearpumpApi
import cloudfoundry_bridge

KAFKA_BROKER_TIMEOUT = 300

def wait_for_frontend():
    """Wait for OISP frontend hearbeat."""
    kafka_server = os.environ["KAFKA"]
    heartbeat_topic = os.environ["KAFKA_HEARTBEAT_TOPIC"]
    print("Connecting to kafka at {}, topic: {} ".format(kafka_server, heartbeat_topic))
    for _ in range(KAFKA_BROKER_TIMEOUT):
        try:
            consumer = kafka.KafkaConsumer(heartbeat_topic, bootstrap_servers=kafka_server,
                                           auto_offset_reset='latest')
            break
        except kafka.errors.NoBrokersAvailable:
            print("No kafka brokers available, trying again")
            time.sleep(1)

    print("Connected to kafka")

    for message in consumer:
        # Frontend heartbeat message is dashboard for historical reasons
        if message.value == "dashboard":
            print("Time, dashboard message ts:", time.time(), message.timestamp)
            break

    print("Frontend is up")


def main():
    """Deploy app to local gearpump instance."""
    rule_engine_jar_name = os.environ['RULE_ENGINE_PACKAGE_NAME']

    # Cloudfoundry needs frontend
    wait_for_frontend()
    cloud_bridge = cloudfoundry_bridge.CloudfoundryBridge()
    config = cloud_bridge.build_config(local=True)

    # We are only interested in port number because we deploy locally
    gearpump_port = os.environ["GEARPUMP"].split(":")[1]
    gearpump_api = GearpumpApi(uri="http://localhost:{}".format(gearpump_port),
                               credentials=cloud_bridge.gearpump_credentials)
    print("Submitting application '{}' into gearpump ...".format(rule_engine_jar_name))
    gearpump_api.submit_app(filename=rule_engine_jar_name,
                            app_name=config['application_name'],
                            gearpump_app_config=config, force=True)


if __name__ == "__main__":
    main()
