package io.carba.filemanagement.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class FileVersion {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne
   private File parentFile;

   private Long sequenceNumber;

   @CreationTimestamp
   private LocalDateTime createdAt;

   @Lob
   private byte[] file;
}
