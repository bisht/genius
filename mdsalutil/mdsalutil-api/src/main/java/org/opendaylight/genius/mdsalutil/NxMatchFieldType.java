/*
 * Copyright (c) 2016 RedHat Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.mdsalutil;

import java.util.Map;

import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.MatchBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowjava.nx.match.rev140421.NxmNxReg;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowplugin.extension.general.rev140714.GeneralAugMatchNodesNodeTableFlow;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowplugin.extension.general.rev140714.GeneralAugMatchNodesNodeTableFlowBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowplugin.extension.general.rev140714.general.extension.grouping.ExtensionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowplugin.extension.general.rev140714.general.extension.list.grouping.ExtensionListBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowplugin.extension.nicira.match.rev140714.NxAugMatchNodesNodeTableFlow;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowplugin.extension.nicira.match.rev140714.NxAugMatchNodesNodeTableFlowBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowplugin.extension.nicira.match.rev140714.NxmNxCtStateKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowplugin.extension.nicira.match.rev140714.nxm.nx.ct.state.grouping.NxmNxCtStateBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowplugin.extension.nicira.match.rev140714.nxm.nx.ct.zone.grouping.NxmNxCtZoneBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.oxm.rev150225.MatchField;

import com.google.common.collect.ImmutableList;

public enum NxMatchFieldType {

    ct_state {
        @Override
        protected Class<? extends MatchField> getMatchType() {
            return NxmNxReg.class;
        }

        @Override
        public void createInnerMatchBuilder(NxMatchInfo matchInfo, Map<Class<?>, Object> mapMatchBuilder) {
            NxmNxCtStateBuilder ctStateBuilder = (NxmNxCtStateBuilder) mapMatchBuilder.get(NxmNxCtStateBuilder.class);

            if (ctStateBuilder == null) {
                ctStateBuilder = new NxmNxCtStateBuilder();
                mapMatchBuilder.put(NxmNxCtStateBuilder.class, ctStateBuilder);
            }

            ctStateBuilder.setCtState(matchInfo.getMatchValues()[0]);
            ctStateBuilder.setMask(matchInfo.getMatchValues()[1]);
        }

        @Override
        public void setMatch(MatchBuilder matchBuilderInOut, MatchInfoBase matchInfo,
                             Map<Class<?>, Object> mapMatchBuilder) {
            NxmNxCtStateBuilder ctStateBuilder = (NxmNxCtStateBuilder) mapMatchBuilder
                    .remove(NxmNxCtStateBuilder.class);

            if (ctStateBuilder != null) {
                NxAugMatchNodesNodeTableFlow nxAugMatch = new NxAugMatchNodesNodeTableFlowBuilder()
                        .setNxmNxCtState(ctStateBuilder.build())
                        .build();
                GeneralAugMatchNodesNodeTableFlow genAugMatch = new GeneralAugMatchNodesNodeTableFlowBuilder()
                        .setExtensionList(ImmutableList.of(new ExtensionListBuilder()
                                                       .setExtensionKey(NxmNxCtStateKey.class)
                                                       .setExtension(new ExtensionBuilder()
                                                       .addAugmentation(NxAugMatchNodesNodeTableFlow.class, nxAugMatch)
                                                                         .build()).build())).build();
                matchBuilderInOut.addAugmentation(GeneralAugMatchNodesNodeTableFlow.class, genAugMatch);
            }
        }
    },
    ct_zone {
        @Override
        protected Class<? extends MatchField> getMatchType() {
            return NxmNxReg.class;
        }

        @Override
        public void createInnerMatchBuilder(NxMatchInfo matchInfo, Map<Class<?>, Object> mapMatchBuilder) {
            NxmNxCtZoneBuilder ctZoneBuilder = (NxmNxCtZoneBuilder) mapMatchBuilder.get(NxmNxCtZoneBuilder.class);

            if (ctZoneBuilder == null) {
                ctZoneBuilder = new NxmNxCtZoneBuilder();
                mapMatchBuilder.put(NxmNxCtStateBuilder.class, ctZoneBuilder);
            }

            ctZoneBuilder.setCtZone((int)matchInfo.getMatchValues()[0]);
        }

        @Override
        public void setMatch(MatchBuilder matchBuilderInOut, MatchInfoBase matchInfo,
                             Map<Class<?>, Object> mapMatchBuilder) {
            NxmNxCtZoneBuilder ctZoneBuilder = (NxmNxCtZoneBuilder) mapMatchBuilder.remove(NxmNxCtZoneBuilder.class);

            if (ctZoneBuilder != null) {
                NxAugMatchNodesNodeTableFlow nxAugMatch = new NxAugMatchNodesNodeTableFlowBuilder()
                        .setNxmNxCtZone(ctZoneBuilder.build())
                        .build();
                GeneralAugMatchNodesNodeTableFlow genAugMatch = new GeneralAugMatchNodesNodeTableFlowBuilder()
                        .setExtensionList(ImmutableList.of(new ExtensionListBuilder()
                                                       .setExtensionKey(NxmNxCtStateKey.class)
                                                       .setExtension(new ExtensionBuilder()
                                                       .addAugmentation(NxAugMatchNodesNodeTableFlow.class, nxAugMatch)
                                                                         .build()).build())).build();
                matchBuilderInOut.addAugmentation(GeneralAugMatchNodesNodeTableFlow.class, genAugMatch);
            }
        }

    };

    public abstract void createInnerMatchBuilder(NxMatchInfo matchInfo, Map<Class<?>, Object> mapMatchBuilder);

    public abstract void setMatch(MatchBuilder matchBuilderInOut, MatchInfoBase matchInfo,
            Map<Class<?>, Object> mapMatchBuilder);

    protected abstract Class<? extends MatchField> getMatchType();

    protected boolean hasMatchFieldMask() {
        // Override this to return true
                return false;
    }

}
