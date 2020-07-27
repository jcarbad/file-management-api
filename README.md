# File Management API
[![CircleCI](https://circleci.com/gh/jcarbad/file-management-api.svg?style=svg)](https://circleci.com/gh/jcarbad/file-management-api)

A small REST API to allow clients to manage files using Spring Boot 2.3 and MySQL 8.
<hr />


  * [How To Run](#how-to-run)
  * [Endpoints](#endpoints)
    + [POST /files](#post--files)
        * [Form Params:](#form-params-)
        * [Headers](#headers)
        * [Response](#response)
        * [Response Body](#response-body)
    + [GET /files/{fileId}](#get--files--fileid-)
        * [Path Params](#path-params)
        * [Query Params](#query-params)
        * [Response:](#response-)
        * [Response Body](#response-body-1)
        * [Response Headers](#response-headers)
    + [GET /files/{fileId}/details](#get--files--fileid--details)
        * [Path Params](#path-params-1)
        * [Response:](#response--1)
        * [Response Body](#response-body-2)
    + [PUT /files/{fileId}](#put--files--fileid-)
        * [Path Params:](#path-params-)
        * [Form Params:](#form-params--1)
        * [Headers:](#headers-)
        * [Response:](#response--2)
    + [DELETE /files/{fileId}](#delete--files--fileid-)
        * [Path Params:](#path-params--1)
        * [Query Params](#query-params-1)
        * [Response:](#response--3)
    + [DELETE /files/{fileId}/all](#delete--files--fileid--all)
        * [Path Params:](#path-params--2)
        * [Response:](#response--4)


<hr/>

## How To Run
1. Clone repo by running:
    ```
    git clone git@github.com:jcarbad/file-management-api.git
    ```
2. Build and package application with:
   ```
   mvn clean package
   ```
3. Run the app as containerized services with Docker Compose:
   ```
   docker-compose up --build
   ```
4. Application should be running under **_localhost:8081/files_**.
<hr />

## Endpoints
### POST /files
##### Form Params
|Name|Description|Type|Example|
|---|---|---|---|
|_filename_| Name of binary file                        | String |my_living.jpg|
|_description_| Description of the file contents       | String |Picture of my living room|
|_mediaType_| User-defined media type of file contents  | String |image/jpeg|
|_file_| Binary representation of file contents         | File   |file=@/Pictures/jack-unsplash.jpg|

##### Headers
|Header|Value|
|---|---|
|_Content-Type_|multipart/form-data|

##### Response
- 201 Created

##### Response Body
```json
{
    "fileId": 3,
    "createdAt": "2020-07-27T02:54:51.528",
    "versions": [
        {
            "filename": "my_lr.jpg",
            "mediaType": "image/jpeg",
            "description": "Photo of my living room",
            "uri": "/files/3?version=1",
            "createdAt": "2020-07-27T02:54:51.528",
            "updatedAt": "2020-07-27T02:54:51.528"
        }
    ]
}
```
<hr />

### GET /files/{fileId}
##### Path Params
|Param|Description|
|---|---|
|_fileId_|ID of file to retrieve|

##### Query Params
|Param|Description|Required|
|---|---|---|
|_version_|Specific file version to fetch. If absent, fetches the lastest version.|No|

##### Response
 - 200 OK

##### Response Body
- Binary representation of content of file requested

##### Response Headers
|Header|Value|
|---|---|
|_Content-Type_|"_..., application/json_"|

<hr />

### GET /files/{fileId}/details
##### Path Params
|Param|Description|
|---|---|
|_fileId_|ID of file to retrieve|

##### Response
- 200 OK

##### Response Body
```json
{
    "fileId": 1,
    "createdAt": "2020-07-01T00:00",
    "versions": [
        {
            "filename": "me.png",
            "mediaType": "image/png",
            "description": "Photo of me",
            "uri": "/files/1?version=1",
            "createdAt": "2020-07-01T00:00",
            "updatedAt": "2020-07-01T00:00"
        },
        {
            "filename": "me-v2.png",
            "mediaType": "image/png",
            "description": "Photo of me V2",
            "uri": "/files/1?version=2",
            "createdAt": "2020-07-01T00:00",
            "updatedAt": "2020-07-01T00:00"
        }
    ]
}
```
<hr />

### PUT /files/{fileId}
##### Path Params
|Param|Description|
|---|---|
|_fileId_|ID of file to retrieve|

##### Form Params
|Name|Description|Type|Example|
|---|---|---|---|
|_filename_| Name of binary file                        | String |my_living.jpg|
|_description_| Description of the file contents       | String |Picture of my living room|
|_mediaType_| User-defined media type of file contents  | String |image/jpeg|
|_file_| Binary representation of file contents         | File   |file=@/Pictures/jack-unsplash.jpg|

##### Headers
|Header|Value|
|---|---|
|_Content-Type_|multipart/form-data|

##### Response
- 204 NO CONTENT

<hr />

### DELETE /files/{fileId}
##### Path Params
|Param|Description|
|---|---|
|_fileId_|ID of file to retrieve|

##### Query Params
|Param|Description|Required|
|---|---|---|
|_version_|Specific file version to delete|Yes|

##### Response
- 204 NO CONTENT

<hr />

### DELETE /files/{fileId}/all
##### Path Params
|Param|Description|
|---|---|
|_fileId_|ID of file to retrieve|

##### Response
- 204 NO CONTENT