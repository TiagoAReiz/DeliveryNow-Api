package DeliveryNow.Api.domain.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface BucketService {
    String uploadImage(MultipartFile image);
    String getBlobUrl(String url);
}
