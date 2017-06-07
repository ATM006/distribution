/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.siddhi.editor.core.internal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.stream.processor.common.EventStreamService;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.query.api.definition.Attribute;

import java.util.List;

/**
 * Class which provides necessary apis for event stream related operations
 */
public class DebuggerEventStreamService implements EventStreamService {
    private static Logger log = LoggerFactory.getLogger(DebuggerEventStreamService.class);

    @Override
    public List<String> getStreamNames(String executionPlanName) {
        DebugRuntime runtimeHolder = EditorDataHolder.getExecutionPlanMap().get(executionPlanName);
        if (runtimeHolder != null) {
            return runtimeHolder.getStreams();
        } else {
            log.error("Execution Plan with name : " + executionPlanName + " is not available");
        }
        return null;
    }

    @Override
    public List<Attribute> getStreamAttributes(String executionPlanName, String streamName) {
        DebugRuntime runtimeHolder = EditorDataHolder.getExecutionPlanMap().get(executionPlanName);
        if (runtimeHolder != null) {
            return runtimeHolder.getStreamAttributes(streamName);
        } else {
            log.error("Execution Plan with name : " + executionPlanName + " is not available");
        }
        return null;
    }

    @Override
    public void pushEvent(String executionPlanName, String streamName, Event event) {
        DebugRuntime runtimeHolder = EditorDataHolder.getExecutionPlanMap().get(executionPlanName);
        if (runtimeHolder != null) {
            try {
                runtimeHolder.getInputHandler(streamName).send(event);
            } catch (Exception e) {
                log.error("Error when pushing events to Siddhi debugger engine ", e);
            }
        }
    }
}
