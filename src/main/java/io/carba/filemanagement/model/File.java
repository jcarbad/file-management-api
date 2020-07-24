package io.carba.filemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class File {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private Long fileId;

   private String filename;

   private String description;

   private Long version;

   private String mediaType;

   @Lob
   @Basic(fetch = FetchType.LAZY)
   private byte[] contents;

   @CreationTimestamp
   private LocalDateTime createdAt;

   @UpdateTimestamp
   private LocalDateTime updatedAt;
}
