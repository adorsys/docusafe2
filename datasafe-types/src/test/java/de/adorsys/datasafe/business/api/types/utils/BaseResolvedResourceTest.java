package de.adorsys.datasafe.business.api.types.utils;

import de.adorsys.datasafe.business.api.types.resource.BasePrivateResource;
import de.adorsys.datasafe.business.api.types.resource.BaseResolvedResource;
import de.adorsys.datasafe.business.api.types.resource.PrivateResource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Instant;

@Slf4j
public class BaseResolvedResourceTest {

    @Test
    @SneakyThrows
    public void testResolveMethods() {
        String URI_STRING = "uri";
        URI uri = new URI("uri");
        PrivateResource privateResource = new BasePrivateResource(uri, uri, uri);
        Instant i = null;
        BaseResolvedResource baseResolvedResource = new BaseResolvedResource(privateResource, i);
        Assertions.assertEquals(URI_STRING, baseResolvedResource.resolve(privateResource).location().toString());
        Assertions.assertEquals(URI_STRING, baseResolvedResource.asPrivate().location().toString());
        Assertions.assertEquals(URI_STRING, baseResolvedResource.location().toString());
    }
}