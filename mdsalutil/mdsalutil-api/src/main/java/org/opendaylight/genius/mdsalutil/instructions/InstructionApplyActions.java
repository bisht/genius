/*
 * Copyright © 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.mdsalutil.instructions;

import java.util.List;
import java.util.Objects;
import org.opendaylight.genius.mdsalutil.ActionInfo;
import org.opendaylight.genius.mdsalutil.ActionInfoList;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.ApplyActionsCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.apply.actions._case.ApplyActionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.Instruction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionKey;
import org.opendaylight.yangtools.util.EvenMoreObjects;

/**
 * Apply actions instruction.
 */
public class InstructionApplyActions extends AbstractInstructionInfoImpl {

    private final ActionInfoList actions;

    public InstructionApplyActions(List<ActionInfo> actionsInfos) {
        this.actions = new ActionInfoList(actionsInfos);
    }

    @Override
    public Instruction buildInstruction(int instructionKey) {
        return new InstructionBuilder()
                .setInstruction(new ApplyActionsCaseBuilder()
                        .setApplyActions(new ApplyActionsBuilder()
                                .setAction(actions.buildActions())
                                .build()
                        )
                        .build()
                )
                .setKey(new InstructionKey(instructionKey))
                .build();
    }

    public List<ActionInfo> getActionInfos() {
        return actions.getActionInfos();
    }

    @Override
    protected boolean equals2(Object obj) {
        return EvenMoreObjects.equalsHelper(this, obj,
            (self, other) -> Objects.equals(self.actions, other.actions));
    }

    @Override
    protected int hashCode2() {
        return Objects.hash(actions);
    }

    @Override
    protected String toString2() {
        return "InstructionApplyActions[" + actions + "]";
    }
}
