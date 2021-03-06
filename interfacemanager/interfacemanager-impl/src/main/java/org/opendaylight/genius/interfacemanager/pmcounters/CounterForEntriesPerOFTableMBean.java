/*
 * Copyright (c) 2016, 2017 Ericsson India Global Services Pvt Ltd. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.interfacemanager.pmcounters;

import java.util.Map;

public interface CounterForEntriesPerOFTableMBean {
    // -----------
    // operations
    // -----------
    void setCounterDetails(Map<String, Integer> map);

    Map<String, Integer> getCounterDetails();

    // -----------
    // attributes
    // -----------
    void invokePMManagedObjects(Map<String, Integer> map);

    Map<String, String> retrieveCounterMap();
}
