/*
 * Copyright (c) 2015 Ericsson India Global Services Pvt Ltd. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.itm.listeners.cache;

import org.opendaylight.controller.md.sal.binding.api.ClusteredDataChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataChangeListener;
import org.opendaylight.controller.md.sal.common.api.data.AsyncDataBroker;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.genius.datastoreutils.AsyncClusteredDataChangeListenerBase;
import org.opendaylight.genius.itm.globals.ITMConstants;
import org.opendaylight.genius.utils.cache.DataStoreCache;
import org.opendaylight.yang.gen.v1.urn.opendaylight.genius.itm.config.rev160406.TunnelMonitorInterval;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by edimjai on 8/4/2016.
 */
public class ItmMonitoringIntervalListener extends AsyncClusteredDataChangeListenerBase<TunnelMonitorInterval, ItmMonitoringIntervalListener>
    implements AutoCloseable {

  private ListenerRegistration<DataChangeListener> listenerRegistration;

  private static final Logger logger = LoggerFactory.getLogger(ItmMonitoringIntervalListener.class);

  public ItmMonitoringIntervalListener(final DataBroker broker) {
    super(TunnelMonitorInterval.class, ItmMonitoringIntervalListener.class);

    try {
      listenerRegistration = broker.registerDataChangeListener(LogicalDatastoreType.OPERATIONAL,
          getWildCardPath(), this,
          AsyncDataBroker.DataChangeScope.BASE);
    } catch (final Exception e) {
      logger.error("ItmMonitoring Interval listener registration fail!", e);
    }
  }

  @Override
  public void close() {
    if (listenerRegistration != null) {
      try {
        listenerRegistration.close();
      } catch (final Exception e) {
        logger.error("Error when cleaning up DataChangeListener.", e);
      }
      listenerRegistration = null;
    }
    logger.info("ItmMonitoring Interval listener Closed");
  }

  @Override
  protected void remove(InstanceIdentifier<TunnelMonitorInterval> key, TunnelMonitorInterval dataObjectModification) {
    DataStoreCache.remove(ITMConstants.ITM_MONIRORING_PARAMS_CACHE_NAME, "Interval");

  }

  @Override
  protected void update(InstanceIdentifier<TunnelMonitorInterval> identifier, TunnelMonitorInterval original,
                        TunnelMonitorInterval update) {
    DataStoreCache.add(ITMConstants.ITM_MONIRORING_PARAMS_CACHE_NAME, "Interval", update);
  }

  @Override
  protected void add(InstanceIdentifier<TunnelMonitorInterval> identifier, TunnelMonitorInterval add) {
    DataStoreCache.add(ITMConstants.ITM_MONIRORING_PARAMS_CACHE_NAME, "Interval", add);
  }

  @Override protected InstanceIdentifier<TunnelMonitorInterval> getWildCardPath() {
    return InstanceIdentifier.create(TunnelMonitorInterval.class);
  }

  @Override
  protected ClusteredDataChangeListener getDataChangeListener() {
    return this;
  }

  @Override
  protected AsyncDataBroker.DataChangeScope getDataChangeScope() {
    return AsyncDataBroker.DataChangeScope.BASE;
  }

}



