package DeliveryNow.Api.infrastructure.adapters.out.external;

import DeliveryNow.Api.domain.interfaces.BucketService;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.azure.core.http.rest.*;
import com.azure.core.util.BinaryData;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import com.azure.storage.blob.options.BlobUploadFromFileOptions;
import com.azure.storage.blob.specialized.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;


@Component
public class BucketServiceImpl implements BucketService {
    private final AzureBlobClient azureBlobClient;
    public BucketServiceImpl(AzureBlobClient azureBlobClient) {
        this.azureBlobClient = azureBlobClient;
    }

    @Override
    public String uploadImage(MultipartFile image) {
        return azureBlobClient.uploadImage(image);
    }

    @Override
    public String getBlobUrl(String url) {
        return azureBlobClient.getBlobUrl(url);
    }


}
