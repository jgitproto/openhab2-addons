/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.ademco.internal.discovery;

import static org.openhab.binding.ademco.internal.AdemcoConstants.*;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.ademco.internal.config.PartitionBridgeConfiguration;
import org.openhab.binding.ademco.internal.config.ZoneThingConfiguration;
import org.openhab.binding.ademco.internal.handler.EnvisalinkBridgeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for discovering the partition/zone thing.
 *
 * @author Wee-Min Chan - Initial Contribution
 *
 */
public class AdemcoDiscoveryService extends AbstractDiscoveryService {
    private final Logger logger = LoggerFactory.getLogger(AdemcoDiscoveryService.class);
    private EnvisalinkBridgeHandler ademcoEnvisalinkBridge;

    public AdemcoDiscoveryService(EnvisalinkBridgeHandler ademcoBridge) throws IllegalArgumentException {
        super(15);
        this.ademcoEnvisalinkBridge = ademcoBridge;
    }

    /**
     * Activates the Discovery Service.
     */
    public void activate() {
        this.ademcoEnvisalinkBridge.registerDiscoveryService(this);
    }

    /**
     * Deactivates the Discovery Service.
     */
    @Override
    public void deactivate() {
        this.ademcoEnvisalinkBridge.unregisterDiscoveryService();
    }

    public void addParitionThing(Short partitionNumber, @NonNull Bridge envisalinkBridge) {
        String thingID = String.format("Partition%d", partitionNumber);
        logger.debug("Adding {} to inbox", thingID);
        Map<String, Object> properties = new HashMap<>(0);
        ThingUID thingUID = new ThingUID(PARTITION_THING_TYPE, envisalinkBridge.getUID(), thingID);
        properties.put(PartitionBridgeConfiguration.PARTITION_NUMBER, partitionNumber);
        thingDiscovered(DiscoveryResultBuilder.create(thingUID).withProperties(properties)
                .withBridge(envisalinkBridge.getUID()).withLabel(thingID).build());
    }

    public void addZoneThing(Integer zone_index, @NonNull Bridge partitionBridge) {
        String thingID = String.format("Zone%d", zone_index);
        logger.debug("Adding {} to inbox", thingID);
        Map<String, Object> properties = new HashMap<>(0);
        ThingUID thingUID = new ThingUID(ZONE_THING_TYPE, partitionBridge.getUID(), thingID);
        properties.put(ZoneThingConfiguration.ZONE_NUMBER, zone_index);
        thingDiscovered(DiscoveryResultBuilder.create(thingUID).withProperties(properties)
                .withBridge(partitionBridge.getUID()).withLabel(thingID).build());
    }

    @Override
    protected void startScan() {
        // Nothing here
    }
}
