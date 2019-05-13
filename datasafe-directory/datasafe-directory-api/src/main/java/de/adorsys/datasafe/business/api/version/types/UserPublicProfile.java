package de.adorsys.datasafe.business.api.version.types;

import de.adorsys.datasafe.business.api.version.types.resource.AbsoluteResourceLocation;
import de.adorsys.datasafe.business.api.version.types.resource.PublicResource;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class UserPublicProfile {

    @NonNull
    private final AbsoluteResourceLocation<PublicResource> publicKeys;

    @NonNull
    private final AbsoluteResourceLocation<PublicResource> inbox;
}