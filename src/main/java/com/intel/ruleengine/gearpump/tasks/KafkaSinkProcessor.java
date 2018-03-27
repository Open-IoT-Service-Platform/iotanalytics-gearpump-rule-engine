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

import io.gearpump.cluster.UserConfig;
import io.gearpump.cluster.client.ClientContext;
import io.gearpump.streaming.javaapi.Processor;
import io.gearpump.streaming.kafka.KafkaSink;

import com.intel.ruleengine.gearpump.util.LogHelper;
import io.gearpump.cluster.UserConfig;
import org.slf4j.Logger;

import java.util.Properties;
import java.io.*;
import java.util.*;

public class KafkaSinkProcessor {

    public static final String KAFKA_URI_PROPERTY = "KAFKA_URI";
    private final Logger logger = LogHelper.getLogger(KafkaSinkProcessor.class);

    private static String name;

    private final KafkaSink kafkaSink;
    private final ClientContext context;

    public KafkaSinkProcessor(UserConfig userConfig, String name, String topic) {


        this.name = name;
        logger.error("KafkaSinkProcessor : "+topic);

        String serverUri = userConfig.getString(KAFKA_URI_PROPERTY).get();

        Properties kafkaProperties = new Properties();
        kafkaProperties.setProperty("bootstrap.servers", serverUri);

        kafkaSink = new KafkaSink(topic, kafkaProperties);
        context = ClientContext.apply();

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("/sink.txt"));
            out.write("======== "+name+" : "+topic+ " : "+serverUri);
            out.close();
        }
        catch (IOException e)
        {
            System.out.println("Exception ");       
        }
    }

    public Processor getKafkaSinkProcessor(int parallelProcessorNumber) {
                logger.error("getKafkaSinkProcessor name: "+name);

        return Processor.sink(kafkaSink, parallelProcessorNumber, name, UserConfig.empty(), context.system());
    }
}
