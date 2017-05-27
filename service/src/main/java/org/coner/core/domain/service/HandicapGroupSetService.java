package org.coner.core.domain.service;

import java.util.Set;

import javax.inject.Inject;

import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.gateway.HandicapGroupSetGateway;

public class HandicapGroupSetService extends AbstractEntityService<
        HandicapGroupSet,
        HandicapGroupSetAddPayload,
        HandicapGroupSetGateway> {

    private final HandicapGroupEntityService handicapGroupEntityService;

    @Inject
    public HandicapGroupSetService(
            HandicapGroupSetGateway gateway,
            HandicapGroupEntityService handicapGroupEntityService
    ) {
        super(HandicapGroupSet.class, gateway);
        this.handicapGroupEntityService = handicapGroupEntityService;
    }

    @Override
    public HandicapGroupSet add(HandicapGroupSetAddPayload addPayload) throws AddEntityException {
        try {
            for (String handicapGroupId : addPayload.getHandicapGroupIds()) {
                handicapGroupEntityService.getById(handicapGroupId);
            }
        } catch (EntityNotFoundException e) {
            throw new AddEntityException(e);
        }
        return super.add(addPayload);
    }


    public HandicapGroupSet addToHandicapGroups(HandicapGroupSet handicapGroupSet, HandicapGroup handicapGroup) {
        Set<HandicapGroup> handicapGroups = handicapGroupSet.getHandicapGroups();
        if (handicapGroups.contains(handicapGroup)) {
            return handicapGroupSet;
        }
        handicapGroups.add(handicapGroup);
        return gateway.save(handicapGroupSet.getId(), handicapGroupSet);
    }
}
