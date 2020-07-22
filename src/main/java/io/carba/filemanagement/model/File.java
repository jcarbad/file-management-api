package io.carba.filemanagement.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
public class File {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long fileId;
   private String filename;
   private String description;
   private String mediaType;

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentFile")
   private Set<FileVersion> versions;

   @CreationTimestamp
   private LocalDateTime createdAt;

   @UpdateTimestamp
   private LocalDateTime updatedAt;
}
