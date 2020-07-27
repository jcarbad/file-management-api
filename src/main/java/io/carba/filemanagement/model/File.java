package io.carba.filemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/**
 *
 * Main entity to represent an actual file stored in our database. Uses an ID as primary key to identify a individual entry.
 * Uses the fileId as a parent, or grouping, ID to identify the different versions of a same File entity.
 *
 * */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class File {
   @Id
   @GeneratedValue(strategy = IDENTITY)
   private Long id;

   private Long fileId;

   private String filename;

   private String description;

   private Long version;

   private String mediaType;

   @Lob
   @Basic(fetch = LAZY)
   private byte[] contents;

   @CreationTimestamp
   private LocalDateTime createdAt;

   @UpdateTimestamp
   private LocalDateTime updatedAt;
}
