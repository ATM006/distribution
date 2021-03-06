/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package io.siddhi.distribution.editor.core.util.designview.beans.configs.siddhielements.query.input.patternsequence;

import io.siddhi.distribution.editor.core.util.designview.beans.configs.siddhielements.query.input.QueryInputConfig;

import java.util.List;

/**
 * Represents a Pattern | Sequence QueryInputConfig, for Siddhi Query.
 */
public class PatternSequenceConfig extends QueryInputConfig {

    private List<PatternSequenceConditionConfig> conditionList;
    private String logic;

    public PatternSequenceConfig(String type, List<PatternSequenceConditionConfig> conditionList, String logic) {

        super(type);
        this.conditionList = conditionList;
        this.logic = logic;
    }

    public List<PatternSequenceConditionConfig> getConditionList() {

        return conditionList;
    }

    public String getLogic() {

        return logic;
    }
}
