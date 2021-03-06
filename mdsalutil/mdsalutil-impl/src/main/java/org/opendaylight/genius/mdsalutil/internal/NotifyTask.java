/*
 * Copyright (c) 2016, 2017 Ericsson India Global Services Pvt Ltd. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.mdsalutil.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NotifyTask implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(NotifyTask.class);

    @Override
    public void run() {
        LOG.debug("Notify Task is running for the task {}", this);
        synchronized (this) {
            notifyAll();
        }
    }
}
