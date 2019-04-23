package de.adorsys.datasafe.business.api.types.profile;

import de.adorsys.datasafe.business.api.types.resource.PrivateResource;
import de.adorsys.datasafe.business.api.types.resource.PublicResource;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class UserPublicProfile {

    @NonNull
    private final PublicResource publicKeys;

    @NonNull
    private final PublicResource inbox;
}
