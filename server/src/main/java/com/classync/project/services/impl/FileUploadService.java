package com.classync.project.services.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class FileUploadService {

    private static final String BUCKET_NAME = "doc-scheduler-6e8de.appspot.com";
    private static final String ASSIGNMENT_FOLDER_NAME = "assignments/";
    private static final String TEACHER_SOLUTION_FOLDER_NAME = "teacher_solutions/";
    private static final String SUBMISSION_FOLDER_NAME = "submissions/";
    private static final String ANNOUNCEMENT_FOLDER_NAME = "announcements/";
    private static final String DOWNLOAD_URL_FORMAT = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media";

    public String uploadFile(MultipartFile file, String fileName, String folder) throws IOException {
        String filePath = folder + fileName;
        BlobId blobId = BlobId.of(BUCKET_NAME, filePath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

        InputStream credentialsStream = getClass().getClassLoader()
                .getResourceAsStream("classync-firebase-storage-private-key.json");
        if (credentialsStream == null) {
            throw new IOException("Firebase credentials file not found");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        try (InputStream fileInputStream = file.getInputStream()) {
            storage.create(blobInfo, fileInputStream);
        }

        return String.format(DOWNLOAD_URL_FORMAT, BUCKET_NAME, URLEncoder.encode(filePath, StandardCharsets.UTF_8));
    }

    public String uploadAssignment(MultipartFile file, String fileName) throws IOException {
        return uploadFile(file, fileName, ASSIGNMENT_FOLDER_NAME);
    }

    public String uploadTeacherSolution(MultipartFile file, String fileName) throws IOException {
        return uploadFile(file, fileName, TEACHER_SOLUTION_FOLDER_NAME);
    }

    public String uploadSubmission(MultipartFile file, String fileName) throws IOException {
        return uploadFile(file, fileName, SUBMISSION_FOLDER_NAME);
    }

    public String uploadAnnouncement(MultipartFile file, String fileName) throws IOException {
        return uploadFile(file, fileName, ANNOUNCEMENT_FOLDER_NAME);
    }

    public String getDownloadUrl(String filePath) {
        return String.format(DOWNLOAD_URL_FORMAT, BUCKET_NAME, URLEncoder.encode(filePath, StandardCharsets.UTF_8));
    }
}
