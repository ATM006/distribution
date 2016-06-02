package org.wso2.carbon.event.stream.template.deployer.internal.util;

import org.wso2.carbon.event.execution.manager.core.internal.util.ExecutionManagerConstants;
import org.wso2.carbon.registry.core.RegistryConstants;

public class EventStreamTemplateDeployerConstants {
    public static final String META_INFO_COLLECTION_PATH = ExecutionManagerConstants.DEPLOYER_META_INFO_PATH
                                                           + RegistryConstants.PATH_SEPARATOR + "eventstream";
    public static final String META_INFO_STREAM_NAME_SEPARATER = ",";
}
