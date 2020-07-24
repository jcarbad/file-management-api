package io.carba.filemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class File {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long fileId;
   private String filename;
   private String description;
   private String mediaType;

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentFile")
   @OnDelete(action = OnDeleteAction.CASCADE)
   private List<FileVersion> versions;

   @CreationTimestamp
   private LocalDateTime createdAt;

   @UpdateTimestamp
   private LocalDateTime updatedAt;
}
