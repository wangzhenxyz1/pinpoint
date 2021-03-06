/*
 * Copyright 2017 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.web.service.map;

import com.navercorp.pinpoint.web.security.ServerMapDataFilter;
import com.navercorp.pinpoint.web.service.LinkDataMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HyunGil Jeong
 */
@Component
public class LinkSelectorFactory {

    private final LinkDataMapService linkDataMapService;

    private final ApplicationsMapCreatorFactory applicationsMapCreatorFactory;

    private final ServerMapDataFilter serverMapDataFilter;

    @Autowired(required = false)
    public LinkSelectorFactory(
            LinkDataMapService linkDataMapService,
            ApplicationsMapCreatorFactory appliationsMapCreatorFactory) {
        this(linkDataMapService, appliationsMapCreatorFactory, null);
    }

    @Autowired(required = false)
    public LinkSelectorFactory(
            LinkDataMapService linkDataMapService,
            ApplicationsMapCreatorFactory appliationsMapCreatorFactory,
            ServerMapDataFilter serverMapDataFilter) {
        this.linkDataMapService = linkDataMapService;
        this.applicationsMapCreatorFactory = appliationsMapCreatorFactory;
        this.serverMapDataFilter = serverMapDataFilter;
    }

    public LinkSelector create(LinkSelectorType linkSelectorType) {
        VirtualLinkMarker virtualLinkMarker = new VirtualLinkMarker();
        VirtualLinkProcessor virtualLinkProcessor = new VirtualLinkProcessor(linkDataMapService, virtualLinkMarker);
        ApplicationsMapCreator applicationsMapCreator = applicationsMapCreatorFactory.create(virtualLinkMarker);
        if (linkSelectorType == LinkSelectorType.UNIDIRECTIONAL) {
            return new UnidirectionalLinkSelector(applicationsMapCreator, virtualLinkProcessor, serverMapDataFilter);
        } else {
            return new BidirectionalLinkSelector(applicationsMapCreator, virtualLinkProcessor, serverMapDataFilter);
        }
    }
}
