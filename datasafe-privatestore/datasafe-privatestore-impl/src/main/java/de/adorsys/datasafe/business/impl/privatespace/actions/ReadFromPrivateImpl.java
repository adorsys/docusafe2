package de.adorsys.datasafe.business.impl.privatespace.actions;

import de.adorsys.datasafe.business.api.encryption.document.EncryptedDocumentReadService;
import de.adorsys.datasafe.business.api.types.UserIDAuth;
import de.adorsys.datasafe.business.api.types.action.ReadRequest;

import javax.inject.Inject;
import java.io.InputStream;

public class ReadFromPrivateImpl implements ReadFromPrivate {

    private final EncryptedResourceResolver resolver;
    private final EncryptedDocumentReadService reader;

    @Inject
    public ReadFromPrivateImpl(EncryptedResourceResolver resolver, EncryptedDocumentReadService reader) {
        this.resolver = resolver;
        this.reader = reader;
    }

    @Override
    public InputStream read(ReadRequest<UserIDAuth> request) {
        return reader.read(resolveRelative(request));
    }

    private ReadRequest<UserIDAuth> resolveRelative(ReadRequest<UserIDAuth> request) {
        return request.toBuilder().location(
                resolver.encryptAndResolvePath(request.getOwner(), request.getLocation())
        ).build();
    }
}