/*
 * Copyright (c) 2016 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.intel.ruleengine.gearpump.tasks;

import com.intel.ruleengine.gearpump.tasks.KafkaSinkProcessor;
import io.gearpump.cluster.UserConfig;

public class KafkaSinkHeartbeatProcessor extends KafkaSinkProcessor {

    public static final String KAFKA_TOPIC_PROPERTY = "KAFKA_HEARTBEAT_TOPIC";

    private static final String NAME = "KafkaSinkHeatbeat";

    public KafkaSinkHeartbeatProcessor(UserConfig userConfig) {
    	super(userConfig, NAME, userConfig.getString(KAFKA_TOPIC_PROPERTY).get());
    }
}
