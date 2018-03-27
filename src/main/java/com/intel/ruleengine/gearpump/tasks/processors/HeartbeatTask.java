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

package com.intel.ruleengine.gearpump.tasks.processors;

import com.google.gson.Gson;
import com.intel.ruleengine.gearpump.tasks.RuleEngineTask;
import io.gearpump.Message;
import com.intel.ruleengine.gearpump.tasks.InvalidMessageTypeException;
import com.intel.ruleengine.gearpump.tasks.messages.controllers.InputMessageParser;
import com.intel.ruleengine.gearpump.util.ConfigHelper;
import io.gearpump.cluster.UserConfig;
import io.gearpump.streaming.javaapi.Processor;
import io.gearpump.streaming.task.StartTime;
import io.gearpump.streaming.task.TaskContext;
import scala.concurrent.duration.FiniteDuration;
import scala.Tuple2;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"checkstyle:illegalcatch", "PMD.AvoidCatchingGenericException"})
public class HeartbeatTask extends RuleEngineTask {

    public static final String KAFKA_HEARTBEAT_INTERVAL_PROPERTY = "KAFKA_HEARTBEAT_INTERVAL";
    private static final String TASK_NAME = "Heartbeat";
    private static final String MESSAGE = "rules-engine";
    private Integer interval;


    public HeartbeatTask(TaskContext context, UserConfig userConfig) {
        super(context, userConfig);

        String heartbeatInterval = ConfigHelper.getConfigValue(userConfig, KAFKA_HEARTBEAT_INTERVAL_PROPERTY);
        if (heartbeatInterval == null) {
            interval = 5000;
        }

        interval = Integer.valueOf(heartbeatInterval);
    }

    @Override
    public void onStart(StartTime startTime) {
        getLogger().info("HeartbeatTask starting...");
        getContext().scheduleOnce(FiniteDuration.create(0, TimeUnit.SECONDS), new SelfTrigger());
    }

    @Override
    public void onNext(Message message) {
        getContext().output(getOutputMessage(message));
        getContext().scheduleOnce(FiniteDuration.create(interval, TimeUnit.MILLISECONDS), new SelfTrigger());
    }

    private Message getOutputMessage(Message message) {
        Object msg = message.msg();

        byte[] key = null;
        byte[] value = null;
        try {
          key = "message".getBytes("UTF-8");
          value = ((String) msg).getBytes("UTF-8");
          Tuple2<byte[], byte[]> tuple = new Tuple2<byte[], byte[]>(key, value);
          return new Message(tuple, now());
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
          return new Message(msg, now());
        }
    }

    public static Processor getProcessor(UserConfig config, int parallelProcessorNumber) {
        return createProcessor(HeartbeatTask.class, config, parallelProcessorNumber, TASK_NAME);
    }

    private class SelfTrigger extends scala.runtime.AbstractFunction0 {
        @Override
        public Object apply() {
            self().tell(new Message(MESSAGE, now()), self());
            return null;
        }
    }

}
