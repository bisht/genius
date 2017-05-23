/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.alivenessmonitor.protocols;

import javax.annotation.concurrent.ThreadSafe;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.urn.opendaylight.genius.alivenessmonitor.rev160411.EtherTypes;

/**
 * Registry of {@link AlivenessProtocolHandler}s.
 *
 * @author Michael Vorburger.ch
 */
@ThreadSafe
public interface AlivenessProtocolHandlerRegistry {

    void register(EtherTypes etherType, AlivenessProtocolHandler protocolHandler);

    @Nullable AlivenessProtocolHandler getOpt(EtherTypes etherType);

    @Nullable AlivenessProtocolHandler getOpt(Class<?> packetClass);

    @NonNull  AlivenessProtocolHandler get(EtherTypes etherType);
}
