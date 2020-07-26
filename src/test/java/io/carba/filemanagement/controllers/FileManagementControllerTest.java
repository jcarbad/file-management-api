package io.carba.filemanagement.controllers;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileManagementControllerTest {

   @Autowired
   private MockMvc mvc;

   @Test
   @Order(1)
   void saveFile() throws Exception {
      MockMultipartFile filePart = new MockMultipartFile("cute_cat.jpg", "cute_cat.jpg", "image/jpeg", "CAT IMAGE".getBytes());

      mvc.perform(
            multipart("/files")
                  .file("file", filePart.getBytes())
                  .param("filename", "cute_cat.jpg")
                  .param("description", "Cute cat climbing a tree")
                  .param("mediaType", "image/jpeg")
                  .contentType(MULTIPART_FORM_DATA_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.fileId").value(3))
            .andExpect(jsonPath("$.versions[0].filename").value("cute_cat.jpg"))
            .andExpect(jsonPath("$.versions[0].description").value("Cute cat climbing a tree"))
            .andExpect(jsonPath("$.versions[0].mediaType").value("image/jpeg"))
            .andExpect(jsonPath("$.versions[0].uri").value("/files/3?version=1"))
      ;
   }

   @Test
   @Order(2)
   void editFile() throws Exception {
      mvc.perform(
            put("/files/1")
                  .param("description", "Picture of me saving the cat")
                  .param("mediaType", "image/png")
                  .contentType(MULTIPART_FORM_DATA_VALUE))
            .andDo(print())
            .andExpect(status().isNoContent())
      ;
   }

   @Test
   @Order(3)
   void fetchAll() throws Exception {
      mvc.perform(get("/files/1/details").accept("application/json"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fileId").value(1))
            .andExpect(jsonPath("$.versions[0].filename").value("me.png"))
            .andExpect(jsonPath("$.versions[0].description").value("Photo of me"))

            .andExpect(jsonPath("$.versions[1].filename").value("me-v2.png"))
            .andExpect(jsonPath("$.versions[1].description").value("Photo of me V2"))

            .andExpect(jsonPath("$.versions[2].filename").value("me-v2.png"))
            .andExpect(jsonPath("$.versions[2].description").value("Picture of me saving the cat"))
      ;
   }

   @Test
   void fetchFile() throws Exception {
      String expectedContent = "{\n" +
            "    \"project\": {\n" +
            "        \"name\": \"Build a Fallout Shelter\",\n" +
            "        \"provider\": \"Apocalypse Co.\"\n" +
            "    }\n" +
            "}";

      mvc.perform(get("/files/2"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(expectedContent))
      ;
   }

   @Test
   void deleteFileVersion() throws Exception{
      mvc.perform(
            delete("/files/1?version=2"))
            .andDo(print())
            .andExpect(status().isNoContent())
      ;
   }

   @Test
   void deleteAll() throws Exception {
      mvc.perform(
            delete("/files/3/all"))
            .andDo(print())
            .andExpect(status().isNoContent())
      ;
   }

   @Test
   void saveFileBadRequest() throws Exception {
      mvc.perform(
            multipart("/files")
                  .file("file", null)
                  .param("filename", "cute_cat.jpg")
                  .param("description", "Cute cat climbing a tree")
                  .param("mediaType", "image/jpeg")
                  .contentType(MULTIPART_FORM_DATA_VALUE))
            .andDo(print())
            .andExpect(status().isBadRequest())
      ;
   }

   @Test
   void fetchFileNotFound() throws Exception {
      mvc.perform(get("/files/99"))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("File with fileId: 99 was not found"))
            .andExpect(jsonPath("$.path").value("/files/99"))
      ;
   }
}