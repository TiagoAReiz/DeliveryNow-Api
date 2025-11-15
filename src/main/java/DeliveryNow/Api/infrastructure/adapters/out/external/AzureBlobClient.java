package DeliveryNow.Api.infrastructure.adapters.out.external;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class AzureBlobClient {

    private final BlobServiceClient blobServiceClient;
    private final BlobContainerClient blobContainerClient;
    public AzureBlobClient(
            @Value("${storage.account-name}") String accountName,
            @Value("${storage.account-key}") String accountKey,
            @Value("${storage.blob-endpoint}") String endpoint
    ) {


        StorageSharedKeyCredential credential =
                new StorageSharedKeyCredential(accountName, accountKey);

        DefaultAzureCredential credential1 = new DefaultAzureCredentialBuilder().build();

        this.blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(endpoint)
                .credential(credential)
                .buildClient();

        String containerName = "quickstartblobs";
        this.blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        if (!blobContainerClient.exists()) {
            blobContainerClient.create();}
    }

    public String uploadImage(MultipartFile image) {
        try {

            String blobName = UUID.randomUUID() + "_" + image.getOriginalFilename();

            BlobClient blob = blobContainerClient.getBlobClient(blobName);

            blob.upload(image.getInputStream());

            return blobName;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage(), e);
        }
    }
    public String getBlobUrl(String blobName) {
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
        BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);

        OffsetDateTime startTime = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime expiryTime = startTime.plusHours(1);

        BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                .setStartTime(startTime);

        String sasToken = blobContainerClient.generateSas(sasSignatureValues);

        return blobClient.getBlobUrl() + "?" + sasToken;

    }

}
