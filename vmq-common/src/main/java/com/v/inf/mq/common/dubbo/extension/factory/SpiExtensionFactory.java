/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.v.inf.mq.common.dubbo.extension.factory;

import com.v.inf.mq.common.dubbo.extension.ExtensionFactory;
import com.v.inf.mq.common.dubbo.extension.ExtensionLoader;
import com.v.inf.mq.common.dubbo.extension.SPI;

/**
 * Apache Dubbo (incubating)
 * Copyright 2018-2019 The Apache Software Foundation
 * Modifications copyright (C) 2019 <Wayne>
 * <p>
 * This product includes software developed at
 * The Apache Software Foundation (http://www.apache.org/).
 * <p>
 * SpiExtensionFactory
 */
public class SpiExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            ExtensionLoader<T> loader = ExtensionLoader.getExtensionLoader(type);
            if (!loader.getSupportedExtensions().isEmpty()) {
                return loader.getAdaptiveExtension();
            }
        }
        return null;
    }

}
